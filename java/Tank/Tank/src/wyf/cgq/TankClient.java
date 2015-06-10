
package wyf.cgq;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;
public class TankClient extends JFrame implements ActionListener,KeyListener{
	public static final int frame_width=50;//窗体的宽度
	public static final int frame_height=35;//窗体的高度
	public static final int frame_opration=10;//操作区域的宽度
	public static final int block_width=20;//单位宽度
	public static final int block_height=20;//单位高度
	ClientWar jpz=new ClientWar(this);//创建客户端主面板
	JPanel jpy=new JPanel();//创建操作区的面板	
	JLabel jlHost=new JLabel("主机名");//提示输入主机ip的标签
	JTextField jtfHost=new JTextField("127.0.0.1");//输入主机ip的文本框，默认是127.0.0.1
	JLabel jlPort=new JLabel("端口号");//提示输入端口号的标签
	JTextField jtfPort=new JTextField("9999");//输入端口号的文本框，默认是9999
	JLabel jlNickName=new JLabel("昵    称");//提示输入昵称的标签
	JTextField jtfNickName=new JTextField("Player");//输入昵称的文本框，默认是player
	JButton jbConnect=new JButton("连接");//创建"连接"按钮
	JButton jbDisConnect=new JButton("断开");//创建"断开"按钮
	JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jpz,jpy);//创建JSplitPane
	Socket sc;//声明Socket引用
	ClientAgentThread cat;//声明客户端代理线程的引用
	int times=0;//辅助控制用
	public TankClient(){//构造器
		this.addComponent();//初始化窗体，将控件添加到窗体中
		this.addListener();//为相应控件注册监听器
		this.initialFrame();//初始化窗体
		jpz.setFocusable(true);
		jpz.requestFocus(true);//使左边面板获得焦点
	}
	public void setState(boolean state){//设置窗体状态的方法
		jtfHost.setEnabled(state);
		jtfPort.setEnabled(state);
		jtfNickName.setEnabled(state);
		jbConnect.setEnabled(state);
		jbDisConnect.setEnabled(!state);
	}
	public void addComponent(){//添加控件 的方法
		jpy.setLayout(null);//设为空布局
		jlHost.setBounds(10,30,40,25);
		jpy.add(jlHost);//添加"主机名"标签
		jtfHost.setBounds(55,30,100,25);
		jpy.add(jtfHost);//添加输入主机名的标签
		jlPort.setBounds(10,60,40,25);
		jpy.add(jlPort);//添加"端口号"标签
		jtfPort.setBounds(55,60,100,25);
		jpy.add(jtfPort);//添加输入端口号的标签
		jlNickName.setBounds(10,90,40,25);
		jpy.add(jlNickName);//添加"昵称"标签
		jtfNickName.setBounds(55,90,100,25);
		jpy.add(jtfNickName);//添加输入昵称的文本框
		jbConnect.setBounds(20,120,60,20);
		jpy.add(jbConnect);//添加"连接"按钮
		jbDisConnect.setBounds(100,120,60,20);
		jpy.add(jbDisConnect);//添加"断开"按钮
		jsp.setDividerLocation((frame_width-frame_opration)*block_width);
		jsp.setDividerSize(4);//设置JSplitPane分割线的位置及宽度
		this.add(jsp);//添加jsp
	}
	public void initialFrame(){//初始化窗体的方法
		this.setState(true);
		Image image=new ImageIcon("ico.gif").getImage(); 
		this.setIconImage(image); //设置图标
		this.setTitle("TankWar--Client");//设置标题
		this.setResizable(false);
		Dimension screenSize = //获得屏幕尺寸
		        Toolkit.getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;//计算屏幕中心坐标
		int centerY=screenSize.height/2;
		int width=frame_width*block_width;//计算窗体的高和宽
		int height=frame_height*block_height;
		this.setBounds(centerX-width/2,centerY-height/2-30,width,height);//使窗体居中显示
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					if(cat==null){//如果代理线程是空则直接退出
						System.exit(0);
						return;
					}
					try{
						cat.dout.writeUTF("<#LEAVE#>");//代理线程不是空，向服务器端发送离开的消息
						cat.setFlag(false);
						sc.close();//关闭socket
					}
					catch(IOException ea){ea.printStackTrace();}
					System.exit(0);
				}
			}
			);
		this.setVisible(true);
	}
	public void addListener(){//为控件注册时间监听器
		jbConnect.addActionListener(this);
		jbDisConnect.addActionListener(this);
		jpz.addKeyListener(this);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==this.jbConnect){//当用户单击"连接"时
			int port=0;
			try{//获得用户输入的端口号
				port=Integer.parseInt(this.jtfPort.getText().trim());
			}
			catch(Exception ee){//如果不是数字则给出提示 
				JOptionPane.showMessageDialog(this,"端口号只能是整数","错误",
				                                JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(port>65535||port<0){//判断端口号是否在有效范围内
				JOptionPane.showMessageDialog(this,"端口号只能是0-65535的整数","错误",
				                                JOptionPane.ERROR_MESSAGE);
				return;
			}
			String name=this.jtfNickName.getText().trim();//获得昵称
			if(name.length()==0){//昵称为空则给出提示信息
				JOptionPane.showMessageDialog(this,"玩家姓名不能为空","错误",
				                                 JOptionPane.ERROR_MESSAGE);
				return;
			}
			try{
				this.setState(false);
				sc=new Socket(this.jtfHost.getText().trim(),port);//创建Socket进行连接
				cat=new ClientAgentThread(this,sc);	//连接成功，创建代理线程
				cat.start();//启动代理线程
				JOptionPane.showMessageDialog(this,"已连接到服务器","提示",
				         JOptionPane.INFORMATION_MESSAGE);//给出连接成功的提示
			}
			catch(Exception ee){
				JOptionPane.showMessageDialog(this,"连接服务器失败","错误",
				          JOptionPane.ERROR_MESSAGE);//连接失败，给出提示
				this.setState(true);
				return;
			}
		}
		else if(e.getSource()==this.jbDisConnect){//当用户单击"断开"按钮时
			try{
				cat.dout.writeUTF("<#LEAVE#>");//向服务器端发送离开的消息
				cat.setFlag(false);//结束代理线程
				sc.close();//关闭连接
			}
			catch(IOException ea){ea.printStackTrace();}
			jpz.setStart(false);
			jpz.initialTank();//重新初始化坦克
			this.setState(true);
		}
		jpz.requestFocus(true);
	}
	public void keyReleased(KeyEvent e){
		times=0;
		if(e.getKeyCode()==32){//当为空格是，发射子弹
			jpz.clientFire();
		}
	}
	public void keyPressed(KeyEvent e){
		times++;
		int key=e.getKeyCode();
		if(times==1){//当时第一次时，更新方向
			if(key==38){
				jpz.setClientDir(1);//将方向设为上
			}
			else if(key==40){
				jpz.setClientDir(2);//将方向设为下
			}
			else if(key==37){
				jpz.setClientDir(3);//将方向设为左
			}
			else if(key==39){
				jpz.setClientDir(4);//将方向设为右
			}
		}
		if(key==37||key==38||key==39||key==40){
			jpz.clientMove();//移动坦克
		}
	}
	public void keyTyped(KeyEvent e){}
	public static void main(String args[]){
		new TankClient();
	}
}