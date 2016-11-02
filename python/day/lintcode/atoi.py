import math
import sys

class Solution:
    # @param str: a string
    # @return an integer

    def atoi(self, strs):
        maps={
        "0":0,
        "1":1,
        "2":2,
        "3":3,
        "4":4,
        "5":5,
        "6":6,
        "7":7,
        "8":8,
        "9":9,
        }
        chp_map=["-","+"]
        is_numberic=self.checkIsNumberic(strs)

        if not is_numberic:
            return 0

        chp=False
        if strs[0] in chp_map:
            chp=strs[0]
            strs=strs[1:]

        length=len(strs)
        if length<=1 :
            integer=maps[strs[0]]
        else:
            integer=0
            left=0
            right=0

            d=0 #方向
            for w in strs:
                if(w=='.'):
                    d=1
                    index=strs.index(w)
                    continue

                if (d==0):
                    offset=maps[w]*int((math.pow(10,length-1)))
                    left=left+offset
                else:
                    offset=maps[w]*int((math.pow(10,(length-1))))
                    right=right+offset
                length=length-1

        integer=right+left*(math.pow(0.1,len(strs)-index))



        if chp != False:
            if chp=="+":
                return integer
            elif chp=="-":
                return -integer
            else:
                return 0

        if integer>2147483647:
            return 2147483647

        if integer<-2147483648:
            return -2147483648

        return integer

    def checkIsNumberic(self,strs):

        digt_map=["0","1","2","3","4","5","6","7","8","9"]
        if(len(strs)==0):
             return False

        if(len(strs)==1):
            return strs[0] in digt_map

        if(len(strs)>=2):
            if ((strs[0] not in ["-","+"]) and (strs[0] not in digt_map)):
                return False
            else:
                if len(strs)==2:
                    return strs[1] in digt_map
        dot_count=0

        for w in strs:
            if (w not in digt_map):
                if w ==".":
                    dot_count=dot_count+1
                else:
                    return False

        if(dot_count>1):
            return False


        return True


s=Solution()
print(s.atoi("1.022"))
