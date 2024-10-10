package attacks;

import entities.*;
import game.GamePanel;
import game.GameUtility;

import java.awt.*;

/**
 * The AutoAttack class defines an AutoAttack object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class AutoAttack extends BaseAttack {
    private BaseEntity target;
    private double projectileSpeed;
    private final static int RADIUS = 5;
    private boolean collided;

    /**
     * Creates an AutoAttack with damage, x, y, projectile speed values and a BaseEntity target
     * @param gamePanel The game panel
     * @param damage The damage of the attack
     * @param x The x position of the attack
     * @param y The y position of the attack
     * @param target The target of the attack
     * @param projectileSpeed The projectile speed of the attack
     */
    public AutoAttack(GamePanel gamePanel, double damage, double x, double y, BaseEntity target, double projectileSpeed) {
        super(gamePanel, damage, x, y, RADIUS);
        this.target = target;
        this.projectileSpeed = projectileSpeed;
    }

    /**
     * Loads the images used to display the attack
     */
    @Override
    protected void loadImage() {
        this.image = GameUtility.getImage("/images/AutoAttack.png",radius*2,radius*2);
    }

    /**
     * Gets the BaseEntity target of the attack
     * @return The BaseEntity target of the attack
     */
    public BaseEntity getTarget() {
        return this.target;
    }

    /**
     * Determines if the attack has collided with the target
     * @return Whether the attack has collided with the target
     */
    public boolean isCollided() {
        return collided;
    }

    /**
     * moves the attack
     */
    public void move() {
        double otherX = target.getX();
        double otherY = target.getY();

        if (otherX != this.x || otherY != this.y) {
            double denominator = Math.sqrt(Math.pow((otherX - this.x), 2) + Math.pow((otherY - this.y), 2));

            if (denominator != 0) {
                double xStep = ((otherX - this.x) / denominator) * projectileSpeed;
                double yStep = ((otherY - this.y) / denominator) * projectileSpeed;

                if (target.entityIntersects(x,y,radius)) {
                    collided = true;
                }

                if (!collided) {
                    this.x += xStep;
                    this.y += yStep;
                }
            }
        }
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
