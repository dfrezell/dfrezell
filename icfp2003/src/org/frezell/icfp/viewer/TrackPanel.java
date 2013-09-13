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

package org.frezell.icfp.viewer;

import java.awt.*;
import java.awt.image.*;
import java.beans.*;
import javax.swing.*;

import org.frezell.icfp.*;
import org.frezell.icfp.viewer.util.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.11 $
 */
public class TrackPanel extends JPanel implements Scrollable, PropertyChangeListener {
    private Image m_trackImage;
    private int m_width;
    private int m_height;

    public TrackPanel() {
        setOpaque(false);
    }

    public void setTrack(Track track) {
        if (track == null) {
            m_trackImage = null;
            m_width = 0;
            m_height = 0;
        } else {
            m_width = track.getWidth();
            m_height = track.getHeight();

            m_trackImage = createImage(new MemoryImageSource(m_width, m_height,
                    new IndexColorModel(Colormap.BITS, Colormap.SIZE, Colormap.MAP, 0, false),
                    track.getPixels(), 0, m_width));
        }
        setSize(m_width, m_height);
        setPreferredSize(new Dimension(m_width, m_height));
    }

    public void paint(Graphics g) {
        if (m_trackImage != null) {
            super.paint(g);
            g.drawImage(m_trackImage, 0, 0, m_width, m_height, null);
            RaceManager.instance().paint(g);
        } else {
            super.paint(g);
        }
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return orientation == SwingConstants.VERTICAL ?
                (int) (visibleRect.height * 0.9) :
                (int) (visibleRect.width * 0.9);
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(RaceManager.EVENT_CAR_MOVED)) {
            repaint(RaceManager.instance().getCarBounds());
        } else if (evt.getPropertyName().equals(TrackMenuBar.EVENT_OPEN_MAP)) {
            Track track = (Track) evt.getNewValue();
            setTrack(track);
            repaint();
        } else if (evt.getPropertyName().equals(TrackMenuBar.EVENT_CLEAR_MAP)) {
            setTrack(null);
            repaint();
        }
    }
}
