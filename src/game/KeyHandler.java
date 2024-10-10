package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The KeyHandler handles key input
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class KeyHandler implements KeyListener {
    private GamePanel gamePanel;
    private boolean AKeyPressed;
    private boolean SKeyPressed;
    private boolean QKeyPressed;
    private boolean WKeyPressed;
    private boolean EKeyPressed;
    private boolean RKeyPressed;
    private boolean ShiftKeyPressed;
    private long pauseStartTime;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public boolean AKeyPressed() {
        return AKeyPressed;
    }

    public boolean SKeyPressed() {
        return SKeyPressed;
    }

    public boolean QKeyPressed() {
        return QKeyPressed;
    }

    public boolean WKeyPressed() {
        return WKeyPressed;
    }

    public boolean EKeyPressed() {
        return EKeyPressed;
    }

    public boolean RKeyPressed() {
        return RKeyPressed;
    }

    public boolean ShiftKeyPressed() {
        return ShiftKeyPressed;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gamePanel.gameState == GamePanel.STATE_MENU) {
            menuStateKeyPressed(e.getKeyCode());
        } else if (gamePanel.gameState == GamePanel.STATE_PLAY) {
            playStateKeyPressed(e.getKeyCode());
        } else if (gamePanel.gameState == GamePanel.STATE_INFO) {
            infoStateKeyPressed(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            AKeyPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            SKeyPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            QKeyPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            WKeyPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            EKeyPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            RKeyPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            ShiftKeyPressed = false;
        }
    }

    private void menuStateKeyPressed(int code) {
        if (gamePanel.gameGUI.menuStateCurrentScreen == GameGUI.STATE_MENU_WELCOME_SCREEN) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gamePanel.gameGUI.menuStateWelcomeItem--;
                if (gamePanel.gameGUI.menuStateWelcomeItem < GameGUI.STATE_MENU_WELCOME_SCREEN_ITEM_PLAY) {
                    gamePanel.gameGUI.menuStateWelcomeItem = GameGUI.STATE_MENU_WELCOME_SCREEN_ITEM_QUIT;
                }
            } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gamePanel.gameGUI.menuStateWelcomeItem++;
                if (gamePanel.gameGUI.menuStateWelcomeItem > GameGUI.STATE_MENU_WELCOME_SCREEN_ITEM_QUIT) {
                    gamePanel.gameGUI.menuStateWelcomeItem = GameGUI.STATE_MENU_WELCOME_SCREEN_ITEM_PLAY;
                }
            } else if (code == KeyEvent.VK_ENTER) {
                if (gamePanel.gameGUI.menuStateWelcomeItem == GameGUI.STATE_MENU_WELCOME_SCREEN_ITEM_PLAY) {
                    gamePanel.gameGUI.menuStateCurrentScreen = GameGUI.STATE_MENU_SELECT_SCREEN;
                    gamePanel.gameGUI.menuStateSelectItem = GameGUI.STATE_MENU_SELECT_SCREEN_ITEM_MARKSMAN;
                } else if (gamePanel.gameGUI.menuStateWelcomeItem == GameGUI.STATE_MENU_WELCOME_SCREEN_ITEM_QUIT) {
                    gamePanel.gameState = GamePanel.STATE_END;
                }
            }
        } else if (gamePanel.gameGUI.menuStateCurrentScreen == GameGUI.STATE_MENU_SELECT_SCREEN) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gamePanel.gameGUI.menuStateSelectItem--;
                if (gamePanel.gameGUI.menuStateSelectItem < GameGUI.STATE_MENU_SELECT_SCREEN_ITEM_MARKSMAN) {
                    gamePanel.gameGUI.menuStateSelectItem = GameGUI.STATE_MENU_SELECT_SCREEN_ITEM_BACK;
                }
            } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gamePanel.gameGUI.menuStateSelectItem++;
                if (gamePanel.gameGUI.menuStateSelectItem > GameGUI.STATE_MENU_SELECT_SCREEN_ITEM_BACK) {
                    gamePanel.gameGUI.menuStateSelectItem = GameGUI.STATE_MENU_SELECT_SCREEN_ITEM_MARKSMAN;
                }
            } else if (code == KeyEvent.VK_ENTER) {
                if (gamePanel.gameGUI.menuStateSelectItem == GameGUI.STATE_MENU_SELECT_SCREEN_ITEM_MARKSMAN) {
                    gamePanel.currentPlayer = GamePanel.MARKSMAN;
                    gamePanel.gameState = GamePanel.STATE_SETUP;
                } else if (gamePanel.gameGUI.menuStateSelectItem == GameGUI.STATE_MENU_SELECT_SCREEN_ITEM_MAGE) {
                    gamePanel.currentPlayer = GamePanel.MAGE;
                    gamePanel.gameState = GamePanel.STATE_SETUP;
                } else if (gamePanel.gameGUI.menuStateSelectItem == GameGUI.STATE_MENU_SELECT_SCREEN_ITEM_FIGHTER) {
                    gamePanel.currentPlayer = GamePanel.FIGHTER;
                    gamePanel.gameState = GamePanel.STATE_SETUP;
                } else if (gamePanel.gameGUI.menuStateSelectItem == GameGUI.STATE_MENU_SELECT_SCREEN_ITEM_BACK) {
                    gamePanel.gameGUI.menuStateCurrentScreen = GameGUI.STATE_MENU_WELCOME_SCREEN;
                }
            }
        }
    }

    private void playStateKeyPressed(int code) {
        if (code == KeyEvent.VK_A) {
            AKeyPressed = true;
        } else if (code == KeyEvent.VK_S) {
            SKeyPressed = true;
        } else if (code == KeyEvent.VK_Q) {
            QKeyPressed = true;
        } else if (code == KeyEvent.VK_W) {
            WKeyPressed = true;
        } else if (code == KeyEvent.VK_E) {
            EKeyPressed = true;
        } else if (code == KeyEvent.VK_R) {
            RKeyPressed = true;
        } else if (code == KeyEvent.VK_SHIFT) {
            ShiftKeyPressed = true;
        } else if (code == KeyEvent.VK_TAB) {
            gamePanel.gameState = GamePanel.STATE_INFO;
            pauseStartTime = System.nanoTime();
        } else if (code == KeyEvent.VK_ESCAPE) {
            gamePanel.gameState = GamePanel.STATE_MENU;
        } else if (code == KeyEvent.VK_ALT) {
            gamePanel.player.gainGold(100);
        }
    }

    private void infoStateKeyPressed(int code) {
        if (code == KeyEvent.VK_TAB) {
            gamePanel.pauseTime -= System.nanoTime()- pauseStartTime;
            gamePanel.gameState = GamePanel.STATE_PLAY;
        } else if (code == KeyEvent.VK_1) {
            gamePanel.player.upgradeMaxHP();
        } else if (code == KeyEvent.VK_2) {
            gamePanel.player.upgradeAutoDmg();
        } else if (code == KeyEvent.VK_3) {
            gamePanel.player.upgradeAbilityDmg();
        } else if (code == KeyEvent.VK_4) {
            gamePanel.player.upgradeMoveSpeed();
        } else if (code == KeyEvent.VK_5) {
            gamePanel.player.upgradeAttackSpeed();
        } else if (code == KeyEvent.VK_6) {
            gamePanel.player.upgradeAbilityHaste();
        } else if (code == KeyEvent.VK_7) {
            gamePanel.player.upgradeLifeSteal();
        }
    }
}
