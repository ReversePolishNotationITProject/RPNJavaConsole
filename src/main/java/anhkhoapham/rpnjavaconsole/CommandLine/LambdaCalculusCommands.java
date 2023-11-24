/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.CommandLine;

import anhkhoapham.rpnjavaconsole.CommandLine.NotationSelection.InputOutputNotationSelection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
     * $delete varName
     * @param varName
     * @return 
     */
    String delete(String varName);
    
    /**
     * $delete $all
     * @return 
     */
    String deleteAll();
    
    /**
     * 
     * @param varName
     * @param unhandledExpression
     * @return Error message, if any.
     */
    Optional<String> set(String varName, List<String> unhandledExpression);
    
    /**
     * $import [filepath]
     * @param filepath
     * @return 
     */
    String importFile(String filepath);
    
    /**
     * $export [filepath]
     * @param filepath
     * @return 
     */
    String exportFile(String filepath);
    
    void notation(Consumer<? super InputOutputNotationSelection> selection);
    
    /**
     * $help [commandName]
     * @param commandName
     * @return 
     */
    String help(String commandName);
    
    default String help()
    {
        
        return ("All supported commands:\n")
                + CommandsUtil.getHelpCommandHelp()
                + CommandsUtil.getPrintCommandHelp()
                + CommandsUtil.getListCommandHelp()
                + CommandsUtil.getSetCommandHelp()
                + CommandsUtil.getDeleteCommandHelp()
                + CommandsUtil.getParseCommandHelp()
                + CommandsUtil.getImportCommandHelp()
                + CommandsUtil.getExportCommandHelp()
                + CommandsUtil.getNotationCommandHelp();
    }
}
