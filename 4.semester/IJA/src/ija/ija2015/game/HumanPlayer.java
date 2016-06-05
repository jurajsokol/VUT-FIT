/*
 * @author xuhrin01, xsokol08
 * 
 * Trieda jedného hráča
 */
package ija.ija2015.game;

import UI.GameUI;
import UI.Policko;
import ija.ija2015.board.Board;
import ija.ija2015.board.Disk;
import ija.ija2015.board.Field;
import ija.ija2015.undo.Move;
import java.io.Serializable;

public class HumanPlayer implements Player, Serializable{
    protected boolean isWhite;
    protected int pocet_kamenov;
    protected Board b;
    public boolean human;
    
    public HumanPlayer(boolean isWhite, int kamene, Board b){
        this.isWhite = isWhite;
        this.pocet_kamenov = kamene;
        this.b = b;
        this.human = true;
    }

    @Override
    public String toString() {
        if(this.isWhite){
            return "white";
        }
        else{
            return "black";
        }
    }
    
    public Field.Direction invertDirection (Field.Direction d) {
        Field.Direction r = null;
        switch(d){
            case L: r = Field.Direction.R;
                    break;
            case LU: r = Field.Direction.RD;
                    break;
            case U: r = Field.Direction.D;
                    break;
            case RU: r = Field.Direction.LD;
                    break;
            case R: r = Field.Direction.L;
                    break;
            case RD: r = Field.Direction.LU;
                    break;
            case D: r = Field.Direction.U;
                    break;
            case LD: r = Field.Direction.RU;
                    break;
        }
        return r;
    }
    
    public boolean emptyPool(){
        return this.pocet_kamenov == 0;
    }
    
    public void init(Board board){
        this.pocet_kamenov = board.getSize()*board.getSize()/2;
        if(this.isWhite){
            Disk d = new Disk(this.isWhite);
            board.getField(board.getSize() / 2, board.getSize() / 2).putDisk(d);
            Disk d2 = new Disk(this.isWhite);
            board.getField((board.getSize() / 2) + 1, (board.getSize() / 2) + 1).putDisk(d2);
        }
        else{
            Disk d = new Disk(this.isWhite);
            board.getField((board.getSize() / 2) + 1, board.getSize() / 2).putDisk(d);
            Disk d2 = new Disk(this.isWhite);
            board.getField(board.getSize() / 2, (board.getSize() / 2) + 1).putDisk(d2);
        }
        this.pocet_kamenov -= 2;
    }
    
    public boolean testCanPutDisk(){
        for(int i = 1; i<=this.b.getSize();i++){
            for(int j = 1; j<=this.b.getSize();j++){
                if(this.canPutDisk(this.b.getField(i, j))){
                    return true;
                }
                
            }
        }
        return false;
    }
    
    public boolean canPutDisk(Field field){
        Field f; // pomocná premenná pre políčko
        if(field.isEmpty()){
            for (Field.Direction d : Field.Direction.values()) { // iteruje každým smerom
                f = field.nextField(d);
                if(!f.isEmpty()){ // policko musi byt neprazdne
                    if(f.getDisk().isWhite() != this.isWhite){ // kontrolue okolie, mala by byť opacna farba
                        f = f.nextField(d); // dalsie policka
                        while(!f.isEmpty()){ // ak je neprazdne
                            if(f.getDisk().isWhite() == this.isWhite){ // rovnaky kamen sa nasiel
                                return true;
                            }
                            f = f.nextField(d);
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean player(){
        return this.human;
    }
    
    public boolean putDisk(Field field, GameUI ui, Game g){
        if(field.canPutDisk() == true){
            Move move = new Move(field);
            int[] pozicia;
            Field f;
            Policko p;
            Disk disk = new Disk(this.isWhite);
            field.putDisk(disk);
            pozicia = field.pozicia();
            ui.vratPolicko(--pozicia[0], --pozicia[1]).turn(this.isWhite);
            int pocet = 1;
            boolean r = true;
            
            if(this.pocet_kamenov == 0){
                System.out.println("Koniec");
                return false;
            }
            this.pocet_kamenov -= 1;

            for (Field.Direction d : Field.Direction.values()) {
                f = field.nextField(d);
                if(!f.isEmpty()){ // kontroluje, či nie je prázdny
                   if(f.getDisk().isWhite() != this.isWhite){ // kontrolue okolie, musi byť opacna farba
                        f = f.nextField(d);
                        while(!f.isEmpty()){ // za nim musia byt nejake kamene
                            if(f.getDisk().isWhite() == this.isWhite){ // rovnaky kamen sa nasiel
                                Field.Direction reversed = this.invertDirection(d);
                                f = f.nextField(reversed);
                                while(f.getDisk().isWhite() != this.isWhite){
                                    f.getDisk().turn();
                                    move.addField(f);
                                    pocet += 1;
                                    pozicia = f.pozicia();
                                    ui.vratPolicko(--pozicia[0], --pozicia[1]).turn(this.isWhite);
                                    f = f.nextField(reversed);
                                }
                                //System.out.println(f.getDisk().isWhite());
                                break;
                            }
                            f = f.nextField(d);
                        }
                    }
                }
            }
            g.addMove(move);
            ui.setPlayer(!this.isWhite);
            ui.skore(this.isWhite, pocet);
            return ui.kontrolaKamenov(!this.isWhite(), true);
        }
        return false;
    }
    
    public boolean isWhite(){
        return this.isWhite;
    }

    @Override
    public boolean randomField(GameUI u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
