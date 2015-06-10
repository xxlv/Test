
package wyf.cgq;
import java.io.*;
import java.net.*;
import java.util.*;
public class CPaintThread extends Thread{//客户端绘制线程类
	TankClient father;
	boolean flag=true;
	public CPaintThread(TankClient father){//构造器
		this.father=father;
	}
	public void run(){//重写的run方法
		while(flag){
			try{sleep(100);}
			catch(Exception e){	e.printStackTrace();}
			father.repaint();//每隔一段时间绘制一次
		}
	}
}
