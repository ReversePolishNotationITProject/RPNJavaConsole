/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.CommandLine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Khoapa
 */
public class CommandsUtil {
        
    private static final Map<String, String> helpTextMap = createHelpTextMap();
    
    private static Map<String, String> createHelpTextMap()
    {
        Map<String, String> map = HashMap.newHashMap(9);
        
        map.put("help", getHelpCommandHelp());
        map.put("print", getPrintCommandHelp());
        map.put("list", getListCommandHelp());
        map.put("set", getSetCommandHelp());
        map.put("delete", getDeleteCommandHelp());
        map.put("parse", getParseCommandHelp());
        map.put("import", getImportCommandHelp());
        map.put("export", getExportCommandHelp());
        map.put("notation", getNotationCommandHelp());
        
        return Collections.unmodifiableMap(map);
    }
    
    public static String getCommandNotExistText(String commandName)
    {
        return "Command " + commandName + " does not exist.";
    }
    
    public static String getHelpCommandHelp()
    {
        return "Show commands usage:\n\n"
                
                +("    Usage:\n\n")
                
                +("        $help\n\n")

                +("    Result: prints $help for every command.\n\n")

                +("        $help $[command]\n\n")

                +("    Result: prints the syntax and usage of $[command].\n\n");                
                
    }
    
    public static String getPrintCommandHelp()
    {
        return
        ("Printing expressions:\n\n")
        
        +("    Usage:\n\n")
        
        +("        $print [expression]\n")
        +("         OR\n")     
        +("        [expression]\n\n")
        +("    Result: prints the expression.\n\n")
        
        +("        $print $[int | bool | char] [expression]\n\n")
        
        +("    Result: prints the interpretion of the expression as that data type, if possible.\n\n");
    }
    
    public static String getListCommandHelp()
    {
        return ("List defined variables:\n\n")
        
        +("    Usage:\n\n")
        
        +("        $list $[builtIn | mutable | commands | all ]\n\n")
        
        +("    Arguments:\n\n")
        
        +("        $builtIn: built-in, readonly variables.\n")
        +("        $mutable: user defined variables.\n")
        +("        $all (default): all defined variables.\n\n")
        
        +("    Result: list out defined variables.\n\n");
    }
    
    public static String getSetCommandHelp()
    {
        return ("Setting a variable:\n\n")
        
        +("    Usage:\n\n")
        
        +("        [varName] = [expression]\n\n")
                
        +("    Result: set [varName] to that expression, if possible.\n\n");
    }
    
    public static String getDeleteCommandHelp()
    {
        return ("Deleting a variable:\n\n")
        
        +("    Usage:\n\n")
        
        +("        [delete] [varName]\n\n")
                
        +("    Result: [varName] is deleted, if possible.\n\n")
        
        +("    Usage:\n\n")
        
        +("        [delete] $all\n\n")
                
        +("    Result: delete all mutable variables.\n\n");
    }
    
    public static String getParseCommandHelp()
    {
        return ("Parsing an expression in other notations:\n\n")
        
        +("    Usage:\n\n")
        
        +("        $parse [varName] $[PN | infix | RPN] [expression]\n\n")
                
        +("    Result: parses [expression] in X notation to [varName], if possible.\n\n");
        //+("            Use \"_\" as [varName] to avoid setting a variable.\n\n")
    }

    public static String getImportCommandHelp() {
        return  """
                Import variables from a dictionary in a json file:
                The notation of the file can be configured using: $notation $input $[PN | infix | RPN]    

                    Usage:

                            $import [filepath]

                    Result: each variable from the file will be added to the global list of mutable variables.
                    Old variables are replaced.
               
                """;
    }
    
    public static String getExportCommandHelp()
    {
        return  """
                Export variables from the global list of mutable variables to a json file:
                The notation of the exported variables can be configured using: $notation $output $[PN | RPN]    
                    
                    Usage:

                        $export [filepath]

                    Result: the json / XML file containing a dictionary of variables.
                           
                """;        
    }

    
    public static String getSerializeHelp()
    {
        return  """
                Serialize the expression to a file.
                    
                    Usage:

                        $serialize [filepath] [expression]

                    Result: the XML file containing the expression.
                              
                """;          
    }
    
    public static String getNotationCommandHelp()
    {
        return  """
                Change the notation of input / output operations.
                   
                    Usage: 
               
                        $notation $input $[PN | infix | RPN] 
                    
                    Result: changes the notation of command line inputs and json imports.

                    Usage:                
                
                        $notation $output $[PN | RPN]
                
                    Result: changes the notation of command line prints and json exports.
                                       
                """;
    }
    
    public static String getCurlyBracketsHelp() {
        return  """
                Curly brackets are used to obtain foreign expression.
                   
                    Usage: 
               
                        { [varName] }
                    
                    Result: the expression is obtained from global variable [varName] at parsing time.
                    An exception is thrown if that variable does not exist.

                    Usage:                
                
                        { ref [varName] }                        
                
                    Result: create an object that references global variable [varName].
                    The global variable does not need to exist at parsing time, but must does so at invocation time.
                    If the global variable is updated, the obtained expression is updated as well.
                
                    Usage:                
                                
                        { file [filepath] }                        

                    Result: create an object that, when invoked, will deserialize the file to obtain the expression.
                    It is the same as the use above, but for expressions stored in files.
                    The file MUST contain a single expression in XML, not a dictionary containing global variables.
                              
                """;        
    }

    /**
     * @return the helpTextMap
     */
    public static Map<String, String> getHelpTextMap() {
        return Collections.unmodifiableMap(helpTextMap);
    }
    private CommandsUtil() {
    }
    
}
