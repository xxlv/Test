#!/usr/bin/ruby
# require 'ap'

#让她变颜色
var="hell1o"
30.upto(40) do |i|
    color=i
    puts "\e[#{color}m#{var}\e[0m"
end
