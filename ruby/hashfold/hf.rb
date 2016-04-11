#!/usr/bin/ruby
class HashFold
    def start(inputs)
        hash={}
        inputs.each do |input|

            self.map(input) do |k,v|
                if hash.key?(k)
                    hash[k]=self.fold(hash[k],v)
                else
                    hash[k]=v
                end
            end
        end
        hash
    end
end


class WordCount < HashFold

    def map(document)
        open(document) do |f|
            for line in f
                puts line
                line.gsub!(/[!#"$%\'()*+,-.\/;<=>?@\[\\\]^_~`{\|}]/," ")
                for word in line.split
                    word.downcase!
                    yield word.strip,1
                end
            end
        end
    end

    def fold(c1,c2)
        return c1+c2
    end

end



WordCount.new.start(ARGV).sort_by{|x|x[1]}.reverse.each do |k,v|
    print k,":",v,"\n"
end
