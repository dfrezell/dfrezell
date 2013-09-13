/*
 * $Id: Move.java,v 1.3 2003/09/17 07:18:11 dfrezell Exp $
 *
 * (C) Copyright 2002, 2003 AirDefense, Inc. All rights reserved.
 */
package org.frezell.jboard;

public class Move {
	private Piece m_piece;
	private int m_from;
	private int m_to;
	private String m_descrip;

	public Move(Piece piece, int from, int to) {
		m_piece = piece;
		m_from = from;
		m_to = to;
		m_descrip = genDescrip();
	}

	public Move(String move) {
		m_descrip = move;
		byte[] m = move.getBytes();
		m_from = (((m[1] - '0') - 1) * 8) + (m[0] - 'a');
		m_to = (((m[3] - '0') - 1) * 8) + (m[2] - 'a');
	}

	public Piece getPiece() {
		return m_piece;
	}

	public int getFrom() {
		return m_from;
	}

	public int getTo() {
		return m_to;
	}

	public String getDescrip() {
		return m_descrip;
	}

	private String genDescrip() {
		StringBuffer buf = new StringBuffer();
		buf.append((char) ('a' + m_from % 8));
		buf.append(m_from / 8 + 1);
		buf.append((char) ('a' + m_to % 8));
		buf.append(m_to / 8 + 1);

		return buf.toString();
	}
}
