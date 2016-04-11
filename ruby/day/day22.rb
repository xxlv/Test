#!/usr/bin/ruby
def test_proc_return
    p=Proc.new do
         return 10
    end
    r=p.call
    r=110+r
    return r
end

def test_lamada_return
    l=lambda { return 10 }
    r=l.call
    return 10+r
end



puts test_lamada_return #20
puts test_proc_return  #10
