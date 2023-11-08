/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.CommandLine;

import static anhkhoapham.rpnjavaconsole.CommandLine.LambdaCalculusCommandsEnum.delete;
import static anhkhoapham.rpnjavaconsole.CommandLine.LambdaCalculusCommandsEnum.list;
import static anhkhoapham.rpnjavaconsole.CommandLine.LambdaCalculusCommandsEnum.parse;
import static anhkhoapham.rpnjavaconsole.CommandLine.LambdaCalculusCommandsEnum.print;
import static anhkhoapham.rpnjavaconsole.Validation.SpecialSymbols.DISCRIMINATOR;
import anhkhoapham.rpnjavaconsole.Validation.TokenSplitter;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author Khoapa
 */
public final class LambdaCalculusCommandMapper implements Function<String, String> {

    
    private final LambdaCalculusCommands commands;
    private final TokenSplitter splitter;
    public LambdaCalculusCommandMapper(LambdaCalculusCommands commands, TokenSplitter splitter) {
        if (commands == null) throw new IllegalArgumentException("commands is null.");
        if (splitter == null) throw new IllegalArgumentException("splitter is null.");
        
        this.commands = commands;
        this.splitter = splitter;
    }

    @Override
    public String apply(String t) {
        var tokens = splitter.apply(t);
        
        if (tokens.isEmpty()) return "";
        
        var firstToken = tokens.get(0);
        
        if (firstToken.charAt(0) == DISCRIMINATOR && firstToken.length() > 1)
        {
            var command = firstToken.substring(1, firstToken.length());
            
            var enumConversion = LambdaCalculusCommandsEnum.TryParse(command);
            
            if (enumConversion.isEmpty())
                
                return "Command \"" + command + "\" not found.";
            
            return handleCommands(enumConversion.get(), tokens);
        }
        else
        {
            if (tokens.size() > 1 && "=".equals(tokens.get(1))) 
                return handleSet(tokens);
                
            return commands.print(tokens);
        }        
    }
    
    private String handleCommands(LambdaCalculusCommandsEnum command, List<String> originalTokens)
    {
        switch (command)
        {                
            case print -> {
                return handlePrintOverload(originalTokens);                
            }
            case list -> {
                return handleList(originalTokens);
            }
            case parse -> {
                return handleParse(originalTokens);             
            }
            case delete -> {
                return handleDelete(originalTokens);
            }
            case help -> {
                return commands.help();
            }
            default -> {
                return "\"" + command + "\" exists but does not have an implementation yet.";
            }        
        }
    }
    
    private String handlePrintOverload(List<String> originalTokens)
    {
        if (originalTokens.size() < 2) {
            return "ERROR: Expression required.";
        }

        if (originalTokens.get(1).charAt(0) == DISCRIMINATOR) {
            if (originalTokens.size() < 3) {
                return "ERROR: Expression required.";
            }

            var typeNameToken = originalTokens.get(1);

            return commands.print(originalTokens.subList(2, originalTokens.size()),
                    typeNameToken.substring(1));
        }

        return commands.print(originalTokens.subList(1, originalTokens.size()));
    }
    
    private String handleList(List<String> originalTokens)
    {       
        if (originalTokens.size() > 2) 
            return "ERROR: Unexpected argument length for command \"list\", expected 0 - 1 arguments.";

        var argument = originalTokens.size() == 1 ? "" : originalTokens.get(1);

        return commands.list(argument);
    }
    
    private String handleParse(List<String> originalTokens)
    {
        if (originalTokens.size() < 4) 
            return "ERROR: Unexpected argument length for command \"parse\", expected 3 arguments.";
            
        var result = commands.parse(originalTokens.get(1),
                originalTokens.get(2),
                originalTokens.subList(3, originalTokens.size()));
        
        if (result.isPresent()) return result.get();
               
        return "Successfully parsed to \"" + originalTokens.get(1) + "\"";
    }
    
    private String handleDelete(List<String> originalTokens)
    {
        if (originalTokens.size() != 2) 
            return "ERROR: Unexpected argument length for command \"delete\", expected 1 argument.";
        
        return commands.delete(originalTokens.get(1));
    }
    
    private String handleSet(List<String> originalTokens)
    {
        if (originalTokens.size() < 3)
            return "ERROR: Expression Required.";
        
        var result = commands.set(originalTokens.get(0), originalTokens.subList(2, originalTokens.size()));
        
        return result.isPresent() ? result.get() : "";
    }
}
