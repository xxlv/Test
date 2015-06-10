# -*- coding:UTF-8 -*-
import urllib.request
import urllib
import re
import sys
# data={}
# data['word']='Hello word'
# url_values=urllib.parse.urlencode(data)
#=================================================================================================================================================

def UTF2Hex(s):
	t=s.encode("UTF-8").encode("hex")
	line=""
	for i in range(0,len(t)-1,2):
		line+="\\x"+t[i]+t[i+1]
	return line

def GBK2Hex(s):
	temp=s.encode("GBK").encode("hex")
	line=""
	for i in range(0,len(temp)-1,2):
		line += "\\x" + temp[i] + temp[i+1]
	return line
#=================================================================================================================================================

url="http://www.58.com/jianzhi/"
full_url=url
data = urllib.request.urlopen(full_url).read()
# type = sys.getfilesystemencoding()
# print(data.decode(type))
# data=str(data)
# # data='<a href="http://ts.58.com/canyinfuwu/21591959057700x.shtml" target="_blank" class="t">Hello我</a>'
# pattern="我"
# # match1=pattern.match('hello12 word hello1')
# s='<a href="http://ts.58.com/canyinfuwu/21591959057700x.shtml" target="_blank" class="t">.*?(...){1,}</a>'
# match1=re.findall(pattern.encode('utf-8'),data.encode('utf-8'),re.I)

def getX(y):
		if y<1000:
			return 200
		x=200
		while(True):
			x+=1
			if(x*5>y):
				return (x//4)*4 

print(getX(1500))

