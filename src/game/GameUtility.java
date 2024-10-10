package game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The GameUtility class provides useful utility functions for managing the game
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class GameUtility {

    public static double calculateAngle(Point A, Point B) {
        int a = B.x - A.x;
        int b = A.y - B.y;
        return Math.atan2(a,b);
    }

    public static BufferedImage getImage(String imageFilePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(GameUtility.class.getResourceAsStream(imageFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  image;
    }
    public static BufferedImage getImage(String imageFilePath, int width, int height) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(GameUtility.class.getResourceAsStream(imageFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedImage scaledImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(image, 0, 0, width, height, null);
        g2.dispose();
        return  scaledImage;
    }

    public static AffineTransform rotateImage(BufferedImage image, int x, int y, double angle) {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.rotate(angle,image.getWidth()/2,image.getHeight()/2);
        return at;
    }

    public static int getXforScreenCenteredText(String text, Graphics2D g2, int screenWidth) {
        return screenWidth/2 - ((int)g2.getFontMetrics().getStringBounds(text, g2).getWidth())/2;
    }

    public static void drawWindow(int x, int y, int width, int height, Graphics2D g2) {
        Color color = new Color(0,0,0,210);
        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255,255,255);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
}
