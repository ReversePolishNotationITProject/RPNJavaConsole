/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * Notify to all subscribers when the stored item is set.
 * @author Khoapa
 * @param <T>
 */
public final class Host<T> {
    
    private T item;
    
    private final LinkedList<Consumer<T>> observers = new LinkedList<>();
    
    public void addRange(Consumer<T>... observers)
    {
        this.observers.addAll(Arrays.asList(observers));
    }
    
    public void addRange(Collection<Consumer<T>> observers)
    {
        this.observers.addAll(observers);
    }
    
    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
        
        for (var observer : observers)
            observer.accept(item);
    }
    
}
