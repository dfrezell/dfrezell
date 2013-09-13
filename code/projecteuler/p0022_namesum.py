#!/usr/bin/python

f = open("names.txt", "r")
line = f.readline()
names = line.split(",")
names.sort()

pos = 1
sum = 0
for name in names:
	i = 0
	for c in name[1:-1]:
		i += (ord(c) - ord('A') + 1)
	#print "%s : %d : %d" % (name, i, (i * pos))
	sum += (i * pos)
	pos += 1

print sum
