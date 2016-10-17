class Solution:

    def compareStrings(self,index,A, B):

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
                index=index+1

            return self.compareStrings(index,A,B)

    def strStr(self,source, target):

        if source is None or target is None:
            return -1

        A=[w for w in source]
        B=[w for w in target]

        if (len(B)==0 and len(A)>0):
            return 0

        if len(B)>len(A):
            return -1

        if len(B)==len(A):
            if A==B:
                return 0
            else:
                return -1

        if B[0] in A:
            if len(B)==1:
                return A.index(B[0])
            return self.compareStrings(A.index(B[0]),A,B)
        else:
            return -1


s=Solution()

assert s.strStr(None,"a")==-1
assert s.strStr("a","a")==0
assert s.strStr("ba","a")==1
assert s.strStr("target","target")==0
