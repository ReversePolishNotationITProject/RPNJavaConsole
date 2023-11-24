/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers.Groupings;

import anhkhoapham.lambdaexpressioninterpreter.LambdaExpressionInterpretationHandler;
import anhkhoapham.rpnjavaconsole.Parsers.NotationTranslation.LambdaExpressionNotationTranslator;
import anhkhoapham.rpnjavaconsole.Parsers.NotationTranslation.LambdaExpressionSpecialTokenResolver;
import anhkhoapham.rpnjavaconsole.VariableSet.VariableSetHandler;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author Khoapa
 */
public record VariablesAndSpecialTokensHandlers(
        LambdaExpressionSpecialTokenResolver resolver,
        LambdaExpressionInterpretationHandler interpretationHandler,
        VariableSetHandler variables,
        LambdaExpressionNotationTranslator notationTranslator,
        Function<String, List<String>> tokenSplitter) {
    
}
