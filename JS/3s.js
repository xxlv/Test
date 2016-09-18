var threeSum = function(nums) {

    if (nums.length < 3) { return [];}

    var returns=[];
    var map=[];
    var unqKey,target;
    len=nums.length

    nums=nums.sort(function(a,b){
        return a-b;
    });
    
    console.log(nums);

    for(var i=0;i<len;++i){

        for(var j=i+1;j<len;++j){

            for(var k=j+1;k<len;++k){

                if(i!==j && j!==k && i!==k){

                        target=[nums[i],nums[j],nums[k]];
                        if ((target[0]+target[1]+target[2])===0){
                        target.sort(function(a,b){
                            return a-b;
                        });
                        unqKey=target.join('')
                        if((map.indexOf(unqKey))<0){
                            returns.push(target);
                            map.push(unqKey);
                        }
                    }
                }
            }
        }
    }
    returns = returns.sort(function(a, b) {
      if (a[0] !== b[0]) {
        return a[0] - b[0];
      }
      if (a[1] !== b[1]) {
        return a[1] - b[1];
      }
      if (a[2] !== b[2]) {
        return a[2] - b[2];
      }
    });

    return returns;
}


var r=threeSum([13,-4,1,3,-1,-1,5,-11,13,9,4,7,5,-5,-13,-4,8,-3,14,-4,14,6,7,11,4,-6,-5,-9,14,3,-9,12,-15,0,-8,-9,14,4,-5,4,-1,-15,-12,-11,-13,-9,1,3,-5,0,14,-6,13,-1,12,2,8,-7,9,0,11,7,-11,3,-8,-11,1,13,8,4,-5,14,4,-2,11,-2,-4,-3,-14,6,4,8,7,3,-8,5,12,7,5,-2,-8,-7,13,-11,12,12,-7,-10,11,-14]);
