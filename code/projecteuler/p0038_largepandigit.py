#!/usr/bin/python

import euler

pan = ""
maxpan = 0

for i in range(2, 10):
	for j in range(1, 10000):
		pan = ""
		for k in range(1, i+1):
			pan += str(k*j)
			if len(pan) == 9 and euler.pandigital(pan[:9]):
				maxpan = max(maxpan, int(pan))

print maxpan
