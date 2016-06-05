package ija.ija2015.board;

import java.io.Serializable;

/**
 *
 * @author xuhrin01, xsokol08
 * 
 * Kamen na hracej doske
 */
 
/* trieda rezentujuca jeden kamen */
public class Disk implements Serializable{
    protected boolean color;

    public Disk(boolean isWhite){
        this.color = isWhite;
    }

    public void turn(){
        this.color = !color;
    }

    public boolean isWhite(){
        return this.color;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.color ? 1 : 0);
        return hash;
    }

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
        final Disk other = (Disk) obj;
        if (this.color != other.color) {
            return false;
        }
        return true;
    }

}
