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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A panel is one of the quadrants of the board.  We use this structure when
 * we read in the data from the map.xml file.  The quadrants start in the
 * bottom right and move counter-clockwise.  Here is what the layout is:
 * <p/>
 * |
 * 3   |   2
 * |
 * ------+-------
 * |
 * 4   |   1
 * |
 */
public class Panel {
    public static final int QUAD1 = 0;
    public static final int QUAD2 = 1;
    public static final int QUAD3 = 2;
    public static final int QUAD4 = 3;

    public static final int[] LOC_TX_1 = {
        136, 137, 138, 139, 140, 141, 142, 143,
        152, 153, 154, 155, 156, 157, 158, 159,
        168, 169, 170, 171, 172, 173, 174, 175,
        184, 185, 186, 187, 188, 189, 190, 191,
        200, 201, 202, 203, 204, 205, 206, 207,
        216, 217, 218, 219, 220, 221, 222, 223,
        232, 233, 234, 235, 236, 237, 238, 239,
        248, 249, 250, 251, 252, 253, 254, 255,
    };
    public static final int[] LOC_TX_2 = {
        120, 104, 88, 72, 56, 40, 24, 8,
        121, 105, 89, 73, 57, 41, 25, 9,
        122, 106, 90, 74, 58, 42, 26, 10,
        123, 107, 91, 75, 59, 43, 27, 11,
        124, 108, 92, 76, 60, 44, 28, 12,
        125, 109, 93, 77, 61, 45, 29, 13,
        126, 110, 94, 78, 62, 46, 30, 14,
        127, 111, 95, 79, 63, 47, 31, 15,
    };
    public static final int[] LOC_TX_3 = {
        119, 118, 117, 116, 115, 114, 113, 112,
        103, 102, 101, 100, 99, 98, 97, 96,
        87, 86, 85, 84, 83, 82, 81, 80,
        71, 70, 69, 68, 67, 66, 65, 64,
        55, 54, 53, 52, 51, 50, 49, 48,
        39, 38, 37, 36, 35, 34, 33, 32,
        23, 22, 21, 20, 19, 18, 17, 16,
        7, 6, 5, 4, 3, 2, 1, 0,
    };
    public static final int[] LOC_TX_4 = {
        135, 151, 167, 183, 199, 215, 231, 247,
        134, 150, 166, 182, 198, 214, 230, 246,
        133, 149, 165, 181, 197, 213, 229, 245,
        132, 148, 164, 180, 196, 212, 228, 244,
        131, 147, 163, 179, 195, 211, 227, 243,
        130, 146, 162, 178, 194, 210, 226, 242,
        129, 145, 161, 177, 193, 209, 225, 241,
        128, 144, 160, 176, 192, 208, 224, 240,
    };

    public static final int[] POS_TX_1 = {
        Wall.TOP,
        Wall.BOTTOM,
        Wall.LEFT,
        Wall.RIGHT,
    };
    public static final int[] POS_TX_2 = {
        Wall.LEFT,
        Wall.RIGHT,
        Wall.BOTTOM,
        Wall.TOP,
    };
    public static final int[] POS_TX_3 = {
        Wall.BOTTOM,
        Wall.TOP,
        Wall.RIGHT,
        Wall.LEFT,
    };
    public static final int[] POS_TX_4 = {
        Wall.RIGHT,
        Wall.LEFT,
        Wall.TOP,
        Wall.BOTTOM,
    };

    public static final int[][] LOC_TX = {
        LOC_TX_1,
        LOC_TX_2,
        LOC_TX_3,
        LOC_TX_4
    };

    public static final int[][] POS_TX = {
        POS_TX_1,
        POS_TX_2,
        POS_TX_3,
        POS_TX_4
    };

    /**
     * Bitfield:  The low 1 byte is the location on the normalized panel.  The
     * next 5 bits is the Target.SHAPE + Target.COLOR.  The other bits are
     * currently unused.
     */
    public int[] m_symbols;
    /**
     * Bitfield:  The low 1 byte is the location on the normalized panel.  The
     * next 3 bits is the Wall.POSITION.  The other bits are
     * currently unused.
     */
    public int[] m_walls;
    public int[] m_rebound;

    public Panel(Node node) {
        readSymbols(node);
        readWalls(node);
        readRebound(node);
    }

    private void readSymbols(Node node) {
        NodeList list = ((Element) node).getElementsByTagName("symbol");
        int symbol = 0;
        int loc;
        String value;

        m_symbols = new int[list.getLength()];

        Node symNode = null;
        for (int i = 0; i < m_symbols.length; i++) {
            symNode = list.item(i);
            value = symNode.getAttributes().getNamedItem("shape").getNodeValue();
            if (value.equals("triangle")) {
                symbol = Target.TRIANGLE;
            } else if (value.equals("square")) {
                symbol = Target.SQUARE;
            } else if (value.equals("hexagon")) {
                symbol = Target.HEXAGON;
            } else if (value.equals("circle")) {
                symbol = Target.CIRCLE;
            } else if (value.equals("swirl")) {
                symbol = Target.SWIRL;
            } else {
                // Throw some sort of parse error exception.
            }
            value = symNode.getAttributes().getNamedItem("color").getNodeValue();
            if (value.equals("red")) {
                symbol += Target.RED;
            } else if (value.equals("green")) {
                symbol += Target.GREEN;
            } else if (value.equals("blue")) {
                symbol += Target.BLUE;
            } else if (value.equals("yellow")) {
                symbol += Target.YELLOW;
            } else if (value.equals("multi")) {
                if (symbol != Target.SWIRL) {
                    // Throw some sort of parse error exception.
                }
            } else {
                // Throw some sort of parse error exception.
            }
            loc = Integer.parseInt(symNode.getAttributes().getNamedItem("location").getNodeValue());

            m_symbols[i] = symbol << 8 | loc;
        }
    }

    private void readWalls(Node node) {
        NodeList list = ((Element) node).getElementsByTagName("wall");
        String value = null;
        int loc = 0;
        int pos = 0;

        m_walls = new int[list.getLength()];

        Node symNode = null;
        for (int i = 0; i < m_walls.length; i++) {
            symNode = list.item(i);
            value = symNode.getAttributes().getNamedItem("position").getNodeValue();
            if (value.equals("bottom")) {
                pos = Wall.BOTTOM;
            } else if (value.equals("right")) {
                pos = Wall.RIGHT;
            } else if (value.equals("top")) {
                pos = Wall.TOP;
            } else if (value.equals("left")) {
                pos = Wall.LEFT;
            } else {
                pos = -1;
            }
            loc = Integer.parseInt(symNode.getAttributes().getNamedItem("location").getNodeValue());

            m_walls[i] = pos << 8 | loc;
        }
    }

    private void readRebound(Node node) {
        NodeList list = ((Element) node).getElementsByTagName("rebound");

        m_rebound = new int[list.getLength()];

        Node symNode = null;
        for (int i = 0; i < m_rebound.length; i++) {
            symNode = list.item(i);
            //m_rebound[i] = Integer.parseInt(symNode.getAttributes().getNamedItem("direction").getNodeValue());
            //m_rebound[i] = Integer.parseInt(symNode.getAttributes().getNamedItem("color").getNodeValue());
            m_rebound[i] = Integer.parseInt(symNode.getAttributes().getNamedItem("location").getNodeValue());
        }
    }
}
