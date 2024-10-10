package entities;

import attacks.MeltZone;
import attacks.SkyBlast;
import attacks.StunBlast;
import game.GamePanel;
import game.GameUtility;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The Mage class defines a Mage object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class Mage extends Champion {
    private static final int MAX_HP = 300;
    private static final int ATTACK_RANGE = 300;
    private static final double ATTACK_SPEED = 0.7;
    private static final double MOVEMENT_SPEED = 6;
    private static final double AUTO_ATTACK_DMG = 10;
    private static final int PROJECTILE_SPEED = 16;
    private final static double ABILITY_DMG = 10;
    private static final int ABILITY_HASTE = 1;
    private static final double LIFE_STEAL = 0;
    private static final long BASE_Q_CD = 3000000000L;
    private static final long BASE_W_CD = 9000000000L;
    private static final long BASE_E_CD = 10000000000L;
    private static final long BASE_R_CD = 30000000000L;
    private static final long R_DURATION = 10000000000L;
    private SkyBlast QAbility;
    private MeltZone WAbility;
    private StunBlast EAbility;
    private boolean RBoosted;
    private double RBoostDmg;
    private BufferedImage boostedImage;

    public Mage(GamePanel gamePanel, double x, double y) {
        super(gamePanel, x, y, MAX_HP, AUTO_ATTACK_DMG, ATTACK_RANGE, ATTACK_SPEED, MOVEMENT_SPEED,PROJECTILE_SPEED,
                ABILITY_HASTE, ABILITY_DMG, LIFE_STEAL, BASE_Q_CD, BASE_W_CD, BASE_E_CD, BASE_R_CD);
    }

    @Override
    protected void loadImage() {
        image = GameUtility.getImage("/images/Mage.png",radius*2,radius*2);
        boostedImage = GameUtility.getImage("/images/BoostedMage.png",radius*2,radius*2);
    }

    @Override
    public void update() {
        super.update();
        useQAbility();
        useWAbility();
        useEAbility();
        useRAbility();
    }

    @Override
    public void updateStatus() {
        super.updateStatus();
    }

    @Override
    public void useQAbility() {
        if (QAbility != null) {
            if (gamePanel.time >= prevQTime + SkyBlast.DURATION) {
                QAbility = null;
            } else {
                BaseEntity enemy;
                for (int i = 0; i < gamePanel.enemyGameEntities.size(); i++) {
                    enemy = gamePanel.enemyGameEntities.get(i);
                    if (enemy.entityIntersects(QAbility.getX(), QAbility.getY(), QAbility.getRadius())) {
                        if (!(enemy instanceof Tower)) {
                            enemy.takeDamage(QAbility.getDamage());
                            if (enemy.isDefeated()) {
                                gold += enemy.getBounty();
                            }
                            recoverHealth(QAbility.getDamage()*lifeSteal);
                        }
                    }
                }
            }
        } else if (gamePanel.keyHandler.QKeyPressed() && gamePanel.getMousePosition() != null && gamePanel.time >= prevQTime + QCoolDown) {
            int mapX = gamePanel.getMousePosition().x - (int)Math.round(gamePanel.screenOffsetX);
            int mapY = gamePanel.getMousePosition().y- (int)Math.round(gamePanel.screenOffsetY);

            Point clickPoint = new Point(mapX, mapY);

            if (rangeIntersects(clickPoint.getX(), clickPoint.getY(), 0)) {
                prevQTime = gamePanel.time;
                QAbility = new SkyBlast(gamePanel, abilityDmg, clickPoint.getX(), clickPoint.getY());
            }
        }
    }

    @Override
    public void useWAbility() {
        if (WAbility != null) {
            if (gamePanel.time >= prevWTime + MeltZone.DURATION) {
                WAbility = null;
            } else {
                WAbility.setX(x);
                WAbility.setY(y);
                BaseEntity enemy;
                for (int i = 0; i < gamePanel.enemyGameEntities.size(); i++) {
                    enemy = gamePanel.enemyGameEntities.get(i);
                    if (enemy.entityIntersects(WAbility.getX(), WAbility.getY(), WAbility.getRadius())) {
                        if (!(enemy instanceof Tower)) {
                            enemy.takeDamage(WAbility.getDamage());
                            recoverHealth(WAbility.getDamage()*0.5);
                            if (enemy.isDefeated()) {
                                gold += enemy.getBounty();
                            }
                            recoverHealth(WAbility.getDamage()*lifeSteal);
                        }
                        if (enemy instanceof MovingEntity) {
                            ((MovingEntity) enemy).changeMoveSpeed(MeltZone.SLOW_FACTOR,MeltZone.SLOW_TIME);
                        }
                    }
                }
            }
        } else if (gamePanel.keyHandler.WKeyPressed() && gamePanel.time >= prevWTime + WCoolDown) {
            prevWTime = gamePanel.time;
            WAbility = new MeltZone(gamePanel, abilityDmg, x, y);
        }
    }

    @Override
    public void useEAbility() {
        if (EAbility != null) {
            BaseEntity enemy;
            boolean collided = false;
            for (int i = 0; i < gamePanel.enemyGameEntities.size(); i++) {
                enemy = gamePanel.enemyGameEntities.get(i);
                if (enemy.entityIntersects(EAbility.getX(), EAbility.getY(), EAbility.getRadius())) {
                    if (!(enemy instanceof Tower)) {
                        enemy.takeDamage(EAbility.getDamage());
                        if (enemy.isDefeated()) {
                            gold += enemy.getBounty();
                        }
                        enemy.becomeStunned(StunBlast.STUN_TIME);
                        recoverHealth(EAbility.getDamage()*lifeSteal);
                    }
                    collided = true;
                }
            }

            if (collided || EAbility.maxRangeTravelled()) {
                EAbility = null;
            } else {
                EAbility.move();
            }
        } else if (gamePanel.keyHandler.EKeyPressed() && gamePanel.time >= prevETime + ECoolDown) {
            if (gamePanel.getMousePosition() != null) {
                int mapX = gamePanel.getMousePosition().x - (int)Math.round(gamePanel.screenOffsetX);
                int mapY = gamePanel.getMousePosition().y- (int)Math.round(gamePanel.screenOffsetY);

                Point clickPoint = new Point(mapX, mapY);

                prevETime = gamePanel.time;
                EAbility = new StunBlast(gamePanel, abilityDmg, x, y, clickPoint);
            }
        }
    }

    @Override
    public void useRAbility() {
        if (RBoosted && gamePanel.time >= prevRTime + R_DURATION) {
            RBoosted = false;
            abilityDmg -= RBoostDmg;
        } else if (gamePanel.keyHandler.RKeyPressed() && gamePanel.time >= prevRTime + RCoolDown) {
            prevRTime = gamePanel.time;
            RBoosted = true;
            RBoostDmg = 2*abilityDmg;
            abilityDmg += RBoostDmg;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (QAbility != null) {
            QAbility.draw(g2);
        }
        if (gamePanel.keyHandler.QKeyPressed()) {
            g2.setColor(new Color(200, 255, 255));
            g2.drawOval(getScreenX() - attackRange, getScreenY() - attackRange, attackRange * 2, attackRange * 2);
        }
        if (WAbility != null) {
            WAbility.draw(g2);
        }
        if (EAbility != null) {
            EAbility.draw(g2);
        }
        g2.setColor(Color.white);
        super.draw(g2);
        g2.setColor(new Color(255,255,255,40));
        g2.fillOval(getScreenX() - radius,getScreenY() - radius, radius*2, radius*2);
        if (RBoosted) {
            g2.drawImage(boostedImage, GameUtility.rotateImage(image,getScreenX()-radius, getScreenY()-radius, angle), null);
        } else {
            g2.drawImage(image, GameUtility.rotateImage(image,getScreenX()-radius, getScreenY()-radius, angle), null);
        }
    }
}
