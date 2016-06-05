/*
 * @author xuhhrin01, xsokol08
 * Trieda reprezentuje konkrétnu hru
 */
package ija.ija2015.game;

import UI.GameUI;
import ija.ija2015.board.Board;
import ija.ija2015.undo.Move;
import ija.ija2015.undo.Undo;
import java.io.*;

public class Game implements Serializable{
    protected Board board;
    protected Player[] players = {null, null};
    protected int current_player;
    protected transient GameUI ui;
    protected String hrac;
    public boolean neplatny_tah = false; // k jed hrac nemoze tahat id druhy
    protected transient Undo undo;
    public int[] skore = {2, 2};
    
    public Game(Board board, String hrac){
        this.hrac = hrac;
        this.board = board;
        this.undo = new Undo();
    }
    
    public void createUndo(){
        this.undo = new Undo();
    }
    
    public void addMove(Move m){
        this.undo.store(m);
    }
    
    public void undo(GameUI u){
        this.undo.execute(u, this);
    }
    
    
    public boolean addPlayer(Player player){
        if(this.players[0] == null){
            this.players[0] = player;
            this.players[0].init(this.board);
            return true;
        }
        else if(this.players[1] == null && this.players[0].isWhite() != player.isWhite()){
            this.players[1] = player;
            player.init(this.board);
            this.current_player = 1;
            return true;
        }
        return false;
    }
    
    public Player currentPlayer(){
        return this.players[this.current_player];
    }
    
    public Board getBoard(){
        return this.board;
    }
    
    public boolean nextPlayer(){
        
        
        if(this.current_player == 1){
            this.current_player = 0;
        }
        else{
            this.current_player = 1;
        }
        
        
        if(!this.currentPlayer().testCanPutDisk()){
            if(this.neplatny_tah){
                this.ui.kontrolaKamenov(!this.currentPlayer().isWhite(), false);
                return true;
            }
            this.neplatny_tah = true;
            this.nextPlayer();
        }
        
        if(this.currentPlayer().player() == false){
            this.currentPlayer().randomField(ui);
            this.nextPlayer();
        }
        
        this.neplatny_tah = false;
        return true;
    }
    
    public void Play(GameUI ui){
        this.ui = ui;
    }

    public boolean tah(){
        return true;
    }
    
    public void print(int p[]){
        System.out.println(p[0] + ", " + p[1]);
    }
    
    public void saveGame(String fileN){
       try {   
        FileOutputStream fileStream = new FileOutputStream(fileN + ".save");   
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);   

        objectStream.writeObject(this);   

        objectStream.close();   
        fileStream.close();   
        } catch (Exception e) {   
            System.out.println("Save object");   
        }   
    }
}
