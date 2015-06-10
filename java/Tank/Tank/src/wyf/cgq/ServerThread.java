
package wyf.cgq;
import java.util.*;import java.awt.*;
import java.awt.event.*;import javax.swing.*;
import javax.swing.event.*;import java.net.*;
import java.io.*;
public class ServerThread extends Thread{//服务器线程
	TankServer father;//声明服务器主类的引用
	ServerSocket ss;//声明ServerSocket引用
	boolean flag=true;//控制线程的标志位
	boolean hasPerson=false;//是否已经有客户端连接上
	public ServerThread(TankServer father,ServerSocket ss){//构造器
		this.father=father;
		this.ss=ss;
	}
	public void run(){//重写的run方法
		while(flag){
			if(hasPerson==false){//如果还没有客户端连接
				try{
					Socket sc=ss.accept();//等待客户端连接
					ServerAgentThread sat=new ServerAgentThread(father,sc,this);
					sat.start();//当有连接时，为其分配一个服务器代理线程并启动
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
	public void setFlag(boolean flag){//设置标志位的方法 
		this.flag=flag;
	}
	public void setHasPerson(boolean hasPerson){//设置线程状态的方法
		this.hasPerson=hasPerson;
	}
}
