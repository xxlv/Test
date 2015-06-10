
package wyf.cgq;
import java.util.*;import java.awt.*;
import java.awt.event.*;import javax.swing.*;
import javax.swing.event.*;
import wyf.cgq.PaintThread;

import java.io.*;
public class ServerWar extends JPanel{
	public static final int HCBulletPower=100;//�������̹���ӵ���ɱ����
	public static final int badPower=50;//���õ���̹�˵��ӵ�ɱ����
	public static final int BulletSpeed=15;//�����ӵ����ٶ�
	WarMap warmap=new WarMap();//������ͼ����
	Map<Integer,Bullet> m_bullet=new HashMap<Integer,Bullet>();//��������ӵ���map
	PaintThread pt;//���������߳�����
	TankServer father;//�������������������
	Tank host_tank;//��������̹�˵�����
	Tank client_tank;//�����ͻ���̹�˵�����
	Tank[] bad_tank=new Tank[5];//��������̹������
	int bullet_id=0;//�����ӵ��ĳ�ʼid��
	private boolean start=false;//��Ϸ״̬��־λ
	public ServerWar(TankServer father){
		this.father=father;
		pt=new PaintThread(father);
		pt.start();//���������̲߳�����
		this.initialTank();//��ʼ��̹��
	}
	public void initialTank(){//��ʼ��̹�˵ķ���
		host_tank=new Tank(0,1,5,1,new Color(255,0,0,200),400,300,600);//����̹��
		client_tank=new Tank(1,1,5,1,new Color(0,255,0,200),400,500,600);//����̹��
		bad_tank[0]=new Tank(2,0,5,4,new Color(0,0,255,200),50,30,300);//����̹��
		bad_tank[1]=new Tank(3,0,5,2,new Color(0,0,255,200),50,30,30);
		bad_tank[2]=new Tank(4,0,5,2,new Color(0,0,255,200),50,300,30);
		bad_tank[3]=new Tank(5,0,5,2,new Color(0,0,255,200),50,720,30);
		bad_tank[4]=new Tank(6,0,5,3,new Color(0,0,255,200),50,710,300);
	}
	@Override
	public void paint(Graphics g){//��д��run����
		if(start==true){//�����Ϸ�Ѿ���ʼ
			warmap.draws(g);//���Ƶ�ͼ
			this.sendMsg("<#HOST#>"+host_tank.getDirection()+"/"+host_tank.getBlood()+
					    "/"+host_tank.getX()+"/"+host_tank.getY());//��ͻ��˴���������Ϣ
			if(host_tank.isLive()){
				host_tank.drawTank(g);//��������
		    }     					                         
			if(client_tank.isLive()){
				client_tank.drawTank(g);//���Ƹ���
			}
			for(int i=0;i<bad_tank.length;i++){//���Ƶ���̹��
				if(Math.random()>0.8){//����һ���ĸ����漴�ı����̹�˵ķ���
					bad_tank[i].setDirection(new Random().nextInt(4)+1);
				}
				if(Math.random()>0.90){//����һ���ĸ����õ���̹�˷����ӵ�
					this.tankFire(bad_tank[i]);
				}
				if(this.canMove(bad_tank[i])){//��������ƶ����ƶ�����̹��
					bad_tank[i].move();
				}
				else{//�����ƶ�������ı��䷽��
					bad_tank[i].setDirection(new Random().nextInt(4)+1);
				}
				this.sendMsg("<#BADTANK#>"+i+"/"+bad_tank[i].getDirection()+"/"+
				bad_tank[i].getX()+"/"+bad_tank[i].getY());//��ͻ��˷��͵���̹�˵���Ϣ
				bad_tank[i].drawTank(g);//���Ƹõ���̹��
			}
			java.util.ArrayList<Integer> list=new ArrayList<Integer>();//����ArrayList����
			Set<Integer> keySet=m_bullet.keySet();//����ӵ��ļ��ļ���
			Iterator<Integer> ii=keySet.iterator();//���keyset�ĵ�����
			while(ii.hasNext()){//����������,���ӵ��ļ�����list
				Integer i=ii.next();
				list.add(i);
			}
			for(Integer i:list){//����list
				Bullet bullet_temp=m_bullet.get(i);//ȡ��һ���ӵ�
				if(bullet_temp==null){
					continue;//�����null�������һ��ѭ��
				}
				if(isAimed(bullet_temp)){
					continue;//����ӵ�����ǽ�ڻ����̹���������һ��ѭ��
				}				
				bullet_temp.move();//�ƶ����ӵ�
				sendMsg("<#UPDATEBULLET#>"+bullet_temp.getId()+"/"+
				        bullet_temp.getX()+"/"+bullet_temp.getY());//����Ϣ���͸��ͻ���
				bullet_temp.drawBullet(g);//�����ӵ�
			}
			for(int i=0;i<bad_tank.length;i++){
				if(bad_tank[i].isLive()==false){//���°����Ѿ������ĵ���̹��
					this.replace(bad_tank[i]);
					this.sendMsg("<#BADTANK#>"+i+"/"+bad_tank[i].getDirection()+"/"+
				    bad_tank[i].getX()+"/"+bad_tank[i].getY());//����Ϣ���͸��ͻ���
				}
			}
		}
		else{//�����Ϸ��û�п�ʼ
			warmap.draws(g);//���Ƶ�ͼ
			host_tank.drawTank(g);//��������̹��
			client_tank.drawTank(g);//���Ƹ���̹��
			for(int i=0;i<bad_tank.length;i++){//���Ƶ���̹��
				bad_tank[i].drawTank(g);
			}
		}
		Color c=g.getColor();//��û�����ɫ
		Font f=g.getFont();//��û�������
		g.setColor(new Color(255,0,0,100));//���û�����ɫ
		g.setFont(new Font("����",Font.BOLD,20));//��������
		g.drawString("����",30,20);//����"����"�ַ���
		g.drawRect(80,5,400,15);//���ƾ��ο�
		g.fill3DRect(80,5,host_tank.getBlood(),15,true);//��������Ѫ������Ѫ����Ϣ
		g.drawString("�÷�"+host_tank.getScore()+"",485,20);//���������÷���Ϣ
		g.setColor(new Color(0,255,0,100));//���û�����ɫ
		g.drawString("����",30,40);//����"����"�ַ���
		g.drawRect(80,25,400,15);//���ƾ��ο�
		g.fill3DRect(80,25,client_tank.getBlood(),15,true);//���ݸ���Ѫ������Ѫ����Ϣ
		g.drawString("�÷�"+client_tank.getScore(),485,40);//���Ƹ����÷���Ϣ
		if(host_tank.isLive()==false&&client_tank.isLive()==false){//������Ϸ��������ʾ��Ϣ
			g.setFont(new Font("����",Font.BOLD,80));
			g.setColor(Color.RED);
			g.drawString("��Ϸ����",240,300);//������Ϸ������ʾ��Ϣ
		}
		g.setColor(c);//��ԭ������ɫ
		g.setFont(f);//��ԭ��������
	}
	public boolean isAimed(Bullet bullet){//�ж��ӵ��Ƿ��������ķ��� 
		int tank_id=bullet.getTank_id();//��ø��ӵ�������̹�˵�id��
		int id=bullet.getId();//����ӵ���id
		if(tank_id<2){//tank_idС��2˵������ҵ�̹�˵��ӵ�
			for(int i=0;i<bad_tank.length;i++){
				if(bullet.getRec().intersects(bad_tank[i].getRec())&&bad_tank[i].isLive()==true){
					//��������̹�ˣ�������е���̹�ˣ����Ƴ����ӵ�
					this.sendMsg("<#REMOVEBULLET#>"+id);//��ͻ��˷����Ƴ��ӵ�����Ϣ
					bad_tank[i].setLive(false);//�������е�̹��״̬��Ϊ����
					if(tank_id==0){//��������ʱ
						this.sendMsg("<#ADDSCORE#>"+0);//��ͻ��˷��ͼӷ���Ϣ
						host_tank.setScore(host_tank.getScore()+5);//�����˸������ӷ�
						if(host_tank.getBlood()+5>400){//��������Ѫ
							host_tank.setBlood(400);
						}
						else{
							host_tank.setBlood(host_tank.getBlood()+5);
						}
					}
					else{//���Ǹ���ʱ
						this.sendMsg("<#ADDSCORE#>"+1);//��ͻ��˷��ͼӷ���Ϣ
						client_tank.setScore(client_tank.getScore()+5);//�����˸������ӷ�
						if(client_tank.getBlood()+5>400){//��������Ѫ
							client_tank.setBlood(400);
						}
						else{
							client_tank.setBlood(client_tank.getBlood()+5);
						}
					}
					this.m_bullet.remove(id);//���ӵ��б����Ƴ����ӵ�
					return true;
				}
			}
		}
		if(tank_id>=2){//tank_id����1˵���ǵ���̹�˵��ӵ�
			if(bullet.getRec().intersects(host_tank.getRec())&&host_tank.isLive()){//��������
				this.sendMsg("<#REMOVEBULLET#>"+id);//��ͻ��˷����Ƴ��ӵ�����Ϣ
				this.sendMsg("<#SUBBLOOD#>"+0+"/"+bullet.getPower());//��ͻ��˷��ͼ�Ѫ����Ϣ
				host_tank.setBlood(host_tank.getBlood()-bullet.getPower());//������Ѫ
				if(host_tank.getBlood()<=0){
					host_tank.setLive(false);//���ʣ��Ѫ��С�ڵ���0��������Ϊ����״̬
					host_tank.setBlood(0);//��Ѫ��Ϊ0
				}
				this.m_bullet.remove(id);//�Ƴ����ӵ�
				return true;
			}
			else if(bullet.getRec().intersects(client_tank.getRec())&&client_tank.isLive()){//���и���
				this.sendMsg("<#REMOVEBULLET#>"+id);//��ͻ��˷����Ƴ��ӵ�����Ϣ
				this.sendMsg("<#SUBBLOOD#>"+1+"/"+bullet.getPower());//��ͻ��˷��ͼ�Ѫ����Ϣ
				client_tank.setBlood(client_tank.getBlood()-bullet.getPower());//������Ѫ
				if(client_tank.getBlood()<=0){
					client_tank.setLive(false);//�������ʣ��Ѫ��С�ڵ���0��������Ϊ����״̬
					client_tank.setBlood(0);//��Ѫ��Ϊ0
				}
				this.m_bullet.remove(id);//�Ƴ����ӵ�
				return true;
			}
		}
		if(bullet.getX()<0||bullet.getX()>this.getWidth()||
		   bullet.getY()<0||bullet.getY()>this.getHeight()||
		   warmap.getState(bullet.getX()/20,bullet.getY()/20)==1
		  ){//�ӵ�Խ�������ǽ��
		  		this.sendMsg("<#REMOVEBULLET#>"+id);//��ͻ��˷����Ƴ��ӵ�����Ϣ
		  		this.m_bullet.remove(id);//�Ƴ����ӵ�
		  		return true;
		  }
		return false;
	}
	public void replace(Tank tank){
		int pos=new Random().nextInt(5)+1;//�������һ��λ�ñ��
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
				this.replace(tank);//�����λ������������̹�ˣ���������λ��
			}
		}
		if(tank.getRec().intersects(host_tank.getRec())){
			this.replace(tank);//�����λ��������̹�ˣ���������λ��
		}
		if(tank.getRec().intersects(client_tank.getRec())){
			this.replace(tank);//�����λ���и���̹�ˣ���������λ��
		}
	}
	public boolean canMove(Tank tank){//�ж�̹���Ƿ�����ƶ��ķ���
		int x=tank.getX();//��ø�̹�˵�λ��
		int y=tank.getY();
		int d=tank.getDirection();//��ø�̹�˵ķ���
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
		){//���Խ�������ǽ���򷵻�false
			return false;
		}
		else {
			if(tank.getId()==0){//���������
				if(tank.getNextRec().intersects(client_tank.getRec())){
					return false;//���ǰ���и����������򷵻�false
				}
				else{//�Ƿ��е���̹������
					for(int i=0;i<bad_tank.length;i++){
						if(tank.getNextRec().intersects(bad_tank[i].getRec())){
							return false;//����е���̹���������򷵻�false
						}
					}
				}
			}
			else if(tank.getId()>1){//����ǵ���̹��
				if(tank.getNextRec().intersects(host_tank.getRec())){
					return false;//����������������򷵻�false
				}
				else if(tank.getNextRec().intersects(client_tank.getRec())){
					return false;//����и����������򷵻�false
				}
				else{
					for(int i=0;i<bad_tank.length;i++){
						if(bad_tank[i].getId()!=tank.getId()){
							if(tank.getNextRec().intersects(bad_tank[i].getRec())){
								return false;//�������������̹���������򷵻�false
							}
						}
					}
				}
			}
			return true;
		}
	}
	public void setStart(boolean start){
		this.start=start;//������Ϸ״̬�ķ���
	}
	public void setHostDir(int direction){
		if(this.start==true){
			host_tank.setDirection(direction);//��������̹�˵ķ���
		}
	}
	public void hostMove(){	
		if(this.canMove(host_tank)&&(this.start==true)){
			host_tank.move();//�����ƶ�
		}
	}
	public void updateClient(int direction,int x,int y){//���¸�����λ�úͷ���
		client_tank.setDirection(direction);//���·���
		client_tank.setX(x);//����λ��
		client_tank.setY(y);
	}
	public void hostFire(){//���������ӵ��ķ���
		if(host_tank.isLive()&&this.start==true){//���������������Ϸ�Ѿ���ʼ
			int id=++this.bullet_id%200;//����һ���ӵ�id�� 
			int tank_id=host_tank.getId();//���������id
			int style=host_tank.getStyle();//�������������
			int direction=host_tank.getDirection();//��������ķ���
			int[] bullet_x_y=host_tank.getXY();//����ӵ�����ʼλ��
			//��ͻ��˷�������ӵ�����Ϣ
			this.sendMsg("<#BULLET#>"+id+"/"+bullet_x_y[0]+"/"+bullet_x_y[1]);
			Bullet bullet=new Bullet(id,tank_id,style,direction,
			            BulletSpeed,HCBulletPower,bullet_x_y[0],bullet_x_y[1]);
			this.m_bullet.put(id,bullet);//�����ӵ�����ӵ��ӵ��б���
		}
	}
	public void clientFire(int direction,int x,int y){//�ͻ��������ӵ��ķ���
		int id=++this.bullet_id%200;//����һ���ӵ�id�� 
		int tank_id=client_tank.getId();//��ø�����id
		int style=client_tank.getStyle();//��ø���������
		this.sendMsg("<#BULLET#>"+id+"/"+x+"/"+y);//��ͻ��˷�������ӵ�����Ϣ
		Bullet bullet=new Bullet(id,tank_id,style,direction,
		            BulletSpeed,HCBulletPower,x,y);
		this.m_bullet.put(id,bullet);//�����ӵ�����ӵ��ӵ��б���
	}
	public void tankFire(Tank tank){//����̹�˷����ӵ�
		int id=++this.bullet_id%200;//����һ���ӵ�id�� 
		int tank_id=tank.getId();//���̹�˵�id��
		int style=tank.getStyle();//���̹�˵�����
		int direction=tank.getDirection();//���̹�˵ķ���
		int[] bullet_x_y=tank.getXY();//����ӵ���λ��
		//��ͻ��˷�������ӵ�����Ϣ
		this.sendMsg("<#BULLET#>"+id+"/"+bullet_x_y[0]+"/"+bullet_x_y[1]);
		Bullet bullet=new Bullet(id,tank_id,style,direction,
		            BulletSpeed,badPower,bullet_x_y[0],bullet_x_y[1]);
		this.m_bullet.put(id,bullet);//�����ӵ�����ӵ��ӵ��б���
	}
	public void sendMsg(String msg){//��ͻ��˷�����Ϣ�ķ���
		try{
			father.sat.dout.writeUTF(msg);
		}
		catch(IOException e){e.printStackTrace();}
	}
}
