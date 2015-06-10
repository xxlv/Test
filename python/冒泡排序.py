#-*- coding:utf-8 -*-

'冒泡排序的Python实现'
__author__='hero'

u=[12,3,3,4,3,4,1,2,3,4,5,5,66,78,1]

def bubble_sort(bubbleList):
	#get list length 
	listlength=len(bubbleList)
	while listlength>0:
		for i in range(listlength-1):
			if bubbleList[i]>bubbleList[i+1]:
				bubbleList[i] = bubbleList[i] + bubbleList[i+1]
				bubbleList[i+1] = bubbleList[i] - bubbleList[i+1]
				bubbleList[i] = bubbleList[i] - bubbleList[i+1]
		listlength-=1
		# unsorted[i],unsorted[i+1]=unsorted[i+1],unsorted[i]	
		print(bubbleList)

					
bubble_sort(u)