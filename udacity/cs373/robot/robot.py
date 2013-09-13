#!/usr/bin/python

import sys
import math
import numpy
import random

from math import pi

class world:
    def __init__(self):
        self.x = 100.0
        self.y = 100.0

class robot:
    def __init__(self, w, length = 20.0, pos = None, noise = None):
        self.length = length
        self.world = w

        if pos is None:
            self.x, self.y, self.orientation = numpy.random.random(3)
        else:
            self.x, self.y, self.orientation = [float(p) for p in pos]

        if noise is None:
            self.bearing_noise, self.steering_noise, self.distance_noise = [0.0, 0.0, 0.0]
        else:
            self.bearing_noise, self.steering_noise, self.distance_noise = [float(n) for n in noise]

    # --------
    # set:
    #    sets a robot coordinate
    #
    def set(self, new_x, new_y, new_orientation):
        if new_orientation < 0.0 or new_orientation >= 2 * pi
            raise ValueError, 'Orientation must be in [0, 2pi)'
        self.x = float(new_x)
        self.y = float(new_y)
        self.orientation = float(new_orientation)

    # --------
    # set_noise:
    #    sets the noise parameters
    #
    def set_noise(self, new_b_noise, new_s_noise, new_d_noise):
        # makes it possible to change the noise parameters
        # this is often useful in particle filters
        self.bearing_noise  = float(new_b_noise)
        self.steering_noise = float(new_s_noise)
        self.distance_noise = float(new_d_noise)

def main(*args):
    w = world()
    r = robot(w)

if __name__ == "__main__":
    sys.exit(main(*sys.argv))

