package entities;

import game.GamePanel;
import game.GameUtility;

import java.awt.*;

/**
 * The MeleeMinion class defines a MeleeMinion object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class MeleeMinion extends Minion {

    private static final int MAX_HP = 200;
    private static final double DAMAGE = 6;
    private static final int RADIUS = 30;
    private static final int ATTACK_RANGE = 40;
    private static final int PROJECTILE_SPEED = ATTACK_RANGE;
    private static final double ATTACK_SPEED = 0.5;
    private static final int MOVE_SPEED = 4;
    private final static int BOUNTY = 80;

    public MeleeMinion(GamePanel gamePanel, int team, double x, double y) {
        super(gamePanel, team, x, y, RADIUS, MAX_HP, DAMAGE, ATTACK_RANGE, ATTACK_SPEED, MOVE_SPEED, PROJECTILE_SPEED, BOUNTY);
    }

    @Override
    protected void loadImage() {
        image = GameUtility.getImage("/images/MeleeMinion.png",radius*2,radius*2);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.red);
        super.draw(g2);
        g2.setColor(new Color(255,255,255,40));
        g2.fillOval(getScreenX()-radius,getScreenY()-radius, radius*2, radius*2);
        g2.drawImage(image, GameUtility.rotateImage(image,getScreenX()-radius, getScreenY()-radius, angle), null);
    }
}