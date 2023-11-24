/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anhkhoapham.rpnjavaconsole.Parsers;

/**
 *
 * @author Khoapa
 */
public record BracketPair(String leftBracket, String rightBracket) {
    
    public BracketPair swap(){
        return new BracketPair(rightBracket, leftBracket);
    }
    
}
