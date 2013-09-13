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

package org.frezell.ricochet.event;

public class GameStatusEvent {
    public static final int NEW_GAME = 0;
    public static final int RESET_POSITION = 1;
    public static final int UNDO = 2;
    public static final int REDO = 3;
    public static final int TARGET_REACHED = 4;
    public static final int TARGET_NOT_REACHED = 5;
    public static final int TARGET_PICKED = 6;

    private int m_status;
    private Object m_param;

    public GameStatusEvent(int status, Object param) {
        m_status = status;
        m_param = param;
    }

    public int getStatus() {
        return m_status;
    }

    public Object getParam() {
        return m_param;
    }
}
