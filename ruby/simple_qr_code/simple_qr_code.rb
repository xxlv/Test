#!/usr/bin/ruby

class SimpleQRCode

    def initialize

        @map=Hash.new
        @map["0"]=0
        @map["1"]=1
        @map["2"]=2
        @map["3"]=3
        @map["4"]=4
        @map["5"]=5
        @map["6"]=6
        @map["7"]=7
        @map["8"]=8
        @map["9"]=9
        @map["A"]=10
        @map["B"]=11
        @map["C"]=12
        @map["D"]=13
        @map["E"]=14
        @map["F"]=15
        @map["G"]=16
        @map["H"]=17
        @map["I"]=18
        @map["J"]=19
        @map["K"]=20
        @map["L"]=21
        @map["M"]=22
        @map["N"]=23
        @map["O"]=24
        @map["P"]=25
        @map["Q"]=26
        @map["R"]=27
        @map["S"]=28
        @map["T"]=29
        @map["U"]=30
        @map["V"]=31
        @map["W"]=32
        @map["X"]=33
        @map["Y"]=34
        @map["Z"]=35
        @map["SP"]=36
        @map["$"]=37
        @map["%"]=38
        @map["*"]=39
        @map["+"]=40
        @map["-"]=41
        @map["."]=42
        @map["/"]=43
        @map[":"]=44

        @map.each do|k,v|
            v=v.to_s(2)
            @map[k]='0'*(8-v.size)+v if v.size < 8
        end
    end

    def info_to_bytes(info)
        bytes_info=[]
        info.each_char do |word|
            word.upcase!
            word='SP' if word==' '

            #fill 8 bits
            (8-@map[word].size).times do
                @map[word]="0#{@map[word]}"
            end
            bytes_info.push @map[word]
        end
        bytes_info.join ""
    end

    def bytes_to_info(bytes)
        byte_info_array =[]
        info=[]
        while bytes.size >8
            byte_info_array.push bytes[0,8]
            bytes=bytes[8,bytes.size-1]
        end

        byte_info_array.push bytes[0,8]
        byte_info_array.each do |b|
            info.push @map.key(b)
        end

        info.join('').gsub! /SP/,' '
    end

end

simple_qr=SimpleQRCode.new
info="i love you"
puts "INFO>> #{info}"
puts "BYTES>> #{simple_qr.info_to_bytes info}"
puts "RESTORE INFO>> #{simple_qr.bytes_to_info simple_qr.info_to_bytes info}"
