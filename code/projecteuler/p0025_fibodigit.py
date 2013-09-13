#!/usr/bin/python

fn_1, fn_2 = 1, 1
fn = fn_1 + fn_2
cnt = 2

while len(str(fn)) < 1000:
	fn = fn_1 + fn_2
	fn_2, fn_1 = fn_1, fn
	cnt += 1

print "cnt =",cnt,",len =",len(str(fn))
