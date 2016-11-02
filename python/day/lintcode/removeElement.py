class Solution:

    """
    @param A: A list of integers
    @param elem: An integer
    @return: The new length after remove
    """

    def removeElement(self, A, elem):
        A=[w for w in A if w!=elem]
        print(A)
        return len(A)




s= Solution()
print(s.removeElement([0,4,4,0,4,4,4,0,2],4))
