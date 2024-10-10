package entities;

import game.GamePanel;

/**
 * The Minion class defines a Minion object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public abstract class Minion extends MovingEntity {
    public Minion(GamePanel gamePanel, int team, double x, double y, int radius, int maxHP, double damage, int attackRange, double attackSpeed, int moveSpeed, int projectileSpeed, int bounty) {
        super(gamePanel, team, x, y, radius, maxHP, damage, attackRange, attackSpeed, moveSpeed, projectileSpeed, bounty);
    }

    @Override
    public void update() {
        super.update();
        findTarget();
        useAutoAttack();
        move();
    }

}
