class Solution:
    # @param strs: A list of strings
    # @return: The longest common prefix
    def longestCommonPrefix(self, strs):

        if len(strs)==0:
            return ""

        min_of_len=len(strs[0])
        min_of_s=strs[0]
        comman_s=[]
        max_s=""
        for s in strs:
            if len(s)<min_of_len:
                min_of_len=len(s)
                min_of_s=s
                
        for index in range(0,min_of_len):
            l=min_of_len

            while(True):
                found=0
                if (l <=0):
                    break
                # print("%d %d "%(index,l))
                subs=min_of_s[index:l]

                for ss in strs:
                    if subs is '':
                        break
                    if subs not in ss:
                        break
                    else:
                        found=found+1
                if found==len(strs):
                    comman_s.append(subs)

                l=l-1

            if len(comman_s)==0:
                return ""

            max_s=comman_s[0]
            for sub_s in comman_s:
                if (len(sub_s)>len(max_s)):
                    max_s=sub_s
        return max_s




















s=Solution()
print(s.longestCommonPrefix(["ABCD","ABCEF","ABCEF"]))
print(s.longestCommonPrefix(["abc","abcd","","ab","ac"]))
