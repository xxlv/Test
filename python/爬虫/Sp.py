
import http.cookiejar,os, urllib.request

#====================================SAVE=======================================
# filename='cookie.txt'#保存cookie的文件名
#创建cookie并保存到本地cookie.txt中
# cookie=http.cookiejar.MozillaCookieJar(filename)
# opener=urllib.request.build_opener(urllib.request.HTTPCookieProcessor(cookie)
# res=opener.open('http://www2.lzcu.edu.cn/jwmis/_data/index_login.aspx')
# cookie.save(ignore_discard=True,ignore_expires=True)



#保存

#===================================LOAD=========================================
filename='cookie.txt'
cookie=http.cookiejar.MozillaCookieJar(filename)#读取cookie

# cookie.load('cookie.txt',ignore_discard=True,ignore_expires=True)

opener=urllib.request.build_opener(urllib.request.HTTPCookieProcessor(cookie))#获取opener
postdata=urllib.parse.urlencode({
		'UserID':'15209392001',
		'PassWord':'20110803050225'
	}).encode(encoding='UTF-8')


res=opener.open('http://www2.lzcu.edu.cn/jwmis/_data/index_login.aspx',postdata)

cookie.save(ignore_discard=True,ignore_expires=True)#保存cookie


gradeUrl = 'http://219.246.16.57/jwweb/XSXJ/KingosLove.aspx'
result = opener.open(gradeUrl)
htmlbytes=result.read()
hd=open('text.html','w')
hd.write(htmlbytes.decode('gbk'))

#===================================TEST=========================================


# cookie=http.cookiejar.MozillaCookieJar()
# cookie.load('cookie.txt',ignore_discard=True,ignore_expires=True)
# postdata=urllib.parse.urlencode({
# 		'UserID':'15209392006',
# 		'PassWord':'20110803050225'
# 	})

# opener=urllib.request.build_opener(urllib.request.HTTPCookieProcessor(cookie))
# res=opener.open('http://www2.lzcu.edu.cn/jwmis/_data/index_login.aspx')


# gradeUrl = 'http://219.246.16.57/jwweb/MAINFRM.aspx'
# result = opener.open(gradeUrl)
# htmlbytes=result.read()
# hd=open('text.html','w')
# hd.write(htmlbytes.decode('gbk'))
# print(result.read())