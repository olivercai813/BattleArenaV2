package entities;

import game.GamePanel;
import game.GameUtility;

import java.awt.*;

/**
 * The MegaMinion class defines a MegaMinion object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class MegaMinion extends Minion {

    private static final int MAX_HP = 2000;
    private static final double DAMAGE = 200;
    private static final int RADIUS = 40;
    private static final int ATTACK_RANGE = 60;
    private static final int PROJECTILE_SPEED = ATTACK_RANGE;
    private static final double ATTACK_SPEED = 1;
    private static final int MOVE_SPEED = 6;
    private static final long MOVE_CD = 5000000000L;
    private static final long MOVE_DURATION = 5000000000L;
    private static final int BOUNTY = 500;
    private long prevMoveTime;

    public MegaMinion(GamePanel gamePanel, int team, double x, double y) {
        super(gamePanel, team, x, y, RADIUS, MAX_HP, DAMAGE, ATTACK_RANGE, ATTACK_SPEED, MOVE_SPEED, PROJECTILE_SPEED, BOUNTY);
    }

    @Override
    protected void loadImage() {
        image = GameUtility.getImage("/images/MegaMinion.png",radius*2,radius*2);
    }

    @Override
    public void move() {
        if (gamePanel.time >= prevMoveTime + MOVE_DURATION + MOVE_CD) {
            prevMoveTime = gamePanel.time;
        } else if (gamePanel.time <= prevMoveTime + MOVE_DURATION) {
            super.move();
        }
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
