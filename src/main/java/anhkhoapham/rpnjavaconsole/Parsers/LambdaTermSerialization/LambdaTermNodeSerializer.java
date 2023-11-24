/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers.LambdaTermSerialization;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Nodes.LambdaTermExpressionNode;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Nodes.LambdaTermNamedNode;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Nodes.LambdaTermRoundBracketNode;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Nodes.LambdaTermSquareBracketNode;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.LambdaTermRoot;
import anhkhoapham.rpnjavaconsole.VariableSet.LambdaTermRefRoot;
import java.util.stream.Stream;

/**
 *
 * @author Khoapa
 */
public interface LambdaTermNodeSerializer {
    
    Stream<String> serializeRoot(LambdaTermRoot parameterNode);
    
    default Stream<String> serializeRefRoot(LambdaTermRefRoot refNode)
    {
        return Stream.of(refNode.revert());
    }
    
    Stream<String> serializeNode(LambdaTermNamedNode namedNode);
    
    Stream<String> serializeNode(LambdaTermRoundBracketNode roundBracketNode);
    
    Stream<String> serializeNode(LambdaTermSquareBracketNode squareBracketNode);
    
    default Stream<String> serializeNodeDefault(LambdaTermExpressionNode node) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("Unsupported Node type. Node does not implement" 
                + LambdaTermNamedNode.class.toString()
                + "\n    or " + LambdaTermRoundBracketNode.class.toString()
                + "\n    or " + LambdaTermSquareBracketNode.class.toString());
    }
}
