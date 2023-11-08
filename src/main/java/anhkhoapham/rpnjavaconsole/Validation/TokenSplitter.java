/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Validation;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author Khoapa
 */
public final class TokenSplitter implements Function<String, List<String>> {

    /**
     * "Hello (world) {don't split this} [square brackets]"
     */
    private final Pattern pattern = Pattern.compile("\\{[^}]*\\}|[^\\s()\\[\\]]+|\\(|\\)|\\[|\\]|,");
    
    @Override
    public List<String> apply(String t) {
        Matcher matcher = pattern.matcher(t);

        List<String> tokens = new ArrayList<>(50);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        
        return tokens;
    }
    
}
