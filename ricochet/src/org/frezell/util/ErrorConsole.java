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

package org.frezell.util;

import javax.swing.*;

public class ErrorConsole {
    private static final String NEWLINE = "\n";
    private static final String TAB = "\t";

    private static ErrorConsole g_instance = new ErrorConsole();
    private JFrame m_frame;
    private JTextArea m_textArea;

    private ErrorConsole() {
        init();
    }

    public static void printStackTrace(Throwable t) {
        g_instance.printError(t);
    }

    private void printError(Throwable t) {
        StackTraceElement[] elements = t.getStackTrace();
        m_textArea.append(NEWLINE);
        m_textArea.append(NEWLINE);
        m_textArea.append(t.toString());
        m_textArea.append(NEWLINE);
        for (int i = 0; i < elements.length; i++) {
            m_textArea.append(TAB);
            m_textArea.append(elements[i].toString());
            m_textArea.append(NEWLINE);
        }
        m_frame.setVisible(true);
    }

    private void init() {
        m_frame = new JFrame("Error Console");
        m_textArea = new JTextArea(25, 50);

        m_frame.getContentPane().add(m_textArea);

        m_frame.pack();
        m_frame.setLocationRelativeTo(null);
    }
}
