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
import java.beans.*;
import java.io.*;
import java.util.Timer;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import org.frezell.icfp.util.*;
import org.frezell.icfp.viewer.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.9 $
 */
public class RaceManager implements PropertyChangeListener, Singleton {
    public static final String EVENT_CAR_MOVED = "CarMoved";
    public static final String EVENT_CAR_STOPPED = "CarStopped";

    public static final String CMD_NOTHING = ".";
    public static final String CMD_ACCELERATE = "a.";
    public static final String CMD_BRAKE = "b.";
    public static final String CMD_LEFT = "l.";
    public static final String CMD_RIGHT = "r.";
    public static final String CMD_ACCEL_LEFT = "al.";
    public static final String CMD_ACCEL_RIGHT = "ar.";

    private static RaceManager g_self = new RaceManager();

    private Car m_car = new Car();
    private SwingPropertyChangeSupport m_changeSupport;
    private Timer m_timer = new Timer();
    private RaceLoop m_task = new RaceLoop();
    private Track m_track;
    private Trace m_trace;
    private StopRace m_stopRace = new StopRace();
    private MoveCar m_moveCar = new MoveCar();

    private RaceManager() {
    }

    public static RaceManager instance() {
        return g_self;
    }

    public String getCommand() {
        return m_trace != null ? m_trace.read() : null;
    }

    public void setCommand(String command) {
        // TODO: add some editing capability to the for Trace files
    }

    public synchronized void startRace(boolean start) {
        if (start) {
            m_timer.scheduleAtFixedRate(m_task = new RaceLoop(), 0, 10);
        } else {
            if (m_task != null) {
                m_task.cancel();
                m_task = null;
            }
            SwingUtilities.invokeLater(m_stopRace);
        }
    }

    public void stepForward() {

    }

    public void stepBackward() {

    }

    private void step(String command) {
        // We are assuming if the command string is null, then the race has
        // ended (at least the trace file is done).  This is a rather poor test
        // and needs to be changed somehow.
        if (command == null) {
            startRace(false);
            return;
        }
        m_car.state = command;
        Physics.step(m_car, parse(command));
    }

    public Rectangle getCarBounds() {
        return m_car.getBoundingBox();
    }

    public Car getCar() {
        return m_car;
    }

    public void paint(Graphics g) {
        m_car.paint(g);
    }

    /**
     * Supports reporting bound property changes.  If <code>oldValue</code>
     * and <code>newValue</code> are not equal and the
     * <code>PropertyChangeEvent</code> listener list isn't empty,
     * then fire a <code>PropertyChange</code> event to each listener.
     * This method has an overloaded method for each primitive type.  For
     * example, here's how to write a bound property set method whose
     * value is an integer:
     * <pre>
     * public void setFoo(int newValue) {
     *     int oldValue = foo;
     *     foo = newValue;
     *     firePropertyChange("foo", oldValue, newValue);
     * }
     * </pre>
     *
     * @param propertyName  the programmatic name of the property
     *		that was changed
     * @param oldValue  the old value of the property (as an Object)
     * @param newValue  the new value of the property (as an Object)
     * @see java.beans.PropertyChangeSupport
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (m_changeSupport != null) {
            m_changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /**
     * Adds a <code>PropertyChangeListener</code> for a specific property.
     * The listener will be invoked only when a call on
     * <code>firePropertyChange</code> names that specific property.
     * <p>
     * If listener is <code>null</code>, no exception is thrown and no
     * action is performed.
     *
     * @param propertyName  the name of the property to listen on
     * @param listener  the <code>PropertyChangeListener</code> to be added
     */
    public synchronized void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (m_changeSupport == null) {
            m_changeSupport = new SwingPropertyChangeSupport(this);
        }
        m_changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes a <code>PropertyChangeListener</code> for a specific property.
     * If listener is <code>null</code>, no exception is thrown and no
     * action is performed.
     *
     * @param propertyName  the name of the property that was listened on
     * @param listener  the <code>PropertyChangeListener</code> to be removed
     */
    public synchronized void removePropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (m_changeSupport == null) {
            return;
        }
        m_changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    private int parse(String command) {
        int action = Physics.ACTION_NOTHING;

        if (command == CMD_NOTHING) {
            action = Physics.ACTION_NOTHING;
        } else if (command == CMD_ACCELERATE) {
            action = Physics.ACTION_ACCELERATE;
        } else if (command == CMD_BRAKE) {
            action = Physics.ACTION_BRAKE;
        } else if (command == CMD_LEFT) {
            action = Physics.ACTION_LEFT;
        } else if (command == CMD_RIGHT) {
            action = Physics.ACTION_RIGHT;
        } else if (command == CMD_ACCEL_LEFT) {
            action = Physics.ACTION_ACCELERATE | Physics.ACTION_LEFT;
        } else if (command == CMD_ACCEL_RIGHT) {
            action = Physics.ACTION_ACCELERATE | Physics.ACTION_RIGHT;
        }
        return action;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // Stop the race to prevent any further updates.
        startRace(false);
        if (evt.getPropertyName() == TrackMenuBar.EVENT_OPEN_MAP) {
            Track track = (Track) evt.getNewValue();
            // Switch to fixed point integer.
            m_car.setX(track.getInitialCarPosition().x);
            m_car.setY(track.getInitialCarPosition().y);
            m_car.d = 0;
            m_car.v = 0;
            m_car.state = CMD_NOTHING;
            m_track = track;
        } else if (evt.getPropertyName() == TrackMenuBar.EVENT_CLEAR_MAP) {
            m_car.setX(0);
            m_car.setY(0);
            m_car.d = 0;
            m_car.v = 0;
            m_car.state = CMD_NOTHING;
            m_track = null;
        } else if (evt.getPropertyName() == TrackMenuBar.EVENT_OPEN_TRACE) {
            Trace trace = (Trace) evt.getNewValue();
            m_trace = trace;
            m_car.setX(m_track.getInitialCarPosition().x);
            m_car.setY(m_track.getInitialCarPosition().y);
            m_car.d = 0;
            m_car.v = 0;
        } else if (evt.getPropertyName() == TrackMenuBar.EVENT_NEW_TRACE) {
            try {
                Trace trace = Trace.newTrace(File.createTempFile("icfp", null));
                m_car.setX(m_track.getInitialCarPosition().x);
                m_car.setY(m_track.getInitialCarPosition().y);
                m_car.d = 0;
                m_car.v = 0;
                m_trace = trace;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else if (evt.getPropertyName() == TrackMenuBar.EVENT_CLEAR_TRACE) {
            m_car.setX(m_track.getInitialCarPosition().x);
            m_car.setY(m_track.getInitialCarPosition().y);
            m_car.d = 0;
            m_car.v = 0;
            m_trace = null;
        }
    }

    public class RaceLoop extends TimerTask {
        public void run() {
            step(getCommand());
            SwingUtilities.invokeLater(m_moveCar);
        }
    }

    public class StopRace implements Runnable {
        public void run() {
            firePropertyChange(EVENT_CAR_STOPPED, null, m_car);
        }
    }

    public class MoveCar implements Runnable {
        public void run() {
            firePropertyChange(EVENT_CAR_MOVED, null, m_car);
        }
    }
}
