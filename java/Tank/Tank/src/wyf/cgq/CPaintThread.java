
package wyf.cgq;
import java.io.*;
import java.net.*;
import java.util.*;
public class CPaintThread extends Thread{//�ͻ��˻����߳���
	TankClient father;
	boolean flag=true;
	public CPaintThread(TankClient father){//������
		this.father=father;
	}
	public void run(){//��д��run����
		while(flag){
			try{sleep(100);}
			catch(Exception e){	e.printStackTrace();}
			father.repaint();//ÿ��һ��ʱ�����һ��
		}
	}
}
