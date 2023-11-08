/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Dependency;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Builders.LambdaTermNodeBuilder;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Builders.LambdaTermNodeBuiltInBuilder;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Iteration.LinkedListStack;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Nodes.LambdaTermExpressionNode;
import anhkhoapham.lambdacalculus.LambdaExpressonTree.Parser.LambdaExpressionExternalTreePorterParser;
import anhkhoapham.lambdacalculus.LambdaExpressonTree.Parser.LambdaExpressionParser;
import anhkhoapham.lambdacalculus.LambdaExpressonTree.Parser.LambdaExpressionTokenHandler;
import anhkhoapham.lambdaexpressioninterpreter.LambdaExpressionClassicInterpreter;
import anhkhoapham.lambdaexpressioninterpreter.LambdaExpressionInterpretationHandler;
import anhkhoapham.lambdaexpressioninterpreter.LambdaExpressionInterpretationHandlerImpl;
import anhkhoapham.lambdaexpressioninterpreter.LambdaExpressionInterpreter;
import anhkhoapham.rpnjavaconsole.CommandLine.LambdaCalculusCommandMapper;
import anhkhoapham.rpnjavaconsole.CommandLine.LambdaCalculusCommands;
import anhkhoapham.rpnjavaconsole.CommandLine.LambdaCalculusCommandsImpl;
import anhkhoapham.rpnjavaconsole.Parsers.BuiltInVariablesInjector;
import anhkhoapham.rpnjavaconsole.Parsers.InfixToRPNRaw;
import anhkhoapham.rpnjavaconsole.Parsers.LambdaExpressionSpecialTokenResolver;
import anhkhoapham.rpnjavaconsole.Validation.TokenSplitter;
import anhkhoapham.rpnjavaconsole.VariableSet.BuiltInVariables;
import anhkhoapham.rpnjavaconsole.VariableSet.VariableSetHandler;
import java.util.function.Function;

/**
 *
 * @author Khoapa
 */
public final class CommandMapperBuilder {

    private final static Function<String, String> commandMapper = buildCommandMapper();

    public static Function<String, String> getCommandMapper() {
        return commandMapper;
    }
          
    private static Function<String, String> buildCommandMapper()
    {
        LambdaTermNodeBuilder nodeBuilder = LambdaTermNodeBuiltInBuilder.get();
        
        LambdaExpressionParser stringParser = new LambdaExpressionExternalTreePorterParser();
        
        var builtin = new BuiltInVariables(stringParser).readOnlyDict();
        
        LambdaExpressionInterpreter interpreter = new LambdaExpressionClassicInterpreter(stringParser, nodeBuilder);
        
        LambdaExpressionInterpretationHandler interpretationHandler = new LambdaExpressionInterpretationHandlerImpl(interpreter);
        
        VariableSetHandler importer = new VariableSetHandler(builtin, interpreter);
        
        LambdaExpressionTokenHandler pnParser = new LambdaExpressionExternalTreePorterParser(importer);
        
        LambdaExpressionTokenHandler reversedRPNParser = new LambdaExpressionExternalTreePorterParser(
                importer,
                LambdaTermNodeBuiltInBuilder.get(),
                () -> new LinkedListStack<LambdaTermExpressionNode>());
        
        LambdaExpressionSpecialTokenResolver resolver = new BuiltInVariablesInjector(builtin);
        
        LambdaCalculusCommands commands = new LambdaCalculusCommandsImpl(
                reversedRPNParser,
                pnParser,
                resolver,
                interpretationHandler,
                importer, 
                new InfixToRPNRaw(builtin));
               
        Function<String, String> result = new LambdaCalculusCommandMapper(commands, new TokenSplitter());
        
        return result;
    }
    private CommandMapperBuilder() {
    }
    
}
