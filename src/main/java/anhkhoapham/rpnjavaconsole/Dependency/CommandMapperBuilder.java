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
import anhkhoapham.rpnjavaconsole.CommandLine.CommandsUtil;
import anhkhoapham.rpnjavaconsole.CommandLine.LambdaCalculusCommandMapper;
import anhkhoapham.rpnjavaconsole.CommandLine.LambdaCalculusCommands;
import anhkhoapham.rpnjavaconsole.CommandLine.LambdaCalculusCommandsImpl;
import anhkhoapham.rpnjavaconsole.Parsers.Groupings.LambdaExpressionParsingHandlers;
import anhkhoapham.rpnjavaconsole.Parsers.Groupings.VariablesAndSpecialTokensHandlers;
import anhkhoapham.rpnjavaconsole.Parsers.LambdaTermSerialization.LambdaTermNodeConcatenatedSerializer;
import anhkhoapham.rpnjavaconsole.Parsers.NotationTranslation.InfixToRPNRaw;
import anhkhoapham.rpnjavaconsole.Parsers.NotationTranslation.LambdaExpressionNotationTranslator;
import anhkhoapham.rpnjavaconsole.Parsers.NotationTranslation.LambdaExpressionSpecialTokenResolver;
import anhkhoapham.rpnjavaconsole.Parsers.VariablesHandler.BuiltInVariablesInjector;
import anhkhoapham.rpnjavaconsole.Validation.TokenSplitter;
import anhkhoapham.rpnjavaconsole.VariableSet.BuiltInRoot;
import anhkhoapham.rpnjavaconsole.VariableSet.BuiltInVariables;
import anhkhoapham.rpnjavaconsole.VariableSet.VariableSetHandler;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author Khoapa
 */
public final class CommandMapperBuilder {
    private final static Function<String, String> commandMapper = buildCommandMapper();
    private static LambdaTermNodeBuilder nodeBuilder;
    private static LambdaExpressionParser stringParser;
    private static Map<String, BuiltInRoot> builtin;
    private static LambdaExpressionInterpreter interpreter;
    private static LambdaExpressionInterpretationHandler interpretationHandler;
    private static VariableSetHandler importer;
    private static LambdaExpressionTokenHandler pnParser;
    private static LambdaExpressionTokenHandler reversedRPNParser;
    private static LambdaExpressionSpecialTokenResolver resolver;
    private static LambdaExpressionNotationTranslator translator;
    private static LambdaCalculusCommands commands;
    
    /**
     * @return the nodeBuilder
     */
    public static LambdaTermNodeBuilder getNodeBuilder() {
        return nodeBuilder;
    }

    /**
     * @return the stringParser
     */
    public static LambdaExpressionParser getStringParser() {
        return stringParser;
    }

    /**
     * @return the interpreter
     */
    public static LambdaExpressionInterpreter getInterpreter() {
        return interpreter;
    }

    /**
     * @return the interpretationHandler
     */
    public static LambdaExpressionInterpretationHandler getInterpretationHandler() {
        return interpretationHandler;
    }

    /**
     * @param aInterpretationHandler the interpretationHandler to set
     */
    public static void setInterpretationHandler(LambdaExpressionInterpretationHandler aInterpretationHandler) {
        interpretationHandler = aInterpretationHandler;
    }

    /**
     * @return the importer
     */
    public static VariableSetHandler getImporter() {
        return importer;
    }

    /**
     * @param aImporter the importer to set
     */
    public static void setImporter(VariableSetHandler aImporter) {
        importer = aImporter;
    }

    /**
     * @return the pnParser
     */
    public static LambdaExpressionTokenHandler getPNParser() {
        return pnParser;
    }

    /**
     * @return the reversedRPNParser
     */
    public static LambdaExpressionTokenHandler getReversedRPNParser() {
        return reversedRPNParser;
    }

    /**
     * @return the resolver
     */
    public static LambdaExpressionSpecialTokenResolver getResolver() {
        return resolver;
    }

    /**
     * @return the commands
     */
    public static LambdaCalculusCommands getCommands() {
        return commands;
    }

            
    public static Function<String, String> getCommandMapper() {
        return commandMapper;
    }
    
    private static Function<String, String> buildCommandMapper()
    {
        nodeBuilder = LambdaTermNodeBuiltInBuilder.get();
        
        stringParser = new LambdaExpressionExternalTreePorterParser();
        
        builtin = new BuiltInVariables(stringParser).readOnlyDict();
        
        interpreter = new LambdaExpressionClassicInterpreter(stringParser, nodeBuilder);
        
        interpretationHandler = new LambdaExpressionInterpretationHandlerImpl(interpreter);
        
        importer = new VariableSetHandler(builtin, interpreter);
        
        pnParser = new LambdaExpressionExternalTreePorterParser(importer);
        
        reversedRPNParser = new LambdaExpressionExternalTreePorterParser(
                importer,
                LambdaTermNodeBuiltInBuilder.get(),
                () -> new LinkedListStack<LambdaTermExpressionNode>());
        
        resolver = new BuiltInVariablesInjector(builtin);
        
        translator = new InfixToRPNRaw(builtin);
       
        var parsers = new LambdaExpressionParsingHandlers(
                pnParser,
                reversedRPNParser,
                LambdaTermNodeConcatenatedSerializer.getPNSerializer(),
                LambdaTermNodeConcatenatedSerializer.getRPNSerializer()
        );
        
        var specialTokenHandlers = new VariablesAndSpecialTokensHandlers(
                resolver,
                interpretationHandler,
                importer,
                getTranslator(),
                new TokenSplitter()
        );
        
        commands = new LambdaCalculusCommandsImpl(
                parsers, specialTokenHandlers, CommandsUtil.getHelpTextMap());
               
        Function<String, String> result = new LambdaCalculusCommandMapper(commands, new TokenSplitter());
        
        return result;
    }

    /**
     * @return the builtin
     */
    public static Map<String, BuiltInRoot> getBuiltin() {
        return Collections.unmodifiableMap(builtin);
    }

    /**
     * @return the translator
     */
    public static LambdaExpressionNotationTranslator getTranslator() {
        return translator;
    }
    private CommandMapperBuilder() {
    }
    
}
