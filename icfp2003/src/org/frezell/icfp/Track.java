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
import java.io.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.7 $
 */
public class Track {
    private int m_width;
    private int m_height;
    private byte[] m_pixels;

    private Point m_car = new Point();
    private Point[] m_finish = new Point[2];

    private Track(int width, int height) {
        m_width = width;
        m_height = height;
        m_pixels = new byte[height * width];
    }

    public int getWidth() {
        return m_width;
    }

    public int getHeight() {
        return m_height;
    }

    public byte[] getPixels() {
        return m_pixels;
    }

    public void setPixels(byte[] pixels) {
        m_pixels = pixels;
    }

    public Point getInitialCarPosition() {
        return m_car;
    }

    public Point[] getFinishLine() {
        return m_finish;
    }

    public static Track getTrack(InputStream is) {
        BufferedInputStream reader = new BufferedInputStream(is);
        Track track;
        try {
            StringBuffer width = new StringBuffer();
            StringBuffer height = new StringBuffer();
            int read;

            do {
                read = reader.read();
                if (Character.isDigit((char) read)) {
                    width.append((char) read);
                }
            } while (read != '\r' && read != '\n');

            // burn a byte and check to see if there is the second part of the
            // newline...this to make sure we can read windows files.
            read = reader.read();
            if (read != '\n') {
                if (Character.isDigit((char) read)) {
                    height.append((char) read);
                }
            }
            do {
                read = reader.read();
                if (Character.isDigit((char) read)) {
                    height.append((char) read);
                }
            } while (read != '\r' && read != '\n');

            track = new Track(Integer.parseInt(width.toString()), Integer.parseInt(height.toString()));
            int offset = 0;
            int length = track.m_width;
            for (int i = 0; i < track.m_height; i++) {
                reader.read(track.m_pixels, offset, length);
                offset += length;

                // need to read the end of line and keep it off the file, if it's
                // not a line ending then we need to add it to our pixel data
                // and shorten the amount we read from the next line.
                read = reader.read();
                if (read != '\r' && read != '\n') {
                    // we've already read one byte into the next line, so we
                    // will read one less byte next time around.
                    length = track.m_width - 1;
                    // Set the next pixel since we've read over to the next line
                    track.m_pixels[offset++] = (byte) read;
                }
                // We do it again to account for windows using two characters
                // to represent an end of line, namely a \r\n.
                read = reader.read();
                if (read != '\r' && read != '\n') {
                    // take one more off the length
                    length--;
                    // Set the next pixel since we've read over to the next line
                    track.m_pixels[offset++] = (byte) read;
                }
            }
            findPositions(track);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            track = null;
        }
        return track;
    }

    public static Track getTrack(File file) throws FileNotFoundException {
        return getTrack(new FileInputStream(file));
    }

    private static void findPositions(Track track) {
        byte[] pixels = track.m_pixels;

        for (int i = 0; i < pixels.length; i++) {
            switch (pixels[i]) {
                case '!': // Finish Line
                    if (track.m_finish[0] == null) {
                        track.m_finish[0] = new Point(i % track.m_width, i / track.m_height);
                    } else {
                        if (track.m_finish[1] == null) {
                            track.m_finish[1] = new Point(i % track.m_width, i / track.m_height);
                        }
                        track.m_finish[1].x = i % track.m_width;
                        track.m_finish[1].y = i / track.m_height;
                    }
                    break;
                case '*': // Car start
                    track.m_car.x = i % track.m_width;
                    track.m_car.y = i / track.m_width;
                    break;
            }
        }
    }
}
