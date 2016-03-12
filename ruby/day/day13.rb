#!/usr/bin/ruby
# %w[rspec-core rspec-expectations rspec-mocks rspec-support].each do |lib|
#     gem lib,:git=>"git://github.com/rspec/#{lib}.git",:branch=>'master'
#     puts "Gem install #{lib}"
# end

# str="ABDBAS"
# new_str=str.gsub(/A/,"*")
# puts new_str
def f1
    yield  if block_given?
end

def f2(&p)
    puts p.class
    puts p.inspect
    p.call if block_given?
end

def f3(p)
    p.call
end

# ["1","2","3"].map(&:to_i)
# class Test
#
#     def initialize(num=0)
#         @num=num
#     end
#
#     def to_proc
#         proc.new {|obj| obj.send('+',@num)}
#     end
#     def o(n,m)
#         puts n,m
#     end
#
# end
# test=Test.new(1)
# test.send("o","2","1")
# f1 { puts "f1" }
# f2 { puts "f2" }
# f3 (proc{ puts "f3" })
# f3 (Proc.new{ puts "f3" })
# f3 (lambda{ puts "f3" })
# f3 (-> { puts "f3" })
# f2  { puts "f2" }
# class Test
#     def initialize(num)
#         @num=num
#     end
#     def to_proc
#         Proc.new {|obj| obj.send('+',@num)}
#     end
#
# end
#
# add_by_9 =Test.new(9)
# puts [1,2,3].map(&add_by_9)

lambda {|x| puts x}
