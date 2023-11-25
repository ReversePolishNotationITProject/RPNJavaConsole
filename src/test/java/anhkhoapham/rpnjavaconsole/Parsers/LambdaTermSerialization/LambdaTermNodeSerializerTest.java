/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers.LambdaTermSerialization;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.LambdaTermRoot;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Parser.LambdaExpressionParser;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Parser.LambdaExpressionTokenHandler;
import anhkhoapham.rpnjavaconsole.Dependency.CommandMapperBuilder;
import static anhkhoapham.rpnjavaconsole.Parsers.LambdaTermSerialization.LambdaTermSerializationUtil.TokenIterableToString;
import anhkhoapham.rpnjavaconsole.Validation.TokenSplitter;
import java.util.List;
import java.util.function.Function;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Khoapa
 */
public class LambdaTermNodeSerializerTest {
    
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    private final LambdaExpressionParser pnParser;
    private final LambdaTermNodeSerializer pnSerializer;
    private final LambdaExpressionTokenHandler reversedRPNParser;
    private final LambdaTermNodeSerializer rpnSerializer;
    private final Function<String, List<String>> splitter = new TokenSplitter();
    
    
    public LambdaTermNodeSerializerTest() {
        this.pnParser = (LambdaExpressionParser) CommandMapperBuilder.getPNParser();
        this.pnSerializer = LambdaTermNodeConcatenatedSerializer.getPNSerializer();
        this.reversedRPNParser = CommandMapperBuilder.getReversedRPNParser();
        this.rpnSerializer = LambdaTermNodeConcatenatedSerializer.getRPNSerializer();        
    }

    /**
     * Test of serializeRoot method, of PNSerializer of class LambdaTermNodeSerializer.
     */
    @Test
    public void testPNSerializeRoot() {
        System.out.println("serializeRoot");
        
        var expResult = "/a /b /c [ /i i ] ( b c a )";
        
        LambdaTermRoot parameterNode = pnParser.parse(expResult);

        var result = TokenIterableToString(pnSerializer.serializeRoot(parameterNode).toList());
        System.out.println(expResult);
        System.out.println(result);        
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of serializeRoot method, of RPNSerializer of class LambdaTermNodeSerializer.
     */
    @Test
    public void testRPNSerializeRoot() {
        System.out.println("serializeRoot");
        
        var expResult = "( c a b ) [ i /i ] /c /b /a";
        var input = "/a /b /c [ /i i ] ( b c a )";
        
        LambdaTermRoot parameterNode = pnParser.parse(input);

        var result = TokenIterableToString(rpnSerializer.serializeRoot(parameterNode).toList());
        System.out.println(expResult);
        System.out.println(result);        
        
        assertEquals(expResult, result);
    }
}
