
package wyf.cgq;
import java.awt.*;import java.awt.event.*;import javax.swing.*;
import javax.swing.event.*;import java.util.*;
public class Tank{
	private int style;//坦克的类型:0是电脑坦克1是玩家坦克
	private int id;//坦克编号0是主机坦克1是副机坦克2~6是电脑坦克
	private int speed;//坦克的速度
	private int direction;//1,2,3,4分别表示上下左右
	private Color color;//坦克的颜色
	private boolean live=true;//坦克的状态，true表示活着，false表示死亡
	private int blood;//坦克的血量
	private int tank_x;//坦克的位置
	private int tank_y;
	private int score=0;//坦克的得分
	public Tank(){}
	public Tank(int id,int style,int speed,int direction,Color color,int blood,int tank_x,int tank_y){
		this.id=id;this.style=style;this.speed=speed;this.direction=direction;
		this.color=color;this.blood=blood;this.tank_x=tank_x;this.tank_y=tank_y;
	}
	public int getStyle(){return this.style;}
	public int getId(){return this.id;}
	public void setSpeed(int speed){this.speed=speed;}
	public int getSpeed(){return this.speed;}
	public int getDirection(){return this.direction;}
	public void setDirection(int direction){this.direction=direction;}
	public void setColor(Color color){this.color=color;}
	public Color getColor(){return this.color;}
	public void setBlood(int blood){this.blood=blood;}
	public int getBlood(){return this.blood;}
	public boolean isLive(){return this.live;}
	public void setLive(boolean live){this.live=live;}
	public int getX(){return this.tank_x;}
	public void setX(int x){this.tank_x=x;}
	public int getY(){return this.tank_y;}
	public void setY(int y){this.tank_y=y;}
	public void setScore(int score){this.score=score;}
	public int getScore(){return this.score;}
	public void move(){
		if(this.live==true){//如果坦克活着
			//方向移动坦克根据
			if(this.direction==1){tank_y=tank_y-speed;}//上移
			else if(this.direction==2){tank_y=tank_y+speed;}//下移
			else if(this.direction==3){tank_x=tank_x-speed;}//左移
			else if(this.direction==4){tank_x=tank_x+speed;}//右移
		}
	}
	public int[] getXY(){//获得子弹初始位置的方法
		int[] x_y=new int[2];//用于存放子弹初始坐标
		if(this.direction==1){x_y[0]=this.tank_x+15;x_y[1]=this.tank_y-15;}
		else if(this.direction==2){x_y[0]=this.tank_x+15;x_y[1]=this.tank_y+40+15;}
		else if(this.direction==3){x_y[0]=this.tank_x-15;x_y[1]=this.tank_y+15;}
		else if(this.direction==4){x_y[0]=this.tank_x+40+15;x_y[1]=this.tank_y+15;}
		return x_y;
	}
	public void drawTank(Graphics g){//绘制坦克的方法
		Color c=g.getColor();//获得画笔颜色
		if(this.id==0){color=new Color(255,0,0,100);}//设置主机的颜色	
		else if(this.id==1){color=new Color(0,255,0,100);}//设置副机的颜色	
		else{color=new Color(0,0,255,100);}//设置电脑坦克的颜色
		g.setColor(color);//设置画笔颜色
		g.fillRoundRect(tank_x,tank_y,40,40,10,10);//绘制坦克主体
		g.setColor(Color.BLACK);
		g.fillRoundRect(tank_x+14,tank_y+14,12,12,5,5);//绘制坦克中央的盖子
		if(direction==1){//当坦克方向向上时
			g.fill3DRect(tank_x+15,tank_y-15,10,17,false);
			g.setColor(new Color(255,0,0));//设置两边圆的颜色
			g.fillOval(tank_x-5,tank_y-5,10,10);g.fillOval(tank_x-5,tank_y+5,10,10);//绘左边圆
			g.fillOval(tank_x-5,tank_y+15,10,10);g.fillOval(tank_x-5,tank_y+25,10,10);
			g.fillOval(tank_x-5,tank_y+35,10,10);g.fillOval(tank_x+35,tank_y-5,10,10);
			g.fillOval(tank_x+35,tank_y+5,10,10);g.fillOval(tank_x+35,tank_y+15,10,10);//绘右边圆
			g.fillOval(tank_x+35,tank_y+25,10,10);g.fillOval(tank_x+35,tank_y+35,10,10);
		}
		else if(direction==2){//方向向下时
			g.fill3DRect(tank_x+15,tank_y+40-2,10,17,false);
			g.setColor(new Color(0,255,0));//设置两边圆的颜色
			g.fillOval(tank_x-5,tank_y-5,10,10);g.fillOval(tank_x-5,tank_y+5,10,10);//绘左边圆
			g.fillOval(tank_x-5,tank_y+15,10,10);g.fillOval(tank_x-5,tank_y+25,10,10);
			g.fillOval(tank_x-5,tank_y+35,10,10);g.fillOval(tank_x+35,tank_y-5,10,10);
			g.fillOval(tank_x+35,tank_y+5,10,10);g.fillOval(tank_x+35,tank_y+15,10,10);//绘右边圆
			g.fillOval(tank_x+35,tank_y+25,10,10);g.fillOval(tank_x+35,tank_y+35,10,10);
		}
		else if(direction==3){//方向向左时
			g.fill3DRect(tank_x-15,tank_y+15,17,10,false);
			g.setColor(new Color(0,0,255));//设置两边圆的颜色
			g.fillOval(tank_x-5,tank_y-5,10,10);g.fillOval(tank_x+5,tank_y-5,10,10);//绘上边圆
			g.fillOval(tank_x+15,tank_y-5,10,10);g.fillOval(tank_x+25,tank_y-5,10,10);
			g.fillOval(tank_x+35,tank_y-5,10,10);g.fillOval(tank_x-5,tank_y+35,10,10);
			g.fillOval(tank_x+5,tank_y+35,10,10);g.fillOval(tank_x+15,tank_y+35,10,10);//绘下边圆
			g.fillOval(tank_x+25,tank_y+35,10,10);g.fillOval(tank_x+35,tank_y+35,10,10);
		}
		else if(direction==4){//方向向右时
			g.fill3DRect(tank_x+40-2,tank_y+15,17,10,false);
			g.setColor(new Color(128,128,128));//设置两边圆的颜色
			g.fillOval(tank_x-5,tank_y-5,10,10);g.fillOval(tank_x+5,tank_y-5,10,10);//绘上边圆
			g.fillOval(tank_x+15,tank_y-5,10,10);g.fillOval(tank_x+25,tank_y-5,10,10);
			g.fillOval(tank_x+35,tank_y-5,10,10);g.fillOval(tank_x-5,tank_y+35,10,10);
			g.fillOval(tank_x+5,tank_y+35,10,10);g.fillOval(tank_x+15,tank_y+35,10,10);//绘下边圆
			g.fillOval(tank_x+25,tank_y+35,10,10);g.fillOval(tank_x+35,tank_y+35,10,10);
		}
		g.setColor(c);//还原画笔颜色
	}
	public Rectangle getRec(){//返回坦克当前的矩形区域
		return new Rectangle(tank_x,tank_y,40,40);
	}
	public Rectangle getNextRec(){//返回移动后的矩形区域
		Rectangle rec=null;
		if(direction==1){rec=new Rectangle(tank_x,tank_y-this.speed-3,40,40);}
		else if(direction==2){rec=new Rectangle(tank_x,tank_y+this.speed+3,40,40);}
		else if(direction==3){rec=new Rectangle(tank_x-this.speed-3,tank_y,40,40);}
		else if(direction==4){rec=new Rectangle(tank_x+this.speed+3,tank_y,40,40);}
		return rec;//返回该矩形
	}
}