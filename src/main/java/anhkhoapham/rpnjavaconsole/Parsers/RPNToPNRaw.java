/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Khoapa
 */
public final class RPNToPNRaw {
    
    public static List<String> translate(List<String> rpn)
    {
        var tokens = new ArrayList<String>(rpn);
        
        Collections.reverse(tokens);
        
        for(int index = 0; index < tokens.size(); index++)
        {
            var token = tokens.get(index);
            
            if (null != token)
            switch (token) {
                case "(" -> tokens.set(index, ")");
                case ")" -> tokens.set(index, "(");
                case "[" -> tokens.set(index, "]");
                case "]" -> tokens.set(index, "[");
                default -> { }
            }
        }
        
        return tokens;
    }
    private RPNToPNRaw() {}
}
