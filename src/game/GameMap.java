package game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The GameMap class defines the map of the game
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class GameMap {
    private BufferedImage image;
    private GamePanel gamePanel;

    public GameMap(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.image = GameUtility.getImage("/images/GameMap.png");
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image,(int)Math.round(gamePanel.screenOffsetX),(int)Math.round(gamePanel.screenOffsetY),GamePanel.GAME_WIDTH,GamePanel.GAME_HEIGHT,null);
    }
}
