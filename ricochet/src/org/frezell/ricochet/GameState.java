/**
 * Copyright (c) 2002, 2003 Drew Frezell
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
import org.frezell.ricochet.menu.MenuFactory;
import org.frezell.util.RandomLib;
import org.frezell.util.XmlReader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

public class GameState implements ActionListener {
    private JFrame m_frame;
    private StatPanel m_statPanel;
    private Panel[] m_panels;
    private Board m_board;
    private Move m_lastMove;
    private Vector m_moves;
    private Vector m_redo;
    private Vector m_moveListeners;
    private Vector m_gameStatusListeners;
    private Player m_mover;
    private int[] m_targetStack;
    private int m_targetStackPos;
    private Square m_square;
    private Target m_target;

    public GameState(JFrame frame) throws SAXException, ParserConfigurationException, IOException {
        m_frame = frame;
        m_moveListeners = new Vector();
        m_gameStatusListeners = new Vector();
        m_moves = new Vector();
        m_redo = new Vector();
        readPanels();
        m_statPanel = new StatPanel();
        addGameStatusListener(m_statPanel);
        addMoveListener(m_statPanel);
        m_targetStack = new int[Target.NUM_TARGETS];
        for (int i = 0; i < m_targetStack.length; i++) {
            m_targetStack[i] = i;
        }
        m_statPanel.addPickTargetListener(this);
        m_statPanel.addBidListener(this);
    }

    public Panel getPanel(int index) {
        return m_panels[index];
    }

    public Move getLastMove() {
        return m_lastMove;
    }

    public Square getSelectedSquare() {
        return m_square;
    }

    public void setSelectedSquare(Square square) {
        m_square = square;
    }

    public Target getCurrentTarget() {
        return m_target;
    }

    public void setCurrentTarget(Target target) {
        m_target = target;
    }

    public boolean areSquaresEmpty(int[] squares) {
        for (int i = 0; i < squares.length; i++) {
            if (m_board.getSquareAt(squares[i]).getRobot() != null) {
                return false;
            }
        }

        return true;
    }

    public void move(Move move) {
        move(move.getFrom(), move.getTo());
    }

    public void move(int startPos, int endPos) {
        move(m_board.getSquareAt(startPos).getRobot(), startPos, endPos);
    }

    public void move(Robot robot, int startPos, int endPos) {
        Square start = m_board.getSquareAt(startPos);
        Square end = m_board.getSquareAt(endPos);
        Move move = null;
        int dir = Wall.INVALID;

        // Check to make sure the move is even valid.  The main idea is to see
        // if the robot will be able to move off of the square at all.  We
        // calculate where the robot will move to after we determine the move
        // is valid.
        if ((dir = MoveValidator.instance().isValidMove(this, start, end)) !=
                Wall.INVALID) {
            // Ok, now we know the robot can move somewhere on our path,
            // So we just need to step through the neighbors along the path
            // of our starting square to figure out where to stop the robot.
            Square neighbor = start.getNeighbor(dir);
            while (!neighbor.isWall(dir) &&
                    neighbor.getNeighbor(dir) != null &&
                    neighbor.getNeighbor(dir).getRobot() == null) {
                // TODO: We need to take into account squares the have the
                // rebound wall.  We just need to determine if the robot passes
                // through or rebounds, if he rebounds, then we need to update
                // the direction and follow the new path.
                neighbor = neighbor.getNeighbor(dir);
            }

            // We check to see if the same robot is trying to move back to
            // the square it came from.  This is a no-no.
            if (m_lastMove != null &&
                    m_lastMove.getRobot().getType() == robot.getType() &&
                    (dir ^ 0x01) == m_lastMove.getDirection()) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            move = new Move(robot, start.getPosition(), neighbor.getPosition());

            // Notify everyone that cares about our move.
            fireMoveEvent(m_mover, move);
            m_lastMove = move;
            m_moves.add(move);
            // we moved, so we need to clear our redo.
            m_redo.clear();

            // Check to see if we made it to our destination with the right
            // robot.
            if (neighbor.getTarget() != null &&
                    neighbor.getTarget() == m_target &&
                    m_target.isValidRobotTarget(robot.getType())) {
                m_moves.clear();
                m_lastMove = null;
                fireGameStatusEvent(GameStatusEvent.TARGET_REACHED,
                        new Integer(m_statPanel.getCount()));
            }
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public Vector getMoves() {
        return m_moves;
    }

    public void addMoveListener(MoveListener listener) {
        if (!m_moveListeners.contains(listener)) {
            m_moveListeners.add(listener);
        }
    }

    public void removeMoveListener(MoveListener listener) {
        if (listener != null) {
            m_moveListeners.remove(listener);
        }
    }

    private void fireMoveEvent(Player player, Move move) {
        MoveListener[] list = (MoveListener[]) m_moveListeners.toArray(new MoveListener[0]);
        MoveEvent event = new MoveEvent(this, player, move);
        for (int i = 0; i < list.length; i++) {
            list[i].movePerformed(event);
        }
    }

    public void addGameStatusListener(GameStatusListener listener) {
        if (!m_gameStatusListeners.contains(listener)) {
            m_gameStatusListeners.add(listener);
        }
    }

    public void removeGameStatusListener(GameStatusListener listener) {
        if (listener != null) {
            m_gameStatusListeners.remove(listener);
        }
    }

    private void fireGameStatusEvent(int status, Object param) {
        GameStatusListener[] list =
                (GameStatusListener[]) m_gameStatusListeners.toArray(new GameStatusListener[0]);
        GameStatusEvent event = new GameStatusEvent(status, param);
        for (int i = 0; i < list.length; i++) {
            list[i].gameStatusChanged(event);
        }
    }

    private void readPanels() throws SAXException, ParserConfigurationException,
            IOException {
        Document doc = XmlReader.parse("data/map.xml");
        NodeList list = doc.getElementsByTagName("panel");
        m_panels = new Panel[list.getLength()];

        for (int i = 0; i < m_panels.length; i++) {
            m_panels[i] = new Panel(list.item(i));
        }
    }

    private Object getConstraints() {
        //GridBagConstraints gc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
        //        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        //        new Insets(0, 0, 0, 0), 0, 0);

        return BorderLayout.CENTER;
    }

    private void doNewGame() {
        Board b = new Board(this);

        // Remove old board and add the new board as a listener to game events.
        removeGameStatusListener(m_board);
        removeMoveListener(m_board);
        addGameStatusListener(b);
        addMoveListener(b);
        m_frame.getContentPane().removeAll();
        m_frame.getContentPane().add(b, getConstraints());
        m_frame.getContentPane().add(m_statPanel, BorderLayout.NORTH);
        m_frame.getContentPane().validate();
        // Let's help the VM garbage collect the old board.
        if (m_board != null) {
            m_board.removeAll();
            m_board = null;
        }
        m_board = b;

        // create a new moves history for each game...we just clear the contents
        // of the old history.
        m_moves.clear();
        // Reset our random target chip pool.
        RandomLib.randomizeList(m_targetStack);
        m_targetStackPos = 0;
        fireGameStatusEvent(GameStatusEvent.NEW_GAME, null);
    }

    private void doQuit() {
        System.exit(0);
    }

    private void doReset() {
        m_moves.clear();
        m_lastMove = null;
        fireGameStatusEvent(GameStatusEvent.RESET_POSITION, null);
    }

    private void doUndo() {
        if (m_redo.isEmpty() && !m_moves.isEmpty()) {
            Move move = (Move) m_moves.remove(m_moves.size() - 1);
            m_redo.add(move);
            if (!m_moves.isEmpty()) {
                m_lastMove = (Move) m_moves.get(m_moves.size() - 1);
            } else {
                m_lastMove = null;
            }
            m_square = m_board.getSquareAt(move.getFrom());
            m_board.getSquareAt(move.getTo()).removeRobot();
            m_board.getSquareAt(move.getTo()).setHighlight(false);
            m_board.getSquareAt(move.getFrom()).setRobot(move.getRobot());
            m_board.getSquareAt(move.getFrom()).setHighlight(true);
            fireGameStatusEvent(GameStatusEvent.UNDO, move);
        }
    }

    private void doRedo() {
        if (!m_redo.isEmpty()) {
            Move move = (Move) m_redo.remove(m_redo.size() - 1);
            m_moves.add(move);
            m_lastMove = move;
            fireGameStatusEvent(GameStatusEvent.REDO, move);
        }
    }

    private void doPreferences() {
        PrefsDialog dialog = new PrefsDialog(m_frame, true);
        if (dialog.showPrefsDialog() == JOptionPane.OK_OPTION) {
            dialog.savePreferences();
        }
    }

    private void doAbout() {
        JOptionPane.showMessageDialog(m_frame,
                "Ricochet Robots v0.1 - Andrew Frezell <dfrezell@speakeasy.net>",
                "About", JOptionPane.PLAIN_MESSAGE);
    }

    private void doPickTarget() {
        m_target = m_board.getTargetAt(m_targetStack[m_targetStackPos++]);
        fireGameStatusEvent(GameStatusEvent.TARGET_PICKED, m_target);
    }

    private void doBid() {
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        // This is a cheap hack, since we are using an image button with no
        // text.  I set the name of the component so we can use that to identify
        // the pick target button.  Also, we can use this to identify the bid
        // text field.
        String name = null;
        // Check to see if the source object is a component before we cast it.
        // All components should have a name field.
        if (e.getSource() instanceof Component) {
            name = ((Component) e.getSource()).getName();
        }

        if (cmd == MenuFactory.NEW_GAME) {
            doNewGame();
        } else if (cmd == MenuFactory.QUIT) {
            doQuit();
        } else if (cmd == MenuFactory.RESET) {
            doReset();
        } else if (cmd == MenuFactory.UNDO) {
            doUndo();
        } else if (cmd == MenuFactory.REDO) {
            doRedo();
        } else if (cmd == MenuFactory.PREFERENCES) {
            doPreferences();
        } else if (cmd == MenuFactory.ABOUT) {
            doAbout();
        } else if (name == StatPanel.PICK_TARGET) {
            doPickTarget();
        } else if (name == StatPanel.BID) {
            doBid();
        }
    }
}
