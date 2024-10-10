package game;

import entities.Champion;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The GameGUI class draws different graphics depending on the state of the game
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class GameGUI {

    public static final int STATE_MENU_WELCOME_SCREEN = 0;
    public static final int STATE_MENU_SELECT_SCREEN = 1;

    public static final int STATE_MENU_WELCOME_SCREEN_ITEM_PLAY = 0;
    public static final int STATE_MENU_WELCOME_SCREEN_ITEM_QUIT = 1;

    public static final int STATE_MENU_SELECT_SCREEN_ITEM_MARKSMAN = 0;
    public static final int STATE_MENU_SELECT_SCREEN_ITEM_MAGE = 1;
    public static final int STATE_MENU_SELECT_SCREEN_ITEM_FIGHTER = 2;
    public static final int STATE_MENU_SELECT_SCREEN_ITEM_BACK = 3;

    private GamePanel gamePanel;
    private int menuTile;
    public int menuStateCurrentScreen;
    public int menuStateWelcomeItem;
    public int menuStateSelectItem;
    private BufferedImage winImage;
    private BufferedImage loseImage;

    public GameGUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        menuStateCurrentScreen = STATE_MENU_WELCOME_SCREEN;
        menuStateWelcomeItem = STATE_MENU_WELCOME_SCREEN_ITEM_PLAY;
        menuStateSelectItem = STATE_MENU_SELECT_SCREEN_ITEM_MARKSMAN;
        menuTile = 48;
        winImage = GameUtility.getImage("/images/WinImage.png", 1000,800);
        loseImage = GameUtility.getImage("/images/LoseImage.png", 800,800);
    }

    public void draw(Graphics2D g2) {
        if (gamePanel.gameState == GamePanel.STATE_MENU) {
            drawMenuScreen(g2);
        } else if (gamePanel.gameState == GamePanel.STATE_PLAY) {
            drawPlayScreen(g2);
        } else if (gamePanel.gameState == GamePanel.STATE_INFO) {
            drawInfoScreen(g2);
        }
    }


    private void drawPlayScreen(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.drawString("FPS: " + gamePanel.gameFPS, gamePanel.screenWidth - 100, 40);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.drawString("$ " + gamePanel.player.getGold(), menuTile, menuTile);

        //draw cooldowns
        g2.setColor(new Color(255,255,255,200));
        g2.setStroke(new BasicStroke(5));
        g2.drawRect(0,gamePanel.screenHeight-menuTile/2,gamePanel.screenWidth/4,menuTile/2);
        g2.drawRect(gamePanel.screenWidth/4,gamePanel.screenHeight-menuTile/2,gamePanel.screenWidth/4,menuTile/2);
        g2.drawRect(gamePanel.screenWidth/2,gamePanel.screenHeight-menuTile/2,gamePanel.screenWidth/4,menuTile/2);
        g2.drawRect(3*gamePanel.screenWidth/4,gamePanel.screenHeight-menuTile/2,gamePanel.screenWidth/4,menuTile/2);

        Champion player = gamePanel.player;
        g2.setColor(new Color(255,255,255,100));
        int qBarLength = (int)(((gamePanel.time - player.getPrevQTime())*gamePanel.screenWidth/4)/player.getQCoolDown());
        int wBarLength = (int)(((gamePanel.time - player.getPrevWTime())*gamePanel.screenWidth/4)/player.getWCoolDown());
        int eBarLength = (int)(((gamePanel.time - player.getPrevETime())*gamePanel.screenWidth/4)/player.getECoolDown());
        int rBarLength = (int)(((gamePanel.time - player.getPrevRTime())*gamePanel.screenWidth/4)/player.getRCoolDown());
        if (qBarLength > gamePanel.screenWidth/4) {
            qBarLength = gamePanel.screenWidth/4;
        }
        if (wBarLength > gamePanel.screenWidth/4) {
            wBarLength = gamePanel.screenWidth/4;
        }
        if (eBarLength > gamePanel.screenWidth/4) {
            eBarLength = gamePanel.screenWidth/4;
        }
        if (rBarLength > gamePanel.screenWidth/4) {
            rBarLength = gamePanel.screenWidth/4;
        }

        g2.fillRect(0,gamePanel.screenHeight-menuTile/2,qBarLength,menuTile/2);
        g2.fillRect(gamePanel.screenWidth/4,gamePanel.screenHeight-menuTile/2,wBarLength,menuTile/2);
        g2.fillRect(gamePanel.screenWidth/2,gamePanel.screenHeight-menuTile/2,eBarLength,menuTile/2);
        g2.fillRect(3*gamePanel.screenWidth/4,gamePanel.screenHeight-menuTile/2,rBarLength,menuTile/2);

        if (gamePanel.won) {
            g2.drawImage(winImage,gamePanel.screenWidth/2 - winImage.getWidth()/2, gamePanel.screenHeight/2 - winImage.getHeight()/2, null);
        }
        if (gamePanel.lost) {
            g2.drawImage(loseImage,gamePanel.screenWidth/2 - loseImage.getWidth()/2, gamePanel.screenHeight/2 - loseImage.getHeight()/2, null);
        }
    }

    private void drawScreenBackground(Graphics2D g2) {
        g2.setColor(new Color(255, 102, 102));
        g2.fillRect(0,0, gamePanel.screenWidth, gamePanel.screenHeight);
    }

    private void drawMenuScreen(Graphics2D g2) {
        drawScreenBackground(g2);
        if (menuStateCurrentScreen == STATE_MENU_WELCOME_SCREEN) {
            String text = "Battle Arena";
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 66F));
            int x = GameUtility.getXforScreenCenteredText(text, g2, gamePanel.screenWidth);
            int y = gamePanel.screenHeight / 3;

            //Shadow
            g2.setColor(Color.black);
            g2.drawString(text, x + 5, y + 5);

            //Main color
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            // Menu
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
            text = "New Game";
            x = GameUtility.getXforScreenCenteredText(text, g2, gamePanel.screenWidth);
            y += menuTile * 4;
            g2.drawString(text, x, y);
            if (menuStateWelcomeItem == STATE_MENU_WELCOME_SCREEN_ITEM_PLAY) {
                g2.drawString(">", x - menuTile, y);
            }

            text = "Quit";
            x = GameUtility.getXforScreenCenteredText(text, g2, gamePanel.screenWidth);
            y += menuTile;
            g2.drawString(text, x, y);
            if (menuStateWelcomeItem == STATE_MENU_WELCOME_SCREEN_ITEM_QUIT) {
                g2.drawString(">", x - menuTile, y);
            }
        } else if (menuStateCurrentScreen == STATE_MENU_SELECT_SCREEN) {
            // Character selection screen
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "Select your character!";
            int x = GameUtility.getXforScreenCenteredText(text, g2, gamePanel.screenWidth);
            int y = gamePanel.screenHeight/3;
            g2.drawString(text, x, y);

            text = "Marksman";
            x = GameUtility.getXforScreenCenteredText(text, g2, gamePanel.screenWidth);
            y += menuTile;
            g2.drawString(text, x, y);
            if (menuStateSelectItem == STATE_MENU_SELECT_SCREEN_ITEM_MARKSMAN) {
                g2.drawString(">", x - menuTile, y);
            }

            text = "Mage";
            x = GameUtility.getXforScreenCenteredText(text, g2, gamePanel.screenWidth);
            y += menuTile;
            g2.drawString(text, x, y);
            if (menuStateSelectItem == STATE_MENU_SELECT_SCREEN_ITEM_MAGE) {
                g2.drawString(">", x - menuTile, y);
            }

            text = "Fighter";
            x = GameUtility.getXforScreenCenteredText(text, g2, gamePanel.screenWidth);
            y += menuTile;
            g2.drawString(text, x, y);
            if (menuStateSelectItem == STATE_MENU_SELECT_SCREEN_ITEM_FIGHTER) {
                g2.drawString(">", x - menuTile, y);
            }

            text = "Back";
            x = GameUtility.getXforScreenCenteredText(text, g2, gamePanel.screenWidth);
            y += menuTile * 2;
            g2.drawString(text, x, y);
            if (menuStateSelectItem == STATE_MENU_SELECT_SCREEN_ITEM_BACK) {
                g2.drawString(">", x - menuTile, y);
            }
        }
    }

    private void drawInfoScreen(Graphics2D g2) {
        int windowX = menuTile*2;
        int windowY = menuTile ;
        int windowWidth = menuTile * 14;
        int windowHeight = menuTile * 16;
        GameUtility.drawWindow(windowX, windowY, windowWidth, windowHeight, g2);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));

        String text = "Max HP";
        g2.drawString(text, menuTile*3, menuTile*3);
        for (int i = 0; i < gamePanel.player.getMaxHpUpgrades(); i++) {
            g2.fillRect(menuTile*i/4 + menuTile * (8 + i), menuTile/3 + menuTile*2, menuTile, menuTile);
        }

        text = "Attack Damage";
        g2.drawString(text, menuTile*3, menuTile*5);
        for (int i = 0; i < gamePanel.player.getAutoDmgUpgrades(); i++) {
            g2.fillRect(menuTile*i/4 + menuTile * (8 + i), menuTile/3 + menuTile*4, menuTile, menuTile);
        }

        text = "Ability Damage";
        g2.drawString(text, menuTile*3, menuTile*7);
        for (int i = 0; i < gamePanel.player.getAbilityDmgUpgrades(); i++) {
            g2.fillRect(menuTile*i/4 + menuTile * (8 + i), menuTile/3 + menuTile*6, menuTile, menuTile);
        }

        text = "Move Speed";
        g2.drawString(text, menuTile*3, menuTile*9);
        for (int i = 0; i < gamePanel.player.getMoveSpeedUpgrades(); i++) {
            g2.fillRect(menuTile*i/4 + menuTile * (8 + i), menuTile/3 + menuTile*8, menuTile, menuTile);
        }

        text = "Attack Speed";
        g2.drawString(text, menuTile*3, menuTile*11);
        for (int i = 0; i < gamePanel.player.getAttackSpeedUpgrades(); i++) {
            g2.fillRect(menuTile*i/4 + menuTile * (8 + i), menuTile/3 + menuTile*10, menuTile, menuTile);
        }

        text = "Ability Haste";
        g2.drawString(text, menuTile*3, menuTile*13);
        for (int i = 0; i < gamePanel.player.getAbilityHasteUpgrades(); i++) {
            g2.fillRect(menuTile*i/4 + menuTile * (8 + i), menuTile/3 + menuTile*12, menuTile, menuTile);
        }

        text = "Life Steal";
        g2.drawString(text, menuTile*3, menuTile*15);
        for (int i = 0; i < gamePanel.player.getLifeStealUpgrades(); i++) {
            g2.fillRect(menuTile*i/4 + menuTile * (8 + i), menuTile/3 + menuTile*14, menuTile, menuTile);
        }

        windowX = gamePanel.screenWidth - menuTile*5;
        windowY = menuTile;
        windowWidth = menuTile*4;
        windowHeight = menuTile*3/2 ;
        GameUtility.drawWindow(windowX, windowY, windowWidth, windowHeight, g2);
        g2.drawString("$ " + gamePanel.player.getGold(), gamePanel.screenWidth - menuTile*4, menuTile*2);
    }
}
