class Solution:
    # @param A, B: Two string.
    # @return: the length of the longest common substring.
    def longestCommonSubstring(self, A, B):
            return self.find(A,B)

    def find(self,A,B):

        len_of_a=len(A)
        len_of_b=len(B)
        diff_a_b=len_of_a-len_of_b
        if len_of_a > len_of_b:
            X=A
            Y=B
        else:
            X=B
            Y=A
        min_len=len(Y)
        lens=[]

        for index in range(0,min_len):
            while(min_len>0):
                s=Y[index:min_len]
                min_len=min_len-1
                # print("[%d , %d] "%(index,min_len))
                if s in X:
                    lens.append(len(s))
            min_len=len(Y)
        if (len(lens)==0):
             return 0
        return max(lens)

s=Solution()
# print(s.longestCommonSubstring("adfanadsnf;andvadsjfafjdlsfnaldjfi*odiasjfasdlnfasldgao;inadfjnals;dfjasdl;jfa;dsjfa;sdnfsd;afhwery894yra7f78asfas8fy43rhaisuey34hiuy^%(9afysdfhaksdhfsdkjfhsdhfakldhfsdkf*h", "dafdnf**"))
print(s.longestCommonSubstring("", ""))
