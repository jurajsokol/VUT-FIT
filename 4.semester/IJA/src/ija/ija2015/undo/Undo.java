/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.ija2015.undo;

import UI.GameUI;
import ija.ija2015.game.Game;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juraj
 */
public class Undo{
    private List<Command> history = new ArrayList<Command>();

    public void store(Command cmd) {
        this.history.add(cmd); // optional 
    }
    
    public void execute(GameUI ui, Game g){
        if(this.history.size() != 0){
            this.history.get(this.history.size() - 1).execute(ui, g);
            this.history.remove(this.history.size() - 1);
    
        }
    }
}
