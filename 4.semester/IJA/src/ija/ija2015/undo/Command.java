/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.ija2015.undo;

import UI.GameUI;
import ija.ija2015.board.Field;
import ija.ija2015.game.Game;

/**
 *
 * @author juraj
 */
public interface Command {
    public void execute(GameUI ui, Game g);
    
    public void addField(Field f);
}
