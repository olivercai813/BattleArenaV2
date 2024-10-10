package entities;

import attacks.AutoAttack;
import attacks.SlowBlast;
import attacks.MegaBlast;
import game.GamePanel;
import game.GameUtility;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The Marksman class defines a Marksman object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class Marksman extends Champion {
    private static final int MAX_HP = 300;
    private static final int ATTACK_RANGE = 250;
    private static final double ATTACK_SPEED = 1.0;
    private static final double MOVEMENT_SPEED = 6;
    private static final double AUTO_ATTACK_DMG = 10;
    private static final int PROJECTILE_SPEED = 36;
    private static final double ABILITY_HASTE = 1.0;
    private static final double ABILITY_DMG = 10;
    private static final double LIFE_STEAL = 0;
    private SlowBlast QAbility;
    private AutoAttack EAbility;
    private MegaBlast RAbility;
    private static final double W_AS_BOOST = 1.8;
    private static final double W_MS_BOOST = 1.4;
    private static final long W_DURATION = 4000000000L;
    private static final int E_DISTANCE = 150;
    protected boolean ASChanged;
    private final static long BASE_Q_CD = 3000000000L;
    private final static long BASE_W_CD = 12000000000L;
    private static final long BASE_E_CD = 9000000000L;
    private final static long BASE_R_CD = 30000000000L;
    private BufferedImage boostedImage;

    public Marksman(GamePanel gamePanel, double x, double y) {
        super(gamePanel, x, y, MAX_HP, AUTO_ATTACK_DMG, ATTACK_RANGE, ATTACK_SPEED, MOVEMENT_SPEED, PROJECTILE_SPEED, ABILITY_HASTE,ABILITY_DMG, LIFE_STEAL, BASE_Q_CD, BASE_W_CD, BASE_E_CD, BASE_R_CD);
    }

    @Override
    protected void loadImage() {
        this.image = GameUtility.getImage("/images/Marksman.png",radius*2,radius*2);
        this.boostedImage = GameUtility.getImage("/images/BoostedMarksman.png",radius*2,radius*2);
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
        if (ASChanged && gamePanel.time >= prevWTime + W_DURATION) {
            attackCD = (long)(BASE_ATTACK_CD/attackSpeed);
            ASChanged = false;
        }
    }

    public void useQAbility() {
        if (QAbility != null) {
            BaseEntity enemy;
            boolean collided = false;
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
                    if (enemy instanceof MovingEntity) {
                        ((MovingEntity) enemy).changeMoveSpeed(SlowBlast.SLOW_FACTOR,SlowBlast.SLOW_TIME);
                    }
                    collided = true;
                }
            }

            if (collided || QAbility.maxRangeTravelled()) {
                QAbility = null;
            } else {
                QAbility.move();
            }
        } else if (gamePanel.keyHandler.QKeyPressed() && gamePanel.time >= prevQTime + QCoolDown) {

            //getMousePosition() instead of mouseHandler.getClickPoint() because there is no clicking
            if (gamePanel.getMousePosition() != null) {
                int mapX = gamePanel.getMousePosition().x - (int)Math.round(gamePanel.screenOffsetX);
                int mapY = gamePanel.getMousePosition().y- (int)Math.round(gamePanel.screenOffsetY);

                Point clickPoint = new Point(mapX, mapY);
//
//            Point clickPoint = gamePanel.getMousePosition();
//            if (clickPoint != null) {

                prevQTime = gamePanel.time;
                QAbility = new SlowBlast(gamePanel, abilityDmg*2, x, y, clickPoint);
            }
        }
    }

    public void useWAbility() {
        if (!ASChanged && gamePanel.keyHandler.WKeyPressed() && gamePanel.time >= prevWTime + WCoolDown) {
            prevWTime = gamePanel.time;
            attackCD = (long)(BASE_ATTACK_CD/(attackSpeed * W_AS_BOOST));
            changeMoveSpeed(W_MS_BOOST, W_DURATION);
            ASChanged = true;
            MSChanged = true;
        }
    }

    public void useEAbility() {
        if (EAbility != null) {
            if (EAbility.getTarget().isDefeated() || EAbility.isCollided()) {
                EAbility.getTarget().takeDamage(EAbility.getDamage());
                if (EAbility.getTarget().isDefeated()) {
                    gold += EAbility.getTarget().getBounty();
                }
                recoverHealth(EAbility.getDamage()*lifeSteal);
                EAbility = null;
            } else {
                EAbility.move();
            }
        }
        if (gamePanel.keyHandler.EKeyPressed() && gamePanel.time >= prevETime + ECoolDown) {
            if (gamePanel.getMousePosition() != null) {
                int mapX = gamePanel.getMousePosition().x - (int)Math.round(gamePanel.screenOffsetX);
                int mapY = gamePanel.getMousePosition().y- (int)Math.round(gamePanel.screenOffsetY);

                Point clickPoint = new Point(mapX, mapY);

                double denominator = Math.sqrt(Math.pow((clickPoint.x - this.x), 2) + Math.pow((clickPoint.y - this.y), 2));
                xStep = ((clickPoint.x - this.x) / denominator) * E_DISTANCE;
                yStep = ((clickPoint.y - this.y) / denominator) * E_DISTANCE;

                boolean collided = false;
                boolean inRange = false;
                BaseEntity enemy;
                for (int i = 0; i<gamePanel.enemyGameEntities.size(); i++) {
                    enemy = gamePanel.enemyGameEntities.get(i);
                    if (enemy.entityIntersects(x+xStep, y+yStep, radius)) {
                        collided = true;
                        break;
                    }
                    if (rangeIntersects(enemy.getX(), enemy.getY(), enemy.getRadius())) {
                        inRange = true;
                        break;
                    }
                }

                if (!collided) {
                    if (inRange) {
                        int targetIndex = 0;
                        double distance;
                        double minDistance = Integer.MAX_VALUE;

                        for (int i = 0; i < gamePanel.enemyGameEntities.size(); i++) {
                            enemy = gamePanel.enemyGameEntities.get(i);
                            distance = Math.sqrt(Math.pow(x - enemy.getX(), 2) + Math.pow(y - enemy.getY(), 2));
                            if (distance < minDistance) {
                                minDistance = distance;
                                targetIndex = i;
                            }
                        }
                        EAbility = new AutoAttack(gamePanel,abilityDmg*2, x, y, gamePanel.enemyGameEntities.get(targetIndex), projectileSpeed);
                    }

                    prevETime = gamePanel.time;
                    x += xStep;
                    y += yStep;
                    this.screenX = (int)(this.x + gamePanel.screenOffsetX);
                    this.screenY = (int)(this.y + gamePanel.screenOffsetY);

                    movePoint = null;
                    xStep = 0;
                    yStep = 0;
                }
            }
        }
    }

    public void useRAbility() {
        if (RAbility != null) {
            BaseEntity enemy;
            for (int i = 0; i < gamePanel.enemyGameEntities.size(); i++) {
                enemy = gamePanel.enemyGameEntities.get(i);
                if (enemy.entityIntersects(RAbility.getX(), RAbility.getY(), RAbility.getRadius())) {
                    if (!(enemy instanceof Tower)) {
                        enemy.takeDamage(RAbility.getDamage());
                        if (enemy.isDefeated()) {
                            gold += enemy.getBounty();
                        }
                        recoverHealth(RAbility.getDamage()*lifeSteal);
                    }
                }
            }

            if (RAbility.maxRangeTravelled()) {
                RAbility = null;
            } else {
                RAbility.move();
            }
        } else if (gamePanel.keyHandler.RKeyPressed() && gamePanel.time >= prevRTime + RCoolDown) {
            if (gamePanel.getMousePosition() != null) {
                int mapX = gamePanel.getMousePosition().x - (int)Math.round(gamePanel.screenOffsetX);
                int mapY = gamePanel.getMousePosition().y- (int)Math.round(gamePanel.screenOffsetY);

                Point clickPoint = new Point(mapX, mapY);

                prevRTime = gamePanel.time;
                RAbility = new MegaBlast(gamePanel, abilityDmg, x, y, clickPoint);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (QAbility != null) {
            QAbility.draw(g2);
        }
        if (EAbility != null) {
            g2.setColor(Color.MAGENTA);
            EAbility.draw(g2);
        }
        if (RAbility != null) {
            RAbility.draw(g2);
        }
        g2.setColor(Color.white);
        super.draw(g2);
        g2.setColor(new Color(255,255,255,40));
        g2.fillOval(getScreenX()-radius,getScreenY()-radius, radius*2, radius*2);
        if (ASChanged) {
            g2.drawImage(boostedImage, GameUtility.rotateImage(image,getScreenX()-radius, getScreenY()-radius, angle), null);
        } else {
            g2.drawImage(image, GameUtility.rotateImage(image,getScreenX()-radius, getScreenY()-radius, angle), null);
        }
    }
}