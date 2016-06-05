/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
logins: uhrin01, xsokol08
Tento súbor obsahuje kód na vygenerovanie rozhrania pre hru.
Zobrazí hracciu doku a jednotlivé políčka.
*/

package UI;
import ija.ija2015.Main;
import ija.ija2015.game.Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Window;
import javax.swing.ImageIcon;

/**
 *
 * @author xuhrin01, xsokol08
 */
public class GameUI extends javax.swing.JFrame {
    protected Game g;
    Policko pole[][];
    int[] skore = {2, 2};
    int[] pocet_kamenov;
    protected int board_size;
    public int pocet;
    
    /**
     * Creates new form GameUI
     * @param p
     * @param g
     */
    public GameUI(int p, Game g) {
        this.g = g;
        this.board_size = p;
        initComponents();
        pridajPolicka(p);
        int s = g.getBoard().getSize();
        pocet_kamenov = new int[2];
        pocet_kamenov[0] = s*s/2 - 2;
        pocet_kamenov[1] = s*s/2 - 3;
    }
    
    public boolean kontrolaKamenov(boolean isWhite, boolean kontrola){
        boolean vyhral;
        
        if(this.skore[0] > this.skore[1]){
            vyhral = true;
        }
        else{
            vyhral = false;
        }
 
        
        if(!kontrola){
            Win.main(vyhral);
        }
        else{
            if(isWhite){
                if(this.pocet_kamenov[0] == 0){
                    Win.main(vyhral);
                    return false;
                }
                else{
                    this.pocet_kamenov[0] -= 1;
                }
            }
            else{
                if(this.pocet_kamenov[1] == 0){
                    Win.main(vyhral);
                    return false;
                }
                else{
                    this.pocet_kamenov[1] -= 1;
                }
            }
        }
        return true;
    }
    
    public void skore(boolean isWhite, int pocet){
        if(isWhite){
            
            this.skore[0] += pocet;
            this.skore[1] -= pocet - 1;
            
            this.pocet_kamenov[0] += 1;
        }
        else{
            this.skore[0] -= pocet  - 1;
            this.skore[1] += pocet;
            
            this.pocet_kamenov[1] += 1;
        }
        g.skore = this.skore;
        skore_b.setText("<html><p>Biely: " + this.skore[0] + "</p></html>");
        Skore_c.setText("<html><p>Čierny: " + this.skore[1] + "</p></html>");
    }
    
    private void pridajPolicka(int pocet){
        this.pocet = pocet;
        this.pole = new Policko[pocet][pocet];
        jPanel4.setLayout(new java.awt.GridLayout(pocet, pocet));
        java.awt.Color borderC = new java.awt.Color(50, 50, 50); // farba hrany
        java.awt.Dimension fieldDimension = new java.awt.Dimension(60, 60);
        java.awt.Font fieldFont = new java.awt.Font("Roboto", 1, 16);
        Dimension d = new Dimension(100, 100);
        // ytvorí hraciu plochu
        for(int i = 0; i < pocet; i++){
            for(int j = 0; j<pocet; j++ ){
                this.pole[i][j] = new Policko(i, j, this.g, this); // nové políčko
                this.pole[i][j].setMinimumSize(d);
                this.pole[i][j].setFont(fieldFont); // nastaví font
                this.pole[i][j].setBackground(Color.GREEN); // nastaví pozadie
                this.pole[i][j].setPreferredSize(fieldDimension);
                this.pole[i][j].setOpaque(true); // povolí farbu
                this.pole[i][j].setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // odsadenie textu
                this.pole[i][j].setBorder(javax.swing.BorderFactory.createLineBorder(borderC)); //orámovanie
                jPanel4.add(this.pole[i][j]); // prida ku paanelu
            }
        }
        
        // inicializuje počiatočné kamene
        ImageIcon white = new ImageIcon(new ImageIcon("src/kamen.png").getImage().getScaledInstance(450/pocet, 450/pocet, Image.SCALE_DEFAULT));
        ImageIcon black = new ImageIcon(new ImageIcon("src/black.png").getImage().getScaledInstance(450/pocet, 450/pocet, Image.SCALE_DEFAULT));
        this.pole[pocet/2-1][pocet/2].setIcon(black);
        this.pole[pocet/2][pocet/2-1].setIcon(black);
        this.pole[pocet/2][pocet/2].setIcon(white);
        this.pole[pocet/2][pocet/2].setForeground(Color.white);
        this.pole[pocet/2-1][pocet/2-1].setIcon(white);
        this.pole[pocet/2-1][pocet/2-1].setForeground(Color.white);
        
        /*this.pole[pocet/2-1][pocet/2].setText("O");
        this.pole[pocet/2][pocet/2-1].setText("O");
        this.pole[pocet/2][pocet/2].setText("O");
        this.pole[pocet/2][pocet/2].setForeground(Color.white);
        this.pole[pocet/2-1][pocet/2-1].setText("O");
        this.pole[pocet/2-1][pocet/2-1].setForeground(Color.white);*/
    }
    
