//坦克类
import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
public class Tank {
	
	Vector<Bullet> bullets=new Vector<Bullet>(); //子弹组
	int positionX;	//坦克的初始位置x
	int positionY;	//坦克的初始位置y
	int direct;		//坦克的方向
	int speed;		//坦克速度
	int bulletSpeed=10;	//子弹速度
	
	
	public int getPositionX() {
		return positionX;
	}
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}




	//坦克上移
	public void moveUp()
	{
		
		this.direct=0;
		if(this.positionY>14)
		this.positionY-=this.speed;
		
	}
	//坦克向右
	public void moveRight(){
		this.direct=1;
		if(this.positionX<580)
		this.positionX+=this.speed;
	}
	//坦克下移
	public void moveDown(){
		
		this.direct=2;
		if(this.positionY<400)
		this.positionY+=this.speed;
	}
	//坦克左移
	public void moveLeft(){
		this.direct=3;
		if(this.positionX>20)
		this.positionX-=this.speed;
	}
	


	//坦克射击子弹行为
	public void shout()
	{	
		//System.out.println("调用者:"+Thread.currentThread().getStackTrace());
		Bullet zd=null;//子弹容器
		switch(this.direct)
		{
		case 0:
			//zd.y-=this.speed;
			//创建一个有方向的子弹
			zd= new Bullet (this.getPositionX(),this.getPositionY(),0,this.bulletSpeed); 
		break;
		
		case 1:
			 zd= new Bullet (this.getPositionX(),this.getPositionY(),1,this.bulletSpeed);
			break;
		
		case 2:
			 zd= new Bullet (this.getPositionX(),this.getPositionY(),2,this.bulletSpeed);
			break;
		
		case 3:
			 zd= new Bullet (this.getPositionX(),this.getPositionY(),3,this.bulletSpeed);
		break;
		
			}
			this.bullets.add(zd);//将子弹加入这个坦克中
			Thread t=new Thread(zd);
			t.start();//启动子弹线程
	}		
}
//敌人坦克
class ETank extends Tank implements Runnable{
	int type;//坦克类型
	Color color;
	Bullet  ezidan;
//	Vector<ETank> ets =new Vector<ETank>();//记录所有的敌人
	ETank(Color color){
		this.color=color;
		this.type=1;
	}
	
////////////////////////////////////////////////////////


	
	public void aotoMove(){
		
		switch(this.getDirect())
		{
		case 0:	

			this.setPositionY(this.getPositionY()-this.speed);
		break;
		case 1:
	
			this.setPositionX(this.getPositionX()+this.speed);
			break;
		case 2:

			this.setPositionY(this.getPositionY()+this.speed);

			break;
		case 3:

			this.setPositionX(this.getPositionX()-this.speed);
			break;
		
			}
		
		
	}
////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////	
//线程 这个线程里面让坦克自动运行
//增加功能： 让坦克在中途有一定概率改变位置	
	public void run(){	
		
	
		while(true)
		{
			
			try{
				Thread.sleep(30);
				}
				catch(Exception e)
				{
				}

			this.aotoMove();
			
	
			}
		}
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////	
//我方坦克
class MTank extends Tank{
	int type;//坦克类型
	Color color;//坦克颜色
	boolean isLive;
	MTank(){
		this.color=Color.green;
		this.type=0;
		this.isLive=true;
	}
	
		
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//炮弹
class Bullet  implements Runnable{
int x;//子弹的x坐标
int y;//子弹的y坐标
int speed;//子弹的速度
int direct;//子弹的方向
boolean isLived; //子弹是否存活

public Bullet(int x,int y ,int direct,int speed){	
	this.x=x;
	this.y=y;
	this.direct=direct;
	this.speed=speed;
	this.isLived=true;//首次创建子弹自动存活
	
}
//线程启动
public void run()
{
	
	//启动子弹
	while(true){
		try{
			Thread.sleep(50);
			
		}
		catch(Exception e)
		{
			
		}
		if(this.isLived)//判断该子弹是否存活
		{
		switch(this.direct)
		{
		case 0:
			this.direct=0;
			this.y-=this.speed;
			break;
		case 1:
			this.direct=1;
			this.x+=this.speed;
			
			break;		
			case 2:
				this.direct=2;
				this.y+=this.speed;
				
				break;
			case 3:
				this.direct=3;
				this.x-=this.speed;
				break;
					}
		}
		else 
			break;
		//System.out.println("x= "+this.x+" y="+this.y);
		
		
	}
	
	
	
	}

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//炸弹类
class Bomb{
	int x;
	int y;
	int life;
	public Bomb(int x, int y){
		this.x=x;
		this.y=y;
		this.life=9;
	}
	
	public void killBomb()
	{
		if(this.life>=1)
		{
			this.life--;
		}
		
	}		
}
	
