package entities;

import attacks.AutoAttack;
import game.GamePanel;
import game.GameUtility;

import java.awt.*;

/**
 * The Champion class defines a Champion object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public abstract class Champion extends MovingEntity {
    private static final int RADIUS = 30;
    private static final int MAX_HP_UPGRADE = 100;
    private static final int AUTO_DMG_UPGRADE = 10;
    private static final int ABILITY_DMG_UPGRADE = 8;
    private static final double MOVE_SPEED_UPGRADE = 1.6;
    private static final double ATTACK_SPEED_UPGRADE = 0.4;
    private static final double ABILITY_HASTE_UPGRADE = 0.4;
    private static final double LIFE_STEAL_UPGRADE = 0.05;
    private static final int GOLD_COST = 100;
    protected int screenX;
    protected int screenY;
    protected double xStep;
    protected double yStep;
    private int maxHpUpgrades;
    private int autoDmgUpgrades;
    private int abilityDmgUpgrades;
    private int moveSpeedUpgrades;
    private int attackSpeedUpgrades;
    private int abilityHasteUpgrades;
    private int lifeStealUpgrades;
    protected double abilityHaste;
    protected double autoAttackDmg;
    protected double abilityDmg;
    protected double lifeSteal;
    protected int gold;
    protected boolean showAttackRange;
    protected long QCoolDown;
    protected long WCoolDown;
    protected long ECoolDown;
    protected long RCoolDown;
    protected long prevQTime;
    protected long prevWTime;
    protected long prevETime;
    protected long prevRTime;

    /**
     * Creates a champion entity with x, y, maxHP, auto attack damage,attack range, attack speed, move speed,
     * projectile speed, ability haste, ability damage, life steal, and cooldown values
     * @param gamePanel The game panel
     * @param x The x position of the champion
     * @param y The y position of the champion
     * @param maxHP The max hp of the champion
     * @param autoAttackDmg The auto attack damage of the champion
     * @param attackRange The attack range of the champion
     * @param attackSpeed The attack speed of the champion
     * @param moveSpeed The move speed of the champion
     * @param projectileSpeed The projectile speed of the champion
     * @param abilityHaste The ability haste of the champion
     * @param abilityDmg The ability damage of the champion
     * @param lifeSteal The life steal of the champion
     * @param QCoolDown The q ability cool down of the champion
     * @param WCoolDown The w ability cool down of the champion
     * @param ECoolDown The e ability cool down of the champion
     * @param RCoolDown The r ability cool down of the champion
     */
    public Champion(GamePanel gamePanel, double x, double y, int maxHP, double autoAttackDmg, int attackRange, double attackSpeed, double moveSpeed, int projectileSpeed,
                    double abilityHaste, double abilityDmg, double lifeSteal, long QCoolDown, long WCoolDown, long ECoolDown, long RCoolDown) {
        super(gamePanel, GamePanel.ALLY, x, y, RADIUS, maxHP, autoAttackDmg, attackRange, attackSpeed, moveSpeed, projectileSpeed, 0);
        this.abilityHaste = abilityHaste;
        this.autoAttackDmg = damage;
        this.abilityDmg = abilityDmg;
        this.lifeSteal = lifeSteal;
        this.QCoolDown = (long)(QCoolDown/abilityHaste);
        this.WCoolDown = (long)(WCoolDown/abilityHaste);
        this.ECoolDown = (long)(ECoolDown/abilityHaste);
        this.RCoolDown = (long)(RCoolDown/abilityHaste);
    }

    /**
     * Sets the screen x position of the entity
     * @param screenX The screen x position of the entity
     */
    public void setScreenX(int screenX) {
        this.screenX = screenX;
    }

    /**
     * Sets the screen y position of the entity
     * @param screenY The screen y position of the entity
     */
    public void setScreenY(int screenY) {
        this.screenY = screenY;
    }

    /**
     * Gets the x position of the entity on the screen
     * @return The x position of the entity on the screen
     */
    @Override
    public int getScreenX() {
        return screenX;
    }

    /**
     * Gets the y position of the entity on the screen
     * @return The y position of the entity on the screen
     */
    @Override
    public int getScreenY() {
        return screenY;
    }

    /**
     * Gets the horizontal distance travelled per step
     * @return The horizontal distance travelled per step
     */
    public double getXStep() {
        return xStep;
    }

    /**
     * Gets the vertical distance travelled per step
     * @return The vertical distance travelled per step
     */
    public double getYStep() {
        return yStep;
    }

    /**
     * Sets the move target point
     * @param moveTarget The move target point
     */
    public void setMoveTarget(Point moveTarget) {
        this.movePoint = moveTarget;
        attackTarget = null;
    }

    /**
     * Sets the BaseEntity attack target
     * @param attackTarget The BaseEntity attack target
     */
    public void setAttackTarget(BaseEntity attackTarget) {
        if (rangeIntersects(attackTarget.getX(),attackTarget.getY(),attackTarget.getRadius())) {
            movePoint = null;
            xStep = 0;
            yStep = 0;
        }
        this.attackTarget = attackTarget;
    }

    /**
     * Adds gold to the champion
     * @param goldEarned The amount of gold added
     */
    public void gainGold(int goldEarned) {
        this.gold += goldEarned;
    }

    /**
     * Gets the gold amount of the champion
     * @return The gold amount of the champion
     */
    public int getGold() {
        return gold;
    }

    /**
     * Gets the cooldown of the QAbility
     * @return The coolodwn of the QAbility
     */
    public long getQCoolDown() {
        return QCoolDown;
    }

    /**
     * Gets the cooldown of the WAbility
     * @return The coolodwn of the WAbility
     */
    public long getWCoolDown() {
        return WCoolDown;
    }

    /**
     * Gets the cooldown of the EAbility
     * @return The coolodwn of the EAbility
     */
    public long getECoolDown() {
        return ECoolDown;
    }

    /**
     * Gets the cooldown of the RAbility
     * @return The coolodwn of the RAbility
     */
    public long getRCoolDown() {
        return RCoolDown;
    }

    /**
     * Gets the time of the previous q ability usage
     * @return The time of the previous q ability usage
     */
    public long getPrevQTime() {
        return prevQTime;
    }

    /**
     * Gets the time of the previous w ability usage
     * @return The time of the previous w ability usage
     */
    public long getPrevWTime() {
        return prevWTime;
    }

    /**
     * Gets the time of the previous e ability usage
     * @return The time of the previous e ability usage
     */
    public long getPrevETime() {
        return prevETime;
    }

    /**
     * Gets the time of the previous r ability usage
     * @return The time of the previous r ability usage
     */
    public long getPrevRTime() {
        return prevRTime;
    }

    /**
     * Gets the number of max hp upgrades
     * @return The number of max hp upgrades
     */
    public int getMaxHpUpgrades() {
        return maxHpUpgrades;
    }

    /**
     * Gets the number of auto attack damage upgrades
     * @return The number of auto attack damage upgrades
     */
    public int getAutoDmgUpgrades() {
        return autoDmgUpgrades;
    }

    /**
     * Gets the number of ability damage upgrades
     * @return The number of ability damage upgrades
     */
    public int getAbilityDmgUpgrades() {
        return abilityDmgUpgrades;
    }

    /**
     * Gets the number of move speed upgrades
     * @return The number of move speed upgrades
     */
    public int getMoveSpeedUpgrades() {
        return moveSpeedUpgrades;
    }

    /**
     * Gets the number of attack speed upgrades
     * @return The number attack speed upgrades
     */
    public int getAttackSpeedUpgrades() {
        return attackSpeedUpgrades;
    }

    /**
     * Gets the number of ability haste upgrades
     * @return The number of ability haste upgrades
     */
    public int getAbilityHasteUpgrades() {
        return abilityHasteUpgrades;
    }

    /**
     * Gets the number of life steal upgrades
     * @return The number of life steal upgrades
     */
    public int getLifeStealUpgrades() {
        return lifeStealUpgrades;
    }

    /**
     * Updates the champion entity
     */
    @Override
    public void update() {
        super.update();
        checkClick();
        checkKey();
        useAutoAttack();
        move();
    }

    /**
     * Upgrades the champion's max hp
     */
    public void upgradeMaxHP() {
        if (maxHpUpgrades < 5 && gold >= (maxHpUpgrades+1)*GOLD_COST) {
            maxHpUpgrades++;
            maxHP += MAX_HP_UPGRADE;
            currentHP += MAX_HP_UPGRADE;
            gold -= maxHpUpgrades*GOLD_COST;
        }
    }

    /**
     * Upgrades the champion's auto attack damage
     */
    public void upgradeAutoDmg() {
        if (autoDmgUpgrades < 5 && gold >= (autoDmgUpgrades+1)*GOLD_COST) {
            autoDmgUpgrades++;
            autoAttackDmg += AUTO_DMG_UPGRADE;
            gold -= autoDmgUpgrades*GOLD_COST;
        }
    }

    /**
     * Upgrades the champion's ability damage
     */
    public void upgradeAbilityDmg() {
        if (abilityDmgUpgrades < 5 && gold >= (abilityDmgUpgrades+1)*GOLD_COST) {
            abilityDmgUpgrades++;
            abilityDmg += ABILITY_DMG_UPGRADE;
            gold -= abilityDmgUpgrades*GOLD_COST;
        }
    }

    /**
     * Upgrades the champion's move speed
     */
    public void upgradeMoveSpeed() {
        if (moveSpeedUpgrades < 5 && gold >= (moveSpeedUpgrades+1)*GOLD_COST) {
            moveSpeedUpgrades++;
            moveSpeed += MOVE_SPEED_UPGRADE;
            gold -= moveSpeedUpgrades*GOLD_COST;
        }
    }

    /**
     * Upgrades the champion's attack speed
     */
    public void upgradeAttackSpeed() {
        if (attackSpeedUpgrades < 5 && gold >= (attackSpeedUpgrades+1)*GOLD_COST) {
            attackSpeedUpgrades++;

            attackCD *= attackSpeed;

            attackSpeed += ATTACK_SPEED_UPGRADE;
            attackCD /= attackSpeed;

            gold -= attackSpeedUpgrades*GOLD_COST;
        }
    }

    /**
     * Upgrades the champion's ability haste
     */
    public void upgradeAbilityHaste() {
        if (abilityHasteUpgrades < 5 && gold >= (abilityHasteUpgrades+1)*GOLD_COST) {
            abilityHasteUpgrades++;

            QCoolDown *= abilityHaste;
            WCoolDown *= abilityHaste;
            ECoolDown *= abilityHaste;
            RCoolDown *= abilityHaste;

            abilityHaste += ABILITY_HASTE_UPGRADE;

            QCoolDown = (long)(QCoolDown/abilityHaste);
            WCoolDown = (long)(WCoolDown/abilityHaste);
            ECoolDown = (long)(ECoolDown/abilityHaste);
            RCoolDown = (long)(RCoolDown/abilityHaste);

            gold -= abilityHasteUpgrades*GOLD_COST;
        }
    }

    /**
     * Upgrades the champion's life steal
     */
    public void upgradeLifeSteal() {
        if (lifeStealUpgrades < 5 && gold >= (lifeStealUpgrades+1)*GOLD_COST) {
            lifeStealUpgrades++;
            lifeSteal += LIFE_STEAL_UPGRADE;
            gold -= lifeStealUpgrades*GOLD_COST;
        }
    }

    /**
     * Checks for mouse input, if the user clicks while pressing A or pressing shift the attack target is set as the entity closest to the mouse click point
     */
    private void checkClick() {
        if (gamePanel.mouseHandler.getClickPoint() != null) {
            int mapX = gamePanel.mouseHandler.getClickPoint().x - (int)Math.round(gamePanel.screenOffsetX);
            int mapY = gamePanel.mouseHandler.getClickPoint().y - (int)Math.round(gamePanel.screenOffsetY);

            Point clickPoint = new Point(mapX, mapY);

            setMoveTarget(clickPoint);

            if (gamePanel.keyHandler.AKeyPressed() || gamePanel.keyHandler.ShiftKeyPressed()) {
                int targetIndex = 0;
                double distance;
                double minDistance = Integer.MAX_VALUE;

                BaseEntity enemy;
                for (int i = 0; i<gamePanel.enemyGameEntities.size(); i++) {
                    enemy = gamePanel.enemyGameEntities.get(i);
                    distance = Math.sqrt(Math.pow(clickPoint.getX() - enemy.getX(), 2) + Math.pow(clickPoint.getY() - enemy.getY(), 2));
                    if (distance < minDistance) {
                        minDistance = distance;
                        targetIndex = i;
                    }
                }
                setAttackTarget(gamePanel.enemyGameEntities.get(targetIndex));
            } else {
                BaseEntity enemy;
                for (int i = 0; i < gamePanel.enemyGameEntities.size(); i++) {
                    enemy = gamePanel.enemyGameEntities.get(i);
                    if (enemy.entityIntersects(clickPoint.getX(), clickPoint.getY(), 0)) {
                        setAttackTarget(enemy);
                    }
                }
            }
        }
    }

    /**
     * Checks for keyboard input, if the user presses A, the champion's attack range is shown
     */
    private void checkKey() {
        showAttackRange = gamePanel.keyHandler.AKeyPressed();
        if (gamePanel.keyHandler.SKeyPressed()) {
            movePoint = null;
            xStep = 0;
            yStep = 0;
        }
    }

    /**
     * Uses the QAbility
     */
    public abstract void useQAbility();

    /**
     * Uses the WAbility
     */
    public abstract void useWAbility();

    /**
     * Uses the EAbility
     */
    public abstract void useEAbility();

    /**
     * Uses the RAbility
     */
    public abstract void useRAbility();

    /**
     * Recovers the champion's health by a specified amount
     * @param healthIncrease The amount of health recovered
     */
    public void recoverHealth(double healthIncrease) {
        this.currentHP += healthIncrease;
        if (currentHP > maxHP) {
            currentHP = maxHP;
        }
    }

    /**
     * Attacks the attack target if there is one
     */
    @Override
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
                    if(autoAttack.getTarget().isDefeated()) {
                        gold += autoAttack.getTarget().getBounty();
                    }
                    recoverHealth(autoAttack.getDamage()*lifeSteal);
                    autoAttack = null;
                }
            }
        }
        if (attackTarget != null && autoAttack == null && rangeIntersects(attackTarget.getX(), attackTarget.getY(), attackTarget.getRadius())
                && (gamePanel.time - prevAttackTime) >= attackCD && !stunned) {
            moveTarget = null;
            prevAttackTime = gamePanel.time;
            autoAttack = new AutoAttack(gamePanel, autoAttackDmg, x, y, attackTarget, projectileSpeed);
        }
    }

    /**
     * Moves the champion toward the move point if there is one
     */
    @Override
    protected void move() {
        if (movePoint != null) {
            double denominator = Math.sqrt(Math.pow((movePoint.x - this.x), 2) + Math.pow((movePoint.y - this.y), 2));

            if (denominator != 0) {
                if (MSChanged) {
                    xStep = ((movePoint.x - this.x) / denominator) * ChangedMS;
                    yStep = ((movePoint.y - this.y) / denominator) * ChangedMS;
                } else {
                    xStep = ((movePoint.x - this.x) / denominator) * moveSpeed;
                    yStep = ((movePoint.y - this.y) / denominator) * moveSpeed;
                }

                boolean colliding = false;
                BaseEntity entity;

                if (team == GamePanel.ENEMY) {
                    for (int i = 0; i < gamePanel.allyGameEntities.size(); i++) {
                        entity = gamePanel.allyGameEntities.get(i);
                        if (entity != this && entity.entityIntersects(x + xStep, y + yStep, radius)) {
                            colliding = true;
                        }
                    }
                } else {
                    for (int i = 0; i < gamePanel.enemyGameEntities.size(); i++) {
                        entity = gamePanel.enemyGameEntities.get(i);
                        if (entity != this && entity.entityIntersects(x + xStep, y + yStep, radius)) {
                            colliding = true;
                        }
                    }
                }

                if (colliding || (attackTarget != null && rangeIntersects(attackTarget.getX(),attackTarget.getY(),attackTarget.getRadius()))) {
                    movePoint = null;
                    xStep = 0;
                    yStep = 0;
                } else if (Math.abs(movePoint.x - this.x) < Math.abs(xStep) || Math.abs(movePoint.y - this.y) < Math.abs(yStep)) {
                    this.x = movePoint.x;
                    this.y = movePoint.y;
                    movePoint = null;
                    xStep = 0;
                    yStep = 0;
                } else {
                    this.x += xStep;
                    this.y += yStep;
                    angle = GameUtility.calculateAngle(new Point((int)x,(int)y), movePoint);
                }
            }
            this.screenX = (int)(this.x + gamePanel.screenOffsetX);
            this.screenY = (int)(this.y + gamePanel.screenOffsetY);
        }
    }

    /**
     * Draws the champion entity's hitbox and it's attack range
     * @param g2
     */
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        if (showAttackRange) {
            g2.setColor(new Color(200, 255, 255));
            g2.drawOval(getScreenX()-attackRange, getScreenY()-attackRange, attackRange * 2, attackRange * 2);
            g2.setColor(new Color(0, 51, 51, 80));
            g2.fillOval(getScreenX()-attackRange + 5, getScreenY()-attackRange + 5, attackRange * 2 - 10, attackRange * 2 - 10);
        }
    }
}
