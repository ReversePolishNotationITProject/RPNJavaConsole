/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers.NotationTranslation;

import java.util.List;

/**
 *
 * @author Khoapa
 */
public interface LambdaExpressionNotationTranslator {
    
    List<String> InfixToRPN(List<String> infix) throws IllegalArgumentException, UnsupportedOperationException;
    
    /**
     * This method does not actually convert RPN to PN.
     * Rather, it reverse element in a list of RPN tokens, then leave the parsing to a specialized parser.
     * @param rpn
     * @return 
     */
    default public List<String> RPNToReversedRPN(List<String> rpn) {
        return RPNToReversedRPN.translate(rpn);
    }
}
