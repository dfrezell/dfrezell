#!/usr/bin/python

digits = ['1','2','3','4','5','6','7','8','9']

def pandigit(x):
	pdigit = True
	for c in digits: pdigit &= c in x
	return pdigit

fn_1, fn_2 = 1, 1
fn = fn_1 + fn_2
cnt = 2

while cnt < 1000000:
	x = True
	fn = fn_1 + fn_2
	fn_2, fn_1 = fn_1, fn
	cnt += 1

	bot = str(fn % 1000000000)
	if pandigit(bot):
		top = str(fn)[:9]
		if pandigit(top):
			print cnt
			break
