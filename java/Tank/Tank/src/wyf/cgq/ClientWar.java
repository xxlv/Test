
package wyf.cgq;
import java.util.*;import java.awt.*;
import java.awt.event.*;import javax.swing.*;
import javax.swing.event.*;import java.io.*;
import java.util.ArrayList;
public class ClientWar extends JPanel{
	WarMap warmap=new WarMap();//������ͼ����
	Map<Integer,Bullet> m_bullet=new HashMap<Integer,Bullet>();//����ӵ���map����
	CPaintThread cpt;//���������̵߳�����
	TankClient father;//�����ͻ��������������
	Tank host_tank;//��������̹�˵�����
	Tank client_tank;//�����ͻ���̹�˵�����
	Tank[] bad_tank=new Tank[5];//��������̹�˵�����
	int bullet_id=0;//�����ӵ���id�� ����0��ʼ
	private boolean start=false;//��Ϸ״̬�ı�־λ��flase˵��û��ʼ����֮��ʼ
	public ClientWar(TankClient father){//������
		this.father=father;
		cpt=new CPaintThread(father);//���������߳�
		cpt.start();//�����û����߳�
		this.initialTank();//��ʼ��̹��
	}
	public void initialTank(){//��ʼ��̹�˵ķ���
		host_tank=new Tank(0,1,5,1,Color.RED,400,300,600);//����̹��
		client_tank=new Tank(1,1,5,1,Color.GREEN,400,500,600);//����̹��
		bad_tank[0]=new Tank(2,0,5,4,Color.BLUE,50,30,300);//����̹��
		bad_tank[1]=new Tank(3,0,5,2,Color.BLUE,50,30,30);
		bad_tank[2]=new Tank(4,0,5,2,Color.BLUE,50,300,30);
		bad_tank[3]=new Tank(5,0,5,2,Color.BLUE,50,720,30);
		bad_tank[4]=new Tank(6,0,5,3,Color.BLUE,50,710,300);
	}
	@Override
	public void paint(Graphics g){//��дpaint����
		if(start==true){//�����Ϸ�Ѿ���ʼ
			warmap.draws(g);//���Ƶ�ͼ
			if(host_tank.isLive()){//�������������
				host_tank.drawTank(g);//��������
			}
			if(client_tank.isLive()){//�������������
				this.sendMsg("<#CLIENT#>"+client_tank.getDirection()+"/"+client_tank.getX()+
				"/"+client_tank.getY());//��������״̬���͸�������
				client_tank.drawTank(g);//���Ƹ���
			}
			for(int i=0;i<bad_tank.length;i++){//���Ƶ���̹��
				bad_tank[i].drawTank(g);
			}
			ArrayList<Integer> list=new ArrayList<Integer>();//����һ��ArrayList����
			Set<Integer> keyset=this.m_bullet.keySet();//����ӵ��ļ����� 
			Iterator<Integer> ii=keyset.iterator();//��õ�����
			while(ii.hasNext()){//����������
				Integer i=ii.next();
				list.add(i);//��������ArrayList��
			}
			for(Integer key:list){//����ArrayList�����ӵ�
				Bullet bullet_temp=m_bullet.get(key);
				if(bullet_temp!=null){
					bullet_temp.drawBullet(g);
				}
			}
		}
		else{//��Ϸ��û�п�ʼ
			warmap.draws(g);//���Ƶ�ͼ
			host_tank.drawTank(g);//��������̹��
			client_tank.drawTank(g);//���ƿͻ���̹��
			for(int i=0;i<5;i++){//���Ƶ���̹��
				bad_tank[i].drawTank(g);
			}
		}
		Color c=g.getColor();//��û�����ɫ
		Font f=g.getFont();//��û�������
		g.setColor(new Color(255,0,0,100));//���û�����ɫ
		g.setFont(new Font("����",Font.BOLD,20));//���û�������
		g.drawString("����",30,20);//�����Ͻǻ���"����"�ַ���
		g.drawRect(80,5,400,15);//����һ�����ο�
		g.fill3DRect(80,5,host_tank.getBlood(),15,true);//��������Ѫ����������Ѫ��ͼ
		g.drawString("�÷�"+host_tank.getScore()+"",485,20);//���������ĵ÷�
		g.setColor(new Color(0,255,0,100));//���û�����ɫ
		g.drawString("����",30,40);//�����Ͻǻ���"����"�ַ���
		g.drawRect(80,25,400,15);//����һ�����ο�
		g.fill3DRect(80,25,client_tank.getBlood(),15,true);//���ݸ���Ѫ����������Ѫ��ͼ
		g.drawString("�÷�"+client_tank.getScore(),485,40);//���Ƹ����ĵ÷�
		if(host_tank.isLive()==false&&client_tank.isLive()==false){//���������������
			g.setFont(new Font("����",Font.BOLD,80));
			g.setColor(Color.RED);
			g.drawString("��Ϸ����",240,300);//������Ϸ������ʾ��Ϣ
		}
		g.setColor(c);//��ԭ������ɫ
		g.setFont(f);//��ԭ��������
	}
	public boolean canMove(Tank tank){//�ж�̹���Ƿ�����ƶ�
		int x=tank.getX();//���̹�˵�X����
		int y=tank.getY();//���̹�˵�Y����
		int d=tank.getDirection();//���̹�˷���
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
		){//���Խ���������ǽ��
			return false;//������ǰ��������false
		}
		else {
			if(tank.getId()==1){//����Ǹ���
				if(tank.getNextRec().intersects(host_tank.getRec())){
					return false;//����ƶ����������ײ���򷵻� false
				}
				else{
					for(int i=0;i<bad_tank.length;i++){
						if(tank.getNextRec().intersects(bad_tank[i].getRec())){
							return false;//��������̹����ײ���򷵻�false
						}
					}
				}
			}
			
		}
		return true;//�����ƶ�������true
	}
	public void updateHost(int direction,int blood,int x,int y){//��������̹��
		host_tank.setDirection(direction);//���·���
		host_tank.setBlood(blood);//����Ѫ��
		host_tank.setX(x);//����X Y ����
		host_tank.setY(y);
	}
	public void setStart(boolean start){//������Ϸ״̬�ķ���
		this.start=start;
	}
	public void setClientDir(int direction){//�ı丱���ķ���
		if(start==true){//�����Ϸ�Ѿ���ʼ�ſɸı�
			client_tank.setDirection(direction);
		}
	}
	public void clientMove(){//������ǰ�ƶ�
		if(this.canMove(this.client_tank)&&start==true){
			client_tank.move();//�����Ϸ��ʼ���ҿ����ƶ����ƶ�
		}
	}
	public void addBullet(int id,int x,int y){//�����ӵ�
		Bullet bullet=new Bullet(id,x,y);
		this.m_bullet.put(id,bullet);//���������ӵ�����m_bullet��
	}
	public void updateBullet(int id,int x,int y){//�����ӵ�
		Bullet bullet=this.m_bullet.get(id);//���Ҫ���µ��ӵ�
		if(bullet!=null){
			bullet.setX(x);//�����ӵ���λ�� 
			bullet.setY(y);
		}
	}
	public void clientFire(){//���������ӵ�
		if(client_tank.isLive()&&start==true){//�����������������Ϸ�Ѿ���ʼ
			int direction=client_tank.getDirection();//���̹�˵ķ���
			int[] x_y=client_tank.getXY();//����ӵ��ķ������ʼλ��
			this.sendMsg("<#FIRECLIENT#>"+direction+"/"+x_y[0]+"/"+x_y[1]);//������������Ϣ
		}
	}
	public void updateTank(int number,int direction,int x,int y){//���µ���̹��
		this.bad_tank[number].setDirection(direction);//���·���
		this.bad_tank[number].setX(x);//����λ��
		this.bad_tank[number].setY(y);
	}
	public void removeBullet(int id){
		this.m_bullet.remove(id);//���ӵ�������ɾ���ӵ�
	}
	public void addScore(int id){//���̹�˼ӷ�
		if(id==0){//������̹�˼ӷ�
			host_tank.setScore(host_tank.getScore()+5);
			if(host_tank.getBlood()+5>400){//���Ѫ����5�κ�ᳬ�������
				host_tank.setBlood(400);//��Ѫ������Ϊ400
			}
			else{//Ѫ��û�г���400��������5��
				host_tank.setBlood(host_tank.getBlood()+5);
			}		
		}
		else{
			client_tank.setScore(client_tank.getScore()+5);//�������ӷ�
			if(client_tank.getBlood()+5>400){//���Ѫ����5�κ�ᳬ�������
				client_tank.setBlood(400);//��Ѫ������Ϊ400
			}
			else{//Ѫ��û�г���400��������5��
				client_tank.setBlood(client_tank.getBlood()+5);
			}
		}
	}
	public void subBlood(int id,int subnum){//����յ�����������Ѫ��
		if(id==0){//���������
			host_tank.setBlood(host_tank.getBlood()-subnum);//����Ѫ��
			if(host_tank.getBlood()<=0){//Ѫ���Ƿ񻹴���0
				host_tank.setLive(false);//����������Ϊ����״̬
				host_tank.setBlood(0);//��Ѫ��Ϊ0
			}
		}
		else{//������Ѫ
			client_tank.setBlood(client_tank.getBlood()-subnum);
			if(client_tank.getBlood()<=0){//���Ѫ��С��0����̹����Ϊ����״̬
				client_tank.setLive(false);
				client_tank.setBlood(0);
			}
		}
	}
	public void sendMsg(String msg){//�����������������Ϣ�ķ���
		try{
			father.cat.dout.writeUTF(msg);//������Ϣ
		}
		catch(IOException e){e.printStackTrace();}
	}
}