
package wyf.cgq;
import java.util.*;import java.awt.*;
import java.awt.event.*;import javax.swing.*;
import javax.swing.event.*;import java.net.*;
import java.io.*;
public class ServerThread extends Thread{//�������߳�
	TankServer father;//�������������������
	ServerSocket ss;//����ServerSocket����
	boolean flag=true;//�����̵߳ı�־λ
	boolean hasPerson=false;//�Ƿ��Ѿ��пͻ���������
	public ServerThread(TankServer father,ServerSocket ss){//������
		this.father=father;
		this.ss=ss;
	}
	public void run(){//��д��run����
		while(flag){
			if(hasPerson==false){//�����û�пͻ�������
				try{
					Socket sc=ss.accept();//�ȴ��ͻ�������
					ServerAgentThread sat=new ServerAgentThread(father,sc,this);
					sat.start();//��������ʱ��Ϊ�����һ�������������̲߳�����
					father.sat=sat;
					this.hasPerson=true;
				}
				catch(Exception e){e.printStackTrace();}
			}
			else
			{
				try
				{
					Thread.sleep(1000);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	public void setFlag(boolean flag){//���ñ�־λ�ķ��� 
		this.flag=flag;
	}
	public void setHasPerson(boolean hasPerson){//�����߳�״̬�ķ���
		this.hasPerson=hasPerson;
	}
}
