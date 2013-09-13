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

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.5 $
 */
public class FPA {
    public static final int INT_MASK = 0xffff0000;
    public static final int FRACTION_MASK = 0x0000ffff;
    public static final int FIXED_SHIFT = 16;

    public static final long TWO_PI = 411775;
    public static final long PI = 205887;
    public static final long PI_2 = 102944;

    public static long mul(long x, long y) {
        long prod = x * y;
        return prod >>> FIXED_SHIFT;
    }

    public static long div(long x, long y) {
        long xShift = x << FIXED_SHIFT;
        return (int) (xShift / y);
    }

    public static long sin(long x) {
        long x2, x3, x5, x7;
        if (x < 0) {
            return -sin(-x);
        }

        if (x > PI_2) {
            return sin(PI - x);
        }

        x2 = mul(x, x);
        x3 = mul(x, x2);
        x5 = mul(x3, x2);
        x7 = mul(x5, x2);

        return x - (x3 / 6) + (x5 / 120) - (x7 / 5040);
    }

    public static long cos(long x) {
        x += PI_2;
        if (x > PI) {
            x -= TWO_PI;
        }
        return sin(x);
    }
}
