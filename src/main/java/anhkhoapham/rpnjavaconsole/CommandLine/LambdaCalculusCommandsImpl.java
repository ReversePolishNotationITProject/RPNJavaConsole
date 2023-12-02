/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.CommandLine;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Parser.LambdaExpressionTokenHandler;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.LambdaTermRoot;
import anhkhoapham.rpnjavaconsole.CommandLine.NotationSelection.InputOutputNotationSelection;
import anhkhoapham.rpnjavaconsole.CommandLine.NotationSelection.PnRpnInfixNotationSelection;
import anhkhoapham.rpnjavaconsole.CommandLine.NotationSelection.PnRpnNotationSelection;
import anhkhoapham.rpnjavaconsole.Parsers.Host;
import anhkhoapham.rpnjavaconsole.Parsers.Groupings.LambdaExpressionParsingHandlers;
import anhkhoapham.rpnjavaconsole.Parsers.Groupings.VariablesAndSpecialTokensHandlers;
import anhkhoapham.rpnjavaconsole.Parsers.LambdaTermDeserializer;
import anhkhoapham.rpnjavaconsole.Parsers.LambdaTermSerialization.LambdaTermNodeSerializer;
import anhkhoapham.rpnjavaconsole.Parsers.LambdaTermSerialization.LambdaTermSerializationUtil;
import static anhkhoapham.rpnjavaconsole.Parsers.LambdaTermSerialization.LambdaTermSerializationUtil.TokenIterableToString;
import anhkhoapham.rpnjavaconsole.Validation.Rules;
import static anhkhoapham.rpnjavaconsole.Validation.SpecialSymbols.DISCRIMINATOR;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author Khoapa
 */
public class LambdaCalculusCommandsImpl implements LambdaCalculusCommands {


    private final LambdaExpressionParsingHandlers parsing;
    private final VariablesAndSpecialTokensHandlers specialTokenHandlers;
    private final Map<String, String> helpTexts;

    private LambdaTermNodeSerializer activeSerializer;
    
    private Host<LambdaTermDeserializer> activeDeserializerHost = new Host<>();
    
    private final LambdaTermDeserializer pnDeserializer;
    private final LambdaTermDeserializer rpnDeserializer;   
    private final LambdaTermDeserializer infixDeserializer;

    private final PnRpnInfixNotationSelection inputSelector = new PnRpnInfixNotationSelection() {
        @Override
        public void infix() {
            activeDeserializerHost.setItem(infixDeserializer);
        }

        @Override
        public void PN() {
            activeDeserializerHost.setItem(pnDeserializer);
        }

        @Override
        public void RPN() {
            activeDeserializerHost.setItem(rpnDeserializer);
        }
    };

    private final PnRpnNotationSelection outputSelector = new PnRpnNotationSelection() {
        @Override
        public void PN() {
            activeSerializer = parsing.pnSerializer();
        }

        @Override
        public void RPN() {
            activeSerializer = parsing.RPNSerializer();
        }
    };
    
    public LambdaCalculusCommandsImpl(
            LambdaExpressionParsingHandlers parsing, 
            VariablesAndSpecialTokensHandlers specialTokenHandlers, 
            Map<String, String> helpTexts,
            Consumer<LambdaTermDeserializer>... observers) {
        Objects.nonNull(parsing);
        Objects.nonNull(specialTokenHandlers);
        Objects.nonNull(helpTexts);
        
        this.parsing = parsing;
        this.specialTokenHandlers = specialTokenHandlers;
        this.helpTexts = helpTexts;
        
        pnDeserializer = i -> handle(i, parsing.pnParser());
        rpnDeserializer = i -> {
            var root = specialTokenHandlers.notationTranslator().RPNToReversedRPN(i);

            return handle(root, parsing.reversedRPNParser());
        };
        infixDeserializer = i -> {
            var rpn = specialTokenHandlers.notationTranslator().InfixToRPN(i);
            var root = specialTokenHandlers.notationTranslator().RPNToReversedRPN(rpn);

            return handle(root, parsing.reversedRPNParser());
        };
        
        this.activeSerializer = parsing.RPNSerializer();
        
        this.activeDeserializerHost.addRange(observers);
        this.activeDeserializerHost.setItem(rpnDeserializer);
    }
     
    public String printTree(List<String> unhandledExpression) {
        try
        {
            var root = handle(unhandledExpression, parsing.pnParser());
            
            var buffer = new StringBuilder(99);
            
            root.print(buffer, "", "");
            
            return buffer.toString();
        }
        catch(IllegalArgumentException e)
        {
            return e.toString();
        }
    }
    
