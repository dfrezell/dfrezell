#!/usr/bin/python

import sys
import math

def main(*argv):
	digit = ""
	pos = 1000000 - 1
	x = [r for r in range(0, 10)]
	total_perm = len(x) * math.factorial(x[-1])
	ub = total_perm
	lb = 0
	perm_per_seg = total_perm

	while (len(x) != 0):
		perm_per_seg /= len(x)
		segment = (pos - lb) / perm_per_seg
		digit += str(x[segment])
		x = x[:segment] + x[segment+1:]
		lb += segment * perm_per_seg
		ub = lb + perm_per_seg
		print lb, ub
	
	print digit


if __name__ == "__main__":
	sys.exit(main(*sys.argv))

