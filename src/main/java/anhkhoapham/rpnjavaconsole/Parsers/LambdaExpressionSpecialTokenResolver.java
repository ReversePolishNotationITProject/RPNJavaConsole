/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers;

import java.util.List;

/**
 * Receives a list of tokens, applies validation and performs special modifications to built-in and integral tokens.
 * @author Khoapa
 */
public interface LambdaExpressionSpecialTokenResolver {
    
    List<String> handle(List<String> expression) throws IllegalArgumentException;
    
}
