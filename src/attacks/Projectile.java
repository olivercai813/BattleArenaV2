package attacks;

import game.GamePanel;
import java.awt.*;

/**
 * The Projectile class defines a Projectile object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public abstract class Projectile extends BaseAttack {

    protected Point target;
    protected double projectileSpeed;
    protected int range;
    protected Point startingPos;
    protected double xStep;
    protected double yStep;

    /**
     * Creates a Projectile with damage, x, y, radius, projectile speed, range values, and a target point
     * @param gamePanel The game panel
     * @param damage The damage of the projectile
     * @param x The x position of the projectile
     * @param y The y position of the projectile
     * @param radius The radius of the projectile
     * @param target The attack target of the projectile
     * @param projectileSpeed The speed of the projectile
     * @param range The range of the projectile
     */
    public Projectile (GamePanel gamePanel, double damage, double x, double y, int radius, Point target, double projectileSpeed, int range) {
        super(gamePanel, damage, x, y, radius);
        this.target = target;
        this.projectileSpeed = projectileSpeed;
        this.range = range;
        startingPos = new Point((int)x,(int)y);
        calculateSteps();
    }

    /**
     * Calculates the step distance of each movement
     */
    private void calculateSteps() {
        double otherX = target.getX();
        double otherY = target.getY();

        double denominator = Math.sqrt(Math.pow((otherX - this.x), 2) + Math.pow((otherY - this.y), 2));

        if (denominator != 0) {
            xStep = ((otherX - this.x) / denominator) * projectileSpeed;
            yStep = ((otherY - this.y) / denominator) * projectileSpeed;
        }
    }

    /**
     * Determines if the projectile has travelled it's maximum range
     * @return Whether the projectile has travelled it's maximum range
     */
    public boolean maxRangeTravelled() {
        return range <= Math.sqrt(Math.pow(this.x - startingPos.getX(), 2) + Math.pow(this.y - startingPos.getY(), 2));
    }

    /**
     * Moves the projectile
     */
    public void move() {
        this.x += xStep;
        this.y += yStep;
    }
}
