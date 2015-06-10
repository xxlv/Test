//̹����
import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
public class Tank {
	
	Vector<Bullet> bullets=new Vector<Bullet>(); //�ӵ���
	int positionX;	//̹�˵ĳ�ʼλ��x
	int positionY;	//̹�˵ĳ�ʼλ��y
	int direct;		//̹�˵ķ���
	int speed;		//̹���ٶ�
	int bulletSpeed=10;	//�ӵ��ٶ�
	
	
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




	//̹������
	public void moveUp()
	{
		
		this.direct=0;
		if(this.positionY>14)
		this.positionY-=this.speed;
		
	}
	//̹������
	public void moveRight(){
		this.direct=1;
		if(this.positionX<580)
		this.positionX+=this.speed;
	}
	//̹������
	public void moveDown(){
		
		this.direct=2;
		if(this.positionY<400)
		this.positionY+=this.speed;
	}
	//̹������
	public void moveLeft(){
		this.direct=3;
		if(this.positionX>20)
		this.positionX-=this.speed;
	}
	


	//̹������ӵ���Ϊ
	public void shout()
	{	
		//System.out.println("������:"+Thread.currentThread().getStackTrace());
		Bullet zd=null;//�ӵ�����
		switch(this.direct)
		{
		case 0:
			//zd.y-=this.speed;
			//����һ���з�����ӵ�
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
			this.bullets.add(zd);//���ӵ��������̹����
			Thread t=new Thread(zd);
			t.start();//�����ӵ��߳�
	}		
}
//����̹��
class ETank extends Tank implements Runnable{
	int type;//̹������
	Color color;
	Bullet  ezidan;
//	Vector<ETank> ets =new Vector<ETank>();//��¼���еĵ���
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
//�߳� ����߳�������̹���Զ�����
//���ӹ��ܣ� ��̹������;��һ�����ʸı�λ��	
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
//�ҷ�̹��
class MTank extends Tank{
	int type;//̹������
	Color color;//̹����ɫ
	boolean isLive;
	MTank(){
		this.color=Color.green;
		this.type=0;
		this.isLive=true;
	}
	
		
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//�ڵ�
class Bullet  implements Runnable{
int x;//�ӵ���x����
int y;//�ӵ���y����
int speed;//�ӵ����ٶ�
int direct;//�ӵ��ķ���
boolean isLived; //�ӵ��Ƿ���

public Bullet(int x,int y ,int direct,int speed){	
	this.x=x;
	this.y=y;
	this.direct=direct;
	this.speed=speed;
	this.isLived=true;//�״δ����ӵ��Զ����
	
}
//�߳�����
public void run()
{
	
	//�����ӵ�
	while(true){
		try{
			Thread.sleep(50);
			
		}
		catch(Exception e)
		{
			
		}
		if(this.isLived)//�жϸ��ӵ��Ƿ���
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
//ը����
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
	