    public Policko vratPolicko(int r, int c){
        return this.pole[r][c];
    }
    
    public void setPlayer(boolean isWhite){
        if(isWhite){
            skore_b.setFont(new java.awt.Font("Roboto", 1, 20));
            Skore_c.setFont(new java.awt.Font("Roboto", 0, 20));
        }
        else{
            skore_b.setFont(new java.awt.Font("Roboto", 0, 20));
            Skore_c.setFont(new java.awt.Font("Roboto", 1, 20));
        }
    }
    
    public void loadGame(){
        ImageIcon white = new ImageIcon(new ImageIcon("src/kamen.png").getImage().getScaledInstance(450/pocet, 450/pocet, Image.SCALE_DEFAULT));
        ImageIcon black = new ImageIcon(new ImageIcon("src/black.png").getImage().getScaledInstance(450/pocet, 450/pocet, Image.SCALE_DEFAULT));
        
        for(int i = 1; i<=this.board_size;i++){
            for(int j = 1; j<=this.board_size;j++){
                if(!g.getBoard().getField(i, j).isEmpty()){
                    if(g.getBoard().getField(i, j).getDisk().isWhite()){
                        this.pole[i-1][j-1].setIcon(white);
                    }
                    else{
                        this.pole[i-1][j-1].setIcon(black);
                    }
              }
          }
      }
      this.skore = g.skore;
      skore_b.setText("<html><p>Biely: " + this.skore[0] + "</p></html>");
        Skore_c.setText("<html><p>Čierny: " + this.skore[1] + "</p></html>");
      this.setPlayer(g.currentPlayer().isWhite());;
    }
    
    public void undo(int pocet, boolean isWhite){
        if(isWhite){
            
            this.skore[1] += pocet;
            this.skore[0] -= pocet + 1;
        }
        else{
            this.skore[1] -= pocet + 1;
            this.skore[0] += pocet;
        }
        
        
        skore_b.setText("<html><p>Biely: " + this.skore[0] + "</p></html>");
        Skore_c.setText("<html><p>Čierny: " + this.skore[1] + "</p></html>");
        
        this.setPlayer(isWhite);
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        skore_b = new javax.swing.JLabel();
        Skore_c = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        Exit = new javax.swing.JButton();
        NovaHra = new javax.swing.JButton();
        SaveBtn = new javax.swing.JButton();

        setDefaultCloseOperation(Main.exit(this));
        setTitle("Othelo hra");
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBackground(new java.awt.Color(50, 50, 50));
        jPanel4.setMinimumSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 451, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)), "Skóre", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 0, 18))); // NOI18N

        skore_b.setBackground(new java.awt.Color(0, 0, 240));
        skore_b.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        skore_b.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        skore_b.setText("Biely: 2");

        Skore_c.setBackground(new java.awt.Color(0, 0, 240));
        Skore_c.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        Skore_c.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Skore_c.setText("Čierny: 2");
        Skore_c.setToolTipText("");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Skore_c, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skore_b, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(skore_b)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Skore_c)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        Exit.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        Exit.setText("Undo");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });

        NovaHra.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        NovaHra.setText("Nová hra");
        NovaHra.setFocusPainted(false);
        NovaHra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NovaHraActionPerformed(evt);
            }
        });

        SaveBtn.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        SaveBtn.setText("Ulož");
        SaveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(SaveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(NovaHra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NovaHra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Exit)
                    .addComponent(SaveBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(177, 177, 177))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        this.g.undo(this);
    }//GEN-LAST:event_ExitActionPerformed

    private void NovaHraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NovaHraActionPerformed
        welcome.main();
    }//GEN-LAST:event_NovaHraActionPerformed

    private void SaveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveBtnActionPerformed
        Save.main(this.g);
    }//GEN-LAST:event_SaveBtnActionPerformed

    public static void main(int p, Game g) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameUI gui = new GameUI(p, g);
                gui.setLocationRelativeTo(null);
                gui.getContentPane().setBackground( Color.WHITE );
                Main.pidaj_okno();
                gui.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Exit;
    private javax.swing.JButton NovaHra;
    private javax.swing.JButton SaveBtn;
    private javax.swing.JLabel Skore_c;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JLabel skore_b;
    // End of variables declaration//GEN-END:variables
}
