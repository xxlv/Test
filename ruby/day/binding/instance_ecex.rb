#!/usr/bin/ruby
require 'ap'
class C
    def initialize
        @x=1
    end
end

class D
    def initialize
        @y=2
    end
    def test_method
        C.new.instance_eval { puts "@x #{@x} , @y #{@y} "} #=>x 1 ,y nil
        C.new.instance_exec(@y) { |y| puts "@x #{@x} , @y #{y} "} #=>@x 1 , @y 2
    end

end

d=D.new
d.test_method

ap BasicObject.methods
