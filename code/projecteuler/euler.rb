#!/usr/bin/ruby

class Integer
	def factorial
		@n = self.abs
		val = 1
		for i in 1..@n do
			val *= i
		end
		return val
	end
end
