/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.ija2015.undo;

import UI.GameUI;
import ija.ija2015.board.Field;
import ija.ija2015.game.Game;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juraj
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juraj
 */
public class Move implements Command{
    private Field polozeny_kamen;
    private List<Field> otocene_kamene = new ArrayList<Field>();
    
    public Move(Field f){
        this.polozeny_kamen = f;
    }
    
    public void addField(Field f){
        this.otocene_kamene.add(f);
    }
    
    
    public void execute(GameUI ui, Game g) {
        int[] pozicia = this.polozeny_kamen.pozicia();
        ui.vratPolicko(--pozicia[0], --pozicia[1]).setIcon(null);
        this.polozeny_kamen.removeDisk();
        
        boolean isWhite = this.otocene_kamene.get(0).getDisk().isWhite();
        for(int i = 0; i<this.otocene_kamene.size();i++){
            this.otocene_kamene.get(i).getDisk().turn();
            pozicia = this.otocene_kamene.get(i).pozicia();
            ui.vratPolicko(--pozicia[0], --pozicia[1]).turn(!isWhite);
        }
        
        g.nextPlayer();
        ui.undo(this.otocene_kamene.size(), isWhite);
    }
    
}
