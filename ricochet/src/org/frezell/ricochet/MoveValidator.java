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

import org.frezell.util.Singleton;

public class MoveValidator implements Singleton {
    /**
     * The increment value for moving along a row
     */
    public static final int ROW_INC = 1;
    /**
     * The increment value for moving along a column
     */
    public static final int COL_INC = Board.COLS;
    /**
     * The increment value for moving along an upper-left to lower-right
     * diagonal (\)
     */

    private static MoveValidator g_self = new MoveValidator();

    private MoveValidator() {
    }

    public static MoveValidator instance() {
        return g_self;
    }

    /**
     * Check to see if the robot is even able to move off the starting square.
     * There are several things we look for:
     * <ol>
     * <li>Are we moving on top of a robot?</li>
     * <li>Is the click on the same row our column?</li>
     * <li>Is there a wall blocking the direction clicked?</li>
     * <li>Is there a robot blocking the direction clicked?</li>
     * </ol>
     *
     * @param state The game state used to find out details of what's happening.
     * @param start The square the the robot is moving from.
     * @param end   The square that will let us know the direction the robot is
     *              moving.
     * @return Whether the move is valid or not.
     */
    public int isValidMove(GameState state, Square start, Square end) {
        int sPos = start.getPosition();
        int ePos = end.getPosition();
        int dir = Wall.INVALID;

        // Make sure we don't try to move the robot ontop of another robot.
        // This shouldn't happen, since the mouse selection code should
        // highlight the new robot instead of trying to validate the move.
        if (end.getRobot() == null) {
            // Validate the move direction.
            if ((dir = getMoveDirection(sPos, ePos)) != Wall.INVALID) {
                // Make sure we don't have wall in our path on the starting
                // starting square and that our neighbor doesn't have a robot
                if (start.isWall(dir) || start.getNeighbor(dir).getRobot() != null) {
                    dir = Wall.INVALID;
                }
            }
        }

        return dir;
    }

    public int getMoveDirection(int start, int end) {
        int delta = Math.abs(start - end);
        boolean isColMove = (delta >= Board.COLS) && (delta % Board.COLS == 0);
        boolean isRowMove = (Math.min(start, end) % Board.COLS) + delta < Board.COLS;
        int direction = Wall.INVALID;
        int diff = end - start;

        if (isColMove) {
            if (diff < 0) {
                direction = Wall.TOP;
            } else {
                direction = Wall.BOTTOM;
            }
        } else if (isRowMove) {
            if (diff < 0) {
                direction = Wall.LEFT;
            } else {
                direction = Wall.RIGHT;
            }
        }

        return direction;
    }
}
