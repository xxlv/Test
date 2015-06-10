
package wyf.cgq;

import java.io.*;
import java.net.*;
import java.util.*;
public class PaintThread extends Thread{//服务器端的绘制方法
	TankServer father;
	boolean flag=true;
	public PaintThread(TankServer father){//构造器
		this.father=father;
	}
	public void run(){//重写的run方法
		while(flag){
			try{
				sleep(100);
			}
			catch(Exception e){e.printStackTrace();}
			father.repaint();//每隔一段时间绘制一次
		}
	}
}