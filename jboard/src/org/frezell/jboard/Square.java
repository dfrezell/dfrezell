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

import javax.swing.*;
import java.awt.*;

public class Square extends JPanel {
	public static final int LIGHT_COLOR = 0;
	public static final int DARK_COLOR = 1;
	private static final Dimension MIN_SIZE = new Dimension(16, 16);
	private static final Dimension PREF_SIZE = new Dimension(64, 64);
	private static final Dimension MAX_SIZE = new Dimension(256, 256);

	private static Color g_light = new Color(0xCF, 0xBE, 0x61);
	private static Color g_dark = new Color(0x71, 0x9E, 0x69);

	private Color m_color;
	private Piece m_piece;
	private int m_pos;

	public Square(int color, int pos, Board board) {
		m_color = (color == LIGHT_COLOR) ? g_light : g_dark;
		m_pos = pos;
		init();
	}

	public void init() {
		setBackground(m_color);
		setMinimumSize(MIN_SIZE);
		setPreferredSize(PREF_SIZE);
		setMaximumSize(MAX_SIZE);
	}

	public void initPiece(Piece piece) {
		m_piece = piece;
	}

	public void removePiece() {
		m_piece = null;
	}

	public void setPiece(Piece piece) {
		m_piece = piece;
	}

	public Piece getPiece() {
		return m_piece;
	}

	public int getPosition() {
		return m_pos;
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (m_piece != null) {
			m_piece.paint(g, this, 0, 0, getWidth(), getHeight());
		}
	}
}