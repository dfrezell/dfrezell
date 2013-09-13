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

import org.frezell.ricochet.event.GameStatusEvent;
import org.frezell.ricochet.event.GameStatusListener;
import org.frezell.ricochet.event.MoveEvent;
import org.frezell.ricochet.event.MoveListener;
import org.frezell.util.PrefsManager;
import org.frezell.util.RandomLib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class Board extends JPanel implements MouseListener, MoveListener,
        GameStatusListener {
    public static final int COLS = 16;
    public static final int ROWS = 16;
    public static final int NUM_SQUARES = COLS * ROWS;

    /**
     * This is based on the max size of a Square (256 pixels).  There are 8
     * Squares plus the 1 pixel space of the border and cell spacing.  There
     * are 9 such border/spacers.  So, 8 * 256 + 9 * 1 = 2057.  If we don't
     * set, the panel assumes a max size of Short.MAX_VALUE, ignoring the
     * max size of each cell in a GridLayout.
     */
    private static final Dimension MAX_SIZE = new Dimension(2057, 2057);

    private Square[] m_squares;
    private GameState m_state;
    private Robot[] m_robots;
    private Target[] m_targets;

    public Board(GameState state) {
        init(state);
        m_state = state;
    }

    public void init(GameState state) {
        setLayout(new GridLayout(ROWS, COLS, 0, 0));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addMouseListener(this);
        setMaximumSize(MAX_SIZE);
        initSquares();
        initBasicWalls();

        m_targets = new Target[Target.NUM_TARGETS];
        int[] panel = {0, 1, 2, 3};
        int swap;
        int tmp;
        // Initialize the random library so we get different set each time...
        // numbers taken from RandomLib, these are the internal limits.
        RandomLib.initialize((int) (System.currentTimeMillis() % 31328L),
                (int) ((System.currentTimeMillis() * 3) % 30081L));

        swap = RandomLib.randomInt(0, 3);
        tmp = panel[0];
        panel[0] = panel[swap];
        panel[swap] = tmp;

        swap = RandomLib.randomInt(0, 3);
        tmp = panel[1];
        panel[1] = panel[swap];
        panel[swap] = tmp;

        swap = RandomLib.randomInt(0, 3);
        tmp = panel[2];
        panel[2] = panel[swap];
        panel[swap] = tmp;

        swap = RandomLib.randomInt(0, 3);
        tmp = panel[3];
        panel[3] = panel[swap];
        panel[swap] = tmp;

        int side = RandomLib.randomInt(0, 1);
        initWalls(state.getPanel(panel[0] * 2 + side), 0);
        initSymbols(state.getPanel(panel[0] * 2 + side), 0);
        side = RandomLib.randomInt(0, 1);
        initWalls(state.getPanel(panel[1] * 2 + side), 1);
        initSymbols(state.getPanel(panel[1] * 2 + side), 1);
        side = RandomLib.randomInt(0, 1);
        initWalls(state.getPanel(panel[2] * 2 + side), 2);
        initSymbols(state.getPanel(panel[2] * 2 + side), 2);
        side = RandomLib.randomInt(0, 1);
        initWalls(state.getPanel(panel[3] * 2 + side), 3);
        initSymbols(state.getPanel(panel[3] * 2 + side), 3);

        initRobots();

    }

    public Square getSquareAt(int pos) {
        return m_squares[pos];
    }

    public Target getTargetAt(int pos) {
        return m_targets[pos];
    }

    public void addMoveListener(MoveListener listener) {
        m_state.addMoveListener(listener);
    }

    /**
     * Reset the positions of all the robots and repaint the screen to clear
     * the paths that are drawn.
     */
    private void doReset() {
        Square oldSquare;
        Square newSquare;
        for (int i = 0; i < m_robots.length; i++) {
            oldSquare = getSquareAt(m_robots[i].getLocation());
            newSquare = getSquareAt(m_robots[i].getHome());
            // Tell the robot that it's back home.
            m_robots[i].setLocation(m_robots[i].getHome());
            oldSquare.removeRobot();
            newSquare.setRobot(m_robots[i]);
            // cheap way of telling the old and new square to repaint
            // themselves
            oldSquare.setHighlight(false);
            newSquare.setHighlight(false);

            // We want to keep the selected robot still highlighted after we
            // reset the position.
            if (oldSquare == m_state.getSelectedSquare()) {
                m_state.setSelectedSquare(newSquare);
            }
        }
        // repaint the selection on the highlighted square, if there is one.
        if (m_state.getSelectedSquare() != null) {
            m_state.getSelectedSquare().setHighlight(true);
        }
        // remove all the robot paths...I'm too lazy to calculate what areas
        // to actually repaint.
        repaint();
    }

    private void doUndo() {
        repaint();
    }

    private void doRedo() {
        repaint();
    }

    private void doTargetReached(boolean reached) {
        if (reached) {
            // We reached our target, so set the new home for all the robots.
            for (int i = 0; i < m_robots.length; i++) {
                m_robots[i].setHome(m_robots[i].getLocation());
            }
            m_state.setCurrentTarget(null);
        } else {
            doReset();
        }
        // remove all the robot paths...I'm too lazy to calculate what areas
        // to actually repaint.
        repaint();
    }

    /**
     * Update the center square to show the target to the user.
     */
    private void doTargetPicked() {
        repaint(m_squares[119].getX(), m_squares[119].getY(),
                m_squares[119].getWidth() * 2, m_squares[119].getHeight() * 2);
    }

    private void initSquares() {
        int pos;
        m_squares = new Square[NUM_SQUARES];

        // This draws the board correctly, with the first squre in the lower
        // left corner.  If we wanted to construct a rotated board (i.e. with
        // the black player perspective, we can change the for loops to:
        //
        //		for (int i = 0; i < 8; i++) {
        //			for (int j = 7; j >= 0; j--) {
        //				// construct
        //			}
        //		}
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                pos = (i * ROWS) + j;
                m_squares[pos] = new Square(pos);
                add(m_squares[pos]);
            }
        }
    }

    /**
     * Crazy ass scheme to setup the basic perimeter and center walls.
     */
    private void initBasicWalls() {
        int pos;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                pos = (i * ROWS) + j;
                m_squares[pos].initNeighbors(this);
                // set the walls for the top row
                if (pos >= 0 && pos < ROWS) {
                    m_squares[pos].setWall(Wall.TOP);
                }
                // set the walls on the left edge of the board
                if (pos % COLS == 0) {
                    m_squares[pos].setWall(Wall.LEFT);
                }
                // set the walls on the right edge of the board
                if (pos % COLS == COLS - 1) {
                    m_squares[pos].setWall(Wall.RIGHT);
                }
                // set the walls on the bottom edge
                if (pos >= NUM_SQUARES - COLS && pos < NUM_SQUARES) {
                    m_squares[pos].setWall(Wall.BOTTOM);
                }
                switch (pos) {
                    // We look for the tiles that are unused in the center of
                    // the board.  Each one has two walls.  This is done
                    // for aesthetic purposes and for completeness.
                    // top left empty tile of center board
                    case 119:
                        m_squares[pos].setWall(Wall.TOP);
                        m_squares[pos].setWall(Wall.LEFT);
                        break;
                        // top right empty tile of center board
                    case 120:
                        m_squares[pos].setWall(Wall.TOP);
                        m_squares[pos].setWall(Wall.RIGHT);
                        break;
                        // bottom left empty tile of center board
                    case 135:
                        m_squares[pos].setWall(Wall.BOTTOM);
                        m_squares[pos].setWall(Wall.LEFT);
                        break;
                        // bottom right empty tile of center board
                    case 136:
                        m_squares[pos].setWall(Wall.BOTTOM);
                        m_squares[pos].setWall(Wall.RIGHT);
                        break;
                        // These tiles are the ones bordering the unused center
                        // tiles.  We need to set them for aesthetics as well as
                        // functional game play purposes.
                        // Tiles border the top edge of the center
                    case 103:
                    case 104:
                        m_squares[pos].setWall(Wall.BOTTOM);
                        break;
                        // Tiles border the left edge of the center
                    case 118:
                    case 134:
                        m_squares[pos].setWall(Wall.RIGHT);
                        break;
                        // Tiles border the right edge of the center
                    case 121:
                    case 137:
                        m_squares[pos].setWall(Wall.LEFT);
                        break;
                        // Tiles border the bottom edge of the center
                    case 151:
                    case 152:
                        m_squares[pos].setWall(Wall.TOP);
                        break;
                }
            }
        }
    }

    /**
     * Set the robots in a random location.  We check to make sure we are not
     * putting the robot in the center or on top of another robot.
     */
    private void initRobots() {
        int robot = 0;
        int nextSquare;
        boolean useBlackRobot = PrefsManager.instance().getBoolean(getClass(), Prefs.PREF_BLACK_ROBOT, false);
        int numRobots = useBlackRobot ? Robot.NUM_ROBOTS : Robot.NUM_ROBOTS - 1;
        m_robots = new Robot[numRobots];

        while (robot < numRobots) {
            nextSquare = RandomLib.randomInt(0, NUM_SQUARES - 1);
            // Make sure we are not putting the robot in the center.
            if (Square.isCenterSquare(nextSquare)) {
                continue;
            }

            // Make sure there is not a robot on the square already.
            if (m_squares[nextSquare].getRobot() == null) {
                m_robots[robot] = new Robot(robot);
                m_robots[robot].setHome(nextSquare);
                m_squares[nextSquare].setRobot(m_robots[robot]);
                robot++;
            }
        }
    }

    private void initWalls(Panel p, int q) {
        for (int i = 0; i < p.m_walls.length; i++) {
            int pos = (p.m_walls[i] >> 8) & 0x00ff;
            int loc = p.m_walls[i] & 0x00ff;
            m_squares[Panel.LOC_TX[q][loc]].setWall(Panel.POS_TX[q][pos]);
        }
    }

    /**
     * Loop over the symbols on a particular {@link Panel} and set them on the
     * proper square.  Note: <code>q</code> is the quadrant that the panel is
     * located.  We use this number to select which lookup table
     * {@link org.frezell.ricochet.Panel#LOC_TX} we use for figuring out where
     * we set this damned symbol.
     *
     * @param p The <code>Panel</code> that we set the symbols for.
     * @param q The quadrant of the panel.
     * @see org.frezell.ricochet.Panel#LOC_TX
     */
    private void initSymbols(Panel p, int q) {
        for (int i = 0; i < p.m_symbols.length; i++) {
            int type = (p.m_symbols[i] >> 8) & 0x00ff;
            int loc = p.m_symbols[i] & 0x00ff;
            m_targets[type] = new Target(type);
            m_squares[Panel.LOC_TX[q][loc]].setTarget(m_targets[type]);
        }
    }

    /**
     * Override setBounds so we force the board to be a square.  This makes sure
     * the tiles are always square as well.  We take the smaller of the width
     * and height.  Then we integer divide by the number of ROWS so we truncate
     * the multiply by ROWS so we are sure to have an even division of squares
     * that will tile nicely and we don't have extra padding around the edge
     * of the board.
     *
     * @param x x location
     * @param y y location
     * @param w the width
     * @param h the height
     */
    public void setBounds(int x, int y, int w, int h) {
        if (w <= 0 || h <= 0) {
            super.setBounds(x, y, w, h);
            return;
        }

        if (w != h) {
            // Finally fixed the problem of the changing border around the board
            // it wasn't so difficult, there may be one problem where the board
            // divides evenly into ROWS, so we make the board 2 pixels bigger
            // We add 2 because of the 1 pixel border around the board.  So
            // we need to pad our number by this amount.
            w = h = (Math.min(w, h) / ROWS) * ROWS + 2;
        }

        super.setBounds(x, y, w, h);
    }

    public void paint(Graphics g) {
        super.paint(g);

        Vector v = m_state.getMoves();
        int index = 0;
        Rectangle dim1 = new Rectangle();
        Rectangle dim2 = new Rectangle();
        Rectangle rect = new Rectangle();

        for (int i = 0; i < v.size(); i++) {
            Move move = (Move) v.get(i);
            g.setColor(move.getRobot().getColor());
            index = move.getFrom();
            dim1.width = m_squares[index].getWidth();
            dim1.height = m_squares[index].getHeight();
            dim1.x = m_squares[index].getX();
            dim1.y = m_squares[index].getY();
            index = move.getTo();
            dim2.width = m_squares[index].getWidth();
            dim2.height = m_squares[index].getHeight();
            dim2.x = m_squares[index].getX();
            dim2.y = m_squares[index].getY();

            rect.x = (dim1.x > dim2.x) ? dim2.x + (dim2.width / 2) : dim1.x + (dim1.width / 2);
            rect.y = (dim1.y > dim2.y) ? dim2.y + (dim2.height / 2) : dim1.y + (dim1.height / 2);
            if (dim1.x == dim2.x) {
                // same column
                rect.height = (dim1.y > dim2.y) ? dim1.y - dim2.y : dim2.y - dim1.y;
                //rect.height = dim2.y - dim1.y;
                rect.width = 4;
            } else {
                // same row
                rect.width = (dim1.x > dim2.x) ? dim1.x - dim2.x : dim2.x - dim1.x;
                //rect.width = dim2.x - dim1.x;
                rect.height = 4;
            }

            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }

        if (m_state.getCurrentTarget() != null) {
            // Figure out the border width to come in so we center on the for
            // center tiles.  This is where we want to paint of target chip.
            int width = m_squares[119].getWidth() - m_state.getCurrentTarget().getImage().getWidth(null) / 2;
            g.drawImage(m_state.getCurrentTarget().getImage(), m_squares[119].getX() + width,
                    m_squares[119].getY() + width, null);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point mouse = e.getPoint();
            Component c = getComponentAt(mouse);

            // The user has clicked a dead zone, so we want to do a semi-circle
            // around the board to get the square.  This doesn't happen too
            // often but we are guarenteed to find a square after going half
            // way around.
            if (!(c instanceof Square)) {
                int x = -1, y = -1;
                while (c instanceof Board) {
                    c = getComponentAt(mouse.x - x, mouse.y - y);
                    if (x <= 1)
                        x++;
                    else if (y <= 1) y++;
                }
            }

            Square oldSquare = m_state.getSelectedSquare();
            Square newSquare = (Square) c;

            if (oldSquare == null) {
                if (newSquare.getRobot() != null) {
                    newSquare.setHighlight(true);
                    m_state.setSelectedSquare(newSquare);
                }
            } else {
                // Is the square the user selecting have a robot on it?  If so
                // then we need to set the highlight on the new robot square.
                if (newSquare.getRobot() != null) {
                    if (oldSquare != null) {
                        oldSquare.setHighlight(false);
                    }

                    // Skip out early, this is our toggle select.  The old robot
                    // is deselected and we return.
                    if (oldSquare == newSquare) {
                        m_state.setSelectedSquare(null);
                        return;
                    }

                    newSquare.setHighlight(true);
                    m_state.setSelectedSquare(newSquare);
                } else {
                    // check if the square is a valid move. We will get a
                    // move message if it is a valid move.
                    m_state.move(oldSquare.getRobot(),
                            oldSquare.getPosition(),
                            newSquare.getPosition());
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void gameStatusChanged(GameStatusEvent e) {
        int status = e.getStatus();

        switch (status) {
            case GameStatusEvent.RESET_POSITION:
                doReset();
                break;
            case GameStatusEvent.UNDO:
                doUndo();
                break;
            case GameStatusEvent.REDO:
                doRedo();
                break;
            case GameStatusEvent.TARGET_REACHED:
                doTargetReached(true);
                break;
            case GameStatusEvent.TARGET_NOT_REACHED:
                doTargetReached(false);
            case GameStatusEvent.TARGET_PICKED:
                doTargetPicked();
                break;
        }
    }

    public void movePerformed(MoveEvent e) {
        Move move = e.getMove();
        Square newSquare = m_squares[move.getTo()];
        Square oldSquare = m_squares[move.getFrom()];

        // Need to tell the robot it's new location.
        oldSquare.getRobot().setLocation(move.getTo());
        // TODO: in the distant future we need to check if the
        // Correct robot made it to the current target.  It
        // can be done here, or better yet...have a listener
        // do something when the move occurs...or do it in the
        // GameState when we validate the move.  There are too
        // many choices.
        newSquare.setRobot(oldSquare.getRobot());
        oldSquare.removeRobot();
        oldSquare.setHighlight(false);
        newSquare.setHighlight(true);
        m_state.setSelectedSquare(newSquare);
        // TODO: clean this bullshit up, but this is a quick
        // hack to force the board to repaint the path of
        // the robots.
        repaint();
    }
}