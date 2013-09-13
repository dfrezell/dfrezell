#!/usr/bin/ruby

fn_1, fn_2 = 1, 1
fn = fn_1 + fn_2
cnt = 2

while fn.to_s.length < 1000 do
	fn = fn_1 + fn_2
	fn_2, fn_1 = fn_1, fn
	cnt += 1
end

print "cnt = ",cnt,", len = ",fn.to_s.length,"\n"
