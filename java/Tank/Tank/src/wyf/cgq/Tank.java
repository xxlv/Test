
package wyf.cgq;
import java.awt.*;import java.awt.event.*;import javax.swing.*;
import javax.swing.event.*;import java.util.*;
public class Tank{
	private int style;//̹�˵�����:0�ǵ���̹��1�����̹��
	private int id;//̹�˱��0������̹��1�Ǹ���̹��2~6�ǵ���̹��
	private int speed;//̹�˵��ٶ�
	private int direction;//1,2,3,4�ֱ��ʾ��������
	private Color color;//̹�˵���ɫ
	private boolean live=true;//̹�˵�״̬��true��ʾ���ţ�false��ʾ����
	private int blood;//̹�˵�Ѫ��
	private int tank_x;//̹�˵�λ��
	private int tank_y;
	private int score=0;//̹�˵ĵ÷�
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
		if(this.live==true){//���̹�˻���
			//�����ƶ�̹�˸���
			if(this.direction==1){tank_y=tank_y-speed;}//����
			else if(this.direction==2){tank_y=tank_y+speed;}//����
			else if(this.direction==3){tank_x=tank_x-speed;}//����
			else if(this.direction==4){tank_x=tank_x+speed;}//����
		}
	}
	public int[] getXY(){//����ӵ���ʼλ�õķ���
		int[] x_y=new int[2];//���ڴ���ӵ���ʼ����
		if(this.direction==1){x_y[0]=this.tank_x+15;x_y[1]=this.tank_y-15;}
		else if(this.direction==2){x_y[0]=this.tank_x+15;x_y[1]=this.tank_y+40+15;}
		else if(this.direction==3){x_y[0]=this.tank_x-15;x_y[1]=this.tank_y+15;}
		else if(this.direction==4){x_y[0]=this.tank_x+40+15;x_y[1]=this.tank_y+15;}
		return x_y;
	}
	public void drawTank(Graphics g){//����̹�˵ķ���
		Color c=g.getColor();//��û�����ɫ
		if(this.id==0){color=new Color(255,0,0,100);}//������������ɫ	
		else if(this.id==1){color=new Color(0,255,0,100);}//���ø�������ɫ	
		else{color=new Color(0,0,255,100);}//���õ���̹�˵���ɫ
		g.setColor(color);//���û�����ɫ
		g.fillRoundRect(tank_x,tank_y,40,40,10,10);//����̹������
		g.setColor(Color.BLACK);
		g.fillRoundRect(tank_x+14,tank_y+14,12,12,5,5);//����̹������ĸ���
		if(direction==1){//��̹�˷�������ʱ
			g.fill3DRect(tank_x+15,tank_y-15,10,17,false);
			g.setColor(new Color(255,0,0));//��������Բ����ɫ
			g.fillOval(tank_x-5,tank_y-5,10,10);g.fillOval(tank_x-5,tank_y+5,10,10);//�����Բ
			g.fillOval(tank_x-5,tank_y+15,10,10);g.fillOval(tank_x-5,tank_y+25,10,10);
			g.fillOval(tank_x-5,tank_y+35,10,10);g.fillOval(tank_x+35,tank_y-5,10,10);
			g.fillOval(tank_x+35,tank_y+5,10,10);g.fillOval(tank_x+35,tank_y+15,10,10);//���ұ�Բ
			g.fillOval(tank_x+35,tank_y+25,10,10);g.fillOval(tank_x+35,tank_y+35,10,10);
		}
		else if(direction==2){//��������ʱ
			g.fill3DRect(tank_x+15,tank_y+40-2,10,17,false);
			g.setColor(new Color(0,255,0));//��������Բ����ɫ
			g.fillOval(tank_x-5,tank_y-5,10,10);g.fillOval(tank_x-5,tank_y+5,10,10);//�����Բ
			g.fillOval(tank_x-5,tank_y+15,10,10);g.fillOval(tank_x-5,tank_y+25,10,10);
			g.fillOval(tank_x-5,tank_y+35,10,10);g.fillOval(tank_x+35,tank_y-5,10,10);
			g.fillOval(tank_x+35,tank_y+5,10,10);g.fillOval(tank_x+35,tank_y+15,10,10);//���ұ�Բ
			g.fillOval(tank_x+35,tank_y+25,10,10);g.fillOval(tank_x+35,tank_y+35,10,10);
		}
		else if(direction==3){//��������ʱ
			g.fill3DRect(tank_x-15,tank_y+15,17,10,false);
			g.setColor(new Color(0,0,255));//��������Բ����ɫ
			g.fillOval(tank_x-5,tank_y-5,10,10);g.fillOval(tank_x+5,tank_y-5,10,10);//���ϱ�Բ
			g.fillOval(tank_x+15,tank_y-5,10,10);g.fillOval(tank_x+25,tank_y-5,10,10);
			g.fillOval(tank_x+35,tank_y-5,10,10);g.fillOval(tank_x-5,tank_y+35,10,10);
			g.fillOval(tank_x+5,tank_y+35,10,10);g.fillOval(tank_x+15,tank_y+35,10,10);//���±�Բ
			g.fillOval(tank_x+25,tank_y+35,10,10);g.fillOval(tank_x+35,tank_y+35,10,10);
		}
		else if(direction==4){//��������ʱ
			g.fill3DRect(tank_x+40-2,tank_y+15,17,10,false);
			g.setColor(new Color(128,128,128));//��������Բ����ɫ
			g.fillOval(tank_x-5,tank_y-5,10,10);g.fillOval(tank_x+5,tank_y-5,10,10);//���ϱ�Բ
			g.fillOval(tank_x+15,tank_y-5,10,10);g.fillOval(tank_x+25,tank_y-5,10,10);
			g.fillOval(tank_x+35,tank_y-5,10,10);g.fillOval(tank_x-5,tank_y+35,10,10);
			g.fillOval(tank_x+5,tank_y+35,10,10);g.fillOval(tank_x+15,tank_y+35,10,10);//���±�Բ
			g.fillOval(tank_x+25,tank_y+35,10,10);g.fillOval(tank_x+35,tank_y+35,10,10);
		}
		g.setColor(c);//��ԭ������ɫ
	}
	public Rectangle getRec(){//����̹�˵�ǰ�ľ�������
		return new Rectangle(tank_x,tank_y,40,40);
	}
	public Rectangle getNextRec(){//�����ƶ���ľ�������
		Rectangle rec=null;
		if(direction==1){rec=new Rectangle(tank_x,tank_y-this.speed-3,40,40);}
		else if(direction==2){rec=new Rectangle(tank_x,tank_y+this.speed+3,40,40);}
		else if(direction==3){rec=new Rectangle(tank_x-this.speed-3,tank_y,40,40);}
		else if(direction==4){rec=new Rectangle(tank_x+this.speed+3,tank_y,40,40);}
		return rec;//���ظþ���
	}
}