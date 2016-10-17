
class Solution:

    def maxNumber(self, nums1, nums2, k):
        
        a=len(nums1)

        b=len(nums2)

        diff=(a+b)-k
        index_of_1=nums1.index(k)
        index_of_2=nums2.index(k)
        nums=nums1+nums2
        nums.sort(reverse=True)
        max_nums=nums[0:k]
        index_of_k_in_max_nums=max_nums.index(k)
        
        if(index_of_k_in_max_nums is None):
            return max_nums
        else:
            if(index_of_k_in_max_nums==nums1.index(k) and index_of_k_in_max_nums==nums2.index(k)):
                return max_nums
            else:

                print(k)
                print(nums1)
                print(nums2)
                print(max_nums)
                
                return []
            


#####

s=Solution()

lists=s.maxNumber([3, 4, 6, 5],[9, 1, 2, 5, 8, 3],5)

print(lists)
