
package wyf.cgq;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
public class Bullet
{
	private int id;//�ӵ���id�ţ��ú�Ψһ��־һ���ӵ�
	private int tank_id;//���ӵ�����̹�˵�Id��
	private int style;//�ӵ����ͣ�0��ʾ�ǵ��˵�̹�˷����ģ�1��ʾ�����̹�˷����� 
	private int direction;//������1��2��3��4�ֱ��ʾ�ϡ��¡�����
	private int speed;//�ӵ��������ٶȣ�
	private int power;//�ӵ���ɱ������������ʹ�Է�������Ѫ
	private int bullet_x;//�ӵ����ھ��ε����Ͻǵ�X����
	private int bullet_y;//�ӵ����ھ��ε����Ͻǵ�Y����
	private boolean live=true;//�ӵ��Ƿ����״̬
	public Bullet(){}//�޲ι�����
	public Bullet(int id,int bullet_x,int bullet_y){//�����������ڿͻ�����
		this.id=id;
		this.bullet_y=bullet_y;
		this.bullet_x=bullet_x;
	}
	public Bullet(int id,int tank_id,int style,int direction,int speed,int power,
	             int bullet_x,int bullet_y){//�����������ڷ���������
		this.id=id;this.tank_id=tank_id;
		this.style=style;this.direction=direction;
		this.speed=speed;this.power=power;
		this.bullet_x=bullet_x;this.bullet_y=bullet_y;
	}
	public void move(){//�ӵ����еķ���
		if(direction==1){this.bullet_y-=speed;}//��������
		else if(direction==2){this.bullet_y+=speed;}//��������
		else if(direction==3){this.bullet_x-=speed;}//��������
		else if(direction==4){this.bullet_x+=speed;}//��������
	}
	public int getId(){return this.id;}//����ӵ���id��
	public int getTank_id(){return this.tank_id;}//����ӵ�����̹�˵�id��
	public int getStyle(){return this.style;}//����ӵ�������
	public int getDirection(){return this.direction;}//����ӵ��ķ���
	public int getPower(){return this.power;}//����ӵ���ɱ����
	public int getX(){return this.bullet_x;}//����ӵ���X����
	public void setX(int x){this.bullet_x=x;}//�����ӵ���X����
	public int getY(){return this.bullet_y;}//����ӵ���Y����
	public void setY(int y){this.bullet_y=y;}//�����ӵ���Y����
	public void drawBullet(Graphics g){//�����ӵ��ķ��� 
		Color c=g.getColor();//��û��ʵ���ɫ
		g.setColor(Color.BLACK);//���û�����ɫΪ��ɫ
		g.fillOval(bullet_x,bullet_y,10,10);//�����ӵ�
		g.setColor(c);//��ԭ���ʵ���ɫ
	}
	public Rectangle getRec(){//����ӵ����ڵľ���
		return new Rectangle(bullet_x,bullet_y,10,10);
	}
}
