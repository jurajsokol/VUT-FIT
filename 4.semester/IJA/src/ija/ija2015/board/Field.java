package ija.ija2015.board;

/**
 *
 * @author xuhrin01, xsokol08
 * 
 * Rozhranie pre oba typy políčok
 */
public interface Field {
   public void addNextField(Field.Direction dirs,Field field); 
   public Field nextField(Field.Direction dirs);
   public boolean putDisk(Disk disk);
   public Disk getDisk();
   public boolean canPutDisk();
   public boolean isEmpty();
   public int[] pozicia();
   public void removeDisk();
   
   static enum Direction{
       L, LU, U, RU, R, RD, D, LD;
   }
}
