package ija.ija2015.board;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author xuhrin01, xsokol08
 * 
 * Jedno okrajove policko, neda a nan vlozit kamen
 */
public class BoardField implements Field, Serializable{
    protected int row;
    protected int column;
    protected Disk disk = null;
    private Field[][] pole;
    
    /**
     *
     * @param row
     * @param col
     */
    public BoardField(int row, int col, Field[][] f){
        this.row = row;
        this.column = col;
        this.pole = f;
    }
    
    /**
     *
     * @return
     */
    @Override
    public boolean isEmpty(){
        return (this.disk == null);
    }
    
    /**
     *
     * @return
     */
    @Override
    public boolean canPutDisk(){
        return this.disk == null;
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
    
    /**
     *
     * @param dirs
     * @return
     */
    @Override
    public Field nextField(Field.Direction dirs){
        int[] xy = d(this.row, this.column, dirs);
        return pole[xy[0]][xy[1]];
    }
    
    public int[] pozicia(){
        int[] s = {this.row, this.column}; 
        return s;
    }

    /**
     *
     * @param dirs
     * @param field
     */
    @Override
    public void addNextField(Direction dirs, Field field) {
        int[] xy = d(this.row, this.column, dirs);
        pole[xy[0]][xy[1]] = field;
    }

    /**
     *
     * @param disk
     * @return
     */
    @Override
    public boolean putDisk(Disk disk) {
        if(this.disk == null){
            this.disk = disk;
            return true;
        }
        else{
            return false;
        }
    }
    
    public void removeDisk(){
        this.disk = null;
    }

    /**
     *
     * @return
     */
    @Override
    public Disk getDisk() {
        return this.disk;
    }
    
    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.row;
        hash = 29 * hash + this.column;
        hash = 29 * hash + Objects.hashCode(this.disk);
        return hash;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BoardField other = (BoardField) obj;
        if (this.row != other.row) {
            return false;
        }
        if (this.column != other.column) {
            return false;
        }
        if (!Objects.equals(this.disk, other.disk)) {
            return false;
        }
        return true;
    }
    
}
