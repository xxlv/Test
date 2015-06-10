import math 
import string 
#计算v1与v2之间的欧拉距离 
def euclidean(v1,v2): 
    d=0.0
    for i in range(len(v1)): 
        d+=(v1[i]-v2[i])**2 
    return math.sqrt(d) 
#计算vec1与所有数据data的距离，并且排序 
def getdistances(data,vec1):
    #data=getVlist(data)
    #vec1=getVlist(vec1)
    distancelist=[] 
    for i in range(len(data)): 
        vec2=data[i]
        distancelist.append((euclidean(vec1,vec2),data[i][8])) 
    distancelist.sort() 
    return distancelist 
 
# 训练元组表示
vlist1=["1.0 1.1 1.2 2.1 0.3 2.3 1.4 0.5 1",
"1.7 1.2 1.4 2.0 0.2 2.5 1.2 0.8 1",
"1.2 1.8 1.6 2.5 0.1 2.2 1.8 0.2 1",
"1.9 2.1 6.2 1.1 0.9 3.3 2.4 5.5 0",
"1.0 0.8 1.6 2.1 0.2 2.3 1.6 0.5 1",
"1.6 2.1 5.2 1.1 0.8 3.6 2.4 4.5 0"]
 
#测试元组表示
vlist2=["1.0 1.1 1.2 2.1 0.3 2.3 1.4 0.5",
"1.7 1.2 1.4 2.0 0.2 2.5 1.2 0.8",
"1.2 1.8 1.6 2.5 0.1 2.2 1.8 0.2",
"1.9 2.1 6.2 1.1 0.9 3.3 2.4 5.5",
"1.0 0.8 1.6 2.1 0.2 2.3 1.6 0.5",
"1.6 2.1 5.2 1.1 0.8 3.6 2.4 4.5"]
#训练元组和测试元组数据处理方法，将字符查转化为float
def getfloat(varr):
    varrlist=[]
    for varrelem in varr:
       # print float(varrelem)       
        varrlist.append(float(varrelem))
    return varrlist     
def getVlist(vlist):
    v_list=[]
    for v in vlist:
        varr=v.split(" ")
        v_list.append(getfloat(varr))
    return v_list
 
vlist1=getVlist(vlist1)  ＃  将训练元组处理成float 型的数组
vlist2=getVlist(vlist2)
#获得类别
def getClasses(distance,k):
    classes={}
    for i in range(k):
        dis=distance[i]
        if classes.__contains__(dis[1]):
            dnum=classes.get(dis[1])+1
            del classes[dis[1]]
            classes.setdefault(dis[1],int(dnum))
        else:
            classes.setdefault(dis[1],1)
    dicnum=0
    classnum=-1    #以下代码为求出数目最大的类别
    for dic in classes:
        #print dic www.2cto.com
        if classes.get(dic)>=dicnum:
           dicnum=classes.get(dic)
           classnum=dic   
    return int(classnum)
  
def knn(vlist1,vlist2):
    for v2 in vlist2:
        distancelist=getdistances(vlist1,v2)
        print "测试元组"
        print v2
        print '所属类别'       
        print getClasses(distancelist,3)
               
knn(vlist1,vlist2)