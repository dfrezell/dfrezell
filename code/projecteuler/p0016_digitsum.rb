#!/usr/bin/ruby
# how amazing is this?  this type of code in C would have been much, much
# bigger...the looping in ruby is a bit odd, but it solves the problem
# quickly
twopow = (2**1000).to_s
sum = 0

twopow.each_byte {
	|c|
	sum += c.to_i - 0x30
}

print "2**1000 digit sum = ",sum,"\n"
