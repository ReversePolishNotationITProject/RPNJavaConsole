/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.CommandLine;

import java.util.Optional;

/**
 *
 * @author Khoapa
 */
public enum LambdaCalculusCommandsEnum {
    
    print,
    list,
    parse,
    delete,
    help;
    
    public static Optional<LambdaCalculusCommandsEnum> TryParse(String input)
    {
        for(var value : LambdaCalculusCommandsEnum.values())
        {
            if (value.toString().equals(input))
                return Optional.of(value);
        }
        
        return Optional.empty();
    }
}
