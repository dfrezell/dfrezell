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

import java.io.*;

public class Xml2UTF8Xml {
    public static void main(String[] args) throws Exception {
        BufferedReader read = new BufferedReader(
                new InputStreamReader(
                        ClassLoader.getSystemClassLoader().getResourceAsStream(
                                "font/kanalist.xml")));
        BufferedWriter write = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("kanalist2.xml"), "UTF8"));
        String str = null;
        String[] astr = null;

        while ((str = read.readLine()) != null) {
            astr = stripStr(str);
            for (int i = 0; i < astr.length; i++) {
                write.write(astr[i]);
            }
            write.newLine();
        }

        read.close();
        write.close();
    }

    static String[] stripStr(String str) {
        char[] chars = str.toCharArray();
        String[] newStrs = new String[getEscapeCount(chars) + 1];
        int offset = 0;
        int index = 0;
        int i = 0;
        int unicode = 0;
        int value = 0;

        for (i = 0; i < chars.length; i++) {
            if (chars[i] == '\\') {
                if (chars[i + 1] == 'u') {
                    value = chars[i + 2];
                    value -= value <= '9' ? '0' : 'W';
                    unicode = (char) (value * 0x1000);
                    value = chars[i + 3];
                    value -= value <= '9' ? '0' : 'W';
                    unicode += (char) (value * 0x100);
                    value = chars[i + 4];
                    value -= value <= '9' ? '0' : 'W';
                    unicode += (char) (value * 0x10);
                    value = chars[i + 5];
                    value -= value <= '9' ? '0' : 'W';
                    unicode += (char) (value);
                    chars[i] = (char) unicode;
                    newStrs[index++] = new String(chars, offset, i + 1 - offset);
                    i += 5;
                    offset = i + 1;
                }
            }
        }

        newStrs[index] = new String(chars, offset, i - offset);

        return newStrs;
    }

    static int getEscapeCount(char[] chars) {
        int count = 0;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\\') {
                if (chars[++i] == 'u') {
                    count++;
                }
            }
        }

        return count;
    }
}
