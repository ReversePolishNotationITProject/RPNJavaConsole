/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.CommandLine.NotationSelection;

import java.util.function.Consumer;

/**
 * Default is input.
 * @author Khoapa
 */
public interface InputOutputNotationSelection {
    
    void input(Consumer<? super PnRpnInfixNotationSelection> selection);
    
    void output(Consumer<? super PnRpnNotationSelection> selection);
}
