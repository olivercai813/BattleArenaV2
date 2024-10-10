package entities;

import game.GamePanel;
import game.GameUtility;

import java.awt.*;

/**
 * The MovingEntity class defines a MovingEntity object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public abstract class MovingEntity extends BaseEntity {
    protected double moveSpeed;
    protected Point movePoint;
    protected BaseEntity moveTarget;
    protected double angle;
    protected boolean MSChanged;
    protected double ChangedMS;
    protected long newMSEndTime;

    /**
     * Creates a moving entity with the team of the entity and its x, y, radius, max hp, dmaage, attack range, attack speed, move speed, projectile speed, and bounty values
     * @param gamePanel The game panel
     * @param team The team of the entity
     * @param x The x value of the entity
     * @param y The y value of the entity
     * @param radius The radius of the entity
     * @param maxHP The max hp of the entity
     * @param damage The damage of the entity
     * @param attackRange The attack range of the entity
     * @param attackSpeed The attack speed of the entity
     * @param moveSpeed The move speed of the entity
     * @param projectileSpeed The projectile speed of the entity
     * @param bounty The bounty of the entity
     */
    public MovingEntity(GamePanel gamePanel, int team, double x, double y, int radius, int maxHP, double damage, int attackRange, double attackSpeed, double moveSpeed, int projectileSpeed, int bounty) {
        super(gamePanel, team, x, y, radius, maxHP, damage, attackRange, attackSpeed, projectileSpeed, bounty);
        this.moveSpeed = moveSpeed;
    }

    /**
     * finds the move and attack targets
     */
    @Override
    public void findTarget() {
        super.findTarget();
        moveTarget = attackTarget;
    }

    /**
     * updates the status of the entity
     */
    @Override
    public void updateStatus() {
        super.updateStatus();
        MSChanged = !(gamePanel.time >= newMSEndTime);
    }

    /**
     * Changes the move speed of the entity by a specified factor for a specified amount of time
     * @param moveSpeedFactor The move speed change factor
     * @param changeDuration The duration of change
     */
    public void changeMoveSpeed(double moveSpeedFactor, long changeDuration) {
        if (!MSChanged) {
            MSChanged = true;
            newMSEndTime = gamePanel.time + changeDuration;
            ChangedMS = moveSpeed * moveSpeedFactor;
        }
    }

    /**
     * Moves the entity if there is a move target
     */
    protected void move() {
        if (moveTarget != null && !rangeIntersects(moveTarget.getX(), moveTarget.getY(), moveTarget.getRadius()) && !stunned) {
            double denominator = Math.sqrt(Math.pow((moveTarget.x - this.x), 2) + Math.pow((moveTarget.y - this.y), 2));


            if (denominator != 0) {
                double xStep;
                double yStep;
                if (MSChanged) {
                    xStep = ((moveTarget.x - this.x) / denominator) * ChangedMS;
                    yStep = ((moveTarget.y - this.y) / denominator) * ChangedMS;
                } else {
                    xStep = ((moveTarget.x - this.x) / denominator) * moveSpeed;
                    yStep = ((moveTarget.y - this.y) / denominator) * moveSpeed;
                }

                boolean colliding = false;
                BaseEntity entity;

                if (team == GamePanel.ENEMY) {
                    for (int i = 0; i < gamePanel.allyGameEntities.size(); i++) {
                        entity = gamePanel.allyGameEntities.get(i);
                        if (entity != this && entity.entityIntersects(x + xStep, y + yStep, radius)) {
                            colliding = true;
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < gamePanel.enemyGameEntities.size(); i++) {
                        entity = gamePanel.enemyGameEntities.get(i);
                        if (entity != this && entity.entityIntersects(x + xStep, y + yStep, radius)) {
                            colliding = true;
                            break;
                        }
                    }
                }

                if (!colliding) {
                    this.x += xStep;
                    this.y += yStep;
                    angle = GameUtility.calculateAngle(new Point((int)x,(int)y), new Point((int)moveTarget.getX(),(int)moveTarget.getY()));
                }
            }
        }
    }
}
