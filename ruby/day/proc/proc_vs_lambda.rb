#!/usr/bin/ruby

require 'ap'

def double(callable_obj)
    callable_obj.call * 2
end
l = lambda { return 10 }

p=Proc.new { return 1 }

ap double(p)
