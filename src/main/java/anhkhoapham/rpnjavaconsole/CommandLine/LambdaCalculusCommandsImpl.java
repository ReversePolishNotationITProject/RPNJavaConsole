/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.CommandLine;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.LambdaTermRoot;
import anhkhoapham.lambdacalculus.LambdaExpressonTree.Parser.LambdaExpressionTokenHandler;
import anhkhoapham.lambdaexpressioninterpreter.LambdaExpressionInterpretationHandler;
import anhkhoapham.rpnjavaconsole.Parsers.LambdaExpressionNotationTranslator;
import anhkhoapham.rpnjavaconsole.Parsers.LambdaExpressionSpecialTokenResolver;
import static anhkhoapham.rpnjavaconsole.Validation.SpecialSymbols.DISCRIMINATOR;
import anhkhoapham.rpnjavaconsole.VariableSet.VariableSetHandler;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Khoapa
 */
public class LambdaCalculusCommandsImpl implements LambdaCalculusCommands {


    private final LambdaExpressionTokenHandler reversedRPNParser;
    private final LambdaExpressionTokenHandler pnParser;
    private final LambdaExpressionSpecialTokenResolver resolver;
    
    private final LambdaExpressionInterpretationHandler interpretationHandler;
    private final VariableSetHandler variables;
    private final LambdaExpressionNotationTranslator notationTranslator;
    
    public LambdaCalculusCommandsImpl(LambdaExpressionTokenHandler reversedRPNParser,
            LambdaExpressionTokenHandler parser,
            LambdaExpressionSpecialTokenResolver resolver,
            LambdaExpressionInterpretationHandler interpretationHandler,
            VariableSetHandler variables,
            LambdaExpressionNotationTranslator notationTranslator) {
        
        // null checks here.
        
        this.reversedRPNParser = reversedRPNParser;
        this.pnParser = parser;
        this.resolver = resolver;
        this.interpretationHandler = interpretationHandler;
        this.variables = variables;
        this.notationTranslator = notationTranslator;
    }
    
    @Override
    public String print(List<String> unhandledExpression) {
        try
        {
            var root = handle(unhandledExpression, pnParser);
            
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
    public String print(List<String> unhandledExpression, String typeName) {
        try
        {
            var root = handle(unhandledExpression, pnParser);
            
            return interpretationHandler.interpret(typeName, root);
        }
        catch(IllegalArgumentException | UnsupportedOperationException e)
        {
            return e.toString();
        }
    }

    @Override
    public String list(String argument) {
        return variables.listVariables(argument);
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
                    var rpn = notationTranslator.translateInfixToRPN(unhandledExpression);
                    root = notationTranslator.translateRPNToPN(rpn);
                }
                case "RPN" -> root = notationTranslator.translateRPNToPN(unhandledExpression);
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
            variables.remove(varName);
            
            if (!variables.contains(varName))
            
                return varName + " doesn't exist.";
                
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
            var root = handle(unhandledExpression, pnParser);
            
            variables.put(varName, root);
            
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
            var root = handle(unhandledExpression, reversedRPNParser);
            
            variables.put(varName, root);
            
            return Optional.empty();
        }
        catch(IllegalArgumentException e)
        {
            return Optional.of(e.toString());
        }  
    }
    
    private LambdaTermRoot handle(List<String> unhandledExpression, LambdaExpressionTokenHandler parser) throws IllegalArgumentException
    {
        var tokens = resolver.handle(unhandledExpression);

        return parser.parse(tokens);
    }
}
