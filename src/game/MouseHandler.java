package game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The MouseHandler class handles mouse input
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class MouseHandler implements MouseListener {
    private Point clickPoint;

    public Point getClickPoint() {
        return this.clickPoint;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        clickPoint = new Point(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clickPoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
