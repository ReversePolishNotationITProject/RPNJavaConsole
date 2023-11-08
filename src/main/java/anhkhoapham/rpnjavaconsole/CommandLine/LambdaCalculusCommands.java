/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.CommandLine;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Khoapa
 */
public interface LambdaCalculusCommands {
    
    /**
     * $print [expression]
     * @param unhandledExpression
     * @return 
     */
    String print(List<String> unhandledExpression);
    
    /**
     * $print $[int | bool | char] [expression]
     * @param unhandledExpression
     * @param typeName
     * @return 
     */
    String print(List<String> unhandledExpression, String typeName);
      
    /**
     * $list
     * $list $[builtIn | mutable | all ]
     * @param argument
     * @return 
     */
    String list(String argument);
    
    /**
     * Parses an expression in a notation to varName.
     * $parse [varName] $[PN | infix | RPN] [expression]
     * @param varName
     * @param unhandledExpression
     * @param expressionType
     * @return Error message, if any.
     */
    Optional<String> parse(String varName, String expressionType, List<String> unhandledExpression);
     
    /**
     * $delete [varName]
     * @param varName
     * @return 
     */
    String delete(String varName);
    
    /**
     * 
     * @param varName
     * @param unhandledExpression
     * @return Error message, if any.
     */
    Optional<String> set(String varName, List<String> unhandledExpression);
    
    default String help()
    {
        var buffer = new StringBuilder(100);
        
        buffer.append("Supported commands:\n");
        
        buffer.append("Printing expressions:\n\n");
        
        buffer.append("    Usage:\n\n");
        
        buffer.append("        $print [expression]\n");
        buffer.append("         OR\n");        
        buffer.append("        [expression]\n\n");
        buffer.append("    Result: prints the expression.\n\n");
        
        buffer.append("        $print $[int | bool | char] [expression]\n\n");
        
        buffer.append("    Result: print the interpretion of the expression as that data type, if possible.\n\n");
        
        buffer.append("List defined variables:\n\n");
        
        buffer.append("    Usage:\n\n");
        
        buffer.append("        $list $[builtIn | mutable | commands | all ]\n\n");
        
        buffer.append("    Arguments:\n\n");
        
        buffer.append("        $builtIn: built-in, readonly variables.\n");
        buffer.append("        $mutable: user defined variables.\n");
        buffer.append("        $all (default): all defined variables.\n\n");
        
        buffer.append("    Result: list out defined variables.\n\n");
        
        buffer.append("Setting a variable:\n\n");
        
        buffer.append("    Usage:\n\n");
        
        buffer.append("        [varName] = [expression]\n\n");
                
        buffer.append("    Result: set [varName] to that expression, if possible.\n\n");
        
        buffer.append("Deleting a variable:\n\n");
        
        buffer.append("    Usage:\n\n");
        
        buffer.append("        [delete] [varName]\n\n");
                
        buffer.append("    Result: [varName] is deleted, if possible.\n\n");        
        
        buffer.append("Parsing an expression in other notations:\n\n");
        
        buffer.append("    Usage:\n\n");
        
        buffer.append("        $parse [varName] $[PN | infix | RPN] [expression]\n\n");
                
        buffer.append("    Result: parses [expression] in X notation to [varName], if possible.\n");
        //buffer.append("            Use \"_\" as [varName] to avoid setting a variable.\n\n"); 
        
        return buffer.toString();
    }
}
