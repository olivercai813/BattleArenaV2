package entities;

import game.GamePanel;
import game.GameUtility;

import java.awt.*;

/**
 * The Tower class defines a Tower object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class Tower extends BaseEntity {
    private final static int RADIUS = 50;
    private final static int MAX_HP = 300;
    private final static double DAMAGE = 90;
    private final static int ATTACK_RANGE = 300;
    private final static int PROJECTILE_SPEED = 16;
    private final static double ATTACK_SPEED = 1.0;
    private final static int BOUNTY = 500;

    public Tower(GamePanel gamePanel, double x, double y) {
        super (gamePanel, GamePanel.ENEMY, x, y, RADIUS, MAX_HP, DAMAGE, ATTACK_RANGE, ATTACK_SPEED, PROJECTILE_SPEED, BOUNTY);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getBounty() {
        return BOUNTY;
    }

    @Override
    protected void loadImage() {
        image = GameUtility.getImage("/images/Tower.png",radius*2,radius*2);
    }

    @Override
    public void update() {
        findTarget();
        useAutoAttack();
    }

    @Override
    public void draw(Graphics2D g2) {
        //set attack colour
        g2.setColor(Color.red);
        super.draw(g2);

        //draw tower and range
        g2.setColor(Color.red);
        g2.drawOval(getScreenX()-attackRange,getScreenY()-attackRange, attackRange*2, attackRange*2);
        g2.setColor(new Color(51,0,0, 80));
        g2.fillOval(getScreenX()-attackRange+5,getScreenY()-attackRange+5, attackRange*2-10, attackRange*2-10);
        g2.setColor(Color.red);
        g2.setColor(new Color(255,255,255,40));
        g2.fillOval(getScreenX()-radius,getScreenY()-radius, radius*2, radius*2);
        g2.drawImage(image,getScreenX()-RADIUS,getScreenY()-RADIUS, null);
    }
}
