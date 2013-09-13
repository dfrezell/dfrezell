#!/usr/bin/python

digit = ""
i = 0

while len(digit) < 1000001:
	digit += str(i)
	i +=1

d1 = int(digit[1])
d10 = int(digit[10])
d100 = int(digit[100])
d1000 = int(digit[1000])
d10000 = int(digit[10000])
d100000 = int(digit[100000])
d1000000 = int(digit[1000000])

print "d1 x d10 x d100 x d1000 x d10000 x d100000 x d1000000 =",d1*d10*d100*d1000*d10000*d100000*d1000000
