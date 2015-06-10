
package wyf.cgq;
import java.awt.*;import java.awt.event.*;
import javax.swing.*;import javax.swing.event.*;
import java.util.*;import java.io.*;
import java.net.*;
public class ClientAgentThread extends Thread{
	TankClient father;//声明客户端主窗体的引用
	Socket sc;//声明Socket引用
	boolean flag=true;//控制该线程的标志位
	DataInputStream din;//声明数据输入流 
	DataOutputStream dout;//声明数据输出流
	public ClientAgentThread(TankClient father,Socket sc){//构造器
		this.father=father;
		this.sc=sc;
		try{//创建输入输出流
			din=new DataInputStream(sc.getInputStream());
			dout=new DataOutputStream(sc.getOutputStream());
			String name=father.jtfNickName.getText().trim();//获得昵称
			dout.writeUTF("<#NICK_NAME#>"+name);//将昵称传送给服务器
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void run(){//重写的run方法
		while(flag){
			try{
				String msg=din.readUTF().trim();//接收客户端信息
				if(msg.startsWith("<#SERVER_DOWN#>")){//收到服务器离开的信息
					father.jpz.setStart(false);//设置jpz状态为，停止游戏
					father.jpz.initialTank();//调用初始化方法还原游戏模型
					this.flag=false;//设置该线程标志位，停止该线程
					this.sc.close();//关闭Socket
					father.cat=null;//将主窗体的代理线程设为空
					JOptionPane.showMessageDialog(father,"主机离开了！！！","提示",
					          JOptionPane.INFORMATION_MESSAGE);//弹出提示对话框
					father.setState(true);//设置客户端主窗体的状态
				}
				else if(msg.startsWith("<#START#>"))
				{
					father.jpz.setStart(true);//设置jpz状态位，开始游戏
				}
				else if(msg.startsWith("<#HOST#>")){//收到更新主机的消息
					String info=msg.substring(8);//获得有用信息
					String[] detail=info.split("/");//分解信息
					int direction=new Integer(detail[0]);//将信息转化为所需要的 
					int blood=new Integer(detail[1]);
					int x=new Integer(detail[2]);
					int y=new Integer(detail[3]);
					father.jpz.updateHost(direction,blood,x,y);//调用updateHost方法更新主机状态
				}
				else if(msg.startsWith("<#BULLET#>")){//收到增加子弹的信息
					String info=msg.substring(10);
					String[] detail=info.split("/");//获得有用信息，并分解
					int id=new Integer(detail[0]);//将信息转化为所需要的
					int x=new Integer(detail[1]);
					int y=new Integer(detail[2]);
					father.jpz.addBullet(id,x,y);//调用addBullet方法向子弹列表中添加子弹
				}
				else if(msg.startsWith("<#UPDATEBULLET#>")){//收到更细子弹的信息
					String info=msg.substring(16);
					String[] detail=info.split("/");//获得有用信息并分解
					int id=new Integer(detail[0]);//将信息转化为需要的
					int x=new Integer(detail[1]);
					int y=new Integer(detail[2]);
					father.jpz.updateBullet(id,x,y);//调用updateBullet方法更新子弹
				}
				else if(msg.startsWith("<#BADTANK#>")){//获得更新电脑坦克的信息
					String info=msg.substring(11);
					String[] detail=info.split("/");//获得有用信息并分解
					int number=new Integer(detail[0]);
					int direction=new Integer(detail[1]);//转化为需要的
					int x=new Integer(detail[2]);
					int y=new Integer(detail[3]);
					father.jpz.updateTank(number,direction,x,y);//更新电脑坦克
				}
				else if(msg.startsWith("<#REMOVEBULLET#>")){//收到删除子弹的信息
					int id=new Integer(msg.substring(16));//获得该子弹的id号
					father.jpz.removeBullet(id);//调用removeBullet方法从子弹列表删除该子弹
				}
				else if(msg.startsWith("<#ADDSCORE#>")){//获得加分的信息
					int id=new Integer(msg.substring(12));//获得加分坦克的id号
					father.jpz.addScore(id);//给该坦克加分
				}
				else if(msg.startsWith("<#SUBBLOOD#>")){//收到减血的信息
					String info=msg.substring(12);//获得有用信息
					String [] detail=info.split("/");//将信息分解
					int id=new Integer(detail[0]);//获得减血的坦克的id号 
					int subnum=new Integer(detail[1]);//获得减血的量
					father.jpz.subBlood(id,subnum);//调用subBlood方法进行减血
				}
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
	public void setFlag(boolean flag){this.flag=flag;}
}