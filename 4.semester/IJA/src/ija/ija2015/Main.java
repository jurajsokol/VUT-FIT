/*
logins: uhrin01, xsokol08
Hlavná trieda, spúšťa a inicializuje otatné objekty.
*/

package ija.ija2015;

 // impoort UI balikov 
import UI.welcome;

// import logiky
import ija.ija2015.board.Board;
import ija.ija2015.game.Game;
import ija.ija2015.game.ReversiRules;
import ija.ija2015.game.HumanPlayer;
import UI.GameUI;
import ija.ija2015.game.PCplayer;
import ija.ija2015.game.Player;
import java.awt.Frame;
import javax.swing.JFrame;


public class Main {
    private static int okna = 0;
    
    public static void main(String args[]){
       welcome.main();
    }
    
    public static void pidaj_okno(){
        Main.okna++;
    }
    
    public static int exit(JFrame frame){
        System.out.println(Main.okna);
        frame.dispose();
        Main.okna--;
        if(Main.okna == 0){
            System.exit(0);
        }
        return 2;
    }
    
    public static void initGame(int bSize, String hrac){
        Player p2;
        ReversiRules rules = new ReversiRules(bSize);
        Board board = new Board(rules);
        Game game = new Game(board, hrac);
       
        Player p1 = new HumanPlayer(true, bSize*bSize/2, board);
        if(hrac == "človek"){
            p2 = new HumanPlayer(false, bSize*bSize/2, board);
        }
        else{
            p2 = new PCplayer(false, bSize*bSize/2, board);
        }
        
        game.addPlayer(p1);
        game.addPlayer(p2);
        GameUI ui = new GameUI(bSize, game);
        ui.main(bSize, game);
        game.Play(ui);
    }
}
