/*
 * $Id: Move.java,v 1.4 2004/08/11 00:45:39 dfrezell Exp $
 *
 * (C) Copyright 2002, 2003 AirDefense, Inc. All rights reserved.
 */
package org.frezell.ricochet;

public class Move {
    private Robot m_robot;
    private int m_from;
    private int m_to;

    public Move(Robot robot, int from, int to) {
        m_robot = robot;
        m_from = from;
        m_to = to;
    }

    public Robot getRobot() {
        return m_robot;
    }

    public int getFrom() {
        return m_from;
    }

    public int getTo() {
        return m_to;
    }

    public int getDirection() {
        return MoveValidator.instance().getMoveDirection(m_from, m_to);
    }
}
