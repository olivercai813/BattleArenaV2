package entities;

import attacks.AxeSwing;
import game.GamePanel;
import game.GameUtility;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The Fighter class defines a Fighter object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class Fighter extends Champion {
    private static final int MAX_HP = 300;
    private static final int ATTACK_RANGE = 70;
    private static final double ATTACK_SPEED = 1.0;
    private static final double MOVEMENT_SPEED = 6;
    private static final double AUTO_ATTACK_DMG = 10;
    private static final int PROJECTILE_SPEED = ATTACK_RANGE;
    private static final int ABILITY_HASTE = 1;
    private static final double ABILITY_DMG = 10;
    private static final double LIFE_STEAL = 0;
    private static final long BASE_Q_CD = 6000000000L;
    private static final long BASE_W_CD = 12000000000L;
    private static final long BASE_E_CD = 9000000000L;
    private static final long BASE_R_CD = 30000000000L;
    private static final int Q_RANGE = ATTACK_RANGE + 30;
    private static final long W_DURATION = 4000000000L;
    private static final double W_MOVE_SPEED_BOOST = 0.4;
    private static final long R_STUN_DURATION = 8000000000L;
    private AxeSwing EAbility;
    private boolean WBoosted;
    private int shieldHP;
    private int moveSpeedBoost;
    private BufferedImage boostedImage;
    private BufferedImage biteImage;
    private BufferedImage roarImage;

    public Fighter(GamePanel gamePanel, double x, double y) {
        super(gamePanel, x, y, MAX_HP, AUTO_ATTACK_DMG, ATTACK_RANGE, ATTACK_SPEED, MOVEMENT_SPEED,PROJECTILE_SPEED,
                ABILITY_HASTE, ABILITY_DMG, LIFE_STEAL, BASE_Q_CD, BASE_W_CD, BASE_E_CD, BASE_R_CD);
    }


    @Override
    protected void loadImage() {
        this.image = GameUtility.getImage("/images/Fighter.png",radius*2,radius*2);
        this.boostedImage = GameUtility.getImage("/images/BoostedFighter.png",radius*2,radius*2);
        this.biteImage = GameUtility.getImage("/images/Bite.png",40,40);
        this.roarImage = GameUtility.getImage("/images/Roar.png");
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
    public void useQAbility() {
        if (gamePanel.keyHandler.QKeyPressed() && gamePanel.time >= prevQTime + QCoolDown) {
            if (gamePanel.getMousePosition() != null) {
                int mapX = gamePanel.getMousePosition().x - (int)Math.round(gamePanel.screenOffsetX);
                int mapY = gamePanel.getMousePosition().y- (int)Math.round(gamePanel.screenOffsetY);

                Point clickPoint = new Point(mapX, mapY);

                double distance = Math.sqrt(Math.pow(x - clickPoint.getX(), 2) + Math.pow(y - clickPoint.getY(), 2));
                BaseEntity enemy;
                for (int i = 0; i < gamePanel.enemyGameEntities.size(); i++) {
                    enemy = gamePanel.enemyGameEntities.get(i);
                    if (enemy.entityIntersects(clickPoint.getX(), clickPoint.getY(), 0) && distance <= Q_RANGE && !(enemy instanceof Tower)) {
                        prevQTime = gamePanel.time;
                        enemy.takeDamage(autoAttackDmg+abilityDmg);
                        if (enemy.isDefeated()) {
                            gold += enemy.getBounty();
                        }
                        recoverHealth(abilityDmg);
                        recoverHealth(abilityDmg*lifeSteal);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void useWAbility() {
        if (WBoosted && gamePanel.time >= prevWTime + W_DURATION) {
            WBoosted = false;
            shieldHP = 0;
            moveSpeed -= moveSpeedBoost;
        } else if (gamePanel.keyHandler.WKeyPressed() && gamePanel.time >= prevWTime + WCoolDown) {
            prevWTime = gamePanel.time;
            WBoosted = true;
            shieldHP = maxHP/4;
            moveSpeedBoost = (int)(moveSpeed * W_MOVE_SPEED_BOOST);
            moveSpeed += moveSpeedBoost;
        }
    }

    @Override
    public void useEAbility() {
        if (EAbility != null) {
            if (gamePanel.time >= prevETime + AxeSwing.DURATION) {
                EAbility = null;
            } else {
                EAbility.setX(x);
                EAbility.setY(y);
                BaseEntity enemy;
                for (int i = 0; i < gamePanel.enemyGameEntities.size(); i++) {
                    enemy = gamePanel.enemyGameEntities.get(i);
                    if (enemy.entityIntersects(EAbility.getX(), EAbility.getY(), EAbility.getRadius())) {
                        if (!(enemy instanceof Tower)) {
                            enemy.takeDamage(EAbility.getDamage());
                            if (enemy.isDefeated()) {
                                gold += enemy.getBounty();
                            }
                            recoverHealth(EAbility.getDamage()*lifeSteal);
                        }
                    }
                }
            }
        } else if (gamePanel.keyHandler.EKeyPressed() && gamePanel.time >= prevETime + ECoolDown) {
            prevETime = gamePanel.time;
            EAbility = new AxeSwing(gamePanel, abilityDmg*2, x, y);
        }
    }

    @Override
    public void useRAbility() {
        if (gamePanel.keyHandler.RKeyPressed() && gamePanel.time >= prevRTime + RCoolDown) {
            prevRTime = gamePanel.time;
            for (int i = 0; i <gamePanel.enemyGameEntities.size(); i++) {
                if (!(gamePanel.enemyGameEntities.get(i) instanceof Tower)) {
                    gamePanel.enemyGameEntities.get(i).becomeStunned(R_STUN_DURATION);
                }
            }
        }
    }

    @Override
    public void takeDamage(double damage) {
        if (WBoosted) {
            shieldHP -= damage;
            if (shieldHP < 0) {
                currentHP += shieldHP;
                shieldHP = 0;
            }
        } else if (currentHP - damage < 0) {
            currentHP = 0;
        } else {
            currentHP -= damage;
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        if (gamePanel.keyHandler.QKeyPressed()) {
            g2.setColor(new Color(200, 255, 255));
            g2.drawOval(getScreenX() - Q_RANGE, getScreenY()  - Q_RANGE, Q_RANGE * 2, Q_RANGE * 2);

            if (gamePanel.getMousePosition() != null) {
                int mouseX = gamePanel.getMousePosition().x;
                int mouseY = gamePanel.getMousePosition().y;

                g2.drawImage(biteImage, mouseX - biteImage.getWidth() / 2, mouseY - biteImage.getHeight() / 2, null);
            }
        }
        if (EAbility != null) {
            EAbility.draw(g2);
        }
        if (gamePanel.keyHandler.RKeyPressed()) {
            g2.drawImage(roarImage, getScreenX() - roarImage.getWidth() / 2, getScreenY() - roarImage.getHeight() / 2, null);
        }

        g2.setColor(Color.white);
        super.draw(g2);
        g2.setColor(new Color(255,255,255,40));
        g2.fillOval(getScreenX()-radius,getScreenY()-radius, radius*2, radius*2);
        if (WBoosted) {
            if (currentHP < maxHP) {
                g2.setColor(Color.gray);
                if ((currentHP + shieldHP) > maxHP) {
                    g2.fillRect(getScreenX()-50+(100*(int)Math.round(currentHP)/maxHP), getScreenY() -radius-30, 100*(maxHP-(int)Math.round(currentHP))/maxHP, 10);
                } else {
                    g2.fillRect(getScreenX()-50+(100*(int)Math.round(currentHP)/maxHP), getScreenY() -radius-29, 100*shieldHP/maxHP, 9);
                }
            }
            g2.drawImage(boostedImage, GameUtility.rotateImage(image,getScreenX()-radius, getScreenY()-radius, angle), null);
        } else {
            g2.drawImage(image, GameUtility.rotateImage(image,getScreenX()-radius, getScreenY()-radius, angle), null);
        }

    }
}