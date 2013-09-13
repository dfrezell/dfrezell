#!/usr/bin/python

import euler

# I did a quick search to find some information about this and found
# http://en.wikipedia.org/wiki/Catalan_number, which is close to what I wanted
# it describes the number of paths without crossing the diagonal.  I then
# searched for the sequence: 1, 2, 6 on
# http://www.research.att.com/~njas/sequences
# and found the "Central binomial coefficients" C(2n,n) = (2n)!/(n!)^2
#
# One description jumped out at me:
#   The number of direct routes from my home to Granny's when Granny 
#   lives n blocks south and n blocks east of my home in Grid City. To 
#   obtain a direct route, from the 2n blocks, choose n blocks on which one
#   travels south.
# Which is what I'm looking for.
#
n = 20

paths = euler.factorial(2 * n) / (euler.factorial(n) ** 2)
print "monotonic paths on",n,"x",n,"grid",paths

