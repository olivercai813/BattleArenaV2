package attacks;


import game.GamePanel;
import game.GameUtility;

import java.awt.*;

/**
 * The MegaBlast class defines an MegaBlast attack object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class MegaBlast extends Projectile {
    private static final int RADIUS = 50;
    private static final int RANGE = 3000;
    private final static int PROJECTILE_SPEED = 20;

    /**
     * Creates a MegaBlast attack with damage, x, and y values, and a target point
     * @param gamePanel
     * @param abilityDmg
     * @param x
     * @param y
     * @param target
     */
    public MegaBlast (GamePanel gamePanel, double abilityDmg, double x, double y, Point target) {
        super(gamePanel, abilityDmg*2, x, y, RADIUS, target, PROJECTILE_SPEED, RANGE);
    }

    /**
     * Loads the images used to display the attack
     */
    @Override
    protected void loadImage() {
        this.image = GameUtility.getImage("/images/MegaBlast.png",radius*2,radius*2);
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
