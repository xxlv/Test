
package wyf.cgq;
import java.util.*;import java.awt.*;
import java.awt.event.*;import javax.swing.*;
import javax.swing.event.*;import java.io.*;
import java.util.ArrayList;
public class ClientWar extends JPanel{
	WarMap warmap=new WarMap();//创建地图对象
	Map<Integer,Bullet> m_bullet=new HashMap<Integer,Bullet>();//存放子弹的map对象
	CPaintThread cpt;//声明绘制线程的引用
	TankClient father;//声明客户端主窗体的引用
	Tank host_tank;//声明主机坦克的引用
	Tank client_tank;//声明客户端坦克的引用
	Tank[] bad_tank=new Tank[5];//创建电脑坦克的数组
	int bullet_id=0;//设置子弹的id号 ，从0开始
	private boolean start=false;//游戏状态的标志位，flase说明没开始，反之则开始
	public ClientWar(TankClient father){//构造器
		this.father=father;
		cpt=new CPaintThread(father);//创建绘制线程
		cpt.start();//启动该绘制线程
		this.initialTank();//初始化坦克
	}
	public void initialTank(){//初始化坦克的方法
		host_tank=new Tank(0,1,5,1,Color.RED,400,300,600);//主机坦克
		client_tank=new Tank(1,1,5,1,Color.GREEN,400,500,600);//副机坦克
		bad_tank[0]=new Tank(2,0,5,4,Color.BLUE,50,30,300);//电脑坦克
		bad_tank[1]=new Tank(3,0,5,2,Color.BLUE,50,30,30);
		bad_tank[2]=new Tank(4,0,5,2,Color.BLUE,50,300,30);
		bad_tank[3]=new Tank(5,0,5,2,Color.BLUE,50,720,30);
		bad_tank[4]=new Tank(6,0,5,3,Color.BLUE,50,710,300);
	}
	@Override
	public void paint(Graphics g){//重写paint方法
		if(start==true){//如果游戏已经开始
			warmap.draws(g);//绘制地图
			if(host_tank.isLive()){//如果主机还活着
				host_tank.drawTank(g);//绘制主机
			}
			if(client_tank.isLive()){//如果副机还活着
				this.sendMsg("<#CLIENT#>"+client_tank.getDirection()+"/"+client_tank.getX()+
				"/"+client_tank.getY());//将副机的状态发送给服务期
				client_tank.drawTank(g);//绘制副机
			}
			for(int i=0;i<bad_tank.length;i++){//绘制电脑坦克
				bad_tank[i].drawTank(g);
			}
			ArrayList<Integer> list=new ArrayList<Integer>();//创建一个ArrayList对象
			Set<Integer> keyset=this.m_bullet.keySet();//获得子弹的键集合 
			Iterator<Integer> ii=keyset.iterator();//获得迭代器
			while(ii.hasNext()){//遍历迭代器
				Integer i=ii.next();
				list.add(i);//将键存入ArrayList中
			}
			for(Integer key:list){//遍历ArrayList绘制子弹
				Bullet bullet_temp=m_bullet.get(key);
				if(bullet_temp!=null){
					bullet_temp.drawBullet(g);
				}
			}
		}
		else{//游戏还没有开始
			warmap.draws(g);//绘制地图
			host_tank.drawTank(g);//绘制主机坦克
			client_tank.drawTank(g);//绘制客户机坦克
			for(int i=0;i<5;i++){//绘制电脑坦克
				bad_tank[i].drawTank(g);
			}
		}
		Color c=g.getColor();//获得画笔颜色
		Font f=g.getFont();//获得画笔字体
		g.setColor(new Color(255,0,0,100));//设置画笔颜色
		g.setFont(new Font("宋体",Font.BOLD,20));//设置画笔字体
		g.drawString("主机",30,20);//在左上角绘制"主机"字符串
		g.drawRect(80,5,400,15);//绘制一个矩形框
		g.fill3DRect(80,5,host_tank.getBlood(),15,true);//根据主机血量绘制主机血量图
		g.drawString("得分"+host_tank.getScore()+"",485,20);//绘制主机的得分
		g.setColor(new Color(0,255,0,100));//设置画笔颜色
		g.drawString("副机",30,40);//在左上角绘制"副机"字符串
		g.drawRect(80,25,400,15);//绘制一个矩形框
		g.fill3DRect(80,25,client_tank.getBlood(),15,true);//根据副机血量绘制主机血量图
		g.drawString("得分"+client_tank.getScore(),485,40);//绘制副机的得分
		if(host_tank.isLive()==false&&client_tank.isLive()==false){//如果主副机都死亡
			g.setFont(new Font("宋体",Font.BOLD,80));
			g.setColor(Color.RED);
			g.drawString("游戏结束",240,300);//绘制游戏结束提示信息
		}
		g.setColor(c);//还原画笔颜色
		g.setFont(f);//还原画笔字体
	}
	public boolean canMove(Tank tank){//判断坦克是否可以移动
		int x=tank.getX();//获得坦克的X坐标
		int y=tank.getY();//获得坦克的Y坐标
		int d=tank.getDirection();//获得坦克方向
		if(
		   (x-15<=0&&d==3)||
		   (x+55>(father.frame_width-father.frame_opration)*father.block_width&&d==4)||
		   (y-15<0&&d==1)||
		   (y+55+15>father.frame_height*father.block_height&&d==2)||
		   (warmap.getState((x+55)/20,(y+20)/20)==1&&d==4)||
	       (warmap.getState((x-15)/20,(y+20)/20)==1&&d==3)||
	       (warmap.getState((x+20)/20,(y-15)/20)==1&&d==1)||
	       (warmap.getState((x+20)/20,(y+55)/20)==1&&d==2)||
	       (warmap.getState((x+55)/20,(y+20)/20+1)==1&&d==4)||
	       (warmap.getState((x+55)/20,(y+20)/20-1)==1&&d==4)||
	       (warmap.getState((x-20)/20,(y+20)/20+1)==1&&d==3)||
	       (warmap.getState((x-20)/20,(y+20)/20-1)==1&&d==3)||
	       (warmap.getState((x+20)/20+1,(y-20)/20)==1&&d==1)||
	       (warmap.getState((x+20)/20-1,(y-20)/20)==1&&d==1)||
	       (warmap.getState((x+20)/20+1,(y+55)/20)==1&&d==2)||
	       (warmap.getState((x+20)/20-1,(y+55)/20)==1&&d==2)
		){//如果越界活着碰到墙壁
			return false;//不可以前进，返回false
		}
		else {
			if(tank.getId()==1){//如果是副机
				if(tank.getNextRec().intersects(host_tank.getRec())){
					return false;//如果移动后和主机碰撞，则返回 false
				}
				else{
					for(int i=0;i<bad_tank.length;i++){
						if(tank.getNextRec().intersects(bad_tank[i].getRec())){
							return false;//如果与电脑坦克碰撞，则返回false
						}
					}
				}
			}
			
		}
		return true;//可以移动，返回true
	}
	public void updateHost(int direction,int blood,int x,int y){//更新主机坦克
		host_tank.setDirection(direction);//更新方向
		host_tank.setBlood(blood);//更新血量
		host_tank.setX(x);//更新X Y 坐标
		host_tank.setY(y);
	}
	public void setStart(boolean start){//设置游戏状态的方法
		this.start=start;
	}
	public void setClientDir(int direction){//改变副机的方向
		if(start==true){//如果游戏已经开始才可改变
			client_tank.setDirection(direction);
		}
	}
	public void clientMove(){//副机向前移动
		if(this.canMove(this.client_tank)&&start==true){
			client_tank.move();//如果游戏开始了且可以移动则移动
		}
	}
	public void addBullet(int id,int x,int y){//增加子弹
		Bullet bullet=new Bullet(id,x,y);
		this.m_bullet.put(id,bullet);//将新增的子弹放入m_bullet中
	}
	public void updateBullet(int id,int x,int y){//更新子弹
		Bullet bullet=this.m_bullet.get(id);//获得要更新的子弹
		if(bullet!=null){
			bullet.setX(x);//更新子弹的位置 
			bullet.setY(y);
		}
	}
	public void clientFire(){//副机发射子弹
		if(client_tank.isLive()&&start==true){//如果副机还活着且游戏已经开始
			int direction=client_tank.getDirection();//获得坦克的方向
			int[] x_y=client_tank.getXY();//获得子弹的发射的起始位置
			this.sendMsg("<#FIRECLIENT#>"+direction+"/"+x_y[0]+"/"+x_y[1]);//给服务器传消息
		}
	}
	public void updateTank(int number,int direction,int x,int y){//更新电脑坦克
		this.bad_tank[number].setDirection(direction);//更新方向
		this.bad_tank[number].setX(x);//更新位置
		this.bad_tank[number].setY(y);
	}
	public void removeBullet(int id){
		this.m_bullet.remove(id);//从子弹集合中删除子弹
	}
	public void addScore(int id){//玩家坦克加分
		if(id==0){//给主机坦克加分
			host_tank.setScore(host_tank.getScore()+5);
			if(host_tank.getBlood()+5>400){//如果血量加5滴后会超过最大量
				host_tank.setBlood(400);//将血量设置为400
			}
			else{//血量没有超过400，则增加5滴
				host_tank.setBlood(host_tank.getBlood()+5);
			}		
		}
		else{
			client_tank.setScore(client_tank.getScore()+5);//给副机加分
			if(client_tank.getBlood()+5>400){//如果血量加5滴后会超过最大量
				client_tank.setBlood(400);//将血量设置为400
			}
			else{//血量没有超过400，则增加5滴
				client_tank.setBlood(client_tank.getBlood()+5);
			}
		}
	}
	public void subBlood(int id,int subnum){//玩家收到攻击，减少血量
		if(id==0){//如果是主机
			host_tank.setBlood(host_tank.getBlood()-subnum);//减少血量
			if(host_tank.getBlood()<=0){//血量是否还大于0
				host_tank.setLive(false);//不大于则设为死亡状态
				host_tank.setBlood(0);//将血设为0
			}
		}
		else{//副机减血
			client_tank.setBlood(client_tank.getBlood()-subnum);
			if(client_tank.getBlood()<=0){//如果血量小于0，则将坦克设为死亡状态
				client_tank.setLive(false);
				client_tank.setBlood(0);
			}
		}
	}
	public void sendMsg(String msg){//用于向服务器发送消息的方法
		try{
			father.cat.dout.writeUTF(msg);//发送消息
		}
		catch(IOException e){e.printStackTrace();}
	}
}