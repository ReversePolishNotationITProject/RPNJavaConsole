/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers.LambdaTermSerialization;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Nodes.LambdaTermExpressionNode;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Nodes.LambdaTermNamedNode;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Nodes.LambdaTermRoundBracketNode;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Nodes.LambdaTermSquareBracketNode;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.LambdaTermRoot;
import anhkhoapham.rpnjavaconsole.Parsers.BracketPair;
import anhkhoapham.rpnjavaconsole.VariableSet.LambdaTermRefRoot;
import java.util.Collection;
import java.util.stream.Stream;

/**
 *
 * @author Khoapa
 */
public final class LambdaTermNodeConcatenatedSerializer implements LambdaTermNodeSerializer {


    private static final LambdaTermNodeConcatenatedSerializer pnSerializer
            = new LambdaTermNodeConcatenatedSerializer(false);
    
    private static final LambdaTermNodeConcatenatedSerializer rpnSerializer
            = new LambdaTermNodeConcatenatedSerializer(true);
    /**
     * @return the pnSerializer
     */
    public static LambdaTermNodeConcatenatedSerializer getPNSerializer() {
        return pnSerializer;
    }
    /**
     * @return the rpnSerializer
     */
    public static LambdaTermNodeConcatenatedSerializer getRPNSerializer() {
        return rpnSerializer;
    }
    
    private final StreamConcatenator concatenator;
    private final BracketPair roundBrackets;
    private final BracketPair squareBrackets;
    
    @SuppressWarnings("Convert2Lambda")
    private LambdaTermNodeConcatenatedSerializer(boolean reverseOrder) {
        concatenator = new StreamConcatenator() {
            @Override
            public <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b) {
                return reverseOrder ? Stream.concat(b, a) : Stream.concat(a, b);
            }
        };
        
        var rBracket = new BracketPair("(", ")");
        var sqBracket = new BracketPair("[", "]");
        
        roundBrackets = reverseOrder ? rBracket.swap() : rBracket;
        squareBrackets = reverseOrder ? sqBracket.swap() : sqBracket;
    }
    
    private Stream<String> serializeNodeSwitch(LambdaTermExpressionNode node, boolean wrapRoundBracket)
    {
        if (node instanceof LambdaTermNamedNode casted)
        {
            return serializeNode(casted);
            
        } else if (node instanceof LambdaTermRoundBracketNode casted)
        { 
            if (wrapRoundBracket) return wrap(serializeNode(casted), roundBrackets);
            
            return serializeNode(casted);
            
        } else if (node instanceof LambdaTermSquareBracketNode casted)
        {              
            return serializeNode(casted);           
        } else return serializeNodeDefault(node);
    }
    
    private Stream<String> serializeNodeSwitch(LambdaTermExpressionNode node)
    {
        return serializeNodeSwitch(node, false);
    }
                 
    
    private Stream<String> wrap(Stream<String> input, BracketPair bracketPair)
    {
        return concatenator.concat(concatenator.concat(
                Stream.of(bracketPair.leftBracket()), 
                    input),
                Stream.of(bracketPair.rightBracket()));
    }
    
    private Stream<String> serializeChildren(Stream<String> parentStream, Collection<LambdaTermExpressionNode> children)
    {
        Stream<String> childrenStream = Stream.empty();
        
        for (var child : children)
        {
            if (child.children().isEmpty())
            {
                childrenStream = Stream.concat(childrenStream, serializeNodeSwitch(child, true));
            }
            else
            {
                childrenStream = Stream.concat(childrenStream, 
                        wrap(serializeNodeSwitch(child, true), roundBrackets));
            }
        }
        
        return concatenator.concat(parentStream, childrenStream);
    }
    
    @Override
    public Stream<String> serializeRoot(LambdaTermRoot parameterNode) {
        
        if (parameterNode instanceof LambdaTermRefRoot refRoot) return serializeRefRoot(refRoot);
        
        @SuppressWarnings("null")
        var stream = Stream.of(parameterNode.displayName());
        
        return concatenator.concat(stream, serializeNodeSwitch(parameterNode.topNode()));
    }

    @Override
    public Stream<String> serializeNode(LambdaTermNamedNode namedNode) {
        
        return serializeChildren(Stream.of(namedNode.displayName()), namedNode.children());
                
    }

    @Override
    public Stream<String> serializeNode(LambdaTermRoundBracketNode roundBracketNode) {
        
        return serializeChildren(serializeRoot(roundBracketNode.substitutedRoot()), roundBracketNode.children());
    }

    @Override
    @SuppressWarnings("null")
    public Stream<String> serializeNode(LambdaTermSquareBracketNode squareBracketNode) {
        
        if (squareBracketNode instanceof LambdaTermRefRoot refRoot)
            return serializeChildren(serializeRefRoot(refRoot), squareBracketNode.children());
            
        return serializeChildren(
                wrap(serializeRoot(squareBracketNode.substitutedRoot()), squareBrackets),
                squareBracketNode.children());
    }    

}
