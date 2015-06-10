
package wyf.cgq;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;
public class ServerAgentThread extends Thread{//�����������߳�
	TankServer father;//�������������������
	ServerThread st;//�����������̵߳�����
	Socket sc;//����Socket����
	DataInputStream din;//�����������������
	DataOutputStream dout;
	boolean flag=true;//�����̵߳ı�־λ
	public ServerAgentThread(TankServer father,Socket sc,ServerThread st){//������
		this.father=father;
		this.st=st;
		this.sc=sc;
		try{//�������������
			din=new DataInputStream(sc.getInputStream());
			dout=new DataOutputStream(sc.getOutputStream());
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void run(){//��д��run����
		while(flag){
			try{
				String msg=din.readUTF().trim();//��ȡ�ͻ��˴�������Ϣ
				if(msg.startsWith("<#NICK_NAME#>")){//���������������
					JOptionPane.showMessageDialog(father,msg.substring(13)+"������Ϸ",
					"��ʾ",JOptionPane.INFORMATION_MESSAGE);//����������Ϸ�ĶԻ���
					father.jbStart.setEnabled(true);//��"��ʼ"��ť��Ϊ����
				}
				else if(msg.startsWith("<#LEAVE#>")){//����ͻ����뿪��
					this.flag=false;//�������߳�
					din.close();//�ر����������
					dout.close();
					sc.close();//�ر�Socket
					st.setHasPerson(false);//���÷������̱߳�־λ
					father.sat=null;//�������������߳���Ϊ��
					father.jpz.setStart(false);//������Ϸ״̬
					JOptionPane.showMessageDialog(father,"�����뿪��Ϸ","��ʾ",
					        JOptionPane.INFORMATION_MESSAGE);//������ʾ��Ϣ
					father.jpz.initialTank();//���³�ʼ��̹��
				}
				else if(msg.startsWith("<#CLIENT#>")){//����Ǹ��¸����Ķ���Ϣ
					String info=msg.substring(10);
					String[] detail=info.split("/");//��ȡ��Ҫ����Ϣ
					int direction=new Integer(detail[0]);
					int x=new Integer(detail[1]);//����Ϣת������Ҫ�ĸ�ʽ
					int y=new Integer(detail[2]);
					father.jpz.updateClient(direction,x,y);//���¸���
				}
				else if(msg.startsWith("<#FIRECLIENT#>")){//�յ����������ӵ�����Ϣ
					String info=msg.substring(14);
					String[] detail=info.split("/");//����ӵ�����Ӧ��Ϣ
					int direction=new Integer(detail[0]);
					int x=new Integer(detail[1]);
					int y=new Integer(detail[2]);
					this.father.jpz.clientFire(direction,x,y);//��ӿͻ����ӵ�
				}
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
	public void setFlag(boolean flag){//�����̱߳�־λ�ķ���
		this.flag=flag;
	}
}