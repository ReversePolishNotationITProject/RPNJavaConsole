/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Validation;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Parser.TokenAnalyzer;
import static anhkhoapham.rpnjavaconsole.Validation.SpecialSymbols.DISCARD;
import anhkhoapham.rpnjavaconsole.VariableSet.BuiltInRoot;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Khoapa
 */
public class Rules {
      
    public static boolean isReserved(String token, Collection<String> builtInVariables)
    {
        return builtInVariables.contains(token) 
                || "(".equals(token) 
                || ")".equals(token) 
                || "{".equals(token) 
                || "}".equals(token)
                || "[".equals(token)
                || "]".equals(token);
                //|| "->".equals(token);
    }
    
    public static boolean isOperator(String token, Map<String, BuiltInRoot> builtIns)
    {
        return builtIns.containsKey(token)
                && builtIns.get(token).priority() > 0
                && (builtIns.get(token).argCount() == 1
                || builtIns.get(token).argCount() == 2);
    }
    
    public static Optional<String> illegalCharactersCheck(String token, Collection<String> builtInVariables)
    {
        if (isReserved(token, builtInVariables)) return Optional.empty();
        
        if (token.charAt(0) == '{' && token.charAt(token.length() - 1) == '}') return Optional.empty();
        
        if (Character.isDigit(token.charAt(0))) return Optional.of("The first character cannot be a number.");
        
        if (DISCARD.equals(token)) return Optional.of("Discard cannot be declared as a variable as it is reserved.");
        
        if (TokenAnalyzer.isParameter(token))
        {                   
            token = token.substring(1, token.length());
            
            if (isReserved(token, builtInVariables)) return Optional.of(token + " cannot be declared as parameter as it is reserved.");
        }
        
        if (!onlyAlphanumeralsAndUnderscore(token)) return Optional.of(token + " is illegal. Only alphanumerics and underscore allowed.");
        
        return Optional.empty();
    }
    
    public static boolean onlyAlphanumeralsAndUnderscore(String token)
    {      
        for(int i = 0; i > token.length() - 1; i++)
        {
            char c = token.charAt(i);
            
            if (!Character.isAlphabetic(c) && c != '_')
            {
                return false;
            }
        }
        
        return true;
    }
    
    private Rules(){}
}
