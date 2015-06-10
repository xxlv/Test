
package wyf.cgq;
import java.awt.*;import java.awt.event.*;
import javax.swing.*;import javax.swing.event.*;
import java.util.*;import java.io.*;
import java.net.*;
public class ClientAgentThread extends Thread{
	TankClient father;//�����ͻ��������������
	Socket sc;//����Socket����
	boolean flag=true;//���Ƹ��̵߳ı�־λ
	DataInputStream din;//�������������� 
	DataOutputStream dout;//�������������
	public ClientAgentThread(TankClient father,Socket sc){//������
		this.father=father;
		this.sc=sc;
		try{//�������������
			din=new DataInputStream(sc.getInputStream());
			dout=new DataOutputStream(sc.getOutputStream());
			String name=father.jtfNickName.getText().trim();//����ǳ�
			dout.writeUTF("<#NICK_NAME#>"+name);//���ǳƴ��͸�������
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void run(){//��д��run����
		while(flag){
			try{
				String msg=din.readUTF().trim();//���տͻ�����Ϣ
				if(msg.startsWith("<#SERVER_DOWN#>")){//�յ��������뿪����Ϣ
					father.jpz.setStart(false);//����jpz״̬Ϊ��ֹͣ��Ϸ
					father.jpz.initialTank();//���ó�ʼ��������ԭ��Ϸģ��
					this.flag=false;//���ø��̱߳�־λ��ֹͣ���߳�
					this.sc.close();//�ر�Socket
					father.cat=null;//��������Ĵ����߳���Ϊ��
					JOptionPane.showMessageDialog(father,"�����뿪�ˣ�����","��ʾ",
					          JOptionPane.INFORMATION_MESSAGE);//������ʾ�Ի���
					father.setState(true);//���ÿͻ����������״̬
				}
				else if(msg.startsWith("<#START#>"))
				{
					father.jpz.setStart(true);//����jpz״̬λ����ʼ��Ϸ
				}
				else if(msg.startsWith("<#HOST#>")){//�յ�������������Ϣ
					String info=msg.substring(8);//���������Ϣ
					String[] detail=info.split("/");//�ֽ���Ϣ
					int direction=new Integer(detail[0]);//����Ϣת��Ϊ����Ҫ�� 
					int blood=new Integer(detail[1]);
					int x=new Integer(detail[2]);
					int y=new Integer(detail[3]);
					father.jpz.updateHost(direction,blood,x,y);//����updateHost������������״̬
				}
				else if(msg.startsWith("<#BULLET#>")){//�յ������ӵ�����Ϣ
					String info=msg.substring(10);
					String[] detail=info.split("/");//���������Ϣ�����ֽ�
					int id=new Integer(detail[0]);//����Ϣת��Ϊ����Ҫ��
					int x=new Integer(detail[1]);
					int y=new Integer(detail[2]);
					father.jpz.addBullet(id,x,y);//����addBullet�������ӵ��б�������ӵ�
				}
				else if(msg.startsWith("<#UPDATEBULLET#>")){//�յ���ϸ�ӵ�����Ϣ
					String info=msg.substring(16);
					String[] detail=info.split("/");//���������Ϣ���ֽ�
					int id=new Integer(detail[0]);//����Ϣת��Ϊ��Ҫ��
					int x=new Integer(detail[1]);
					int y=new Integer(detail[2]);
					father.jpz.updateBullet(id,x,y);//����updateBullet���������ӵ�
				}
				else if(msg.startsWith("<#BADTANK#>")){//��ø��µ���̹�˵���Ϣ
					String info=msg.substring(11);
					String[] detail=info.split("/");//���������Ϣ���ֽ�
					int number=new Integer(detail[0]);
					int direction=new Integer(detail[1]);//ת��Ϊ��Ҫ��
					int x=new Integer(detail[2]);
					int y=new Integer(detail[3]);
					father.jpz.updateTank(number,direction,x,y);//���µ���̹��
				}
				else if(msg.startsWith("<#REMOVEBULLET#>")){//�յ�ɾ���ӵ�����Ϣ
					int id=new Integer(msg.substring(16));//��ø��ӵ���id��
					father.jpz.removeBullet(id);//����removeBullet�������ӵ��б�ɾ�����ӵ�
				}
				else if(msg.startsWith("<#ADDSCORE#>")){//��üӷֵ���Ϣ
					int id=new Integer(msg.substring(12));//��üӷ�̹�˵�id��
					father.jpz.addScore(id);//����̹�˼ӷ�
				}
				else if(msg.startsWith("<#SUBBLOOD#>")){//�յ���Ѫ����Ϣ
					String info=msg.substring(12);//���������Ϣ
					String [] detail=info.split("/");//����Ϣ�ֽ�
					int id=new Integer(detail[0]);//��ü�Ѫ��̹�˵�id�� 
					int subnum=new Integer(detail[1]);//��ü�Ѫ����
					father.jpz.subBlood(id,subnum);//����subBlood�������м�Ѫ
				}
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
	public void setFlag(boolean flag){this.flag=flag;}
}