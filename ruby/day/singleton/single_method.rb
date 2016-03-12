#!/usr/bin/ruby
require 'singleton'
require 'awesome_print'
class Myclass

    include Singleton

    def report
        @x=1
        @x
    end
end



ap Myclass.instance.methods
ap Myclass.instance.report
