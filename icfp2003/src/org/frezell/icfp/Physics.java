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
 * @version $Revision: 1.7 $
 */
public class Physics {
    public static final int ACTION_NOTHING = 0;
    public static final int ACTION_ACCELERATE = 1;
    public static final int ACTION_LEFT = ACTION_ACCELERATE << 1;
    public static final int ACTION_RIGHT = ACTION_ACCELERATE << 2;
    public static final int ACTION_BRAKE = ACTION_ACCELERATE << 3;

    public static final int A = 24;
    public static final int B = 36;
    public static final int T = 64;
    public static final int L = 20000;
    public static final int F0 = 4;
    public static final int F1 = 12;
    public static final int F2 = 24;

    public static void step(Car car, int action) {
        if (((action & ACTION_ACCELERATE) == ACTION_ACCELERATE) &&
                ((action & ACTION_BRAKE) == ACTION_BRAKE)) {
            throw new IllegalArgumentException("Cannot accelerate and brake at same time.");
        } else if (((action & ACTION_BRAKE) == ACTION_BRAKE) &&
                (((action & ACTION_LEFT) == ACTION_LEFT) || ((action & ACTION_RIGHT) == ACTION_RIGHT))) {
            throw new IllegalArgumentException("Cannot brake and turn at same time.");
        } else if (((action & ACTION_LEFT) == ACTION_LEFT) &&
                ((action & ACTION_RIGHT) == ACTION_RIGHT)) {
            throw new IllegalArgumentException("Cannot turn both ways at same time.");
        }

        car.v -= F0 + FPA.mul(F1, car.v) + FPA.mul(F2, FPA.mul(car.v, car.v));

        if (((action & ACTION_ACCELERATE) == ACTION_ACCELERATE)) {
            car.v += A;
        }

        if (((action & ACTION_BRAKE) == ACTION_BRAKE)) {
            car.v -= B;
        }

        if (car.v < 0) {
            car.v = 0;
        }

        if (((action & ACTION_LEFT) == ACTION_LEFT)) {
            car.d -= FPA.div(T, FPA.mul(car.v, car.v) + L);
        }

        if (((action & ACTION_RIGHT) == ACTION_RIGHT)) {
            car.d += FPA.div(T, FPA.mul(car.v, car.v) + L);
        }

        while (car.d < -FPA.PI) {
            car.d += FPA.TWO_PI;
        }

        while (car.d > FPA.PI) {
            car.d -= FPA.TWO_PI;
        }

        car.x += FPA.mul(car.v, FPA.cos(car.d));
        car.y += FPA.mul(car.v, FPA.sin(car.d));
    }
}
