
package wyf.cgq;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
public class Bullet
{
	private int id;//子弹的id号，该号唯一标志一个子弹
	private int tank_id;//该子弹所属坦克的Id号
	private int style;//子弹类型，0表示是敌人的坦克发出的，1表示是玩家坦克发出的 
	private int direction;//代表方法1、2、3、4分别表示上、下、左、右
	private int speed;//子弹的运行速度，
	private int power;//子弹的杀伤力，即可以使对方掉多少血
	private int bullet_x;//子弹所在矩形的左上角的X坐标
	private int bullet_y;//子弹所在矩形的左上角的Y坐标
	private boolean live=true;//子弹是否存活的状态
	public Bullet(){}//无参构造器
	public Bullet(int id,int bullet_x,int bullet_y){//构造器，用于客户端用
		this.id=id;
		this.bullet_y=bullet_y;
		this.bullet_x=bullet_x;
	}
	public Bullet(int id,int tank_id,int style,int direction,int speed,int power,
	             int bullet_x,int bullet_y){//构造器，用于服务器端用
		this.id=id;this.tank_id=tank_id;
		this.style=style;this.direction=direction;
		this.speed=speed;this.power=power;
		this.bullet_x=bullet_x;this.bullet_y=bullet_y;
	}
	public void move(){//子弹运行的方法
		if(direction==1){this.bullet_y-=speed;}//向上运行
		else if(direction==2){this.bullet_y+=speed;}//向下运行
		else if(direction==3){this.bullet_x-=speed;}//向左运行
		else if(direction==4){this.bullet_x+=speed;}//向下运行
	}
	public int getId(){return this.id;}//获得子弹的id号
	public int getTank_id(){return this.tank_id;}//获得子弹所属坦克的id号
	public int getStyle(){return this.style;}//获得子弹的类型
	public int getDirection(){return this.direction;}//获得子弹的方向
	public int getPower(){return this.power;}//获得子弹的杀伤力
	public int getX(){return this.bullet_x;}//获得子弹的X坐标
	public void setX(int x){this.bullet_x=x;}//设置子弹的X坐标
	public int getY(){return this.bullet_y;}//获得子弹的Y坐标
	public void setY(int y){this.bullet_y=y;}//设置子弹的Y坐标
	public void drawBullet(Graphics g){//绘制子弹的方法 
		Color c=g.getColor();//获得画笔的颜色
		g.setColor(Color.BLACK);//设置画笔颜色为黑色
		g.fillOval(bullet_x,bullet_y,10,10);//绘制子弹
		g.setColor(c);//还原画笔的颜色
	}
	public Rectangle getRec(){//获得子弹所在的矩形
		return new Rectangle(bullet_x,bullet_y,10,10);
	}
}
