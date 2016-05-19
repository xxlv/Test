#!/usr/bin/ruby
require 'net'
require 'net/http'


uri = URI('http://202.201.0.239/academic/j_acegi_security_check')

req = Net::HTTP::Post.new(uri)

req.set_form_data('groupId' => '', 'j_username' => '2005-03-31','j_password'=>'1')

res = Net::HTTP.start(uri.hostname, uri.port) do |http|
  http.request(req)
end

case res
when Net::HTTPSuccess, Net::HTTPRedirection
  puts res
else
  res.value
end
