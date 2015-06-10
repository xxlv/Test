
package wyf.cgq;
import java.util.*;import java.awt.*;
import java.awt.event.*;import javax.swing.*;
import javax.swing.event.*;
import wyf.cgq.PaintThread;

import java.io.*;
public class ServerWar extends JPanel{
	public static final int HCBulletPower=100;//这是玩家坦克子弹的杀伤力
	public static final int badPower=50;//设置电脑坦克的子弹杀伤力
	public static final int BulletSpeed=15;//设置子弹的速度
	WarMap warmap=new WarMap();//创建地图对象
	Map<Integer,Bullet> m_bullet=new HashMap<Integer,Bullet>();//创建存放子弹的map
	PaintThread pt;//声明绘制线程引用
	TankServer father;//声明服务器主类的引用
	Tank host_tank;//声明主机坦克的引用
	Tank client_tank;//声明客户机坦克的引用
	Tank[] bad_tank=new Tank[5];//创建电脑坦克数组
	int bullet_id=0;//设置子弹的初始id号
	private boolean start=false;//游戏状态标志位
	public ServerWar(TankServer father){
		this.father=father;
		pt=new PaintThread(father);
		pt.start();//创建绘制线程并启动
		this.initialTank();//初始化坦克
	}
	public void initialTank(){//初始化坦克的方法
		host_tank=new Tank(0,1,5,1,new Color(255,0,0,200),400,300,600);//主机坦克
		client_tank=new Tank(1,1,5,1,new Color(0,255,0,200),400,500,600);//副机坦克
		bad_tank[0]=new Tank(2,0,5,4,new Color(0,0,255,200),50,30,300);//电脑坦克
		bad_tank[1]=new Tank(3,0,5,2,new Color(0,0,255,200),50,30,30);
		bad_tank[2]=new Tank(4,0,5,2,new Color(0,0,255,200),50,300,30);
		bad_tank[3]=new Tank(5,0,5,2,new Color(0,0,255,200),50,720,30);
		bad_tank[4]=new Tank(6,0,5,3,new Color(0,0,255,200),50,710,300);
	}
	@Override
	public void paint(Graphics g){//重写的run方法
		if(start==true){//如果游戏已经开始
			warmap.draws(g);//绘制地图
			this.sendMsg("<#HOST#>"+host_tank.getDirection()+"/"+host_tank.getBlood()+
					    "/"+host_tank.getX()+"/"+host_tank.getY());//向客户端传递主机信息
			if(host_tank.isLive()){
				host_tank.drawTank(g);//绘制主机
		    }     					                         
			if(client_tank.isLive()){
				client_tank.drawTank(g);//绘制副机
			}
			for(int i=0;i<bad_tank.length;i++){//绘制电脑坦克
				if(Math.random()>0.8){//按照一定的概率随即改变电脑坦克的方向
					bad_tank[i].setDirection(new Random().nextInt(4)+1);
				}
				if(Math.random()>0.90){//按照一定的概率让电脑坦克发射子弹
					this.tankFire(bad_tank[i]);
				}
				if(this.canMove(bad_tank[i])){//如果可以移动则移动电脑坦克
					bad_tank[i].move();
				}
				else{//不能移动则随机改变其方向
					bad_tank[i].setDirection(new Random().nextInt(4)+1);
				}
				this.sendMsg("<#BADTANK#>"+i+"/"+bad_tank[i].getDirection()+"/"+
				bad_tank[i].getX()+"/"+bad_tank[i].getY());//向客户端发送电脑坦克的信息
				bad_tank[i].drawTank(g);//绘制该电脑坦克
			}
			java.util.ArrayList<Integer> list=new ArrayList<Integer>();//创建ArrayList对象
			Set<Integer> keySet=m_bullet.keySet();//获得子弹的键的集合
			Iterator<Integer> ii=keySet.iterator();//获得keyset的迭代器
			while(ii.hasNext()){//遍历迭代器,将子弹的键放入list
				Integer i=ii.next();
				list.add(i);
			}
			for(Integer i:list){//遍历list
				Bullet bullet_temp=m_bullet.get(i);//取出一个子弹
				if(bullet_temp==null){
					continue;//如果是null则进入下一次循环
				}
				if(isAimed(bullet_temp)){
					continue;//如果子弹击中墙壁或敌人坦克则进入下一次循环
				}				
				bullet_temp.move();//移动该子弹
				sendMsg("<#UPDATEBULLET#>"+bullet_temp.getId()+"/"+
				        bullet_temp.getX()+"/"+bullet_temp.getY());//将信息发送给客户端
				bullet_temp.drawBullet(g);//绘制子弹
			}
			for(int i=0;i<bad_tank.length;i++){
				if(bad_tank[i].isLive()==false){//重新安排已经死亡的电脑坦克
					this.replace(bad_tank[i]);
					this.sendMsg("<#BADTANK#>"+i+"/"+bad_tank[i].getDirection()+"/"+
				    bad_tank[i].getX()+"/"+bad_tank[i].getY());//将信息发送给客户端
				}
			}
		}
		else{//如果游戏还没有开始
			warmap.draws(g);//绘制地图
			host_tank.drawTank(g);//绘制主机坦克
			client_tank.drawTank(g);//绘制副机坦克
			for(int i=0;i<bad_tank.length;i++){//绘制电脑坦克
				bad_tank[i].drawTank(g);
			}
		}
		Color c=g.getColor();//获得画笔颜色
		Font f=g.getFont();//获得画笔字体
		g.setColor(new Color(255,0,0,100));//设置画笔颜色
		g.setFont(new Font("宋体",Font.BOLD,20));//设置字体
		g.drawString("主机",30,20);//绘制"主机"字符串
		g.drawRect(80,5,400,15);//绘制矩形框
		g.fill3DRect(80,5,host_tank.getBlood(),15,true);//根据主机血量绘制血量信息
		g.drawString("得分"+host_tank.getScore()+"",485,20);//绘制主机得分信息
		g.setColor(new Color(0,255,0,100));//设置画笔颜色
		g.drawString("副机",30,40);//绘制"副机"字符串
		g.drawRect(80,25,400,15);//绘制矩形框
		g.fill3DRect(80,25,client_tank.getBlood(),15,true);//根据副机血量绘制血量信息
		g.drawString("得分"+client_tank.getScore(),485,40);//绘制副机得分信息
		if(host_tank.isLive()==false&&client_tank.isLive()==false){//绘制游戏结束的提示信息
			g.setFont(new Font("宋体",Font.BOLD,80));
			g.setColor(Color.RED);
			g.drawString("游戏结束",240,300);//绘制游戏结束提示信息
		}
		g.setColor(c);//还原画笔颜色
		g.setFont(f);//还原画笔字体
	}
	public boolean isAimed(Bullet bullet){//判断子弹是否击中物体的方法 
		int tank_id=bullet.getTank_id();//获得该子弹的所属坦克的id号
		int id=bullet.getId();//获得子弹的id
		if(tank_id<2){//tank_id小于2说明是玩家的坦克的子弹
			for(int i=0;i<bad_tank.length;i++){
				if(bullet.getRec().intersects(bad_tank[i].getRec())&&bad_tank[i].isLive()==true){
					//遍历电脑坦克，如果击中电脑坦克，则移除该子弹
					this.sendMsg("<#REMOVEBULLET#>"+id);//向客户端发送移除子弹的信息
					bad_tank[i].setLive(false);//将被击中的坦克状态设为死亡
					if(tank_id==0){//当是主机时
						this.sendMsg("<#ADDSCORE#>"+0);//向客户端发送加分信息
						host_tank.setScore(host_tank.getScore()+5);//主机端给主机加分
						if(host_tank.getBlood()+5>400){//给主机加血
							host_tank.setBlood(400);
						}
						else{
							host_tank.setBlood(host_tank.getBlood()+5);
						}
					}
					else{//当是副机时
						this.sendMsg("<#ADDSCORE#>"+1);//向客户端发送加分信息
						client_tank.setScore(client_tank.getScore()+5);//主机端给副机加分
						if(client_tank.getBlood()+5>400){//给副机加血
							client_tank.setBlood(400);
						}
						else{
							client_tank.setBlood(client_tank.getBlood()+5);
						}
					}
					this.m_bullet.remove(id);//在子弹列表中移除该子弹
					return true;
				}
			}
		}
		if(tank_id>=2){//tank_id大于1说明是电脑坦克的子弹
			if(bullet.getRec().intersects(host_tank.getRec())&&host_tank.isLive()){//击中主机
				this.sendMsg("<#REMOVEBULLET#>"+id);//向客户端发送移除子弹的信息
				this.sendMsg("<#SUBBLOOD#>"+0+"/"+bullet.getPower());//向客户端发送减血的信息
				host_tank.setBlood(host_tank.getBlood()-bullet.getPower());//主机减血
				if(host_tank.getBlood()<=0){
					host_tank.setLive(false);//如果剩余血量小于等于0，则将其设为死亡状态
					host_tank.setBlood(0);//将血设为0
				}
				this.m_bullet.remove(id);//移除该子弹
				return true;
			}
			else if(bullet.getRec().intersects(client_tank.getRec())&&client_tank.isLive()){//击中副机
				this.sendMsg("<#REMOVEBULLET#>"+id);//向客户端发送移除子弹的信息
				this.sendMsg("<#SUBBLOOD#>"+1+"/"+bullet.getPower());//向客户端发送减血的信息
				client_tank.setBlood(client_tank.getBlood()-bullet.getPower());//副机减血
				if(client_tank.getBlood()<=0){
					client_tank.setLive(false);//如果副机剩余血量小于等于0，则将其设为死亡状态
					client_tank.setBlood(0);//将血设为0
				}
				this.m_bullet.remove(id);//移除该子弹
				return true;
			}
		}
		if(bullet.getX()<0||bullet.getX()>this.getWidth()||
		   bullet.getY()<0||bullet.getY()>this.getHeight()||
		   warmap.getState(bullet.getX()/20,bullet.getY()/20)==1
		  ){//子弹越界或碰上墙壁
		  		this.sendMsg("<#REMOVEBULLET#>"+id);//向客户端发送移除子弹的信息
		  		this.m_bullet.remove(id);//移除该子弹
		  		return true;
		  }
		return false;
	}
	public void replace(Tank tank){
		int pos=new Random().nextInt(5)+1;//随机产生一个位置编号
		switch(pos){
			case 1:tank.setX(30);tank.setY(300);tank.setDirection(4);break;
			case 2:tank.setX(30);tank.setY(30);tank.setDirection(2); break;
			case 3:tank.setX(300);tank.setY(30);tank.setDirection(2);break;
			case 4:tank.setX(720);tank.setY(30);tank.setDirection(2);break;
			case 5:tank.setX(710);tank.setY(300);tank.setDirection(3);break;
		}
		tank.setLive(true);
		for(int i=0;i<bad_tank.length;i++){
			if(bad_tank[i].getId()!=tank.getId()&&tank.getRec().intersects(bad_tank[i].getRec())){
				this.replace(tank);//如果该位置有其他电脑坦克，则继续随机位置
			}
		}
		if(tank.getRec().intersects(host_tank.getRec())){
			this.replace(tank);//如果该位置有主机坦克，则继续随机位置
		}
		if(tank.getRec().intersects(client_tank.getRec())){
			this.replace(tank);//如果该位置有副机坦克，则继续随机位置
		}
	}
	public boolean canMove(Tank tank){//判断坦克是否可以移动的方法
		int x=tank.getX();//获得该坦克的位置
		int y=tank.getY();
		int d=tank.getDirection();//获得该坦克的方向
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
		){//如果越界或碰上墙壁则返回false
			return false;
		}
		else {
			if(tank.getId()==0){//如果是主机
				if(tank.getNextRec().intersects(client_tank.getRec())){
					return false;//如果前面有副机阻拦，则返回false
				}
				else{//是否有电脑坦克阻拦
					for(int i=0;i<bad_tank.length;i++){
						if(tank.getNextRec().intersects(bad_tank[i].getRec())){
							return false;//如果有电脑坦克阻拦，则返回false
						}
					}
				}
			}
			else if(tank.getId()>1){//如果是电脑坦克
				if(tank.getNextRec().intersects(host_tank.getRec())){
					return false;//如果有主机阻拦，则返回false
				}
				else if(tank.getNextRec().intersects(client_tank.getRec())){
					return false;//如果有副机阻拦，则返回false
				}
				else{
					for(int i=0;i<bad_tank.length;i++){
						if(bad_tank[i].getId()!=tank.getId()){
							if(tank.getNextRec().intersects(bad_tank[i].getRec())){
								return false;//如果有其他电脑坦克阻拦，则返回false
							}
						}
					}
				}
			}
			return true;
		}
	}
	public void setStart(boolean start){
		this.start=start;//设置游戏状态的方法
	}
	public void setHostDir(int direction){
		if(this.start==true){
			host_tank.setDirection(direction);//设置主机坦克的方向
		}
	}
	public void hostMove(){	
		if(this.canMove(host_tank)&&(this.start==true)){
			host_tank.move();//主机移动
		}
	}
	public void updateClient(int direction,int x,int y){//更新副机的位置和方向
		client_tank.setDirection(direction);//更新方向
		client_tank.setX(x);//更新位置
		client_tank.setY(y);
	}
	public void hostFire(){//主机发射子弹的方法
		if(host_tank.isLive()&&this.start==true){//如果主机活着且游戏已经开始
			int id=++this.bullet_id%200;//产生一个子弹id号 
			int tank_id=host_tank.getId();//获得主机的id
			int style=host_tank.getStyle();//获得主机的类型
			int direction=host_tank.getDirection();//获得主机的方向
			int[] bullet_x_y=host_tank.getXY();//获得子弹的起始位置
			//向客户端发送添加子弹的信息
			this.sendMsg("<#BULLET#>"+id+"/"+bullet_x_y[0]+"/"+bullet_x_y[1]);
			Bullet bullet=new Bullet(id,tank_id,style,direction,
			            BulletSpeed,HCBulletPower,bullet_x_y[0],bullet_x_y[1]);
			this.m_bullet.put(id,bullet);//创建子弹并添加到子弹列表中
		}
	}
	public void clientFire(int direction,int x,int y){//客户机发射子弹的方法
		int id=++this.bullet_id%200;//产生一个子弹id号 
		int tank_id=client_tank.getId();//获得副机的id
		int style=client_tank.getStyle();//获得副机的类型
		this.sendMsg("<#BULLET#>"+id+"/"+x+"/"+y);//向客户端发送添加子弹的信息
		Bullet bullet=new Bullet(id,tank_id,style,direction,
		            BulletSpeed,HCBulletPower,x,y);
		this.m_bullet.put(id,bullet);//创建子弹并添加到子弹列表中
	}
	public void tankFire(Tank tank){//电脑坦克发射子弹
		int id=++this.bullet_id%200;//产生一个子弹id号 
		int tank_id=tank.getId();//获得坦克的id号
		int style=tank.getStyle();//获得坦克的类型
		int direction=tank.getDirection();//获得坦克的方向
		int[] bullet_x_y=tank.getXY();//获得子弹的位置
		//向客户端发送添加子弹的信息
		this.sendMsg("<#BULLET#>"+id+"/"+bullet_x_y[0]+"/"+bullet_x_y[1]);
		Bullet bullet=new Bullet(id,tank_id,style,direction,
		            BulletSpeed,badPower,bullet_x_y[0],bullet_x_y[1]);
		this.m_bullet.put(id,bullet);//创建子弹并添加到子弹列表中
	}
	public void sendMsg(String msg){//向客户端发送信息的方法
		try{
			father.sat.dout.writeUTF(msg);
		}
		catch(IOException e){e.printStackTrace();}
	}
}
