/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Validation;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Khoapa
 */

@Deprecated
public interface TokenValidator {
    
    Optional<String> validate(String token);
    
    default Optional<String> validate(List<String> tokens)
    {
        StringBuilder buffer = new StringBuilder(99);
        
        for(var token : tokens)
        {
            var result = validate(token);
            
            if (result.isPresent())
                buffer.append(result.get()).append("\n");
        }
        
        if (buffer.isEmpty())
        {
            return Optional.empty();
        }
        else
            return Optional.of(buffer.toString());
    }
    
}
