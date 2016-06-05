package ija.ija2015.board;

import java.io.Serializable;

/**
 *
 * @author xuhrin01, xsokol08
 * 
 * Jedno policko hracej dosky, na ktorom je kamen
 */
public class BorderField implements Field, Serializable{
    protected Disk d = null;
    
    @Override
    public boolean isEmpty(){
        return true;
    }
    
    @Override
    public boolean canPutDisk(){
        return false;
    }
    
    @Override
    public void addNextField(Direction dirs, Field field) {
        return;
    }

    @Override
    public Field nextField(Field.Direction dirs){
        return null;
    }
    
    public int[] pozicia(){
        return null;
    }
    
    @Override
    public boolean putDisk(Disk disk){
        if(null == this.d){
            this.d = disk;
            return true;
        }
        else {
            return false;
        }
    }
    
    @Override
    public Disk getDisk(){
       return this.d; 
    }

    @Override
    public void removeDisk() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
