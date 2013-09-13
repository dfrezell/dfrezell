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
import javax.swing.*;

import org.frezell.icfp.util.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.3 $
 */
public class About extends JDialog {
    public static final String[] AUTHORS = {
        "Drew Frezell"
    };
    public static final String[] EMAILS = {
        "dfrezell@speakeasy.net"
    };
    public static final String WEBSITE = "http://www.frezell.org/icfp/";

    public About() {
        super((JFrame) null, "About", false);
        init();
    }

    public void init() {
        JPanel panel = new JPanel();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Aspect ratio of a widescreen television
        setSize(352, 198);
        // Center the dialog on the screen...the size is set first so the center
        // is calculated properly
        setLocationRelativeTo(null);

        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(TrackViewer.APPNAME + " " + Version.VERSION_STRING));
        panel.add(new JLabel(WEBSITE));
        panel.add(Box.createRigidArea(new Dimension(8, 8)));
        panel.add(new JLabel("Authors:"));
        for (int i = 0; i < AUTHORS.length; i++) {
            panel.add(new JLabel("   " + AUTHORS[i] + " - <" + EMAILS[i] + ">"));
        }
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        getContentPane().add(panel, BorderLayout.CENTER);
    }
}
