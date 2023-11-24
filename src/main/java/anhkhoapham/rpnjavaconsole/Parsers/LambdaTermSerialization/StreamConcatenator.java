/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers.LambdaTermSerialization;

import java.util.stream.Stream;

/**
 *
 * @author Khoapa
 */
public interface StreamConcatenator {
    
    <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b);
    
}
