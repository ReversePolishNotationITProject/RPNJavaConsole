/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Validation;

import java.util.Collection;
import java.util.Optional;

/**
 *
 * @author Khoapa
 */
@Deprecated
public final class TokenDefaultValidator implements TokenValidator {

    
    private final Collection<String> builtInVariables;
    public TokenDefaultValidator(Collection<String> builtInVariables) {
        this.builtInVariables = builtInVariables;
    }
    
    @Override
    public Optional<String> validate(String token) {
        
        return Rules.illegalCharactersCheck(token, builtInVariables);
    }    
}
