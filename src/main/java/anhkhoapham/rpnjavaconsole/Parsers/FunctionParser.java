/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khoapa
 */
public final class FunctionParser {

   
    private static final FunctionParser obj = new FunctionParser();
    public static FunctionParser get() {
        return obj;
    }
    
    public List<String> parseFunc(List<String> tokens) {
        
        var newExpression = new ArrayList<String>(tokens.size());
        
        boolean insertLeftRoundBracket = true;
        
        for(var index = 0; index < tokens.size(); index++)
        {
            var token = tokens.get(index);
            
            if (insertLeftRoundBracket)
            {
                newExpression.add("(");
                insertLeftRoundBracket = false;
            }
            if (token.equals(",") || index == tokens.size() - 1)
            {
                newExpression.add(")");
                insertLeftRoundBracket = true;
            }
            else
            {
                newExpression.add(token);
            }
        }
        
        return newExpression;
    }
    
}
