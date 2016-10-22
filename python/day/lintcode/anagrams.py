class Solution:
    # @param strs: A list of strings
    # @return: A list of strings
    def anagrams(self,strs):
        return self.getAnagrams(strs)


    def getAnagrams(self,strs):
        new_strs=[]

        if strs is None or len(strs) < 1:
            return []

        if len(strs)>0:
            anagram_s=strs[0]
            strs.remove(anagram_s)
        else:
            return []


        for s in strs:
            if self.checkIsAnagram(s,anagram_s):
                new_strs.append(s)
                new_strs.append(anagram_s)
                strs.remove(s)
                

        for s3 in strs:
            if (len(new_strs)>0):
                if self.checkIsAnagram(new_strs[0],s3):
                    new_strs.append(s3)
                    strs.remove(s3)

        return new_strs


    def checkIsAnagram(self,a,b):

        a=[w for w in a]
        b=[w for w in b]
        a.sort()
        b.sort()

        return a==b



s=Solution()

print(s.anagrams(["tea","","eat","","tea",""]))
