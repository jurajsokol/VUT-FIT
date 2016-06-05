/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.ija2015.game;

import UI.GameUI;
import ija.ija2015.board.Board;
import ija.ija2015.board.Field;

/**
 *
 * @author juraj
 */
public interface Player {

    @Override
    public String toString();
    
    public Field.Direction invertDirection (Field.Direction d);
    
    public boolean emptyPool();
    
    public void init(Board board);
    
    public boolean canPutDisk(Field field);
    
    public boolean putDisk(Field field, GameUI ui, Game g);
    
    public boolean isWhite();

    public boolean testCanPutDisk();
    
    public boolean player();
    
    public boolean randomField(GameUI ui);
}
