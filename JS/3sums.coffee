threeSum=(nums)->
    
    return [] if nums.length < 3
    returns=[]
    map=[]

    for item1,key1 in nums
        for item2 ,key2 in nums
            for item3,key3 in nums
                if key1!=key2 and key1!=key3 and key2!=key3
                    if item1+item2+item3==0
                        target=[item1,item2,item3].sort (a,b)->
                            a-b
                        itemUnq=target.join('')
                        if itemUnq not in map
                            index=[key1,key2,key3].sort().join('')
                            map.push itemUnq
                            returns.push target


    returns=returns.sort (a,b)->
        return b[0]-a[0] if a[0]!=b[0]
        return b[1]-a[1] if a[1]!=b[1]
        return b[2]-a[2] if a[2]!=b[2]
    returns

# r=threeSum [-9,14,-7,-8,9,1,-10,-8,13,12,6,9,3,-3,-15,-15,1,8,-7,-4,-6,8,2,-10,8,11,-15,3,0,-11,-1,-1,10,0,6,5,-14,3,12,-15,-7,-5,9,11,-1,1,3,-15,-5,11,-12,-4,-4,-2,-6,-10,-6,-6,0,2,-9,14,-14,-14,-9,-1,-2,-7,-12,-13,-15,-4,-3,1,14,3,-12,3,3,-10,-9,-1,-7,3,12,-6,0,13,4,-15,0,2,6,1,3,13,8,-13,13,11,11,13,14,-6]

# for res in r
#     console.log res
