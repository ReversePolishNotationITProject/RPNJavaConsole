/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.VariableSet;

import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.Custom.NullRoot;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.Custom.VoidRoot;
import anhkhoapham.lambdacalculus.LambdaExpressionTree.Root.LambdaTermRoot;
import anhkhoapham.lambdacalculus.LambdaExpressonTree.Parser.LambdaExpressionParser;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * https://en.wikipedia.org/wiki/Church_encoding
 * https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/operators/
 * @author Khoapa
 */

public final class BuiltInVariables {

    
    private static final String TRUE_STRING = "/a /_ a";
    private static final String FALSE_STRING = "/_ /b b";
    private static final String NOT_STRING = "/p /a /b p b a";
    private static final String AND_STRING = "/p /q p q p";
    
    private static final String PRED_STRING = "/n /f /x n ( /g /h h ( g f ) ) ( /u x ) ( /i i )"; 
    private static final String SUBTRACT_STRING = "/m /n n " + wrap(PRED_STRING) + " m";
    
    private static final String IS_ZERO_STRING = "/n n ( /_ " + wrap(FALSE_STRING) + " )";
    private static final String LEQ_STRING = "/m /n " + wrap(IS_ZERO_STRING) + " ( "
            + wrap(SUBTRACT_STRING)
            + " m n )";
    private static final String EQ_STRING = "/m /n "
            + wrap(AND_STRING)
            + " ( " + wrap(LEQ_STRING) + " m n ) ( "
            + wrap(LEQ_STRING) + " n m )";
    
    private static final String LESS_STRING = "/m /n "
            + wrap(AND_STRING)
            + " ( "
            + wrap(LEQ_STRING)
            + " m n ) ( "
            + wrap(NOT_STRING)
            + " ( "
            + wrap(EQ_STRING)
            + " m n ) )";
    
  
    private static String wrap(String input)
    {
        return "[ " + input + " ]";
    }
    private final LambdaExpressionParser parser;
    private final Map<String, BuiltInRoot> readOnlyDict;
    public BuiltInVariables(LambdaExpressionParser parser) {
        if (parser == null) throw new IllegalArgumentException("parser is null.");
        
        this.parser = parser;
        readOnlyDict = buildDict();
    }
    // TODO: Missing pow, div
    private Map<String, BuiltInRoot> buildDict()
    {
        var dict = new HashMap<String, BuiltInRoot>(25);
        
        Variable.putAll(dict,
                identity(), trueVal(), falseVal(), zero(), voidVal(), nullVal(),
                and(), or(), not(), xor(), getIf(),
                succ(), add(), mul(), pow(),
                pred(), sub(), div(),
                isZero(), lEQ(), equality(), inequality(), lessThan(), moreThan(), gEQ(),
                yCombinator()
        );
        
        return Collections.<String, BuiltInRoot>unmodifiableMap(dict);
    }
    private Variable identity()
    {
        return new Variable("ID", parser.parse("/i i"), 0);
    }
    
    private Variable trueVal()
    {
        return new Variable("true", parser.parse(TRUE_STRING), 0);
    }
    
    private Variable falseVal()
    {
        return new Variable("false", parser.parse(FALSE_STRING), 0);
    }
    
    private Variable zero()
    {
        return new Variable("0", parser.parse(FALSE_STRING), 0);
    }
    
    private Variable voidVal()
    {
        return new Variable("void", VoidRoot.get(), 0);
    }
    
    private Variable nullVal()
    {
        return new Variable("null", NullRoot.get(), 0);
    }
    
    private Variable and()
    {
        return new Variable("&&", parser.parse(AND_STRING), 2, 30);
    }
    
    private Variable or()
    {
        return new Variable("||", parser.parse("/p /q p p q"), 2, 10);
    }
    
    private Variable not()
    {
        return new Variable("!", parser.parse(NOT_STRING), 1, 100);
    }
        
    private Variable xor()
    {
        return new Variable("^^", parser.parse("/a /b a ( " + wrap(NOT_STRING) + " b ) b"), 2, 20);
    }
    
    private Variable getIf()
    {
        return new Variable("?", parser.parse("/p /a /b p a b"), -1);
    }
    
    private Variable isZero()
    {
        return new Variable("IsZero", parser.parse(
                IS_ZERO_STRING), 1);
    }
    
    private Variable lEQ()
    {
        return new Variable("<=", parser.parse(LEQ_STRING), 2, 50);
    }
    
    private Variable equality()
    {
        return new Variable("==", parser.parse(EQ_STRING), 2, 40);
    }
    
    private Variable inequality()
    {
        return new Variable("!=", parser.parse(wrap(NOT_STRING) + " " + wrap(EQ_STRING)), 2, 40);
    }
            
    private Variable lessThan()
    {
        return new Variable("<", parser.parse(LESS_STRING), 2, 50);
    }
    
    private Variable moreThan()
    {
        return new Variable(">", parser.parse(wrap(NOT_STRING) + " " + wrap(LEQ_STRING)), 2, 50);
    }
    
    private Variable gEQ()
    {
        return new Variable(">=", parser.parse(wrap(NOT_STRING) + " " + wrap(LESS_STRING)), 2, 50);
    }
    
    
    private Variable succ()
    {
        return new Variable("++", parser.parse("/n /f /x f ( n f x )"), 1, 100);
    }
    
    private Variable add()
    {
        return new Variable("+", parser.parse("/m /n /f /x m f ( n f x )"), 2, 60);
    }
    
    private Variable mul()
    {
        return new Variable("*", parser.parse("/m /n /f /x m ( n f ) x"), 2, 70);
    }
    
    private Variable pow()
    {
        return new Variable("**", parser.parse("/m /n n m"), 2, 80);
    }
    
    private Variable pred()
    {
        return new Variable("--", parser.parse(PRED_STRING), 1, 100);
    }
    
    private Variable sub()
    {
        return new Variable("-", parser.parse(SUBTRACT_STRING), 2, 60);
    }
    
    // TODO: implement this monstrocity.
    private Variable div()
    {
        return new Variable("/", parser.parse(
                "/u u")
                , 2, 70);
    }
    
    private Variable yCombinator()
    {
        return new Variable("Y", parser.parse("/f ( /x f ( x x ) ) ( /x f ( x x ) )"), 1, 100);
    }
    
 
    /**
     * @return the readOnlyDict
     */
    public Map<String, BuiltInRoot> readOnlyDict() {
        return readOnlyDict;
    }
    private static record Variable(String name, LambdaTermRoot root, int argCount, int priority)
    {
        Variable(String name, LambdaTermRoot root, int argCount) {
            this(name, root, argCount, 0);
        }
               
        public void put(Map<String, BuiltInRoot> map)
        {
            map.put(name, new BuiltInRoot(root, argCount, priority));
        }
        
        public static void putAll(Map<String,  BuiltInRoot> map, Variable... inputs)
        {
            for(var input : inputs)
                input.put(map);
        }
    }
}
