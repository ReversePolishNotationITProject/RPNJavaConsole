/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers.LambdaTermSerialization;

/**
 *
 * @author Khoapa
 */
public class LambdaTermSerializationUtil {
    
    public static String TokenIterableToString(Iterable<String> tokens)
    {
        var buffer = new StringBuilder(16);
        
        var prefix = "";
        
        for(var token : tokens)
        {   
            if (token.isBlank()) continue;
            
            buffer.append(prefix).append(token);
            
            prefix = " ";
        }
        
        return buffer.toString();
    }

    private LambdaTermSerializationUtil() {
    }
    
}
