/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers;

import anhkhoapham.rpnjavaconsole.Parsers.NotationTranslation.LambdaExpressionNotationTranslator;
import anhkhoapham.rpnjavaconsole.Dependency.CommandMapperBuilder;
import anhkhoapham.rpnjavaconsole.Validation.TokenSplitter;
import anhkhoapham.treeanditerationlibrary.ReadOnlyTree.ReadOnlyTreeNodeExtensions;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Khoapa
 */
public class LambdaExpressionNotationTranslatorTest {
    
    public LambdaExpressionNotationTranslatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of translateInfixToRPN method, of class LambdaExpressionNotationTranslator.
     */
    @Test
    public void testTranslateInfixToRPN() {
        System.out.println("translateInfixToRPN");
        
        var splitter = new TokenSplitter();
        
        List<String> infix = splitter.apply("(2 * 5) + (4 * 6)");
        LambdaExpressionNotationTranslator instance =  CommandMapperBuilder.getTranslator();
        List<String> expResult = splitter.apply("2 5 * 4 6 * +");
        List<String> result = instance.InfixToRPN(infix);
        assertTrue(ReadOnlyTreeNodeExtensions.compare(expResult, result));
    }

    /**
     * Test of translateRPNToPN method, of class LambdaExpressionNotationTranslator.
     */
    @Test
    public void testTranslateRPNToPN() {
        System.out.println("translateRPNToPN");
        var splitter = new TokenSplitter();
        
        List<String> rpn = splitter.apply("2 5 * 4 6 * +");
        LambdaExpressionNotationTranslator instance =  CommandMapperBuilder.getTranslator();
        List<String> expResult = splitter.apply("+ * 6 4 * 5 2");
        List<String> result = instance.RPNToReversedRPN(rpn);
        assertTrue(ReadOnlyTreeNodeExtensions.compare(expResult, result));
    }
}
