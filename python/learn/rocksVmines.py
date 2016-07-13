from urllib import request

import sys
#read data from uci data repository
target_url = ("https://archive.ics.uci.edu/ml/machine-learning-"
"databases/undocumented/connectionist-bench/sonar/sonar.all-data")


with request.urlopen(target_url) as f:

    data=str(f.read())
    data=data.split("\\n")



    xList = []
    labels = []
    for line in data:
        row=line.strip().split(',')
        xList.append(row)

    colCounts=[]
    types=[0]*3
    colLen=len(xList[1])

    print(xList)


    for colIndex in range(colLen):
        for row in xList:
            try:
                a=float(row[colIndex])
                if(isinstance(a,float)):
                    types[0]+=1

            except Exception as e:

                if(len(row[colIndex])>0):
                    types[1]+=1
                else:
                    types[2]+=1

            colCounts.append(types)
            types=[0]*3











# sys.stdout.write("Number of Rows of Data = " + str(len(xList)) + '\n')
# sys.stdout.write("Number of Columns of Data = " + str(len(xList[1])))
