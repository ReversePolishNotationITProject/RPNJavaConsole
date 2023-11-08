/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.VariableSet;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.LambdaTermRoot;

/**
 *
 * @author Khoapa
 */
public record BuiltInRoot(LambdaTermRoot root, int argCount, int priority) {

    public BuiltInRoot(LambdaTermRoot root, int argCount) {
        this(root, argCount, 0);
    }
 
    
}
