/*
 * Copyright (c) 2003 Drew Frezell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.frezell.ricochet;

import org.frezell.util.GraphicsUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;

public class Square extends JPanel {
    public static final int RL_TILE = 0;
    public static final int LR_TILE = 1;
    public static final int CENTER_TILE = 2;
    public static final int WALL_SEGMENT_VERT = 3;
    public static final int WALL_SEGMENT_HORZ = 4;
    public static final int NUM_IMAGES = 5;

    public static final int MAX_NEIGHBORS = 4;

    private static final Dimension MIN_SIZE;
    private static final Dimension PREF_SIZE;
    private static final Dimension MAX_SIZE;

    private static BufferedImage[] g_origImages;
    private static BufferedImage[] g_accelImages;

    private Robot m_robot;
    private Target m_target;
    private int m_pos;
    private boolean m_highlight;
    private Square[] m_neighbors;
    /**
     * Use a bitfield to specify whether the square has a wall section.
     */
    byte m_wall;

    static {
        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        MIN_SIZE = new Dimension(16, 16);
        PREF_SIZE = new Dimension((int) (bounds.getHeight() / 20.0),
                (int) (bounds.getHeight() / 20.0));
        MAX_SIZE = new Dimension(256, 256);
    }

    static {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String dir = "img/";

        g_origImages = new BufferedImage[NUM_IMAGES];
        g_accelImages = new BufferedImage[NUM_IMAGES];

        try {
            g_origImages[RL_TILE] = ImageIO.read(loader.getResource(dir + "RR_Floor_Tile_RL.png"));
            g_origImages[LR_TILE] = ImageIO.read(loader.getResource(dir + "RR_Floor_Tile_LR.png"));
            g_origImages[CENTER_TILE] = ImageIO.read(loader.getResource(dir + "Center_Tile.png"));
            g_origImages[WALL_SEGMENT_VERT] = ImageIO.read(loader.getResource(dir + "Wall_Segment_Vert.png"));
            g_origImages[WALL_SEGMENT_HORZ] = ImageIO.read(loader.getResource(dir + "Wall_Segment_Horz.png"));
        } catch (IOException ioe) {
            Logger.getAnonymousLogger().warning("Error loading images: " + ioe.getMessage());
        }
    }

    public Square(int pos) {
        m_pos = pos;
        m_wall = 0;
        init();
    }

    public void init() {
        setMinimumSize(MIN_SIZE);
        setPreferredSize(PREF_SIZE);
        setMaximumSize(MAX_SIZE);
    }

    public void initNeighbors(Board board) {
        m_neighbors = new Square[MAX_NEIGHBORS];

        // there are no top neighbors for the top row
        if (m_pos - Board.COLS >= 0) {
            m_neighbors[Wall.TOP] = board.getSquareAt(m_pos - Board.COLS);
        }
        // there are no bottom neighbors for the bottom row
        if (m_pos + Board.COLS < Board.NUM_SQUARES) {
            m_neighbors[Wall.BOTTOM] = board.getSquareAt(m_pos + Board.COLS);
        }
        // there are no left neighbors for the left side
        if (m_pos % Board.COLS != 0) {
            m_neighbors[Wall.LEFT] = board.getSquareAt(m_pos - 1);
        }
        // there are no right neighbors for the right side
        if (m_pos % Board.COLS != Board.COLS - 1) {
            m_neighbors[Wall.RIGHT] = board.getSquareAt(m_pos + 1);
        }
    }

    public void removeRobot() {
        m_robot = null;
    }

    public void setRobot(Robot robot) {
        m_robot = robot;
        robot.setLocation(m_pos);
    }

    public void resetWalls() {
        m_wall = 0;
    }

    public void clearWall(int direction) {
        m_wall &= ~(1 << direction);
        if (m_neighbors[direction] != null) {
            // We need to clear our neighbors wall as well.
            m_neighbors[direction].m_wall &= ~(1 << (direction ^ 1));
        }
    }

    public void setWall(int direction) {
        m_wall |= 1 << direction;
        if (m_neighbors[direction] != null) {
            // We need to set our neighbors wall as well.
            m_neighbors[direction].m_wall |= 1 << (direction ^ 1);
        }
    }

    public boolean isWall(int direction) {
        return (m_wall & (1 << direction)) != 0;
    }

    public Square getNeighbor(int direction) {
        return m_neighbors[direction];
    }

    public Robot getRobot() {
        return m_robot;
    }

    public void removeTarget() {
        m_target = null;
    }

    public void setTarget(Target target) {
        m_target = target;
    }

    public Target getTarget() {
        return m_target;
    }

    public int getPosition() {
        return m_pos;
    }

    public void setHighlight(boolean highlight) {
        m_highlight = highlight;
        repaint();
    }

    public static boolean isCenterSquare(int square) {
        return (square == 119 || square == 120 || square == 135 || square == 136);
    }

    private void doPaintBackground(Graphics2D g2d, int index) {
        int x = 0;
        int y = 0;
        int width = getWidth();
        int height = getHeight();
        BufferedImage accelImage = g_accelImages[index];

        if (accelImage == null ||
                (accelImage.getWidth() != width ||
                accelImage.getHeight() != height)) {
            BufferedImage image = g_origImages[index];
            // Since the tiles of the squares are well, "square" we scale by the
            // same value.
            double scale = (double) width / (double) image.getWidth();
            g_accelImages[index] = GraphicsUtils.scaleImage(g2d, image,
                    width, height, scale, scale);
            accelImage = g_accelImages[index];
        }

        g2d.drawImage(accelImage, x, y, null);
    }

    private void doPaintHighlight(Graphics2D g2d) {
        g2d.setColor(Color.yellow);
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g2d.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
        g2d.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
        g2d.drawRect(3, 3, getWidth() - 7, getHeight() - 7);
        g2d.setColor(new Color(255, 255, 0, 64));
        g2d.fillRect(4, 4, getWidth() - 8, getHeight() - 8);
    }

    private void doPaintWalls(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage accelImageV = g_accelImages[WALL_SEGMENT_VERT];
        BufferedImage accelImageH = g_accelImages[WALL_SEGMENT_HORZ];

        if (accelImageV == null || accelImageV.getHeight() != height) {
            BufferedImage image = g_origImages[WALL_SEGMENT_VERT];
            double scale = (double) height / (double) image.getHeight();
            g_accelImages[WALL_SEGMENT_VERT] = GraphicsUtils.scaleImage(g2d, image, (int) (image.getWidth() * scale), height, scale, scale);
            accelImageV = g_accelImages[WALL_SEGMENT_VERT];
        }

        if (accelImageH == null || accelImageH.getWidth() != width) {
            BufferedImage image = g_origImages[WALL_SEGMENT_HORZ];
            double scale = (double) width / (double) image.getWidth();
            g_accelImages[WALL_SEGMENT_HORZ] = GraphicsUtils.scaleImage(g2d, image, width, (int) (image.getHeight() * scale), scale, scale);
            accelImageH = g_accelImages[WALL_SEGMENT_HORZ];
        }

        if (isWall(Wall.TOP)) {
            g2d.translate(0, -(accelImageH.getHeight() >> 1));
            g2d.drawImage(accelImageH, 0, 0, null);
            g2d.translate(0, (accelImageH.getHeight() >> 1));
        }
        if (isWall(Wall.LEFT)) {
            g2d.translate(-(accelImageV.getWidth() >> 1), 0);
            g2d.drawImage(accelImageV, 0, 0, null);
            g2d.translate(accelImageV.getWidth() >> 1, 0);

        }
        if (isWall(Wall.BOTTOM)) {
            g2d.drawImage(accelImageH, 0, height - (accelImageH.getHeight() >> 1), null);
        }
        if (isWall(Wall.RIGHT)) {
            g2d.drawImage(accelImageV, width - (accelImageV.getWidth() >> 1), 0, null);
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int index = m_pos % 2;

        // set the even row background images to be the opposite from the odd
        // rows...the images should look like this:
        // L R L R L R L R L R L R L R L R
        // R L R L R L R L R L R L R L R L
        // L R L R L R L R L R L R L R L R
        // ...
        if ((m_pos % (Board.COLS * 2) >= Board.COLS &&
                m_pos % (Board.COLS * 2) < (Board.COLS * 2))) {
            // This is a quick way to flip the index from 1->0 and back again.
            index ^= 0x01;
        }

        // Quick hack to paint the center tiles a neutral color.
        if (m_pos == 119 || m_pos == 120 || m_pos == 135 || m_pos == 136) {
            index = 2;
        }

        super.paint(g);

        doPaintBackground(g2d, index);

        if (m_highlight) {
            doPaintHighlight(g2d);
        }
        if (m_target != null) {
            m_target.paint(g, this, 0, 0, getWidth(), getHeight());
        }
        if (m_robot != null) {
            m_robot.paint(g, this, 0, 0, getWidth(), getHeight());
        }
        if (m_wall != 0) {
            doPaintWalls(g);
        }

        //String str = Integer.toString(m_pos);
        //g2d.setColor(Color.BLACK);
        //g2d.setFont(m_f);
        //java.awt.geom.Rectangle2D rect = g.getFontMetrics().getStringBounds(str, g2d);
        //
        //g2d.drawString(str, (getWidth() / 2) - (int) (rect.getWidth() / 2.0),
        //        (getHeight() / 2) - (int) (rect.getHeight() / 2.0));
    }
}