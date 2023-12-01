/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package anhkhoapham.rpnjavaconsole;

import anhkhoapham.rpnjavaconsole.Dependency.CommandMapperBuilder;
import anhkhoapham.rpnjavaconsole.Validation.TokenSplitter;
import java.util.Scanner;
import java.util.function.Function;

/**
 *
 * @author Khoapa
 */
public class RPNJavaConsole {

    public static void main(String[] args) {
        
        System.out.println("Reverse Polish Notation Console. Input \"$help\" to print help text for all commands.");
        
        Function<String, String> commandInput = CommandMapperBuilder.getCommandMapper();
        
        try (Scanner scanner = new Scanner(System.in)) {

            boolean running = true;
            
            while (running) {
                System.out.print("> ");
                
                var command = scanner.nextLine();
                
                if (command.equals("$exit") || command.equals("$quit"))
                {
                    running = false;
                }
                else
                {
                    var output = commandInput.apply(command);
                    
                    System.out.print(output);
                    
                    if (!output.isEmpty() && output.charAt(output.length() - 1) != '\n')
                        System.out.println();
                }
            }
        }      
    }
}
