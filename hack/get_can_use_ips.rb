#!/usr/bin/ruby
require 'net/ping'

##扫描局域网内可以使用的ip,即ping失败的ip
can_use_ips=[]

def up?(host)
    check = Net::Ping::External.new(host)
    check.ping?
end

206.upto(255) do |i|
    200.upto(255) do |j|
        ip="192.168.#{i}.#{j}"
        unless up?(ip)
            puts "Found IP: #{ip}"
            can_use_ips << ip
        end
    end
end
