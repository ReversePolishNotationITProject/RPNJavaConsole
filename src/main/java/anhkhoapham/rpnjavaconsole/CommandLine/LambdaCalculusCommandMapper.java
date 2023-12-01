/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.CommandLine;

import anhkhoapham.rpnjavaconsole.CommandLine.NotationSelection.PnRpnInfixNotationSelection;
import anhkhoapham.rpnjavaconsole.CommandLine.NotationSelection.PnRpnNotationSelection;
import static anhkhoapham.rpnjavaconsole.Validation.SpecialSymbols.DISCRIMINATOR;
import anhkhoapham.rpnjavaconsole.Validation.TokenSplitter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Khoapa
 */
public final class LambdaCalculusCommandMapper implements Function<String, String> {

    
    private final LambdaCalculusCommands commands;
    private final TokenSplitter splitter;
    private final Map<String, Function<List<String>, String>> commandMap;
    
    public LambdaCalculusCommandMapper(LambdaCalculusCommands commands, TokenSplitter splitter) {
        if (commands == null) throw new IllegalArgumentException("commands is null.");
        if (splitter == null) throw new IllegalArgumentException("splitter is null.");
        
        this.commands = commands;
        this.splitter = splitter;
        
        commandMap = createCommandMap();
    }

    @Override
    public String apply(String t) {
        var tokens = splitter.apply(t);
        
        if (tokens.isEmpty()) return "";
        
        var firstToken = tokens.get(0);
        
        if (firstToken.charAt(0) == DISCRIMINATOR && firstToken.length() > 1)
        {
            var command = firstToken.substring(1, firstToken.length());
            
            if (!commandMap.containsKey(command)) return CommandsUtil.getCommandNotExistText(command);
            
            try
            {
                return commandMap.get(command).apply(tokens);
            }
            catch (IllegalArgumentException | UnsupportedOperationException e)
            {
                return e.getMessage();
            }
        }
        else
        {
            if (tokens.size() > 1 && "=".equals(tokens.get(1))) 
                return handleSet(tokens);
                
            return commands.print(tokens);
        }        
    }
    
    private Map<String, Function<List<String>, String>> createCommandMap()
    {
        Map<String, Function<List<String>, String>> map = HashMap.newHashMap(7);
        
        map.put("print", tokens -> handlePrintOverload(tokens));
        map.put("list", tokens -> handleList(tokens));
        map.put("parse", tokens -> handleParse(tokens));
        map.put("delete", tokens -> handleDelete(tokens));
        map.put("help", tokens -> handleHelp(tokens));
        map.put("import", tokens -> commands.importFile(tokens.get(1)));
        map.put("export", tokens -> commands.exportFile(tokens.get(1)));
        map.put("serialize", tokens -> handleSerialize(tokens));
        map.put("notation", tokens -> handleNotation(tokens));
        
        return map;
    }
    
    
    private String handlePrintOverload(List<String> originalTokens)
    {
        if (originalTokens.size() < 2) {
            return "ERROR: Expression required.";
        }

        if (originalTokens.get(1).charAt(0) == DISCRIMINATOR) {
            if (originalTokens.size() < 3) {
                return "ERROR: Expression required.";
            }

            var typeNameToken = originalTokens.get(1);

            return commands.print(originalTokens.subList(2, originalTokens.size()),
                    typeNameToken.substring(1));
        }

        return commands.print(originalTokens.subList(1, originalTokens.size()));
    }
    
    private String handleList(List<String> originalTokens)
    {       
        if (originalTokens.size() > 2) 
            return "ERROR: Unexpected argument length for command \"list\", expected 0 - 1 arguments.";

        var argument = originalTokens.size() == 1 ? "" : originalTokens.get(1);

        return commands.list(argument);
    }
    
    private String handleParse(List<String> originalTokens)
    {
        if (originalTokens.size() < 4) 
            return "ERROR: Unexpected argument length for command \"parse\", expected 3 arguments.";
            
        var result = commands.parse(originalTokens.get(1),
                originalTokens.get(2),
                originalTokens.subList(3, originalTokens.size()));
        
        if (result.isPresent()) return result.get();
               
        return "Successfully parsed to \"" + originalTokens.get(1) + "\"";
    }
    
    private String handleDelete(List<String> originalTokens)
    {
        if (originalTokens.size() != 2) 
            return "ERROR: Unexpected argument length for command \"delete\", expected 1 argument.";
        
        if (originalTokens.get(1).charAt(0) == DISCRIMINATOR)
            if (originalTokens.get(1).equals(DISCRIMINATOR + "all"))
                return commands.deleteAll();
            else return CommandsUtil.getCommandNotExistText(originalTokens.get(1));
        
        return commands.delete(originalTokens.get(1));
    }
    
    private String handleSet(List<String> originalTokens)
    {
        if (originalTokens.size() < 3)
            return "ERROR: Expression Required.";
        
        var result = commands.set(originalTokens.get(0), originalTokens.subList(2, originalTokens.size()));
        
        return result.isPresent() ? result.get() : "";
    }
    
    private String handleHelp(List<String> originalTokens)
    {
        if (originalTokens.size() > 2) 
            return "ERROR: Unexpected argument length for command \"help\", expected 0 - 1 arguments.";
        
        if (originalTokens.size() == 1)
            return commands.help();
        
        return commands.help(originalTokens.get(1));
    }
    
    private String handleSerialize(List<String> originalTokens)
    {
        if (originalTokens.size() < 3) 
            return "ERROR: Insufficient argument length for command \"serialize\", expected at least 3 arguments. ";

        return commands.serialize(originalTokens.get(1), originalTokens.subList(2, originalTokens.size()));
    }
    
    private String handleNotation(List<String> originalTokens)
    {
        if (originalTokens.size() != 3) 
            return "ERROR: Unexpected argument length for command \"notation\", expected 2 arguments.";
        
        if (originalTokens.get(1).charAt(0) != DISCRIMINATOR) return "Expected \'" + DISCRIMINATOR + "\'.";
        if (originalTokens.get(2).charAt(0) != DISCRIMINATOR) return "Expected \'" + DISCRIMINATOR + "\'.";    
                
        var flowToken = originalTokens.get(1).substring(1);
        
        var notationToken = originalTokens.get(2).substring(1);
        
        switch (flowToken) {
            case "input" ->                 {
                    Consumer<PnRpnInfixNotationSelection> notationSelector;
                    switch (notationToken) {
                        case "PN" -> notationSelector = i -> i.PN();
                        case "infix" -> notationSelector = i -> i.infix();
                        case "RPN" -> notationSelector = i -> i.RPN();
                        default -> {
                            return "Notation \"" + originalTokens.get(2) + "\" not found.";
                        }
                    }       commands.notation(i -> i.input(notationSelector));
                }
            case "output", "both" -> {
                Consumer<PnRpnNotationSelection> notationSelector;
                
                switch (notationToken) {
                    case "PN" -> notationSelector = i -> i.PN();
                    case "RPN" -> notationSelector = i -> i.RPN();
                    case "infix" -> {
                        return "infix not yet support for output.";
                    }
                    default -> {
                        return "Notation \"" + originalTokens.get(2) + "\" not found.";
                    }
                }       
                
                if ("output".equals(flowToken)) {
                    commands.notation(i -> i.output(notationSelector));
                }       
                commands.notation(i -> i.both(notationSelector));
            }
            default ->  {
                return "Option \"" + DISCRIMINATOR + flowToken + "\" not found.";
                }
        }

        
        return "Successfully set notation of " + flowToken + " to " + notationToken + "."; 
    }
}
