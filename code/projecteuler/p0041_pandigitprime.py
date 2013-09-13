#!/usr/bin/python

import euler

def permute(seq):
	if not seq:
		return [seq]
	else:
		temp = []
		for k in range(len(seq)):
			part = seq[:k] + seq[k+1:]
			for m in permute(part):
				temp.append(seq[k:k+1] + m)
		return temp

"""
there is a cute little formula about divisibilty:
	A number is divisible by 3 if the sum of the digits is divisible by 3.
So, with pandigital numbers:
	sum(1:2) = 3
	sum(1:3) = 6
	sum(1:4) = 10
	sum(1:5) = 15
	sum(1:6) = 21
	sum(1:7) = 28
	sum(1:8) = 36
	sum(1:9) = 45
from this, a pandigital prime has to be 7 digits or 4 digits, we were given
the larget 4 digit pandigital prime, so we just need to check the 7 digit
numbers
"""
for i in permute('7654321'):
	if euler.isprime(int(i)):
		print i
		break
