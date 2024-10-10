package entities;

import attacks.AcidPool;
import attacks.AutoAttack;
import game.GamePanel;
import game.GameUtility;
import java.awt.*;

/**
 * The Baron class defines a Baron entity
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class Baron extends BaseEntity {
    private final static int RADIUS = 100;
    private final static int MAX_HP = 5000;
    private final static double DAMAGE = 100;
    private final static int ATTACK_RANGE = 400;
    private final static double ATTACK_SPEED = 0.2;
    private final static int PROJECTILE_SPEED = 8;
    private final static int BARON_ABILITY_DMG = 60;
    private AcidPool baronAbility;
    private long prevBaronAbilityTime;

    /**
     * Creates a Baron entity with x and y values
     * @param gamePanel The game panel
     * @param x The x position of the entity
     * @param y The y position of the entity
     */
    public Baron (GamePanel gamePanel, double x, double y) {
        super(gamePanel, GamePanel.ENEMY, x, y, RADIUS, MAX_HP, DAMAGE, ATTACK_RANGE, ATTACK_SPEED, PROJECTILE_SPEED, 0);
    }

    /**
     * Loads the images used to display the entity
     */
    @Override
    protected void loadImage() {
        image = GameUtility.getImage("/images/Baron.png",radius*2,radius*2);
    }

    /**
     * Updates the entity
     */
    @Override
    public void update() {
        super.update();
        findTarget();
        useAutoAttack();
    }

    /**
     * Attacks the attack target if there is one
     */
    @Override
    protected void useAutoAttack() {
        if (baronAbility != null) {
            if (gamePanel.time >= prevBaronAbilityTime + AcidPool.DURATION) {
                baronAbility = null;
            } else {
                BaseEntity ally;
                for (int i = 0; i < gamePanel.allyGameEntities.size(); i++) {
                    ally = gamePanel.allyGameEntities.get(i);
                    if (ally.entityIntersects(baronAbility.getX(), baronAbility.getY(), baronAbility.getRadius())) {
                        ally.takeDamage(baronAbility.getDamage());
                        if (ally instanceof MovingEntity) {
                            ((MovingEntity) ally).changeMoveSpeed(AcidPool.SLOW_FACTOR,AcidPool.SLOW_TIME);
                        }
                    }
                }
            }
        }
        if (autoAttack != null) {
            if (autoAttack.getTarget().isDefeated()) {
                attackTarget = null;
                autoAttack = null;
            } else {
                if (!autoAttack.isCollided()) {
                    autoAttack.move();
                } else {
                    autoAttack.getTarget().takeDamage(autoAttack.getDamage());
                    baronAbility = new AcidPool(gamePanel, BARON_ABILITY_DMG,autoAttack.getTarget().getX(),autoAttack.getTarget().getY());
                    prevBaronAbilityTime = gamePanel.time;
                    autoAttack = null;
                }
            }
        }
        if (attackTarget != null && autoAttack == null && rangeIntersects(attackTarget.getX(), attackTarget.getY(), attackTarget.getRadius())
                && (gamePanel.time - prevAttackTime) >= attackCD && !stunned) {
            prevAttackTime = gamePanel.time;
            autoAttack = new AutoAttack(gamePanel, damage, x, y, attackTarget, projectileSpeed);
        }
    }

    /**
     * Draws the entity to the JPanel
     * @param g2 The graphics object
     */
    @Override
    public void draw(Graphics2D g2) {
        if (baronAbility != null) {
            baronAbility.draw(g2);
        }

        //set attack colour
        g2.setColor(new Color(153, 0, 76));
        super.draw(g2);

        //draw baron
        g2.setColor(new Color(255,255,255,40));
        g2.fillOval(getScreenX()-radius,getScreenY()-radius, radius*2, radius*2);
        g2.drawImage(image,getScreenX()-RADIUS,getScreenY()-RADIUS, null);
    }
}
