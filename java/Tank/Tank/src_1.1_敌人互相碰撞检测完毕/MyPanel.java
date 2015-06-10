import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
/*
 * 
 * 
 * 11月7日增加事件： 让敌人坦克击中自己的坦克时候自己坦克消失
 * 
 * 
 * */
class MyPanel extends JPanel implements KeyListener,Runnable{
	
	int times=0;
	Vector<ETank> Eheros = new Vector<ETank>();//创建一个vector存放敌人坦克	
	Vector<Bomb> Bombs= new Vector<Bomb>();//创建一个vector存放炸弹	
	Bullet bullet = null;//子弹容器	
	MTank hero=null; //我方英雄
	ETank Ehero=null; //敌人英雄
	//Bullet  zidan=null; 
	Bullet  ezidan=null;
	int EheroNumber=4;//敌人数量
	//int x=20;//初始x位置
	//int y=20;//初始y位置
	//int speed=20;//速度
	//int direct=0;//方向
	//boolean SHOUT=false;
	//Graphics _G=null;//保存画笔

  	
	//炸弹图片组合
	Image image1=null;
	Image image2=null;
	Image image3=null;
	
	//构造函数初始化面板
	  public MyPanel(){	
		//初始化我的坦克
		hero= new MTank();
		hero.setDirect(0);//设置初始方向
		hero.setSpeed(10);//设置初始速度
		hero.setPositionX(400);//设置初始位置x
		hero.setPositionY(300);//设置初始位置y
		
		//将敌人坦克加入集合
		for(int i=0;i<this.EheroNumber;i++)
		{		
			Ehero=new ETank(Color.red);
			Ehero.setDirect(2);
			Ehero.setPositionX((1+i)*150);
			Ehero.setPositionY(0);
			Ehero.setSpeed(3);//设置敌人坦克初始速度		
			Thread t=new Thread(Ehero);
			t.start(); 		//坦克自动运行
			//加入敌人的坦克组vector
//			Bullet zd= new Bullet (Ehero.getPositionX(),Ehero.getPositionY(),0,Ehero.bulletSpeed); 
//			Ehero.bullets.add(zd);		
			Eheros.add(Ehero);
			Ehero.shout();//创建一个子弹并启动线程  将子弹加入到向量

			
		}
		
		///初始化炸弹
		try {
			image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
			image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
			image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		
	}
	
	public void paint(Graphics g)
	{	
		//this._G=g;
		g.setColor(Color.black);//主屏幕背景色
		g.fill3DRect(0, 0, 800, 460, false); //主银幕绘制
		if(this.hero.isLive&&this.hero!=null)
		DrawTank(this.hero.getPositionX(),this.hero.getPositionY(),this.hero.getDirect(),g,this.hero.type);//画出坦克
		
		for(int b=0;b<this.Bombs.size();b++){	
		Bomb bb=this.Bombs.get(b);
		if(this.Bombs.size()>0){
			
			if(bb.life>6&&bb.life<=9)
			{
				//System.out.print("爆炸！画出炸弹");
				g.drawImage(this.image1, bb.x, bb.y, 30, 30, this);
				bb.killBomb();
			}
			else if(bb.life<=6&&bb.life>3)
			{
				
				g.drawImage(this.image2, bb.x, bb.y, 30, 30, this);
				bb.killBomb();
			}
			else if(bb.life>0&&bb.life<=3)
			{
				//System.out.println(bb.life);
				g.drawImage(this.image3, bb.x, bb.y,30, 30, this);
				bb.killBomb();
			}
			else
				{

					this.Bombs.remove(bb);
				}
			}	
		}
		
		//当用户敲击space的时候 子弹才不为空  此时绘制子弹
		if(this.hero.bullets.size()>0){
			
		for(int i=0;i<this.hero.bullets.size();i++)
		{
	
			if(this.hero.bullets.get(i)!=null&&this.hero.bullets.get(i).isLived){
			//画出我方坦克每一个子弹
			this.DrawBullet(this.hero.bullets.get(i).x, this.hero.bullets.get(i).y, this.hero.bullets.get(i).direct, g);
		}
			}
		
		}	

		
		
		//绘制敌人坦克 并自动发射子弹	 注意着是在paint函数里面 这个函数总是在不停调用
		for(int i=0;i<this.Eheros.size();i++)
		{			
			ETank ehero=this.Eheros.get(i);//取出第一个坦克

			//启动该坦克的线程
			//这里创建的线程会创建一个子弹 但是由于子弹没有死亡 刷新的频率太快导致增加多个子弹 
			
			//DrawTank(ehero.getPositionX(),ehero.getPositionY(),ehero.getDirect(),g,ehero.type);//画出敌人坦克
			DrawTank(ehero.getPositionX(),ehero.getPositionY(),ehero.getDirect(),g,ehero.type);//画出敌人坦克
//			if(ehero.bullets.size()==0)
//			{
//				ehero.shout();
//			}
//	
			
			//遍历一辆坦克的所有子弹
			//判断子弹是否存在 保证绘制子弹不会出错
			
				if(ehero.bullets.size()>0)
				{				
				//System.out.println("子弹="+et.bullets.size());
				//System.out.println("子弹目前有 "+ ehero.bullets.size());
			for(int k=0;k<ehero.bullets.size();k++){
				//取出第一个坦克的所有子弹
				Bullet eb=ehero.bullets.get(k);
			
				if(eb.isLived&&eb!=null)
				{
				//System.out.println(ehero.bullets.get(k).x);
			this.DrawBullet(ehero.bullets.get(k).x, ehero.bullets.get(k).y, ehero.bullets.get(k).direct, g);
			//break;//敌人每次只发射一个子弹
				}
			else if(!eb.isLived&&eb!=null){
				//System.out.println("子弹已经死亡！");
				//eb.isLived=false;
				ehero.bullets.remove(eb);
				 
					}
			}
				}
				
			}
	

		
//			

			//this.DrawBullet(et.positionX,et.y, et.direct, g);
//			
			//Thread ets=new Thread(this.Ehero);
			//ets.start();//敌人自动启动
			//this.Ehero.shout(); //敌人开炮
//			
//			this.DrawBullet(this.ezidan.x, this.ezidan.y, this.ezidan.direct,g); //画出子弹
//			System.out.println( this.ezidan.y+"and"+this.ezidan.x);
	
		
}		
	//画坦克
	public void DrawTank(int x,int y,int direct,Graphics g,int type){
		if(type==0)
			g.setColor(this.hero.color);
		else if(type==1)
			g.setColor(this.Ehero.color);
		switch(direct){
		case 0://上
		g.fill3DRect(x,y, 10, 40, true);//左侧矩形
		g.fill3DRect(x+10, y+5, 20, 20, false);//中间矩形
		g.fill3DRect(x+30, y, 10, 40, true);//右侧矩形
		g.fillOval(x+14, y+10, 10, 10);//中间椭圆
		g.fillRect(x+17, y-9, 5, 20);//炮筒
		break;
		case 1://右
			g.fill3DRect(x,y, 40, 10, true);//上侧矩形
			g.fill3DRect(x+10, y+10, 20, 20, false);//中间矩形
			g.fill3DRect(x, y+30, 40, 10, true);//下侧矩形
			g.fillOval(x+15, y+15, 10, 10);//中间椭圆
			g.fillRect(x+25, y+18, 23, 5);//炮筒
			break;
		case 2://下
			g.fill3DRect(x,y, 10, 40, true);//左侧矩形
			g.fill3DRect(x+10, y+5, 20, 20, false);//中间矩形
			g.fill3DRect(x+30, y, 10, 40, true);//右侧矩形
			g.fillOval(x+14, y+10, 10, 10);//中间椭圆
			g.fillRect(x+17, y+20, 5, 20);//炮筒
			break;
		case 3://左
			g.fill3DRect(x,y, 40, 10, true);//上侧矩形
			g.fill3DRect(x+10, y+10, 20, 20, false);//中间矩形
			g.fill3DRect(x, y+30, 40, 10, true);//下侧矩形
			g.fillOval(x+15, y+15, 10, 10);//中间椭圆
			g.fillRect(x-3, y+18, 20, 5);//炮筒
			break;
			
		}

	}

	
	//画子弹
	public void DrawBullet(int x,int y,int direct,Graphics g){
		g.setColor(Color.red);
		switch(direct){
		case 0://上
		g.fill3DRect(x+18,y-20, 4,4, true);
		break;
		case 1://右
			g.fill3DRect(x+50,y+20, 4, 4, true);
			break;
		case 2://下
			g.fill3DRect(x+17,y+45,4, 4, true);
			break;
		case 3://左
			//g.fill3DRect(x-10,y+19, 4, 4, true);
			g.fillOval(x-10,y+19, 5, 5);
			break;
			
		}

	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(this.hero.isLive){//当坦克存活的时候才能操作
		if(e.getKeyCode()==KeyEvent.VK_UP){
			this.hero.moveUp();
		}
			else if(e.getKeyCode()==KeyEvent.VK_DOWN){
				
				this.hero.moveDown();
		}
			else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
				this.hero.moveRight();
		}	
			else if(e.getKeyCode()==KeyEvent.VK_LEFT){
				this.hero.moveLeft();
			}
			else if(e.getKeyCode()==KeyEvent.VK_SPACE)
			{
				//zidan=new ZiDan(this.x,this.y); //创建一个子弹
				//zidan=new ZiDan(this.x,this.y ,this.direct,this.speed);
//				Bullet b=new Bullet(this.x,this.y,2,this.speed);//创建一个向上的子弹 后面要分情况
//				this.hero.bullet.add(b);
				this.hero.shout();
//				System.out.println("此时坦克的坐标：x ="+this.hero.getPositionX()+" y="+this.hero.getPositionY());
				
		
			}
		}
		
		this.repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	//刷新面板线程
	public void run()
	{
	
		while(true)
		{
			try{
				
				Thread.sleep(50);
			}
			catch(Exception e)
			{
			}
			
			//这里做碰撞检测
			hitTest(hero);
			this.repaint();
			
				
		}

	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//碰撞检测 50毫秒执行一次
	//传入敌人的坦克和我方的坦克
	public void hitTest(MTank h){		
		
		//获取我方发射的所有子弹 检测是否击中敌机
		for(int i=0;i<h.bullets.size();i++)			
		{
			//获取子弹
			Bullet mb=h.bullets.get(i);
			//碰撞检测子弹
			if(mb.x<0||mb.x>600||mb.y<0||mb.y>400)
			{
				h.bullets.remove(mb);
				mb.isLived=false;//让这个子弹死亡
				}	
			
			//遍历敌机
			for(int k=0;k<Eheros.size();k++)									
			{
				ETank et=Eheros.get(k);//取出敌人的坦克
				
				//击中敌人
				if(mb.x>=et.positionX-20&&mb.x<=et.positionX+40&&mb.y>et.positionY-20&&mb.y<=et.positionY+40){
					{//击中敌人之后马上生成一个新敌人
					Ehero=new ETank(Color.CYAN);
					Ehero.setDirect(2);
					Ehero.setPositionX((int)(Math.random()*400));
					Ehero.setPositionY(0);
					Ehero.setSpeed(3);//设置敌人坦克初始速度		
					Thread t=new Thread(Ehero);
					t.start();//坦克自动运行
					//加入敌人的坦克组vector
					Eheros.add(Ehero);
					Ehero.shout();//创建一个子弹并启动线程  将子弹加入到向量
					}
					//从坦克组移除这架敌机并增加一个炸弹	
					Eheros.remove(et);
					//移除子弹
					this.hero.bullets.remove(mb);
					System.out.println("此时子弹的位置为：x ="+mb.x+" y="+mb.y);
					this.Bombs.add(new Bomb(et.positionX,et.positionY));//增加一个炸弹
					System.out.println("此时敌人坦克的坐标：x ="+et.getPositionX()+" y="+et.getPositionY());
					}	
			}	
		}
		//检测敌人坦克的子弹是否出界	
		for(int k=0;k<Eheros.size();k++)//遍历敌机
		{
			
			ETank et=Eheros.get(k);//取出敌人的坦克
			//System.out.println("子弹"+i+"目前的y	是："+mb.y);
		
			//遍历敌人的子弹
			//System.out.println("子弹开始时size()为"+et.bullets.size());
			for(int j=0;j<et.bullets.size();j++)
			{

				Bullet eb=et.bullets.get(j);
				if(eb.x<0||eb.x>700||eb.y<0||eb.y>400)
				{		
					eb.isLived=false; 		//子弹死亡
					et.bullets.remove(eb);	//移除向量
					et.shout();				//增加一个子弹
		
					
				}
						
			}
				
	}
		
		
		//检测敌人的坦克是否出界**
		for(int k=0;k<Eheros.size();k++)									
		{
			ETank et=Eheros.get(k);//取出敌人的坦克	
////////////////////////////////////////////////////////////////////////////////////////////////////
			//遍历敌人坦克的子弹 看看是否击中我方
			for(int b=0;b<et.bullets.size();b++)
			{
				Bullet eb=et.bullets.get(b);//获取敌人子弹
				if(eb.x>hero.positionX-20&&eb.x<hero.positionX+40&&eb.y>hero.positionY-20&&eb.y<hero.positionY+40){
					
					this.Bombs.add(new Bomb(hero.positionX,hero.positionY));//增加一个炸弹
					this.hero.isLive=false;
					//下面让这个坦克占据在画面的位置移除游戏区
					this.hero.setPositionX(-10000);
					this.hero.setPositionY(-10000);
	
					}
			}	
////////////////////////////////////////////////////////////////////////////////////////////////////
		//碰撞到墙壁改变方向 或者有概率中途改变
		if(et.positionX<=0||et.positionX>700||et.positionY<0||et.positionY>400||(int)(Math.random()*700)==666)	
		{
			switch(et.getDirect())
			{
			case 0:

				int r=(int)(Math.random()*4);
				if(r!=0)
				{
					et.setDirect(r);
					
					}
				else{

					et.setDirect(2);


				}

				for(int i=0;i<=10;i++) //增加一个稍微的延迟
				{
					
					//这是一个延迟器 保证坦克不会再转方向后马上判断又转向 因为如果碰到墙壁 马上转向 此时仍旧在碰撞中会继续转向 				
					//et.aotoMove();
//					
				et.setPositionX(et.getPositionX());
				et.setPositionY(et.getPositionY()+et.speed);
					
						
				}
			break;
			case 1:

				int r1=(int)(Math.random()*4);
				if(r1!=1)
				{
					et.setDirect(r1);

				}
				else
				{
					et.setDirect(3);

				}
				for(int i=0;i<=10;i++) //这是一个延迟效果 保证坦克在碰撞完毕后不会立即再次检测 导致出现无头苍蝇
				{
					//et.aotoMove();
		
					et.setPositionX(et.getPositionX()-et.speed);
					et.setPositionY(et.getPositionY());
				}
				break;
			case 2:

				int r2=(int)(Math.random()*4);
				if(r2!=2)
				{

					et.setDirect(r2);

				}
				else
				{

					et.setDirect(0);
				}
				for(int i=0;i<7;i++)
				{
					///et.aotoMove();
					//System.out.println("he");
					//et.aotoMove();
					et.setPositionX(et.getPositionX());
					et.setPositionY(et.getPositionY()-et.speed);
					//System.out.println("此时的方向是："+et.direct);
				}
				break;
			case 3:

				int r3=(int)(Math.random()*4);
				if( r3!=3)
				{
					et.setDirect(r3);

				}
				else{

					et.setDirect(1);

				}

				for(int i=0;i<=10;i++)
				{
					//et.aotoMove();

					et.setPositionX(et.getPositionX()+et.speed);
					et.setPositionY(et.getPositionY());
				}
				break;
				
				}

			}
////////////////////////////////////////////////////////////////////////////////////////////////////
			//敌人不能相互碰撞
		for(int k1=k+1;k1<Eheros.size();k1++){
			ETank et2=Eheros.get(k1);
					if(Math.abs(et.getPositionY()-et2.getPositionY())<=40&&Math.abs(et.getPositionX()-et2.getPositionX())<=40)//说明两个互相碰撞
					{
						switch(et.getDirect())
						{
						case 0:
						case 2:
							switch(et2.getDirect())
							{
							case 0:
							case 2:
								et2.setDirect(1);
								et.setDirect(3);
						break;
							case 1:
							case 3:
								et2.setDirect(0);
								et.setDirect(3);
						break;
							}
						break;
						
						case 1:
						case 3:
							switch(et2.getDirect())
							{
							case 0:
							case 2:
								et2.setDirect(0);
								et.setDirect(2);
						break;
							case 1:
							case 3:
								et2.setDirect(1);
								et.setDirect(3);
				
						break;
							}
							break;
						
						
						}
						
						try {
							Thread.sleep(40);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;						
					}	
	}			
			
///////////////////////////////////////////////////////////////////////////////////////////////////		
		}		
//end 检测敌人坦克是否越界问题***		
}
	}