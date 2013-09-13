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

package org.frezell.icfp;

import java.awt.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.6 $
 */
public class Car {
    String state;
    int x;
    int y;
    int v;
    int d;

    private Rectangle m_bounds = new Rectangle();

    public int getFixedPointX() {
        return x;
    }

    public int getFixedPointY() {
        return y;
    }

    public int getX() {
        return x >>> 16;
    }

    public int getY() {
        return y >>> 16;
    }

    public int getFixedPointVelocity() {
        return v;
    }

    public int getFixedPointDirection() {
        return d;
    }

    public int getVelocity() {
        return v >>> 16;
    }

    public int getDirection() {
        return d >>> 16;
    }

    public void setFixedPointX(int fixedX) {
        x = fixedX;
    }

    public void setFixedPointY(int fixedY) {
        y = fixedY;
    }

    public void setX(int x) {
        this.x = x << 16;
    }

    public void setY(int y) {
        this.y = y << 16;
    }

    public String getState() {
        return state;
    }

    public Rectangle getBoundingBox() {
        return m_bounds;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.ORANGE);
        g2d.fillRect(getX(), getY(), 8, 4);
        m_bounds.setBounds(getX() - 1, getY() - 1, 10, 6);
    }
}
