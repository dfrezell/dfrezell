#!/usr/bin/ruby

require "euler"

nfact = (100.factorial).to_s
sum = 0

nfact.each_byte {
	|c|
	sum += c.to_i - 0x30
}

print "100! digit sum = ",sum,"\n"
