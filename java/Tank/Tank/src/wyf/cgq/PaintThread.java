
package wyf.cgq;

import java.io.*;
import java.net.*;
import java.util.*;
public class PaintThread extends Thread{//�������˵Ļ��Ʒ���
	TankServer father;
	boolean flag=true;
	public PaintThread(TankServer father){//������
		this.father=father;
	}
	public void run(){//��д��run����
		while(flag){
			try{
				sleep(100);
			}
			catch(Exception e){e.printStackTrace();}
			father.repaint();//ÿ��һ��ʱ�����һ��
		}
	}
}