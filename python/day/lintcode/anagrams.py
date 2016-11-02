class Solution:

    # 思想是：1 采用一个递归来实现，首先获取尝试获取第一个关键字拥有的全部翻转字符串
    #        2  检查当前字符串是否还有字符串，也就是不属于第一个翻转集合的字符串，对这个字符串本身进行
    # @param strs: A list of strings
    # @return: A list of strings
    def anagrams(self,strs):

        return self.getAnagrams(strs)



    def getAnagrams(self,strs):

        new_strs=[]
        hash_box={}
        for s in strs:
            key=self.getKey(s)
            if hash_box.get(key,None) is None:
                hash_box[key]=[s]
            else:
                b=hash_box[key][0]
                if(self.checkIsAnagram(s,b)):
                    tmp=hash_box.get(key)
                    tmp.append(s)
                    hash_box[key]=tmp

        for k in hash_box:
            if len(hash_box[k])>=2:
                for v in hash_box[k]:
                    new_strs.append(v)
        return new_strs





    def checkIsAnagram(self,a,b):

        if a==b:
            return True

        x=[w for w in a]
        y=[w for w in b]
        x.sort()
        y.sort()
        return x==y

    def getKey(self,s):

        x=[w for w in s]
        x.sort()
        return "".join(x)









s=Solution()



print(s.anagrams(["tea","and","ate","eat","den"]))

# print(s.anagrams(["tea","","eat","","tea",""]))
