#!/urs/bin/ruby

class Myclass


    @myvar=1
    @@myvar=2

    def getvar
        @myvar
    end

    def self.getvar
        @myvar
    end


end


myclass=Myclass.new
puts myclass.getvar
puts Myclass.getvar
