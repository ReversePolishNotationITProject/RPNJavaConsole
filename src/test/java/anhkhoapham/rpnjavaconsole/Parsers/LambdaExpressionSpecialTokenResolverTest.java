/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers;

import anhkhoapham.rpnjavaconsole.Parsers.NotationTranslation.LambdaExpressionSpecialTokenResolver;
import anhkhoapham.rpnjavaconsole.Dependency.CommandMapperBuilder;
import anhkhoapham.rpnjavaconsole.Validation.TokenSplitter;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Khoapa
 */
public class LambdaExpressionSpecialTokenResolverTest {
    
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    public LambdaExpressionSpecialTokenResolverTest() {
    }

    /**
     * Test of handle method, of class LambdaExpressionSpecialTokenResolver.
     */
    @Test
    public void testHandle() {
        var splitter = new TokenSplitter();
        
        System.out.println("handle");
        List<String> expression = splitter.apply("+ 1 2");
        LambdaExpressionSpecialTokenResolver instance = CommandMapperBuilder.getResolver();
        List<String> expResult = splitter.apply("{ref +} {1} {2}");
        List<String> result = instance.handle(expression);
        
        //TODO: Implement test.
        //assertTrue(ReadOnlyTreeNodeExtensions.compare(expResult, result));
    }
}
