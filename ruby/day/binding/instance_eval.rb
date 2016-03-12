#!/usr/bin/ruby
class Foo
    def initialize(v)
        $v=v
    end
    def print_v
        puts $v
    end

end


foo=Foo.new 10
foo.print_v #=>10

foo.instance_eval do
    $v=$v+1
end
foo.print_v #=>11
