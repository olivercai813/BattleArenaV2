package attacks;

import game.GamePanel;
import game.GameUtility;

import java.awt.*;

/**
 * The AxeSwing class defines an AxeSwing attack object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class AxeSwing extends BaseAttack {
    public static final long DURATION = 50000000;
    private static final int RADIUS = 150;

    /**
     * Creates an AxeSwing attack with damage, x, and y values
     * @param gamePanel The game panel
     * @param damage The damage of the attack
     * @param x The x position of the attack
     * @param y The y position of the attack
     */
    public AxeSwing(GamePanel gamePanel, double damage, double x, double y) {
        super (gamePanel, 2*damage, x, y, RADIUS);
    }

    /**
     * Loads the images used to display the attack
     */
    @Override
    protected void loadImage() {
        this.image = GameUtility.getImage("/images/AxeSwing.png",radius*2,radius*2);
    }

    /**
     * Draws the attack to the JPanel
     * @param g The graphics object
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(image,getScreenX()-RADIUS,getScreenY()-RADIUS, null);
    }
}
