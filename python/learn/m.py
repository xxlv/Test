__author__ = 'mike_bowles'

from urllib import request

import sys
#read data from uci data repository
target_url = ("https://archive.ics.uci.edu/ml/machine-learning-"
"databases/undocumented/connectionist-bench/sonar/sonar.all-data")
with request.urlopen(target_url) as f:
    data=str(f.read())
    xList = []
    labels = []
    data=data.split('\\n')

    for line in data:
        row=line.strip().split(',')
        xList.append(row)




# sys.stdout.write("Number of Rows of Data = " + str(len(xList)) + '\n')
# sys.stdout.write("Number of Columns of Data = " + str(len(xList[1])))
