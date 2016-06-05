package ija.ija2015.board;

import java.io.Serializable;

/**
 *
 * @author xuhrin01, xsokol08
 * 
 * Reprezentuje hraciu dosku
 */

public class Board implements Serializable{
    private final Rules rule;
    protected Field[][] pole;
    
    public Board(Rules rules){
        this.rule = rules;
        int size = rules.getSize();
        this.pole = new Field[size+2][size+2];
        
        // inicializuje hraciu dosku
        for(int i = 1; i <= size; i++ ){
            for(int j = 1; j <= size; j++){
                this.pole[i][j] = new BoardField(i, j, this.pole);
            }
        }
        
        // inicializuje okraje
        for(int i = 0; i <= size+1; i++){
            Field f = new BorderField();
            // horizontalne
            this.pole[i][0] = f;
            this.pole[i][size+1] = f;
            //vertikalne
            this.pole[0][i] = f;
            this.pole[size+1][i] = f;
        }
    }
    
    /**
     *
     * @param row
     * @param col
     * @return
     */
    public Field getField(int row, int col){
        return this.pole[row][col];
    }
    
    Rules getRules(){
        return this.rule;
    }
    
    public void newField(int i, int j){
        this.pole[i + 1][j + 1] = new BoardField(i + 1, j + 1, this.pole);
    }
    /**
     *
     * @return
     */
    public int getSize(){
        return this.rule.getSize(); 
    }
}
