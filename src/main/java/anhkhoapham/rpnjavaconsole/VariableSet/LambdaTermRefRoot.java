/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.VariableSet;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Nodes.LambdaTermExpressionNode;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.LambdaTermRoot;
import java.util.Objects;
import java.util.function.Supplier;

/**
 *
 * @author Khoapa
 */
public class LambdaTermRefRoot implements LambdaTermRoot {
    
    private final String variableName;
    private final Supplier<LambdaTermRoot> getRootFunc;
    private final boolean showRef;
    private final String reversion;
    private final boolean specialReversion;
    
    public LambdaTermRefRoot(String variableName, Supplier<LambdaTermRoot> getRootFunc, boolean showRef, String reversion)
    {
        Objects.requireNonNull(variableName);
        Objects.requireNonNull(getRootFunc);
        Objects.requireNonNull(reversion);
        
        this.variableName = variableName;
        this.getRootFunc = getRootFunc;
        this.showRef = showRef;
        this.reversion = reversion;
        
        specialReversion = true;
    }
    
    public LambdaTermRefRoot(String variableName, Supplier<LambdaTermRoot> getRootFunc, boolean showRef)
    {
        Objects.requireNonNull(variableName);
        Objects.requireNonNull(getRootFunc);
        
        this.variableName = variableName;
        this.getRootFunc = getRootFunc;
        this.showRef = showRef;
        this.reversion = "{" + (showRef? "ref " : "") + variableName + "}";
        
        specialReversion = false;
    }
    
    public LambdaTermRoot root() throws IllegalArgumentException
    {
        return getRootFunc.get();
    }
    
    @Override
    public String displayName() {
        return specialReversion ? reversion : (showRef? "ref " : "" + variableName);
    }

    public String revert() {
        return reversion;
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
