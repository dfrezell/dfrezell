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

package org.frezell.util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class GraphicsUtils {
    public static void centerWindow(Window window) {
        window.setLocation(getCenterOffset(window));
    }

    public static Point getCenterOffset(Window window) {
        Dimension dim = window.getSize();
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();

        return new Point(center.x - (dim.width / 2),
                center.y - (dim.height / 2));
    }

    public static BufferedImage scaleImage(Graphics2D g2d, BufferedImage orig,
                                           int width, int height,
                                           double scaleX, double scaleY) {
        BufferedImage accel;

        AffineTransform tx = new AffineTransform();
        AffineTransformOp op;
        tx.setToScale(scaleX, scaleY);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        // The accelerated image is no longer valid, so we create another
        // one and draw our scaled image into it.
        accel = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.BITMASK);
        ((Graphics2D) accel.getGraphics()).drawImage(orig, op, 0, 0);

        return accel;
    }
}
