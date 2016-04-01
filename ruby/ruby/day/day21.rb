#!/usr/bin/ruby
class String

    alias :real_length ,:length

    def length
        real_length>5?'long':'short'
    end

end


s.length
