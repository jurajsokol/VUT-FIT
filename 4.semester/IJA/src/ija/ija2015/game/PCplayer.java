/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.ija2015.game;

import UI.GameUI;
import ija.ija2015.board.Board;
import ija.ija2015.board.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author juraj
 */
public class PCplayer extends HumanPlayer{
    Random r;

    public PCplayer(boolean b, int kamene, Board board) {
        super(b, kamene, board);
        super.human = false;
    }
    
    public boolean randomField(GameUI ui){
        List<Field> policka = new ArrayList<Field>();
        Field f;
        
        for(int i  = 1; i<=super.b.getSize();i++){
            for(int j  = 1; i<=super.b.getSize();j++){
                f = super.b.getField(i, j);
                if(super.canPutDisk(f)){
                    policka.add(f);
                }
            }
        }
        
        f = policka.get(r.nextInt(policka.size()));
        System.out.println("random" + f.pozicia()[0] + " " + f.pozicia()[1]);
        super.putDisk(f, ui, null);
        return true;
    }

}
