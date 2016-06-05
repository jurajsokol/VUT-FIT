/*
 * @author uhrin01, xsokol08
 *
 * Trieda obsahujúca pravidlá hry Reversi 
 */
package ija.ija2015.game;

import ija.ija2015.board.BoardField;
import ija.ija2015.board.Rules;
import ija.ija2015.board.Field;
import java.io.Serializable;

public class ReversiRules implements Rules, Serializable{
    protected int kamene;
    protected int velkost;
    
    public ReversiRules(int size){
        
        this.velkost = size;
        this.kamene = size*size/2;
    }
    
    public int getSize(){
        return this.velkost;
    }
    
    public int numberDisks(){
        return this.kamene;
    }
    
    /**public Field createField(int row, int col){
        return new BoardField(row, col);
    }
    */
}
