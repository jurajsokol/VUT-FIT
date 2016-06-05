/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;
import ija.ija2015.game.Game;
import ija.ija2015.board.Field;
import ija.ija2015.game.HumanPlayer;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author juraj
 */
public class Policko extends javax.swing.JLabel{
    protected int[] rc = new int[2];
    Game g;
    GameUI u;
    
    public Policko(int r, int c, Game g, GameUI u){
        this.rc[0] = ++r;
        this.rc[1] = ++c;
        this.g = g;
        this.u = u;
        //this.setText(r + " " + c);
        this.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        FieldMouseClicked(evt);
                    }
                });
    }
    
    public void turn(boolean isWhite){
        //this.setText("O");
        if(isWhite){
            ImageIcon white = new ImageIcon(new ImageIcon("src/kamen.png").getImage().getScaledInstance(450/u.pocet, 450/u.pocet, Image.SCALE_DEFAULT));
            this.setIcon(white);
            //this.setForeground(Color.white);
        }
        else{
            ImageIcon black = new ImageIcon(new ImageIcon("src/black.png").getImage().getScaledInstance(450/u.pocet, 450/u.pocet, Image.SCALE_DEFAULT));
               this.setIcon(black);
//this.setForeground(Color.black);
        }
    }
    
    public int[] poloha(){
        
        return this.rc;
    }
    
    public int[] d(int row, int column, Field.Direction dirs){
        int[] direction = {row, column};
        
        switch (dirs){
            case L: direction[1]--;
                    break;
            case LU: direction[0]--;
                     direction[1]--;
                     break;
            case U: direction[0]--;
                    break;
            case RU: direction[0]--;
                     direction[1]++;
                     break;
            case R: direction[1]++;
                    break;
            case RD: direction[0]++;
                     direction[1]++;
                     break;
            case D: direction[0]++;
                    break;
            case LD: direction[0]++;
                     direction[1]--;
                     break;
        }
        return direction;
    }
    
    public Policko nextPolicko(Field.Direction d){
        int[] xy = this.d(this.rc[0], this.rc[1], d);
        return u.pole[xy[0]][xy[1]];
    }
    
    
    private void FieldMouseClicked(java.awt.event.MouseEvent evt) {
        Field f = g.getBoard().getField(rc[0], rc[1]); // získanie políčka
        if(f.canPutDisk()){ // kontrrola, či môže položiť kameň
            if(g.currentPlayer().canPutDisk(f)){ // kontrola, či hráč môže položiiť kameň
                g.currentPlayer().putDisk(f, this.u, this.g); // položí kameň
                g.nextPlayer();
                g.neplatny_tah = false;
            }
            else{
                System.out.println("Nemožno položiť na " + this.rc[0] + " " + this.rc[1]);
            }
        }
        else{
            System.out.println("disk");
        }
        
    }

    public void changeColour(HumanPlayer p) {
        if(p.isWhite()){
            this.setForeground(Color.WHITE);
        }
        else{
            this.setForeground(Color.BLACK);
        }
        this.validate();
    }
}
