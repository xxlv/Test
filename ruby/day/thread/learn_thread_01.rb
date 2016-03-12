#!/usr/bin/ruby
require "Thread"

threads=[]
threads << Thread.new { 311.times do puts "world " end }
threads << Thread.new { 31.times do puts "Hello " end }
threads.each do |thr|
    thr.join
end
