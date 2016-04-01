#!/usr/bin/ruby
class String

    alias_method :real_length ,:length

    def length
        puts caller[0]
        real_length>5?'long':'short'
    end


end


class Fixnum
    alias_method :old_plus, :+
    def +(v)
        old_plus(v).old_plus(1)
    end
end

puts 1+1
