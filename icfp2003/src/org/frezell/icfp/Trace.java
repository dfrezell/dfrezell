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

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.7 $
 */
public class Trace {
    public static final int BUFSIZE = 4;

    private String m_name;
    private FileChannel m_channel;
    private ByteBuffer m_buffer;
    private int m_bufferLength;
    private boolean m_fileEmpty;

    public Trace(FileInputStream fis, String name) {
        m_name = name;
        m_channel = fis.getChannel();
        m_buffer = ByteBuffer.allocate(BUFSIZE);
        try {
            m_bufferLength = fillBuffer(m_channel, m_buffer);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean hasMore() {
        return !m_fileEmpty;
    }

    public void write(String command) {
        char[] cmd = command.toCharArray();
        for (int i = 0; i < cmd.length; i++) {
            putChar(cmd[i]);
        }
    }

    public String read() {
        String command = null;
        byte b;

        if (m_fileEmpty) {
            return command;
        }

        // The order of letters in a command string doesn't matter.  Meaning
        // 'al.' and 'la.' are the same and are allowed.
        switch (b = get()) {
            case 'a':
                switch (b = get()) {
                    case 'l':
                        command = RaceManager.CMD_ACCEL_LEFT;
                        break;
                    case 'r':
                        command = RaceManager.CMD_ACCEL_RIGHT;
                        break;
                    case '.':
                        command = RaceManager.CMD_ACCELERATE;
                        break;
                }
                break;
            case 'b':
                command = RaceManager.CMD_BRAKE;
                break;
            case '.':
                command = RaceManager.CMD_NOTHING;
                break;
            case 'l':
                switch (b = get()) {
                    case 'a':
                        command = RaceManager.CMD_ACCEL_LEFT;
                        break;
                    case '.':
                        command = RaceManager.CMD_LEFT;
                        break;
                }
                break;
            case 'r':
                switch (b = get()) {
                    case 'a':
                        command = RaceManager.CMD_ACCEL_RIGHT;
                        break;
                    case '.':
                        command = RaceManager.CMD_RIGHT;
                        break;
                }
                break;
        }

        // Read the remaing end of command '.', if it hasn't already been read
        // the '.' indicates an end of a command string.
        if (b != '.') {
            get();
        }

        return command;
    }

    public void save() {
        try {
            m_channel.write(m_buffer);
            m_channel.force(true);
            m_buffer.clear();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String getName() {
        return m_name;
    }

    private byte get() {
        byte b = 0;

        if (m_buffer.hasRemaining() && m_buffer.position() <  m_bufferLength) {
            b = m_buffer.get();
        } else {
            try {
                m_bufferLength = fillBuffer(m_channel, m_buffer);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            if (m_bufferLength == BUFSIZE) {
                b = m_buffer.get();
            } else if (m_bufferLength < BUFSIZE) {
                b = m_buffer.get();
            } else if (m_bufferLength == -1) {
                m_fileEmpty = true;
            }
        }
        return b;
    }

    private void putChar(char c) {
        // TODO: This is just a shell and needs to check for full buffers and such.
        m_buffer.putChar(c);
    }

    private int fillBuffer(FileChannel channel, ByteBuffer buffer) throws IOException {
        int read;
        buffer.rewind();
        read = channel.read(buffer);
        buffer.rewind();
        return read;
    }

    public static Trace newTrace(File file) throws FileNotFoundException {
        return new Trace(new FileInputStream(file), file.getName());
    }

    public static Trace newTrace(FileInputStream fis, String name) {
        return new Trace(fis, name);
    }

    public static Trace readTrace(File file) throws FileNotFoundException {
        file.setReadOnly();
        return readTrace(new FileInputStream(file), file.getName());
    }

    public static Trace readTrace(FileInputStream fis, String name) {
        return new Trace(fis, name);
    }
}
