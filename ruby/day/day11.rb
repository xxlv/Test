#!/usr/bin/ruby
class Greeting
    def initialize(text)
        @text=text
    end

    def wellcome
        @text
    end


end


my_obj=Greeting.new("Hello")

puts my_obj.class
puts my_obj.class.instance_methods()
