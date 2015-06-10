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
 * 11��7�������¼��� �õ���̹�˻����Լ���̹��ʱ���Լ�̹����ʧ
 * 
 * 
 * */
class MyPanel extends JPanel implements KeyListener,Runnable{
	
	int times=0;
	Vector<ETank> Eheros = new Vector<ETank>();//����һ��vector��ŵ���̹��	
	Vector<Bomb> Bombs= new Vector<Bomb>();//����һ��vector���ը��	
	Bullet bullet = null;//�ӵ�����	
	MTank hero=null; //�ҷ�Ӣ��
	ETank Ehero=null; //����Ӣ��
	//Bullet  zidan=null; 
	Bullet  ezidan=null;
	int EheroNumber=4;//��������
	//int x=20;//��ʼxλ��
	//int y=20;//��ʼyλ��
	//int speed=20;//�ٶ�
	//int direct=0;//����
	//boolean SHOUT=false;
	//Graphics _G=null;//���滭��

  	
	//ը��ͼƬ���
	Image image1=null;
	Image image2=null;
	Image image3=null;
	
	//���캯����ʼ�����
	  public MyPanel(){	
		//��ʼ���ҵ�̹��
		hero= new MTank();
		hero.setDirect(0);//���ó�ʼ����
		hero.setSpeed(10);//���ó�ʼ�ٶ�
		hero.setPositionX(400);//���ó�ʼλ��x
		hero.setPositionY(300);//���ó�ʼλ��y
		
		//������̹�˼��뼯��
		for(int i=0;i<this.EheroNumber;i++)
		{		
			Ehero=new ETank(Color.red);
			Ehero.setDirect(2);
			Ehero.setPositionX((1+i)*150);
			Ehero.setPositionY(0);
			Ehero.setSpeed(3);//���õ���̹�˳�ʼ�ٶ�		
			Thread t=new Thread(Ehero);
			t.start(); 		//̹���Զ�����
			//������˵�̹����vector
//			Bullet zd= new Bullet (Ehero.getPositionX(),Ehero.getPositionY(),0,Ehero.bulletSpeed); 
//			Ehero.bullets.add(zd);		
			Eheros.add(Ehero);
			Ehero.shout();//����һ���ӵ��������߳�  ���ӵ����뵽����

			
		}
		
		///��ʼ��ը��
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
		g.setColor(Color.black);//����Ļ����ɫ
		g.fill3DRect(0, 0, 800, 460, false); //����Ļ����
		if(this.hero.isLive&&this.hero!=null)
		DrawTank(this.hero.getPositionX(),this.hero.getPositionY(),this.hero.getDirect(),g,this.hero.type);//����̹��
		
		for(int b=0;b<this.Bombs.size();b++){	
		Bomb bb=this.Bombs.get(b);
		if(this.Bombs.size()>0){
			
			if(bb.life>6&&bb.life<=9)
			{
				//System.out.print("��ը������ը��");
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
		
		//���û��û�space��ʱ�� �ӵ��Ų�Ϊ��  ��ʱ�����ӵ�
		if(this.hero.bullets.size()>0){
			
		for(int i=0;i<this.hero.bullets.size();i++)
		{
	
			if(this.hero.bullets.get(i)!=null&&this.hero.bullets.get(i).isLived){
			//�����ҷ�̹��ÿһ���ӵ�
			this.DrawBullet(this.hero.bullets.get(i).x, this.hero.bullets.get(i).y, this.hero.bullets.get(i).direct, g);
		}
			}
		
		}	

		
		
		//���Ƶ���̹�� ���Զ������ӵ�	 ע��������paint�������� ������������ڲ�ͣ����
		for(int i=0;i<this.Eheros.size();i++)
		{			
			ETank ehero=this.Eheros.get(i);//ȡ����һ��̹��

			//������̹�˵��߳�
			//���ﴴ�����̻߳ᴴ��һ���ӵ� ���������ӵ�û������ ˢ�µ�Ƶ��̫�쵼�����Ӷ���ӵ� 
			
			//DrawTank(ehero.getPositionX(),ehero.getPositionY(),ehero.getDirect(),g,ehero.type);//��������̹��
			DrawTank(ehero.getPositionX(),ehero.getPositionY(),ehero.getDirect(),g,ehero.type);//��������̹��
//			if(ehero.bullets.size()==0)
//			{
//				ehero.shout();
//			}
//	
			
			//����һ��̹�˵������ӵ�
			//�ж��ӵ��Ƿ���� ��֤�����ӵ��������
			
				if(ehero.bullets.size()>0)
				{				
				//System.out.println("�ӵ�="+et.bullets.size());
				//System.out.println("�ӵ�Ŀǰ�� "+ ehero.bullets.size());
			for(int k=0;k<ehero.bullets.size();k++){
				//ȡ����һ��̹�˵������ӵ�
				Bullet eb=ehero.bullets.get(k);
			
				if(eb.isLived&&eb!=null)
				{
				//System.out.println(ehero.bullets.get(k).x);
			this.DrawBullet(ehero.bullets.get(k).x, ehero.bullets.get(k).y, ehero.bullets.get(k).direct, g);
			//break;//����ÿ��ֻ����һ���ӵ�
				}
			else if(!eb.isLived&&eb!=null){
				//System.out.println("�ӵ��Ѿ�������");
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
			//ets.start();//�����Զ�����
			//this.Ehero.shout(); //���˿���
//			
//			this.DrawBullet(this.ezidan.x, this.ezidan.y, this.ezidan.direct,g); //�����ӵ�
//			System.out.println( this.ezidan.y+"and"+this.ezidan.x);
	
		
}		
	//��̹��
	public void DrawTank(int x,int y,int direct,Graphics g,int type){
		if(type==0)
			g.setColor(this.hero.color);
		else if(type==1)
			g.setColor(this.Ehero.color);
		switch(direct){
		case 0://��
		g.fill3DRect(x,y, 10, 40, true);//������
		g.fill3DRect(x+10, y+5, 20, 20, false);//�м����
		g.fill3DRect(x+30, y, 10, 40, true);//�Ҳ����
		g.fillOval(x+14, y+10, 10, 10);//�м���Բ
		g.fillRect(x+17, y-9, 5, 20);//��Ͳ
		break;
		case 1://��
			g.fill3DRect(x,y, 40, 10, true);//�ϲ����
			g.fill3DRect(x+10, y+10, 20, 20, false);//�м����
			g.fill3DRect(x, y+30, 40, 10, true);//�²����
			g.fillOval(x+15, y+15, 10, 10);//�м���Բ
			g.fillRect(x+25, y+18, 23, 5);//��Ͳ
			break;
		case 2://��
			g.fill3DRect(x,y, 10, 40, true);//������
			g.fill3DRect(x+10, y+5, 20, 20, false);//�м����
			g.fill3DRect(x+30, y, 10, 40, true);//�Ҳ����
			g.fillOval(x+14, y+10, 10, 10);//�м���Բ
			g.fillRect(x+17, y+20, 5, 20);//��Ͳ
			break;
		case 3://��
			g.fill3DRect(x,y, 40, 10, true);//�ϲ����
			g.fill3DRect(x+10, y+10, 20, 20, false);//�м����
			g.fill3DRect(x, y+30, 40, 10, true);//�²����
			g.fillOval(x+15, y+15, 10, 10);//�м���Բ
			g.fillRect(x-3, y+18, 20, 5);//��Ͳ
			break;
			
		}

	}

	
	//���ӵ�
	public void DrawBullet(int x,int y,int direct,Graphics g){
		g.setColor(Color.red);
		switch(direct){
		case 0://��
		g.fill3DRect(x+18,y-20, 4,4, true);
		break;
		case 1://��
			g.fill3DRect(x+50,y+20, 4, 4, true);
			break;
		case 2://��
			g.fill3DRect(x+17,y+45,4, 4, true);
			break;
		case 3://��
			//g.fill3DRect(x-10,y+19, 4, 4, true);
			g.fillOval(x-10,y+19, 5, 5);
			break;
			
		}

	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(this.hero.isLive){//��̹�˴���ʱ����ܲ���
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
				//zidan=new ZiDan(this.x,this.y); //����һ���ӵ�
				//zidan=new ZiDan(this.x,this.y ,this.direct,this.speed);
//				Bullet b=new Bullet(this.x,this.y,2,this.speed);//����һ�����ϵ��ӵ� ����Ҫ�����
//				this.hero.bullet.add(b);
				this.hero.shout();
//				System.out.println("��ʱ̹�˵����꣺x ="+this.hero.getPositionX()+" y="+this.hero.getPositionY());
				
		
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
	//ˢ������߳�
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
			
			//��������ײ���
			hitTest(hero);
			this.repaint();
			
				
		}

	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//��ײ��� 50����ִ��һ��
	//������˵�̹�˺��ҷ���̹��
	public void hitTest(MTank h){		
		
		//��ȡ�ҷ�����������ӵ� ����Ƿ���ел�
		for(int i=0;i<h.bullets.size();i++)			
		{
			//��ȡ�ӵ�
			Bullet mb=h.bullets.get(i);
			//��ײ����ӵ�
			if(mb.x<0||mb.x>600||mb.y<0||mb.y>400)
			{
				h.bullets.remove(mb);
				mb.isLived=false;//������ӵ�����
				}	
			
			//�����л�
			for(int k=0;k<Eheros.size();k++)									
			{
				ETank et=Eheros.get(k);//ȡ�����˵�̹��
				
				//���е���
				if(mb.x>=et.positionX-20&&mb.x<=et.positionX+40&&mb.y>et.positionY-20&&mb.y<=et.positionY+40){
					{//���е���֮����������һ���µ���
					Ehero=new ETank(Color.CYAN);
					Ehero.setDirect(2);
					Ehero.setPositionX((int)(Math.random()*400));
					Ehero.setPositionY(0);
					Ehero.setSpeed(3);//���õ���̹�˳�ʼ�ٶ�		
					Thread t=new Thread(Ehero);
					t.start();//̹���Զ�����
					//������˵�̹����vector
					Eheros.add(Ehero);
					Ehero.shout();//����һ���ӵ��������߳�  ���ӵ����뵽����
					}
					//��̹�����Ƴ���ܵл�������һ��ը��	
					Eheros.remove(et);
					//�Ƴ��ӵ�
					this.hero.bullets.remove(mb);
					System.out.println("��ʱ�ӵ���λ��Ϊ��x ="+mb.x+" y="+mb.y);
					this.Bombs.add(new Bomb(et.positionX,et.positionY));//����һ��ը��
					System.out.println("��ʱ����̹�˵����꣺x ="+et.getPositionX()+" y="+et.getPositionY());
					}	
			}	
		}
		//������̹�˵��ӵ��Ƿ����	
		for(int k=0;k<Eheros.size();k++)//�����л�
		{
			
			ETank et=Eheros.get(k);//ȡ�����˵�̹��
			//System.out.println("�ӵ�"+i+"Ŀǰ��y	�ǣ�"+mb.y);
		
			//�������˵��ӵ�
			//System.out.println("�ӵ���ʼʱsize()Ϊ"+et.bullets.size());
			for(int j=0;j<et.bullets.size();j++)
			{

				Bullet eb=et.bullets.get(j);
				if(eb.x<0||eb.x>700||eb.y<0||eb.y>400)
				{		
					eb.isLived=false; 		//�ӵ�����
					et.bullets.remove(eb);	//�Ƴ�����
					et.shout();				//����һ���ӵ�
		
					
				}
						
			}
				
	}
		
		
		//�����˵�̹���Ƿ����**
		for(int k=0;k<Eheros.size();k++)									
		{
			ETank et=Eheros.get(k);//ȡ�����˵�̹��	
////////////////////////////////////////////////////////////////////////////////////////////////////
			//��������̹�˵��ӵ� �����Ƿ�����ҷ�
			for(int b=0;b<et.bullets.size();b++)
			{
				Bullet eb=et.bullets.get(b);//��ȡ�����ӵ�
				if(eb.x>hero.positionX-20&&eb.x<hero.positionX+40&&eb.y>hero.positionY-20&&eb.y<hero.positionY+40){
					
					this.Bombs.add(new Bomb(hero.positionX,hero.positionY));//����һ��ը��
					this.hero.isLive=false;
					//���������̹��ռ���ڻ����λ���Ƴ���Ϸ��
					this.hero.setPositionX(-10000);
					this.hero.setPositionY(-10000);
	
					}
			}	
////////////////////////////////////////////////////////////////////////////////////////////////////
		//��ײ��ǽ�ڸı䷽�� �����и�����;�ı�
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

				for(int i=0;i<=10;i++) //����һ����΢���ӳ�
				{
					
					//����һ���ӳ��� ��֤̹�˲�����ת����������ж���ת�� ��Ϊ�������ǽ�� ����ת�� ��ʱ�Ծ�����ײ�л����ת�� 				
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
				for(int i=0;i<=10;i++) //����һ���ӳ�Ч�� ��֤̹������ײ��Ϻ󲻻������ٴμ�� ���³�����ͷ��Ӭ
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
					//System.out.println("��ʱ�ķ����ǣ�"+et.direct);
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
			//���˲����໥��ײ
		for(int k1=k+1;k1<Eheros.size();k1++){
			ETank et2=Eheros.get(k1);
					if(Math.abs(et.getPositionY()-et2.getPositionY())<=40&&Math.abs(et.getPositionX()-et2.getPositionX())<=40)//˵������������ײ
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
//end ������̹���Ƿ�Խ������***		
}
	}