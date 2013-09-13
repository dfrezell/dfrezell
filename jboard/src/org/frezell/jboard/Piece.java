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

package org.frezell.jboard;

import javax.imageio.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.logging.*;

public class Piece {
	private static final BufferedImage[] ORIGINAL_IMAGES;

	private static BufferedImage[] g_accelImages;
	private static AffineTransform g_tx;
	private static AffineTransformOp g_op;

	private int m_color;
	private int m_type;
	private boolean m_moved;

	static {
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		String dir = "img/";

		g_tx = new AffineTransform();
		ORIGINAL_IMAGES = new BufferedImage[12];
		g_accelImages = new BufferedImage[12];

		try {
			ORIGINAL_IMAGES[Chess.WHITE + Chess.KING] = ImageIO.read(loader.getResource(dir + "kld129.png"));
			ORIGINAL_IMAGES[Chess.WHITE + Chess.QUEEN] = ImageIO.read(loader.getResource(dir + "qld129.png"));
			ORIGINAL_IMAGES[Chess.WHITE + Chess.ROOK] = ImageIO.read(loader.getResource(dir + "rld129.png"));
			ORIGINAL_IMAGES[Chess.WHITE + Chess.BISHOP] = ImageIO.read(loader.getResource(dir + "bld129.png"));
			ORIGINAL_IMAGES[Chess.WHITE + Chess.KNIGHT] = ImageIO.read(loader.getResource(dir + "nld129.png"));
			ORIGINAL_IMAGES[Chess.WHITE + Chess.PAWN] = ImageIO.read(loader.getResource(dir + "pld129.png"));

			ORIGINAL_IMAGES[Chess.BLACK + Chess.KING] = ImageIO.read(loader.getResource(dir + "kdd129.png"));
			ORIGINAL_IMAGES[Chess.BLACK + Chess.QUEEN] = ImageIO.read(loader.getResource(dir + "qdd129.png"));
			ORIGINAL_IMAGES[Chess.BLACK + Chess.ROOK] = ImageIO.read(loader.getResource(dir + "rdd129.png"));
			ORIGINAL_IMAGES[Chess.BLACK + Chess.BISHOP] = ImageIO.read(loader.getResource(dir + "bdd129.png"));
			ORIGINAL_IMAGES[Chess.BLACK + Chess.KNIGHT] = ImageIO.read(loader.getResource(dir + "ndd129.png"));
			ORIGINAL_IMAGES[Chess.BLACK + Chess.PAWN] = ImageIO.read(loader.getResource(dir + "pdd129.png"));
		} catch (IOException ioe) {
			Logger.getAnonymousLogger().warning("Error loading images: " + ioe.getMessage());
		}
	}

	public Piece(int color, int type) {
		m_color = color;
		m_type = type;
	}

	public void paint(Graphics g, Component c, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;
		BufferedImage accelImage = g_accelImages[m_color + m_type];

		if (accelImage == null || (accelImage.getWidth() != width || accelImage.getHeight() != height)) {
			BufferedImage image = ORIGINAL_IMAGES[m_color + m_type];
			g_tx.setToScale((double) width / (double) image.getWidth(),
					(double) height / (double) image.getHeight());
			g_op = new AffineTransformOp(g_tx, AffineTransformOp.TYPE_BILINEAR);

			// The accelerated image is no longer valid, so we create another
			// one and draw our scaled image into it.
			g_accelImages[m_color + m_type] = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.BITMASK);
			accelImage = g_accelImages[m_color + m_type];
			((Graphics2D) accelImage.getGraphics()).drawImage(image, g_op, 0, 0);
		}

		g2d.drawImage(accelImage, x, y, null);
	}

	public Image getImage() {
		return ORIGINAL_IMAGES[m_color + m_type];
	}

	public int getColor() {
		return m_color;
	}

	public int getType() {
		return m_type;
	}

	public void setMoved(boolean moved) {
		m_moved = moved;
	}

	public boolean hasMoved() {
		return m_moved;
	}

	public void promoteTo(int type) {
		m_type = type;
	}
}