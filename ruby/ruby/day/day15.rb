#!/usr/bin/ruby

# s=%Q(""hello ,world "")  #=> ""hello ,world ""
# s=%Q+""hello ,world ""+  #=> ""hello ,world ""
# s=%!""hello ,world ""!   #=>  ""hello ,world ""
# s=%Q!'''hello ,world'''! #=> '''hello ,world'''
# s=%!'''hello ,world'''! #=> '''hello ,world'''
# s=%q{""hello ,world ""}
# s=%w(\ ab c )   #=>  ""hello ,world ""
require 'awesome_print'
require 'ghee'

# gh=Ghee.basic_auth('1252804799@qq.com','Aa1239359')
# all_gists=gh.users('nusco').gists
# ap all_gists
class Roulette
    def method_missing(name,*args)

        number=0
        person=name.to_s.capitalize
        ap person    
        3.times do
            number=rand(10)+1
            puts "#{number}"
        end
        "#{person} got a #{number}"
    end
end
r=Roulette.new
r.hello
