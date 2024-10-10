package game;

import audio.AudioUtility;
import entities.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The GamePanel class creates the JPanel of the game and runs it in a game loop
 * ATTRIBUTES ARE PURPOSELY PUBLIC
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class GamePanel extends JPanel implements Runnable {

    public static final int STATE_END = -1;
    public static final int STATE_MENU = 0;
    public static final int STATE_SETUP = 1;
    public static final int STATE_PLAY = 2;
    public static final int STATE_INFO = 3;

    public static final int GAME_WIDTH = 3200;
    public static final int GAME_HEIGHT = 3200;
    public static final int ALLY = 0;
    public static final int ENEMY = 1;
    public static final int MARKSMAN = 0;
    public static final int MAGE = 1;
    public static final int FIGHTER = 2;
    private static final Point PLAYER_SPAWN = new Point(300, GAME_HEIGHT-300);
    private static final Point BARON_SPAWN = new Point(GAME_WIDTH-500, 500);
    private static final Point TOWER_SPAWN_1 = new Point(400, 400);
    private static final Point TOWER_SPAWN_2 = new Point(GAME_WIDTH/2, GAME_HEIGHT/2);
    private static final Point TOWER_SPAWN_3 = new Point(GAME_WIDTH-400, GAME_HEIGHT-400);
    private static final Point ALLY_MELEE_MINION_SPAWN_1 = new Point(PLAYER_SPAWN.x, PLAYER_SPAWN.y-200);
    private static final Point ALLY_MELEE_MINION_SPAWN_2 = new Point(PLAYER_SPAWN.x+200, PLAYER_SPAWN.y);
    private static final Point ALLY_RANGED_MINION_SPAWN_1 = new Point(PLAYER_SPAWN.x-200, PLAYER_SPAWN.y);
    private static final Point ALLY_RANGED_MINION_SPAWN_2 = new Point(PLAYER_SPAWN.x, PLAYER_SPAWN.y+200);
    private static final Point ENEMY_MELEE_MINION_SPAWN_1 = new Point(TOWER_SPAWN_1.x-130,TOWER_SPAWN_1.y+130);
    private static final Point ENEMY_MELEE_MINION_SPAWN_2 = new Point(TOWER_SPAWN_2.x-130,TOWER_SPAWN_2.y+130);
    private static final Point ENEMY_MELEE_MINION_SPAWN_3 = new Point(TOWER_SPAWN_3.x-130,TOWER_SPAWN_3.y+130);
    private static final Point ENEMY_RANGED_MINION_SPAWN_1 = new Point(TOWER_SPAWN_1.x+130,TOWER_SPAWN_1.y-130);
    private static final Point ENEMY_RANGED_MINION_SPAWN_2 = new Point(TOWER_SPAWN_2.x+130,TOWER_SPAWN_2.y-130);
    private static final Point ENEMY_RANGED_MINION_SPAWN_3 = new Point(TOWER_SPAWN_3.x+130,TOWER_SPAWN_3.y-130);
    private static final int SPAWN_INTERVAL = 1200;
    private static final int GAME_END_DISPLAY_INTERVAL = 300;

    private Thread gameThread;
    public GameGUI gameGUI;
    private GameMap gameMap;
    private JFrame frame;
    public double screenOffsetX = 0;
    public double screenOffsetY = 0;
    public int gameState;
    public int screenWidth;
    public int screenHeight;
    public ArrayList<BaseEntity> allyGameEntities;
    public ArrayList<BaseEntity> enemyGameEntities;
    public long time;
    public long pauseTime;
    public MouseHandler mouseHandler = new MouseHandler();
    public KeyHandler keyHandler = new KeyHandler(this);
    public Champion player;
    public int currentPlayer;
    public Rectangle playerBoundary;
    public int gameFPS;
    private boolean mouseChanged;
    private int spawnCounter;
    public boolean won;
    public boolean lost;

    private int endCounter;

    /**
     * Creates the GamePanel
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addMouseListener(mouseHandler);
        this.addKeyListener(keyHandler);
        this.setFocusTraversalKeysEnabled(false);
        this.setFocusable(true);
        this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/NewCursor.png")).getImage(), new Point(0,0),"custom cursor"));
    }

    /**
     * Configures and prepares the game
     */
    public void configGame() {
        gameMap = new GameMap(this);
        gameGUI = new GameGUI(this);
        gameState = STATE_MENU;

        //set full screen
        frame = (JFrame)SwingUtilities.getWindowAncestor((this));
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow((frame));

        screenWidth = frame.getWidth();
        screenHeight = frame.getHeight();

        playerBoundary = new Rectangle(screenWidth/3,screenHeight/3,screenWidth/3,screenHeight/3);
    }

    /**
     * Creates the game entities
     */
    public void createEntities() {
        if (currentPlayer == MARKSMAN) {
            player = new Marksman(this,PLAYER_SPAWN.x, PLAYER_SPAWN.y);
        } else if (currentPlayer == MAGE) {
            player = new Mage(this,PLAYER_SPAWN.x,PLAYER_SPAWN.y);
        } else if (currentPlayer == FIGHTER) {
            player = new Fighter(this, PLAYER_SPAWN.x, PLAYER_SPAWN.y);
        }
        player.setScreenX((int)player.getX());
        player.setScreenY(screenHeight-300);
        screenOffsetX = player.getScreenX() - player.getX();
        screenOffsetY = player.getScreenY() - player.getY();

        allyGameEntities = new ArrayList<>();
        enemyGameEntities = new ArrayList<>();
        allyGameEntities.add(player);
        enemyGameEntities.add(new Baron(this, BARON_SPAWN.getX(), BARON_SPAWN.getY()));
        enemyGameEntities.add(new Tower(this,TOWER_SPAWN_1.getX(), TOWER_SPAWN_1.getY()));
        enemyGameEntities.add(new Tower(this,TOWER_SPAWN_2.getX(), TOWER_SPAWN_2.getY()));
        enemyGameEntities.add(new Tower(this,TOWER_SPAWN_3.getX(), TOWER_SPAWN_3.getY()));
        spawn();
    }

    /**
     * Starts the game thread
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Runs the game loop
     */
    @Override
    public void run() {
        double FPS = 60.0;
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/drawInterval;
            timer += currentTime - lastTime;
            lastTime = System.nanoTime();


            if (delta >= 1) {
                time = currentTime - pauseTime;
                update();
                repaint();
//                drawToBufferedImage();
//                drawToPanel();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                gameFPS = drawCount;
                drawCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * Updates the game depending on the game state
     */
    public void update() {
        if (gameState == STATE_END) {
            AudioUtility.closeAllAudio();
            System.exit(0);
        } else if (gameState == STATE_SETUP) {
            AudioUtility.stopMenuMusic();
            AudioUtility.playGameMusic();
            won = false;
            lost = false;
            endCounter = 0;
            spawnCounter = 0;
            createEntities();
            gameState = STATE_PLAY;
        } else if (gameState == STATE_PLAY) {
            if (won || lost) {
                endCounter++;
                if (endCounter >= GAME_END_DISPLAY_INTERVAL) {
                    gameState = STATE_MENU;
                    endCounter = 0;
                }
            } else {
                spawnCounter++;
                if (spawnCounter >= SPAWN_INTERVAL) {
                    spawn();
                    spawnCounter = 0;
                }
                checkKey();
                for (int i = 0; i < allyGameEntities.size(); i++) {
                    allyGameEntities.get(i).update();
                }
                for (int i = 0; i < enemyGameEntities.size(); i++) {
                    enemyGameEntities.get(i).update();
                }
                despawn();
                setScreenOffset();
            }
        } else if (gameState == STATE_MENU) {
            AudioUtility.stopGameMusic();
            AudioUtility.playMenuMusic();
            screenOffsetX = 0;
            screenOffsetY = 0;
        }
    }

    /**
     * checks for key input
     */
    private void checkKey() {
        if (keyHandler.AKeyPressed()) {
            mouseChanged = true;
            this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/TargetCursor.png")).getImage(),
            new Point(Toolkit.getDefaultToolkit().getBestCursorSize(250, 250).height/2,
            Toolkit.getDefaultToolkit().getBestCursorSize(250, 250).width/2),"custom cursor"));
        } else if (mouseChanged) {
            mouseChanged = false;
            this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getClass().getResource("/images/NewCursor.png")).getImage(), new Point(0,0),"custom cursor"));
        }
    }

    /**
     * spawns game entities
     */
    private void spawn() {
        allyGameEntities.add(new MeleeMinion(this, ALLY, ALLY_MELEE_MINION_SPAWN_1.getX(),ALLY_MELEE_MINION_SPAWN_1.getY()));
        allyGameEntities.add(new MeleeMinion(this, ALLY, ALLY_MELEE_MINION_SPAWN_2.getX(),ALLY_MELEE_MINION_SPAWN_2.getY()));
        allyGameEntities.add(new RangedMinion(this, ALLY, ALLY_RANGED_MINION_SPAWN_1.getX(),ALLY_RANGED_MINION_SPAWN_1.getY()));
        allyGameEntities.add(new RangedMinion(this, ALLY, ALLY_RANGED_MINION_SPAWN_2.getX(),ALLY_RANGED_MINION_SPAWN_2.getY()));
        enemyGameEntities.add(new MeleeMinion(this, ENEMY, ENEMY_MELEE_MINION_SPAWN_1.getX(),ENEMY_MELEE_MINION_SPAWN_1.getY()));
        enemyGameEntities.add(new MeleeMinion(this, ENEMY, ENEMY_MELEE_MINION_SPAWN_2.getX(),ENEMY_MELEE_MINION_SPAWN_2.getY()));
        enemyGameEntities.add(new MeleeMinion(this, ENEMY, ENEMY_MELEE_MINION_SPAWN_3.getX(),ENEMY_MELEE_MINION_SPAWN_3.getY()));
        enemyGameEntities.add(new RangedMinion(this, ENEMY, ENEMY_RANGED_MINION_SPAWN_1.getX(), ENEMY_RANGED_MINION_SPAWN_1.getY()));
        enemyGameEntities.add(new RangedMinion(this, ENEMY, ENEMY_RANGED_MINION_SPAWN_2.getX(), ENEMY_RANGED_MINION_SPAWN_2.getY()));
        enemyGameEntities.add(new RangedMinion(this, ENEMY, ENEMY_RANGED_MINION_SPAWN_3.getX(), ENEMY_RANGED_MINION_SPAWN_3.getY()));
    }

    /**
     * despawns game entities if they are defeated
     */
    private void despawn() {
        BaseEntity entity;
        for (int i = allyGameEntities.size()-1; i >=0; i--) {
            entity = allyGameEntities.get(i);
            if (entity.isDefeated()) {
                if (entity instanceof Champion) {
                    lost = true;
                } else {
                    allyGameEntities.remove(entity);
                }
            }
        }
        for (int i = enemyGameEntities.size()-1; i >=0; i--) {
            entity = enemyGameEntities.get(i);
            if (entity.isDefeated()) {
                enemyGameEntities.remove(entity);
                if (entity instanceof Baron) {
                    won = true;
                } else if (entity instanceof Tower) {
                    enemyGameEntities.add(new MegaMinion(this,ENEMY,entity.getX(),entity.getY()));
                }
            }
        }
    }

    /**
     * sets the screen offset
     */
    private void setScreenOffset() {
        if ((player.getScreenX() < playerBoundary.x && player.getXStep() < 0) || (player.getScreenX() > playerBoundary.x + playerBoundary.width && player.getXStep() > 0)) {
            screenOffsetX -= player.getXStep();
        }
        if ((player.getScreenY() < playerBoundary.y && player.getYStep() < 0) || (player.getScreenY() > playerBoundary.y + playerBoundary.height && player.getYStep() > 0)) {
            screenOffsetY -= player.getYStep();
        }

        if (screenOffsetX > 0) {
            screenOffsetX = 0;
        }
        if (screenOffsetX < screenWidth - GAME_WIDTH) {
            screenOffsetX = screenWidth - GAME_WIDTH;
        }
        if (screenOffsetY > 0) {
            screenOffsetY = 0;
        }
        if (screenOffsetY < screenHeight - GAME_HEIGHT) {
            screenOffsetY = screenHeight - GAME_HEIGHT;
        }
    }

    /**
     * draws to the JPanel
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        if (gameGUI != null) {
            if (gameState == STATE_MENU) {
                gameGUI.draw(g2);
            } else if (enemyGameEntities != null && allyGameEntities != null){
                gameMap.draw(g2);
                ArrayList<BaseEntity> enemies = new ArrayList<>(enemyGameEntities);
                for (BaseEntity enemy : enemies) {
                    enemy.draw(g2);
                }
                ArrayList<BaseEntity> allies = new ArrayList<>(allyGameEntities);
                for (BaseEntity ally : allies) {
                    ally.draw(g2);
                }
                gameGUI.draw(g2);
            }
        }
    }
}
