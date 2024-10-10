package entities;

import attacks.*;
import game.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The BaseEntity class defines a BaseEntity object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public abstract class BaseEntity {
    protected GamePanel gamePanel;
    protected int team;
    protected double x;
    protected double y;
    protected int radius;
    protected double currentHP;
    protected int maxHP;
    protected int attackRange;
    protected double attackSpeed;
    protected int projectileSpeed;
    protected double damage;
    protected int bounty;
    protected BaseEntity attackTarget;
    protected AutoAttack autoAttack;
    public static final long BASE_ATTACK_CD = 1000000000L;
    protected long attackCD;
    protected long prevAttackTime;
    protected boolean stunned;
    protected long stunEndTime;
    protected BufferedImage image;

    /**
     * Creates a BaseEntity with the team of the entity and the x, y, radius, maxHP, damage, attack range, attack speed, projectile speed, bounty values
     * @param gamePanel The game panel
     * @param team The team of the entity
     * @param x The x position of the entity
     * @param y The y position of the entity
     * @param radius The radius of the entity
     * @param maxHP The maxHP of the entity
     * @param damage The damage of the entity
     * @param attackRange The attack range of the entity
     * @param attackSpeed The attack speed of the entity
     * @param projectileSpeed The projectile speed of the entity
     * @param bounty The bounty of the entity
     */
    public BaseEntity(GamePanel gamePanel, int team, double x, double y, int radius, int maxHP, double damage, int attackRange, double attackSpeed, int projectileSpeed, int bounty) {
        this.gamePanel = gamePanel;
        this.team = team;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.maxHP = maxHP;
        this.damage = damage;
        this.attackRange = attackRange;
        this.attackSpeed = attackSpeed;
        this.projectileSpeed = projectileSpeed;
        this.bounty = bounty;
        currentHP = maxHP;
        attackCD = (long)(BASE_ATTACK_CD/this.attackSpeed);
        loadImage();
    }

    /**
     * Gets the x position of the entity on the map
     * @return The x position of the entity
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets the y position of the entity on the map
     * @return The y position of the entity
     */
    public double getY() {
        return this.y;
    }

    /**
     * Gets the x position of the entity on the screen
     * @return The x position of the entity on the screen
     */
    public int getScreenX() {
        return (int)(x + gamePanel.screenOffsetX);
    }

    /**
     * Gets the y position of the entity on the screen
     * @return The y position of the entity on the screen
     */
    public int getScreenY() {
        return (int)(y + gamePanel.screenOffsetY);
    }

    /**
     * Gets the radius of the entity
     * @return The radius of the entity
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * Gets the bounty value of the entity
     * @return The bounty value of the entity
     */
    public int getBounty() {
        return bounty;
    }

    /**
     * Determines the entity's current HP reaches 0 and is defeated
     * @return Whether the entity is defeated
     */
    public boolean isDefeated() {
        return currentHP == 0;
    }

    /**
     * Loads the images used to display the entity
     */
    protected abstract void loadImage();

    /**
     * Finds the nearest target in the opposite team sets it as the attack target
     */
    protected void findTarget() {
        int targetIndex = 0;
        double distance;
        double minDistance = Integer.MAX_VALUE;

        if (team == GamePanel.ALLY) {
            BaseEntity enemy;
            for (int i = 0; i < gamePanel.enemyGameEntities.size(); i++) {
                enemy = gamePanel.enemyGameEntities.get(i);
                distance = Math.sqrt(Math.pow(this.x - enemy.getX(), 2) + Math.pow(this.y - enemy.getY(), 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    targetIndex = i;
                }
            }
            attackTarget = gamePanel.enemyGameEntities.get(targetIndex);
        } else {
            BaseEntity ally;
            for(int i = 0; i < gamePanel.allyGameEntities.size(); i++) {
                ally = gamePanel.allyGameEntities.get(i);
                distance = Math.sqrt(Math.pow(this.x - ally.getX(), 2) + Math.pow(this.y - ally.getY(), 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    targetIndex = i;
                }
            }
            attackTarget = gamePanel.allyGameEntities.get(targetIndex);
        }
    }

    /**
     * Determines if this entity intersects a circle
     * @param otherX The x position of the circle
     * @param otherY The y position of the circle
     * @param otherRadius The radius of the circle
     * @return Whether the entity intersects a circle
     */
    public boolean entityIntersects(double otherX, double otherY, double otherRadius) {
        double distance = Math.sqrt(Math.pow(this.x - otherX, 2) + Math.pow(this.y - otherY, 2));
        return distance <= this.radius - otherRadius || distance <= otherRadius - this.radius
                || distance < this.radius + otherRadius || distance == this.radius + otherRadius;
    }

    /**
     * Determines if this entity's attack range intersects a circle
     * @param otherX The x position of the circle
     * @param otherY The y position of the circle
     * @param otherRadius The radius of the circle
     * @return Whether the entity's attack range intersects a circle
     */
    public boolean rangeIntersects(double otherX, double otherY, double otherRadius) {
        double distance = Math.sqrt(Math.pow(this.x - otherX, 2) + Math.pow(this.y - otherY, 2));
        return distance <= this.attackRange - otherRadius || distance <= otherRadius - this.attackRange
                || distance < this.attackRange + otherRadius || distance == this.attackRange + otherRadius;
    }

    /**
     * Updates the entity
     */
    public void update() {
        updateStatus();
    }

    /**
     * Updates the status of the entity
     */
    public void updateStatus() {
        stunned = !(gamePanel.time >= stunEndTime);
    }

    /**
     * Stuns the entity for a set duration
     * @param stunDuration The duration to be stunned
     */
    protected void becomeStunned(long stunDuration) {
        if (!stunned) {
            stunned = true;
            stunEndTime = gamePanel.time + stunDuration;
        }
    }

    /**
     * Attacks the attack target if there is one
     */
    protected void useAutoAttack() {
        if (autoAttack != null) {
            if (autoAttack.getTarget().isDefeated()) {
                attackTarget = null;
                autoAttack = null;
            } else {
                if (!autoAttack.isCollided()) {
                    autoAttack.move();
                } else {
                    autoAttack.getTarget().takeDamage(autoAttack.getDamage());
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
     * Subtracts the specified amount of damage from this entity's currentHP
     * @param damage The amount of damage taken
     */
    protected void takeDamage(double damage) {
        if (currentHP - damage <= 0) {
            currentHP = 0;
        } else {
            currentHP -= damage;
        }
    }

    /**
     * Draws the entity's attacks health bar
     * @param g2
     */
    public void draw(Graphics2D g2) {
        //draw autoattacks
        if (autoAttack != null) {
            autoAttack.draw(g2);
        }

        //set colour depending on team
        if (team == GamePanel.ALLY) {
            g2.setColor(new Color(255,255,0, 200));
        } else {
            g2.setColor(new Color(204,0,0,200));
        }

        //draw health bars
        g2.drawRect(getScreenX()-50, getScreenY()-radius-30, 100, 10);
        g2.fillRect(getScreenX()-50, getScreenY()-radius-30, 100*(int)currentHP/maxHP, 10);
    }
}
