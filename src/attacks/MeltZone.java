package attacks;

import game.GamePanel;
import game.GameUtility;
import java.awt.*;

/**
 * The MeltZone class defines a MeltZone attack object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class MeltZone extends BaseAttack {
    public static final long DURATION = 3000000000L;
    public static final double SLOW_FACTOR = 0.6;
    public static final long SLOW_TIME = 3000000000L;
    private static final double BASE_DAMAGE = 0.04;
    private static final int RADIUS = 300;

    /**
     * Creates a MeltZone attack with damage, x, and y values
     * @param gamePanel The game panel
     * @param damage The damage of the attack
     * @param x The x position of the attack
     * @param y The y position of the attack
     */
    public MeltZone(GamePanel gamePanel, double damage, double x, double y) {
        super (gamePanel, BASE_DAMAGE * damage, x, y, RADIUS);
    }

    /**
     * Loads the images used to display the attack
     */
    protected void loadImage() {
        this.image = GameUtility.getImage("/images/MeltZone.png",radius*2,radius*2);
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
