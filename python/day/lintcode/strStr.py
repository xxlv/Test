class Solution:

    def findIndex(self,index,A, B):
        print(index)
        if(index+len(B)>len(A)):
            return -1

        if(A[index:index+len(B)]==B):
            return index
        else:
            _A=A[index+1:]
            if index>=len(A)-1:
                return -1

            if B[0] in _A:
                index=_A.index(B[0])+index+1
            else:
                return -1

            return self.findIndex(index,A,B)


    def strStr(self,source, target):

        # 边界判断
        if source is None or target is None:
            return -1

        if (len(target)==0 and len(source)>0):
            return 0

        if len(target)>len(source):
            return -1

        if len(target)==len(source):
            if source==target:
                return 0
            else:
                return -1


        if target[0] in source:
            if len(target)==1:
                return source.index(target[0])

            return self.findIndex(source.index(target[0]),source,target)
        else:
            return -1

s=Solution()
print(s.strStr("abcab2cabda2bbaabca1bd2abc1abd2abcabd2abcabd","abcabd"))

assert s.strStr(None,"a")==-1
assert s.strStr("a","a")==0
assert s.strStr("ba","a")==1
assert s.strStr("target","target")==0
