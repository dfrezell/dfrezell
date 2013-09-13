#!/usr/bin/python

import array

# compute 2 6side dice probability table
d6count = 6
side  = 6

dice6 = [0] * ((d6count * side) + 1)

for d1 in range(1,side+1):
    for d2 in range(1,side+1):
        for d3 in range(1,side+1):
            for d4 in range(1,side+1):
                for d5 in range(1,side+1):
                    for d6 in range(1,side+1):
                        dice6[d1+d2+d3+d4+d5+d6] += 1

# compute 2 4side dice probability table
d4count = 9
side  = 4

dice4 = [0] * ((d4count * side) + 1)

for d1 in range(1,side+1):
    for d2 in range(1,side+1):
        for d3 in range(1,side+1):
            for d4 in range(1,side+1):
                for d5 in range(1,side+1):
                    for d6 in range(1,side+1):
                        for d7 in range(1,side+1):
                            for d8 in range(1,side+1):
                                for d9 in range(1,side+1):
                                    dice4[d1+d2+d3+d4+d5+d6+d7+d8+d9] += 1

# compute odds of win/draw/lose 4side vs 6side
d4total = sum(dice4)
d6total = sum(dice6)

print "d4total =",d4total
print "d6total =",d6total
print

win  = 0
draw = 0
lose = 0
for d in range(d4count,len(dice4)):
    win  += dice4[d]*sum(dice6[:d])
    draw += dice4[d]*dice6[d]
    lose += dice4[d]*sum(dice6[d+1:])

print "d4 win  =",float(win)/(d4total*d6total)
print "d4 draw =",float(draw)/(d4total*d6total)
print "d4 lose =",float(lose)/(d4total*d6total)
