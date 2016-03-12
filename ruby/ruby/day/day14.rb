#!/usr/bin/ruby

require 'awesome_print'
module Before end
module After end

class Example
    prepend Before
    include After
end

#puts Example.ancestors #=> Before Example After Object Kernel BasicObject
# ap Example.ancestors
ap [].methods
