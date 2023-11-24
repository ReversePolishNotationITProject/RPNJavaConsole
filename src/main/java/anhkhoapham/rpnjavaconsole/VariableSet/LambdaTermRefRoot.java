/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.VariableSet;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Nodes.LambdaTermExpressionNode;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.LambdaTermRoot;
import java.util.function.Supplier;

/**
 *
 * @author Khoapa
 */
public class LambdaTermRefRoot implements LambdaTermRoot {
    
    private final String variableName;
    private final Supplier<LambdaTermRoot> getRootFunc;
    private final boolean showRef;
    
    public LambdaTermRefRoot(String variableName, Supplier<LambdaTermRoot> getRootFunc, boolean showRef)
    {
        if (variableName == null) throw new IllegalArgumentException("\"variableName\" is null.");
        if (getRootFunc == null) throw new IllegalArgumentException("\"getRootFunc\" is null.");
        
        this.variableName = variableName;
        this.getRootFunc = getRootFunc;
        this.showRef = showRef;
    }
    
    public LambdaTermRefRoot(String variableName, Supplier<LambdaTermRoot> getRootFunc)
    {
        this(variableName, getRootFunc, true);
    }
    
    public LambdaTermRoot root() throws IllegalArgumentException
    {
        return getRootFunc.get();
    }
    
    @Override
    public String displayName() {
        return showRef? "ref " : "" + variableName;
    }

    public String revert() {
        return "{" + (showRef? "ref " : "") + variableName + "}";
    }
    
    @Override
    public LambdaTermExpressionNode topNode() {
        return root().topNode();
    }

    @Override
    public LambdaTermRoot substitute(LambdaTermRoot visitingRoot, String substitutedName) {
        return root().substitute(visitingRoot, substitutedName);
    }

    @Override
    public LambdaTermRoot invoke(LambdaTermRoot input) {
        return root().invoke(input);
    }

    @Override
    public void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        root().print(buffer, prefix, childrenPrefix);
    }
    
}
