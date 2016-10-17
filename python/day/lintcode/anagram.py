class Solution:

    def anagram(self, s, t):
        s=[w for w in s]
        t=[w for w in t]
        s.sort()
        t.sort()
        return s==t

s=Solution()
s.anagram("ab","bc")
