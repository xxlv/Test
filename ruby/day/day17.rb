#!/usr/bin/ruby

def setup(&block)
    @setups << block
end

def event(description,&block)
    @events << {:description =>description,:condition=>block}
end

@setups=[]
@events=[]

@events.each do |e|
    @setups.each do |s|
        setup.call
    end

end
