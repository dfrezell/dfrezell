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

public class Robot {
    public static final Color RED = new Color(243, 28, 34);
    public static final Color GREEN = new Color(75, 144, 39);
    public static final Color BLUE = new Color(35, 68, 127);
    public static final Color YELLOW = new Color(254, 213, 32);
    public static final Color BLACK = new Color(20, 20, 20);

    /**
     * The order of robots should not be changed, since we use them as indices
     * in an array.  Just your standard red robot.
     */
    public static final int RED_ROBOT = 0;
    /**
     * The order of robots should not be changed, since we use them as indices
     * in an array.  Just your standard green robot.
     */
    public static final int GREEN_ROBOT = 1;
    /**
     * The order of robots should not be changed, since we use them as indices
     * in an array.  Just your standard blue robot.
     */
    public static final int BLUE_ROBOT = 2;
    /**
     * The order of robots should not be changed, since we use them as indices
     * in an array.  Just your standard yellow robot.
     */
    public static final int YELLOW_ROBOT = 3;
    /**
     * The order of robots should not be changed, since we use them as indices
     * in an array.  The black robot should be last in the list since it may
     * or may not be used.  This depends on whether the preference is set or
     * not.
     *
     * @see org.frezell.ricochet.Prefs#PREF_BLACK_ROBOT
     */
    public static final int BLACK_ROBOT = 4;
    /**
     * A count of the number of robots.
     */
    public static final int NUM_ROBOTS = 5;

    private static BufferedImage[] g_origImage;
    private static BufferedImage[] g_accelImage;

    private Color m_color;
    private int m_type;
    private int m_home;
    private int m_loc;

    static {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String dir = "img/";

        try {
            g_origImage = new BufferedImage[NUM_ROBOTS];
            g_accelImage = new BufferedImage[NUM_ROBOTS];

            g_origImage[RED_ROBOT] = ImageIO.read(loader.getResource(dir + "Robot_Red.png"));
            g_origImage[GREEN_ROBOT] = ImageIO.read(loader.getResource(dir + "Robot_Green.png"));
            g_origImage[BLUE_ROBOT] = ImageIO.read(loader.getResource(dir + "Robot_Blue.png"));
            g_origImage[YELLOW_ROBOT] = ImageIO.read(loader.getResource(dir + "Robot_Yellow.png"));
            g_origImage[BLACK_ROBOT] = ImageIO.read(loader.getResource(dir + "Robot_Black.png"));
        } catch (IOException ioe) {
            Logger.getAnonymousLogger().warning("Error loading images: " + ioe.getMessage());
        }
    }

    public Robot(int type) {
        m_type = type;
        switch (m_type) {
            case RED_ROBOT:
                m_color = RED;
                break;
            case GREEN_ROBOT:
                m_color = GREEN;
                break;
            case BLUE_ROBOT:
                m_color = BLUE;
                break;
            case YELLOW_ROBOT:
                m_color = YELLOW;
                break;
            case BLACK_ROBOT:
                m_color = BLACK;
                break;
        }
    }

    public void paint(Graphics g, Component c, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;

        if (g_accelImage[m_type] == null ||
                (g_accelImage[m_type].getWidth() != width ||
                g_accelImage[m_type].getHeight() != height)) {
            BufferedImage image = g_origImage[m_type];
            // Since the tiles of the squares are well, "square" we scale by the
            // same value.
            double scale = (double) width / (double) image.getWidth();
            g_accelImage[m_type] = GraphicsUtils.scaleImage(g2d, image,
                    width, height, scale, scale);
        }

        g2d.drawImage(g_accelImage[m_type], x, y, null);
    }

    public Image getImage() {
        return g_origImage[m_type];
    }

    /**
     * The color of each of the robots.  This is used to draw the trace line
     * of the <code>Robots</code> movements.
     *
     * @return The {@link Color} of the robot's path line.
     * @see #RED
     * @see #GREEN
     * @see #BLUE
     * @see #YELLOW
     * @see #BLACK
     * @see org.frezell.ricochet.Board#paint(java.awt.Graphics)
     */
    public Color getColor() {
        return m_color;
    }

    /**
     * The type of robot, currently this is the same as the color of the robot.
     *
     * @return The robot type specified by <code>RED_ROBOT</code>, etc.
     * @see #RED_ROBOT
     * @see #GREEN_ROBOT
     * @see #BLUE_ROBOT
     * @see #YELLOW_ROBOT
     * @see #BLACK_ROBOT
     */
    public int getType() {
        return m_type;
    }

    /**
     * Get the starting location of the robot for this session.  This may not
     * match the robot's current location.
     *
     * @return The robot's starting location.
     * @see #setHome
     */
    public int getHome() {
        return m_home;
    }

    /**
     * Set the robot's starting location.  This will be updated after each
     * successful demonstration of a path.
     *
     * @param home The starting location of the robot.
     * @see #getHome
     */
    public void setHome(int home) {
        m_home = home;
    }

    /**
     * The location of the robot.  This should map to the square index kept
     * in the <code>m_squares</code> in the {@link Board} object.
     *
     * @return The index of the current location of the Robot.
     * @see #setLocation
     * @see org.frezell.ricochet.Board#m_squares
     */
    public int getLocation() {
        return m_loc;
    }

    /**
     * Set the new location of the robot, <code>loc</code> matches the index
     * value in the <code>m_squares</code> array in the {@link Board}
     * object.
     *
     * @param loc The index of the new location of the Robot.
     * @see #getLocation
     * @see org.frezell.ricochet.Board#m_squares
     */
    public void setLocation(int loc) {
        m_loc = loc;
    }
}