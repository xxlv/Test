http=require 'http'



opts=
    hostname: 'http://www.jianshu.com'
    port: 80
    path: '/p/c87c73d63ddc',
    method: 'GET',
    headers:
        'Content-Type': 'application/x-www-form-urlencoded'
        'Cookie': 'read_mode=day; default_font=font2; __utmt=1; signin_redirect=http%3A%2F%2Fwww.jianshu.com%2Fp%2Fc87c73d63ddc; _session_id=aURtY2lKZUdyTFpBWk91ZHd6NGhFeTdCbFdJcEtjU3kvSkUxK0FaQ1l2YmV0SVdCQ2ZuV1dEWnhGSXduU0llMjNMa2lDMWJtSUQ5Yko3WU1PK0Y4YnJTek5xanVLb05FZm5xRndhUzlwSUU4Y2JUVmNIdHJOWTFSdjJSMjhDMGtQYllrdXlwUWVQWFlMTFZzaDJVMkpHdnVBcFVXanNSaU1STm9EcmhMbjYxb0EzcWdyR05ZMTlQRTdvaUNyb0F6V2pMUThVbmZqTXNxWWxSZzl3NGcvY1R4TnkyRnMxOGdKa3RuRThNalpWMVVXSzNXbHgxZnRPQVk5dE5RTnZXenBWbVcyd292SmV0QTVsanRuSGtkamRwWHNYWXpaN3Q2VEZ0QlZsV3Q5dW1WbzdBaDdaM09ESVFGbmtnL3doUzg1MTd0MGphVGU0WEJhbXBSMVVhY0VzaCtxTFp0dkRnSnNGVjJIeXh6ODAwNzNEcmppWUtIVXg4RDBRazlEUTloVVR0QmJrOFcvd0FBMnNVdWNBN2pGdz09LS1aZU1xZnZHRDZUajZzMDd0M3RuUCt3PT0%3D--ca1d41630f6cf4e6fafc7ee002b8f7a4645b4862; Hm_lvt_0c0e9d9b1e7d617b3e6842e85b9fb068=1464966396,1464968198,1464968202,1464968348; Hm_lpvt_0c0e9d9b1e7d617b3e6842e85b9fb068=1464969554; __utma=194070582.373814849.1464096449.1464928226.1464964633.15; __utmb=194070582.176.10.1464964633; __utmc=194070582; __utmz=194070582.1464964633.15.8.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=194070582.|2=User%20Type=Visitor=1'
        'Referer': 'http://www.jianshu.com/collection/xYuZYD'
        'Upgrade-Insecure-Requests':1
        'If-None-Match':'W/"251c29cf610cc882f2579c81c37e8066"'

i=0


setTimeout ()->
    http.get opts ,(res)->
        console.log i++
,1000

setTimeout ()->
    http.get opts ,(res)->
        console.log i++
,1000
