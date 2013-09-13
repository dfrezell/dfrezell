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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;

public class Target {
    public static final int RED = 0;
    public static final int GREEN = 1;
    public static final int BLUE = 2;
    public static final int YELLOW = 3;

    public static final int TRIANGLE = 0;
    public static final int SQUARE = 4;
    public static final int HEXAGON = 8;
    public static final int CIRCLE = 12;

    public static final int RED_TRIANGLE = 0;
    public static final int GREEN_TRIANGLE = 1;
    public static final int BLUE_TRIANGLE = 2;
    public static final int YELLOW_TRIANGLE = 3;
    public static final int RED_SQUARE = 4;
    public static final int GREEN_SQUARE = 5;
    public static final int BLUE_SQUARE = 6;
    public static final int YELLOW_SQUARE = 7;
    public static final int RED_HEXAGON = 8;
    public static final int GREEN_HEXAGON = 9;
    public static final int BLUE_HEXAGON = 10;
    public static final int YELLOW_HEXAGON = 11;
    public static final int RED_CIRCLE = 12;
    public static final int GREEN_CIRCLE = 13;
    public static final int BLUE_CIRCLE = 14;
    public static final int YELLOW_CIRCLE = 15;
    public static final int SWIRL = 16;

    public static final int NUM_TARGETS = 17;

    private static BufferedImage[] g_origImage;
    private static BufferedImage[] g_accelImage;

    private int m_type;

    static {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String dir = "img/";

        try {
            g_origImage = new BufferedImage[NUM_TARGETS];
            g_accelImage = new BufferedImage[NUM_TARGETS];

            g_origImage[RED_TRIANGLE] = ImageIO.read(loader.getResource(dir + "Target_Triangle_Red.png"));
            g_origImage[GREEN_TRIANGLE] = ImageIO.read(loader.getResource(dir + "Target_Triangle_Green.png"));
            g_origImage[BLUE_TRIANGLE] = ImageIO.read(loader.getResource(dir + "Target_Triangle_Blue.png"));
            g_origImage[YELLOW_TRIANGLE] = ImageIO.read(loader.getResource(dir + "Target_Triangle_Yellow.png"));
            g_origImage[RED_SQUARE] = ImageIO.read(loader.getResource(dir + "Target_Square_Red.png"));
            g_origImage[GREEN_SQUARE] = ImageIO.read(loader.getResource(dir + "Target_Square_Green.png"));
            g_origImage[BLUE_SQUARE] = ImageIO.read(loader.getResource(dir + "Target_Square_Blue.png"));
            g_origImage[YELLOW_SQUARE] = ImageIO.read(loader.getResource(dir + "Target_Square_Yellow.png"));
            g_origImage[RED_HEXAGON] = ImageIO.read(loader.getResource(dir + "Target_Hexagon_Red.png"));
            g_origImage[GREEN_HEXAGON] = ImageIO.read(loader.getResource(dir + "Target_Hexagon_Green.png"));
            g_origImage[BLUE_HEXAGON] = ImageIO.read(loader.getResource(dir + "Target_Hexagon_Blue.png"));
            g_origImage[YELLOW_HEXAGON] = ImageIO.read(loader.getResource(dir + "Target_Hexagon_Yellow.png"));
            g_origImage[RED_CIRCLE] = ImageIO.read(loader.getResource(dir + "Target_Circle_Red.png"));
            g_origImage[GREEN_CIRCLE] = ImageIO.read(loader.getResource(dir + "Target_Circle_Green.png"));
            g_origImage[BLUE_CIRCLE] = ImageIO.read(loader.getResource(dir + "Target_Circle_Blue.png"));
            g_origImage[YELLOW_CIRCLE] = ImageIO.read(loader.getResource(dir + "Target_Circle_Yellow.png"));
            g_origImage[SWIRL] = ImageIO.read(loader.getResource(dir + "Target_Swirl.png"));
        } catch (IOException ioe) {
            Logger.getAnonymousLogger().warning("Error loading images: " + ioe.getMessage());
        }
    }

    public Target(int type) {
        m_type = type;
    }

    public void paint(Graphics g, Component c, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        int index = m_type;

        if (g_accelImage[index] == null ||
                (g_accelImage[index].getWidth() != width ||
                g_accelImage[index].getHeight() != height)) {
            BufferedImage image = g_origImage[index];
            // Since the tiles of the squares are well, "square" we scale by the
            // same value.
            double scale = (double) width / (double) image.getWidth();
            g_accelImage[index] = GraphicsUtils.scaleImage(g2d, image,
                    width, height, scale, scale);
        }

        g2d.drawImage(g_accelImage[index], x, y, null);
    }

    public Image getImage() {
        return g_origImage[m_type];
    }

    public boolean isValidRobotTarget(int robotType) {
        boolean valid = false;
        switch (robotType) {
            case Robot.RED_ROBOT:
                switch (m_type) {
                    case RED_TRIANGLE:
                    case RED_SQUARE:
                    case RED_HEXAGON:
                    case RED_CIRCLE:
                    case SWIRL:
                        valid = true;
                        break;
                }
                break;
            case Robot.GREEN_ROBOT:
                switch (m_type) {
                    case GREEN_TRIANGLE:
                    case GREEN_SQUARE:
                    case GREEN_HEXAGON:
                    case GREEN_CIRCLE:
                    case SWIRL:
                        valid = true;
                        break;
                }
                break;
            case Robot.BLUE_ROBOT:
                switch (m_type) {
                    case BLUE_TRIANGLE:
                    case BLUE_SQUARE:
                    case BLUE_HEXAGON:
                    case BLUE_CIRCLE:
                    case SWIRL:
                        valid = true;
                        break;
                }
                break;
            case Robot.YELLOW_ROBOT:
                switch (m_type) {
                    case YELLOW_TRIANGLE:
                    case YELLOW_SQUARE:
                    case YELLOW_HEXAGON:
                    case YELLOW_CIRCLE:
                    case SWIRL:
                        valid = true;
                        break;
                }
                break;
            case Robot.BLACK_ROBOT:
                switch (m_type) {
                    case SWIRL:
                        valid = true;
                        break;
                }
                break;
        }
        return valid;
    }
}
