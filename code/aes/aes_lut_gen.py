#!/usr/bin/env python

def xtime(n):
    x = n << 1
    if x > 0xff:
        x ^= 0x0000001b
        x &= 0x00ff
    return x

xtime_02_LUT = [xtime(x) for x in range(0,256)]
xtime_03_LUT = [xtime(x) ^ x for x in range(0,256)]
xtime_09_LUT = [xtime(xtime(xtime(x))) ^ x for x in range(0,256)]
xtime_0b_LUT = [xtime(xtime(xtime(x))) ^ xtime(x) ^ x for x in range(0,256)]
xtime_0d_LUT = [xtime(xtime(xtime(x))) ^ xtime(xtime(x)) ^ x for x in range(0,256)]
xtime_0e_LUT = [xtime(xtime(xtime(x))) ^ xtime(xtime(x)) ^ xtime(x) for x in range(0,256)]


print "unsigned char xtime_02_LUT[] = {"
for i,x in enumerate(xtime_02_LUT):
    if i % 16 == 0:
        print "   ",
    print "0x%2.2x," % (x,),
    if i % 16 == 15:
        print ""
print "};"
print "unsigned char xtime_03_LUT[] = {"
for i,x in enumerate(xtime_03_LUT):
    if i % 16 == 0:
        print "   ",
    print "0x%2.2x," % (x,),
    if i % 16 == 15:
        print ""
print "};"
print "unsigned char xtime_09_LUT[] = {"
for i,x in enumerate(xtime_09_LUT):
    if i % 16 == 0:
        print "   ",
    print "0x%2.2x," % (x,),
    if i % 16 == 15:
        print ""
print "};"
print "unsigned char xtime_0b_LUT[] = {"
for i,x in enumerate(xtime_0b_LUT):
    if i % 16 == 0:
        print "   ",
    print "0x%2.2x," % (x,),
    if i % 16 == 15:
        print ""
print "};"
print "unsigned char xtime_0d_LUT[] = {"
for i,x in enumerate(xtime_0d_LUT):
    if i % 16 == 0:
        print "   ",
    print "0x%2.2x," % (x,),
    if i % 16 == 15:
        print ""
print "};"
print "unsigned char xtime_0e_LUT[] = {"
for i,x in enumerate(xtime_0e_LUT):
    if i % 16 == 0:
        print "   ",
    print "0x%2.2x," % (x,),
    if i % 16 == 15:
        print ""
print "};"
