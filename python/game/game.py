import pygame
from pygame.locals import *
import math 
import random

# 2 - Initialize the game
pygame.init()
width, height = 640, 480
keys=[False,False,False,False]
playerpos=[200,100]

badtimer=100
badtimer1=0
badguys=[[640,100]]
healthvalue=194

#hero speed 
speed=5 

acc=[0,0]
arrows=[]


screen=pygame.display.set_mode((width, height))
  
# 3 - Load images
player = pygame.image.load("resources/images/dude.png")
arrow = pygame.image.load("resources/images/bullet.png")
grass = pygame.image.load("resources/images/grass.png")
castle = pygame.image.load("resources/images/castle.png")
badguyimg1 = pygame.image.load("resources/images/badguy.png")
badguyimg=badguyimg1

# 4 - keep looping through
while 1:
	# 5 - clear the screen before drawing it again
	screen.fill(0)
	badtimer-=1

	# 6 - draw the screen elements
	mouse_pos=pygame.mouse.get_pos()
	#get angle  atan2 求原点与原点引出的射线的最小角度（y<0时候顺时针旋转计算）
	angle=math.atan2(mouse_pos[1]-(playerpos[1]+32),mouse_pos[0]-(playerpos[0]+26))
	#转换为度数
	#//  360/2π  
	playerrot=pygame.transform.rotate(player,360-angle*57.29)	
	#//get_rect  means : get the rectangular area of the Surface
	playerpos1 = (playerpos[0]-playerrot.get_rect().width//2, playerpos[1]-playerrot.get_rect().height//2)
	    

	
	for x in range(int(width/grass.get_width())+1):
		for y  in range(int(height/grass.get_height())+1):
			screen.blit(grass,((x*100),(y*100)))
	#场景小精灵		
	screen.blit(castle,(0,30))
	screen.blit(castle,(0,135))
	screen.blit(castle,(0,240))
	screen.blit(castle,(0,350))
	# screen.blit(player, playerpos)
	screen.blit(playerrot, playerpos1)
#=====================================================================#
	if badtimer==0:
		badguys.append([640, random.randint(50,430)])
		badtimer=100-(badtimer1*2)
		if badtimer1>=35:
			badtimer1=35
		else:
			badtimer1+=5
			index=0
	for badguy in badguys:
		if badguy[0]<-64:
			badguys.pop(index)
			badguy[0]-=7
			index+=1
	for badguy in badguys:
		screen.blit(badguyimg, badguy)
#=====================================================================#


	for bullet in arrows:
		index=0
		velx=math.cos(bullet[0])*10
		vely=math.sin(bullet[0])*10
		bullet[1]+=velx
		bullet[2]+=vely
		if bullet[1]<-64 or bullet[1]>640 or bullet[2]<-64 or bullet[2]>480:
			arrows.pop(index)
		index+=1
		for projectile in arrows:
			arrow1=pygame.transform.rotate(arrow, 360-projectile[0]*57.29)#旋转精灵
			screen.blit(arrow1,(projectile[1],projectile[2]))
		# 7 - update the screen
	pygame.display.flip()
	# 8 - loop through the events
	for event in pygame.event.get():
		if event.type==pygame.KEYDOWN:
			if event.key==K_UP:
				keys[0]=True
			elif event.key==K_LEFT:
				keys[1]=True
			elif event.key==K_DOWN:
				keys[2]=True
			elif event.key==K_RIGHT:
				keys[3]=True		
		if event.type==pygame.KEYUP:
			if event.key==K_UP:
				keys[0]=False
			elif event.key==K_LEFT:
				keys[1]=False
			elif event.key==K_DOWN:
				keys[2]=False
			elif event.key==K_RIGHT:
				keys[3]=False
		if event.type==pygame.MOUSEBUTTONDOWN:
			position=pygame.mouse.get_pos()
			acc[1]+=1
			arrows.append([math.atan2(position[1]-(playerpos1[1]+32),position[0]-(playerpos1[0]+26)),playerpos1[0]+32,playerpos1[1]+32])
			print(acc)    

		if keys[0]:
			playerpos[1]+=speed
		elif keys[1]:
			playerpos[0]-=speed
		elif keys[2]:
			playerpos[1]-=speed
		elif keys[3]:
			playerpos[0]+=speed
	# check if the event is the X button
	if event.type==pygame.QUIT:
		# if it is quit the game
		pygame.quit()
		exit(0)