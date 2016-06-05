/*
 * @author uhrin01, xsokol08
 *
 * Trieda pre skóre
 */
package ija.ija2015.game;

public class Skore {
    protected int white;
    protected int black;
    
    public Skore(){
        this.white = 2;
        this.black = 2;
    }
    
    public void addPoint(boolean color){
        if(color){
            this.white ++;
            this.black --;
        }
        else{
            this.white --;
            this.black ++;
        }
    }
    
    public int showSkore(boolean color){
        if(color){
            return this.white;
        }
        return this.black;
    }
}