    @Override
    public String print(List<String> unhandledExpression) {
        try
        {
            var root = activeDeserializerHost.getItem().parse(unhandledExpression);
            
            return TokenIterableToString(activeSerializer.serializeRoot(root).toList());
        }
        catch(IllegalArgumentException e)
        {
            return e.toString();
        }
    }

    @Override
    public String print(List<String> unhandledExpression, String typeName) {
        try
        {
            var root = activeDeserializerHost.getItem().parse(unhandledExpression);
            
            return specialTokenHandlers.interpretationHandler().interpret(typeName, root);
        }
        catch(IllegalArgumentException | UnsupportedOperationException e)
        {
            return e.toString();
        }
    }

    @Override
    public String list(String argument) {
        return specialTokenHandlers.variables().listVariables(argument);
    }

    @Override
    public Optional<String> parse(String varName, String expressionType, List<String> unhandledExpression) {
        
        List<String> root;
        
        if (expressionType.charAt(0) != DISCRIMINATOR)
            return Optional.of("\'$\' expected.");
        
        expressionType = expressionType.substring(1);
        
        try
        {
            switch (expressionType) {
                case "PN" ->
                {
                    root = unhandledExpression;
                    return set(varName, root);
                }
                case "infix" -> {
                    var rpn = specialTokenHandlers.notationTranslator().InfixToRPN(unhandledExpression);
                    root = specialTokenHandlers.notationTranslator().RPNToReversedRPN(rpn);
                }
                case "RPN" -> root = specialTokenHandlers.notationTranslator().RPNToReversedRPN(unhandledExpression);
                default -> {
                    return Optional.of("Unknown notation type \"" + expressionType + "\".");
                }
            }
        }
        catch (IllegalArgumentException | UnsupportedOperationException e )
        {
            return Optional.of(e.getMessage());
        }
        
        return setRPN(varName, root);
    }

    @Override
    public String delete(String varName) {
        try
        {
            if (!specialTokenHandlers.variables().contains(varName))
            
                return varName + " doesn't exist.";            
            
            specialTokenHandlers.variables().remove(varName);             
            return varName + " success fully removed.";
        }
        catch(IllegalArgumentException e)
        {
            return e.toString();
        }       
    }

    @Override
    public Optional<String> set(String varName, List<String> unhandledExpression) {
        try
        {
            var root = activeDeserializerHost.getItem().parse(unhandledExpression);
            
            var check = Rules.illegalCharactersCheck(varName, specialTokenHandlers.variables().getBuiltIn().keySet());
            
            if (check.isPresent()) return check;
            
            specialTokenHandlers.variables().put(varName, root);
            
            return Optional.empty();
        }
        catch(IllegalArgumentException e)
        {
            return Optional.of(e.toString());
        }  
    }
    
    
    private Optional<String> setRPN(String varName, List<String> unhandledExpression)
    {
        try
        {
            var root = handle(unhandledExpression, parsing.reversedRPNParser());
            
            specialTokenHandlers.variables().put(varName, root);
            
            return Optional.empty();
        }
        catch(IllegalArgumentException e)
        {
            return Optional.of(e.toString());
        }  
    }
    
    private LambdaTermRoot handle(List<String> unhandledExpression, LambdaExpressionTokenHandler parser) throws IllegalArgumentException
    {
        var tokens = specialTokenHandlers.resolver().handle(unhandledExpression);

        return parser.parse(tokens);
    }
    
    /**
     * Get extension of a file, if possible.
     * @param filepath
     * @return Format switch label
     */
    private String getFormat(String filepath)
    {
        if (isFormat(filepath, ".xml")) return "$xml";
        if (isFormat(filepath, ".json")) return "$json";
        return null;
    }
    
    private boolean isFormat(String filepath, String extension)
    {
        int index = filepath.lastIndexOf(extension);
        
        return index > -1 && index == (filepath.length() - extension.length());
    }
    
