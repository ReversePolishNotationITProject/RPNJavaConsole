/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers.VariablesHandler;

import anhkhoapham.rpnjavaconsole.Parsers.NotationTranslation.LambdaExpressionSpecialTokenResolver;
import anhkhoapham.rpnjavaconsole.Validation.Rules;
import anhkhoapham.rpnjavaconsole.VariableSet.BuiltInRoot;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Receives a list of tokens, applies validation and performs special modifications to built-in and integral tokens.
 * @author Khoapa
 */
public final class BuiltInVariablesInjector implements LambdaExpressionSpecialTokenResolver {
     
    private final Map<String, BuiltInRoot> builtIn;
    public BuiltInVariablesInjector(Map<String, BuiltInRoot> builtIns) {
        if (builtIns == null) throw new IllegalArgumentException("builtIns is null.");
        
        this.builtIn = builtIns;
    }
    
    @Override
    public List<String> handle(List<String> expression) {
                
        var list = new LinkedList<String>();
        
        final int leftBracketNumber = -1;
        
        var argumentCountStack = new ArrayDeque<Integer>(4);
        
        for(int index = 0; index < expression.size(); index++)
        {
            var token = expression.get(index);
                        
            if (!argumentCountStack.isEmpty() && argumentCountStack.peekLast() != leftBracketNumber)
            {
                var operandCount = argumentCountStack.removeLast();
                
                argumentCountStack.add(operandCount - 1);   
            }
                      
            if (Rules.isOperator(token, builtIn))
            {
                list.add("(");
                argumentCountStack.add(builtIn.get(token).argCount());                
            }
            else if ("(".equals(token) || "[".equals(token))
            {
                argumentCountStack.add(leftBracketNumber);
            }
            else if (")".equals(token) || "]".equals(token))
            {
                while(true)
                {
                    if (argumentCountStack.isEmpty())
                        throw new IllegalArgumentException("Unexpected \")\".");
                    
                    if (argumentCountStack.removeLast() == leftBracketNumber) break;
                    
                    list.add(")");
                }
            }
                       
            list.add(validateToken(token));

            if (!argumentCountStack.isEmpty() && argumentCountStack.peekLast() == 0)
            {
                list.add(")");
                argumentCountStack.removeLast();
            }
        }
        
        // flush
        while(!argumentCountStack.isEmpty())
        {
            list.add(")");
            argumentCountStack.removeLast();
        }
        
        return list;
    }

    private String validateToken(String token)
    {
        if (Character.isDigit(token.charAt(0)))
            {
                try
                {
                    int value = Integer.parseInt(token);
                    return wrapNum(value);
                }
                catch(NumberFormatException e)
                {
                    throw new IllegalArgumentException(e);
                }
            }
        else
        {            
            var message = Rules.illegalCharactersCheck(token, builtIn.keySet());
            
            if (message.isPresent())
                throw new IllegalArgumentException(message.get());
            
            if (builtIn.containsKey(token))
            {
                return getRef(token);
            }
            else
            {
                return token;
            }
        }
    }
    
    private String getRef(String token)
    {
        return "{ref " + token + "}";
    }
    
    
    private String wrapNum(int number)
    {
        return "{" + number + "}";
    }
}
