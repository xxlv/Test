class Solution:

    def compareStrings(self, A, B):

        A=[w for w in A]
        B=[w for w in B]

        count=0
        for b in B:
            if b in A:
                count=count+1
                A.remove(b)

        return count==len(B)

    #
    #
    # def diff(self,a, b):
    #     return [aa for aa in a if aa not in b]

s=Solution()
print(s.compareStrings("ABCDEFG","ACC"))