    @Override
    @SuppressWarnings({"unchecked", "null"})
    public String importFile(String filepath) {

        String format = getFormat(filepath);
        
        switch (format)
        {
            case "$json" -> {
                try (java.io.FileReader reader = new FileReader(filepath)) {
                    Type type = new TypeToken<Map<String, Object>>(){}.getType();
                    Map<String, Object> rawStrings = new Gson().fromJson(reader, type);

                    for (var name : rawStrings.keySet())
                    {
                        var value = rawStrings.get(name);

                        String str;

                        if (value instanceof Iterable list)
                        {
                            str = "";
                            for (var line : list)
                                str += line + " ";
                        }
                        else if (value instanceof String strvalue)
                            str = strvalue;
                        else
                            throw new IllegalArgumentException("Invalid type: " + value.getClass() + ". Type must either be String or String[].");

                        var tokens = specialTokenHandlers.tokenSplitter().apply(str);
                        
                        
                        var root = activeDeserializerHost.getItem().parse(tokens);

                        specialTokenHandlers.variables().put(name, root);
                    }

                } catch (IOException | JsonSyntaxException ex) {
                    return ex.getMessage();
                }

                return "Successfully imported file \"" + filepath + "\".";
            }
            
            case "$xml" -> {
                try (java.io.FileReader reader = new FileReader(filepath)) {
                    
                    Map<String, String> rawStrings = new XmlMapper().readValue(
                            reader, new TypeReference<Map<String, String>>() {});

                    for (var name : rawStrings.keySet())
                    {
                        var tokens = specialTokenHandlers.tokenSplitter().apply(rawStrings.get(name));                        
                        var root = activeDeserializerHost.getItem().parse(tokens);

                        specialTokenHandlers.variables().put(name, root);
                    }
                } catch (IOException | JsonSyntaxException ex) {
                    return ex.getMessage();
                }                
                        
                return "Successfully imported file \"" + filepath + "\".";                
            }
            default -> {
                return "File format not supported.";
            }
        }
    }

    @Override
    public String exportFile(String filepath) {

        String format = getFormat(filepath);
        switch(format)
        {
            case "$json" -> {
                var gson = new Gson();
                
                
                var map = new HashMap<String, String>(32);
                
                var globalVars = specialTokenHandlers.variables().getVariables();
                
                for(var name : globalVars.keySet())
                {
                    var root = globalVars.get(name);
                    
                    var stream = activeSerializer.serializeRoot(root);
                    
                    map.put(name, LambdaTermSerializationUtil.TokenIterableToString(stream.toList()));
                }
                
                String json = gson.toJson(map);
                
                try (var writer = new FileWriter(filepath)) {
                    
                    writer.write(json);
                    
                } catch (IOException | JsonSyntaxException ex) {
                    return ex.getMessage();
                }
                
                return "Successfully exported to file \"" + filepath + "\".";
            }
            
            case "$xml" -> {
                                
                var map = new HashMap<String, String>(32);
                
                var globalVars = specialTokenHandlers.variables().getVariables();
                
                for(var name : globalVars.keySet())
                {
                    var root = globalVars.get(name);
                    
                    var stream = activeSerializer.serializeRoot(root);
                    
                    map.put(name, LambdaTermSerializationUtil.TokenIterableToString(stream.toList()));
                }
                
                try (var writer = new FileWriter(filepath)) {  
                    String xml = new XmlMapper().writeValueAsString(map);              
                    writer.write(xml);
                    
                } catch (IOException | JsonSyntaxException ex) {
                    return ex.getMessage();
                }                
                
                return "Successfully exported to file \"" + filepath + "\".";                
            }
            
            default -> {
                return "File format not supported.";               
            }
        }        
    }

    @Override
    public String help(String commandName) {
        if (helpTexts.containsKey(commandName))
            return helpTexts.get(commandName);
        
        return CommandsUtil.getCommandNotExistText(commandName);
    }

    @Override
    public String deleteAll() {
        return specialTokenHandlers.variables().removeAll();
    }

    @Override
    public void notation(Consumer<? super InputOutputNotationSelection> selection) {
              
        var selector = new InputOutputNotationSelection()
        {
            @Override
            public void input(Consumer<? super PnRpnInfixNotationSelection> selection) {
                selection.accept(inputSelector);
            }

            @Override
            public void output(Consumer<? super PnRpnNotationSelection> selection) {
                selection.accept(outputSelector);
            }          

            @Override
            public void both(Consumer<? super PnRpnNotationSelection> selection) {
                selection.accept(inputSelector);
                selection.accept(outputSelector);
            }
        };
        
        selection.accept(selector);
    }

    @Override
    public String serialize(String filepath, List<String> tokens) {
        
        var root = activeDeserializerHost.getItem().parse(tokens);
        
        var result = activeSerializer.serializeRoot(root);
        
        try (var writer = new FileWriter(filepath)) {  
                    String xml = new XmlMapper().writeValueAsString(TokenIterableToString(result.toList()));              
                    writer.write(xml);
                    
        } catch (IOException | JsonSyntaxException ex) {
            return ex.getMessage();
        }                
                
        return "Successfully exported to file \"" + filepath + "\".";        
    }
}
