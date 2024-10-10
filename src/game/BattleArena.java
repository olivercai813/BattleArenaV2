package game;

import javax.swing.*;

/**
 * The BattleArena class creates the JFrame of the game and launches it
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class BattleArena extends JFrame {

    public void launch() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Battle Arena Game");
        setUndecorated(true);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        gamePanel.configGame();
        gamePanel.startGameThread();
    }
    public static void main(String[] args) {
        new BattleArena().launch();
    }
}
