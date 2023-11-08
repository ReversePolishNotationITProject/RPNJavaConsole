/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers;

import java.util.List;

/**
 *
 * @author Khoapa
 */
public interface LambdaExpressionNotationTranslator {
    
    List<String> translateInfixToRPN(List<String> infix) throws IllegalArgumentException, UnsupportedOperationException;
    
    default public List<String> translateRPNToPN(List<String> rpn) {
        return RPNToPNRaw.translate(rpn);
    }
}
