/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers.Groupings;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Parser.LambdaExpressionTokenHandler;
import anhkhoapham.rpnjavaconsole.Parsers.LambdaTermSerialization.LambdaTermNodeSerializer;

/**
 *
 * @author Khoapa
 */
public record LambdaExpressionParsingHandlers(
        LambdaExpressionTokenHandler pnParser,
        LambdaExpressionTokenHandler reversedRPNParser,
        LambdaTermNodeSerializer pnSerializer,
        LambdaTermNodeSerializer RPNSerializer) {
    
}
