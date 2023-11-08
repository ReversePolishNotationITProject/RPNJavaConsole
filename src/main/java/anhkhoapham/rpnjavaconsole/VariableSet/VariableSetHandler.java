/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.VariableSet;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.LambdaTermRoot;
import anhkhoapham.lambdacalculus.LambdaExpressonTree.Parser.External.ExternalLambdaTreeParser;
import anhkhoapham.lambdaexpressioninterpreter.LambdaExpressionInterpreter;
import anhkhoapham.rpnjavaconsole.Validation.SpecialSymbols;
import static anhkhoapham.rpnjavaconsole.Validation.SpecialSymbols.DISCARD;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static anhkhoapham.rpnjavaconsole.Validation.SpecialSymbols.DISCRIMINATOR;

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
    
    public boolean contains(String varName)
    {
        return variables.containsKey(varName);
    }
    
    public Optional<LambdaTermRoot> tryGet(String varName, boolean getReference)
    {
        if (builtIn.containsKey(varName)) {

            return Optional.of(new LambdaTermRefRoot(varName, () -> builtIn.get(varName).root()));
        } else if (variables.containsKey(varName)) {

            if (getReference) {
                return Optional.of(new LambdaTermRefRoot(varName, () -> variables.get(varName)));
            } else {
                return Optional.of(variables.get(varName));
            }
        }
        return Optional.empty();
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

                if (ref) {
                    return new LambdaTermRefRoot(Integer.toString(value),
                            () -> interpreter.translateInt(value));
                } else {
                    return interpreter.translateInt(value);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }
        } else if (ref) {
            return new LambdaTermRefRoot(variable, () -> get(variable));
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
}
