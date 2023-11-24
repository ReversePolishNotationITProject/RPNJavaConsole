/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers.NotationTranslation;

import anhkhoapham.rpnjavaconsole.Validation.Rules;
import anhkhoapham.rpnjavaconsole.VariableSet.BuiltInRoot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Khoapa
 */
public final class InfixToRPNRaw implements LambdaExpressionNotationTranslator {
    
    private final Map<String, BuiltInRoot> dict;
    
    public InfixToRPNRaw(Map<String, BuiltInRoot> dict) 
    {
        if (dict == null) throw new IllegalArgumentException();
        
        this.dict = dict;
    }
    
    @Override
    public List<String> InfixToRPN(List<String> infix) throws IllegalArgumentException, UnsupportedOperationException
    {        
        var outputQueue = new ArrayList<String>(infix.size());
        
        var operatorStack = new LinkedList<String>();
        
        var unaryOperatorStack = new LinkedList<String>();
        
        for (int index = 0; index < infix.size(); index++)
        {
            var token = infix.get(index);
            
            if ("[".equals(token) || "]".equals(token))
            {
                throw new UnsupportedOperationException(token + " is not supported in infix notation");
            }
            
            if (Rules.isOperator(token, dict))
            {
                BuiltInRoot operator = dict.get(token);
                
                if (operator.argCount() == 1)
                {                   
                    unaryOperatorStack.add(token);
                }
                else if (operator.argCount() == 2
                        && !hasOperatorsInScope(unaryOperatorStack))
                {                   
                    while (!operatorStack.isEmpty() && !"(".equals(operatorStack.peekLast()))
                    {
                        if (!Rules.isOperator(operatorStack.peekLast(), dict)) break;
                        
                        BuiltInRoot topOperator = dict.get(operatorStack.peekLast());
                        
                        if (topOperator.priority() > operator.priority()
                                || (topOperator.priority() == operator.priority()
                                && !"**".equals(token)))
                            pushToOutputQueue(outputQueue, operatorStack.removeLast());
                        else
                            break;
                    }
                    
                    operatorStack.addLast(token);
                }
                else
                    throwArgumentException(index, infix);
            }
            else if (",".equals(token))
            {
                if (hasOperatorsInScope(unaryOperatorStack))
                    throwArgumentException(index, infix);
                
                while (hasOperatorsInScope(operatorStack))
                    pushToOutputQueue(outputQueue, operatorStack.removeLast());
            }
            else if ("(".equals(token))
            {
                operatorStack.addLast("(");
                unaryOperatorStack.push("(");
            }
            else if (")".equals(token))
            {
                if (hasOperatorsInScope(unaryOperatorStack))
                    throwArgumentException(index, infix);
                
                while (hasOperatorsInScope(operatorStack))
                {               
                    pushToOutputQueue(outputQueue, operatorStack.removeLast());
                    
                    if (operatorStack.isEmpty()) throwArgumentException(index, infix);
                }
                
                if (!"(".equals(operatorStack.peekLast())) throwArgumentException(index, infix);
                
                operatorStack.removeLast();
                unaryOperatorStack.removeLast();
                
                if (!operatorStack.isEmpty() && !Rules.isOperator(operatorStack.peekLast(), dict))
                    pushToOutputQueue(outputQueue, operatorStack.peekLast());
                
                while (hasOperatorsInScope(unaryOperatorStack))
                    outputQueue.add(unaryOperatorStack.removeLast());         
            }
            else if (index < infix.size() - 1 && "(".equals(infix.get(index + 1)))
            {
                operatorStack.add(token);
                outputQueue.add("(");
            }
            else
            {
                outputQueue.add(token);
                
                while(!unaryOperatorStack.isEmpty() && !"(".equals(unaryOperatorStack.peekLast()))
                    outputQueue.add(unaryOperatorStack.removeLast());
            }
        }
        
        if (!unaryOperatorStack.isEmpty()) throwArgumentException(infix);
        
        while (!operatorStack.isEmpty())
        {
            var token = operatorStack.removeLast();
            
            if ("(".equals(token)) throwArgumentException(infix);
             
            outputQueue.add(token);
        }
        
        return outputQueue;
    }
    
    /**
     * Push token to output queue, then add a right bracket if it is a function.
     * @param outputQueue
     * @param token 
     */
    private void pushToOutputQueue(List<String> outputQueue, String token)
    {    
        outputQueue.add(token);
        
        // if is function.
        if (!Rules.isOperator(token, dict))
            outputQueue.add(")");
    }
    
    private boolean hasOperatorsInScope(Deque<String> operatorStack)
    {
        return !operatorStack.isEmpty() && !"(".equals(operatorStack.peekLast());
    }
    
    private void throwArgumentException(int index, List<String> tokens) throws IllegalArgumentException
    {
        throw new IllegalArgumentException("Token \"" + tokens.get(index) + "\" at [" + index + "] in " 
            + Arrays.toString(tokens.toArray()));
    }
    
    private void throwArgumentException(List<String> tokens) throws IllegalArgumentException
    {
        throw new IllegalArgumentException(Arrays.toString(tokens.toArray()));
    }            
}
