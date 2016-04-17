#!/usr/bin/ruby

def i(*args)
    1
end
def told_to(*args)
    puts "say something to #{args[0]}"
    yield self if block_given?
end

case i :was,:young
when 1
    i told_to :my_mom do
        can i.be.rich?
    end
end
