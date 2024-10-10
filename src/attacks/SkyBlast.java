package attacks;

import game.GamePanel;
import game.GameUtility;

/**
 * The SkyBlast class defines a SkyBlast attack object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
import java.awt.*;
public class SkyBlast extends BaseAttack {
    public static final long DURATION = 500000000L;
    private static final double BASE_DAMAGE = 0.3;
    private static final int RADIUS = 80;

    /**
     * Creates a SkyBlast attack with damage, x, and y values
     * @param gamePanel The game panel
     * @param damage The damage of the attack
     * @param x The x position of the attack
     * @param y The y position of the attack
     */
    public SkyBlast (GamePanel gamePanel, double damage, double x, double y) {
        super (gamePanel, BASE_DAMAGE *damage, x, y, RADIUS);
    }

    /**
     * Loads the images used to display the attack
     */
    protected void loadImage() {
        this.image = GameUtility.getImage("/images/SkyBlast.png",radius*2,radius*2);
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
