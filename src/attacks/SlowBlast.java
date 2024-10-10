package attacks;

import game.GamePanel;
import game.GameUtility;
import java.awt.*;

/**
 * The SlowBlast class defines a SlowBlast attack object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class SlowBlast extends Projectile {
    public static final double SLOW_FACTOR = 0.4;
    public static final long SLOW_TIME = 3000000000L;
    private static final int RADIUS = 10;
    private static final int RANGE = 400;
    private final static int PROJECTILE_SPEED = 30;

    /**
     * Creates a SlowBlast attack with damage, x, and y values and a target point
     * @param gamePanel The game panel
     * @param abilityDmg The damage of the attack
     * @param x The x position of the attack
     * @param y The y position of the attack
     * @param target The target point of the attack
     */
    public SlowBlast(GamePanel gamePanel, double abilityDmg, double x, double y, Point target) {
        super(gamePanel, abilityDmg, x, y, RADIUS, target, PROJECTILE_SPEED, RANGE);
    }

    /**
     * Loads the images used to display the attack
     */
    protected void loadImage() {
        this.image = GameUtility.getImage("/images/SlowBlast.png",radius*2,radius*2);
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
