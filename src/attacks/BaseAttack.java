package attacks;

import game.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The BaseAttack class defines a base attack object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public abstract class BaseAttack {
    GamePanel gamePanel;
    protected double damage;
    protected double x;
    protected double y;
    protected int radius;
    protected BufferedImage image;

    /**
     * Creates a BaseAttack with damage, x, y, and radius values
     * @param gamePanel The game panel
     * @param damage The damage of the attack
     * @param x The x position of the attack
     * @param y The y position of the attack
     * @param radius The radius of the attack
     */
    public BaseAttack(GamePanel gamePanel, double damage, double x, double y, int radius) {
        this.gamePanel = gamePanel;
        this.damage = damage;
        this.x = x;
        this.y = y;
        this.radius = radius;
        loadImage();
    }

    /**
     * Loads the images used to display the attack
     */
    protected abstract void loadImage();

    /**
     * Gets the damage of the attack
     * @return The damage of the attack
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Gets the radius of the attack
     * @return The radius of the attack
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Gets the x position of the attack on the map
     * @return The x position of the attack on the map
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y position of the attack on the map
     * @return The y position of the attack on the map
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the x position of the attack on the screen
     * @return The x position of the attack on the screen
     */
    public int getScreenX() {
        return (int)Math.round(x + gamePanel.screenOffsetX);
    }

    /**
     * Gets the y position of the attack on the screen
     * @return The y position of the attack on the screen
     */
    public int getScreenY() {
        return (int)Math.round(y + gamePanel.screenOffsetY);
    }

    /**
     * Sets the x position of the attack on the map
     * @param x The x position of the attack on the map
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the y position of the attack on the map
     * @param y The y position of the attack on the map
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Draws the attack to the JPanel
     * @param g The graphics object
     */
    public abstract void draw(Graphics g);
}
