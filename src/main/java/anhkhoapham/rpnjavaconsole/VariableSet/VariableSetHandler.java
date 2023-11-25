/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.VariableSet;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.LambdaTermRoot;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Parser.ExternalLambdaTreeParser;
import anhkhoapham.lambdaexpressioninterpreter.LambdaExpressionInterpreter;
import static anhkhoapham.rpnjavaconsole.Validation.SpecialSymbols.DISCARD;
import static anhkhoapham.rpnjavaconsole.Validation.SpecialSymbols.DISCRIMINATOR;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Khoapa
 */
public final class VariableSetHandler implements ExternalLambdaTreeParser {

            
    private final Map<String, BuiltInRoot> builtIn;
    
    private final Map<String, LambdaTermRoot> variables = new HashMap<>(32);
    
    private final LambdaExpressionInterpreter interpreter;
    public VariableSetHandler(Map<String, BuiltInRoot> builtIns, LambdaExpressionInterpreter interpreter) {
        if (builtIns == null) throw new IllegalArgumentException("builtIns is null.");
        if (interpreter == null) throw new IllegalArgumentException("interpreter is null.");
        
        this.builtIn = builtIns;
        this.interpreter = interpreter;
    }
    
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public Map<String, LambdaTermRoot> getVariables()
    {
        return variables;
    }
    
    public boolean contains(String varName)
    {
        return variables.containsKey(varName);
    }
    
    public void put(String varName, LambdaTermRoot root) throws IllegalArgumentException
    {
        if (builtIn.containsKey(varName))
            throw new IllegalArgumentException(varName + " is a read-only variable.");
        
        if (DISCARD.equals(varName)) return;
        
        variables.put(varName, root);
    }
    
    public void remove(String varName) throws IllegalArgumentException
    {
        if (builtIn.containsKey(varName))
            throw new IllegalArgumentException(varName + " is a read-only variable.");
        
        variables.remove(varName);
    }
    
    public String removeAll()
    {
        if (variables.isEmpty()) return "No variables to remove.";
        
        variables.clear();
        
        return "Delete all mutable variables.";
    }
    
    public String listVariables(String option)
    {
        var buffer = new StringBuilder(99);
        
        if (option.charAt(0) != DISCRIMINATOR) return "ERROR: \"$\" expected";
        
        option = option.substring(1);
        
        if ("all".equals(option) || "builtIn".equals(option))
        {
            buffer.append("Built In variables: \n");
            for(var variable : builtIn.entrySet())
                buffer.append("\t").append(variable.getKey()).append("\n");
            
            buffer.append("\n");
        }
        
        if ("all".equals(option) || "mutables".equals(option))
        {
            buffer.append("Mutable variables: \n");
            for(var variable : variables.entrySet())
                buffer.append("\t").append(variable.getKey()).append("\n");
            
            buffer.append("\n");
        }
        
        if (buffer.isEmpty()) return "ERROR: " + option + " is not a recognized option.";
        
        return buffer.toString();
    }

    /**
     *
     * @param input the value of input
     * @return 
     * @throws IllegalArgumentException
     */
    @Override
    public LambdaTermRoot parse(String input) throws IllegalArgumentException {
        
        String[] tokens = input.split(" ");

        if (tokens.length != 1 && tokens.length != 2) {
            throw new IllegalArgumentException("Variable handler expected 1 - 2 arguments.");
        }

        var ref = tokens.length != 1;
        var variable = tokens.length == 1 ? tokens[0] : tokens[1];

        if (Character.isDigit(variable.charAt(0))) {
            try {
                int value = Integer.parseInt(variable);

                var num = Integer.toString(value);
                
                if (ref) {
                    return new LambdaTermRefRoot(num,
                            () -> interpreter.translateInt(value), false, num);
                } else {
                    return interpreter.translateInt(value);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }
        } else if (ref) {
            if (builtIn.containsKey(variable)) {
                return new LambdaTermRefRoot(variable, () -> builtIn.get(variable).root(), true, variable);
            }
            return new LambdaTermRefRoot(variable, () -> variables.get(variable), true);         
        }
        return get(variable);
    }
    
    public LambdaTermRoot get(String varName) throws IllegalArgumentException
    {
        if (builtIn.containsKey(varName)) {
            return builtIn.get(varName).root();
        }

        if (variables.containsKey(varName)) {
            return variables.get(varName);
        }

        if (DISCARD.equals(varName))
            throw new IllegalArgumentException("Cannot get value from discard.");
        
        throw new IllegalArgumentException(varName + " cannot be found.");
    }

    @Override
    public LambdaTermRoot getMissing(String varName) throws IllegalArgumentException {
        return get(varName);
    }
}
