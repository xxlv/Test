
package wyf.cgq;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;
public class TankServer extends JFrame implements ActionListener,KeyListener{
	public static final int frame_width=50;//窗体的宽度
	public static final int frame_height=35;//窗体的高度
	public static final int frame_opration=10;//操作区域的宽度
	public static final int block_width=20;//单位宽度
	public static final int block_height=20;//单位高度
	ServerWar jpz=new ServerWar(this);//创建服务器端主面板
	JPanel jpy=new JPanel();//创建操作区的面板		
	JLabel jlPort=new JLabel("端口号");//创建提示用户输入端口号的标签
	JTextField jtfPort=new JTextField("9999");//创建用于输入端口号的文本框，默认是9999
	JLabel jlNickName=new JLabel("昵    称");//创建用于提示输入昵称的标签
	JTextField jtfNickName=new JTextField("Player2");//创建用于输入昵称的文本框
	JButton jbNew=new JButton("建主");//创建"建主","停止","开始"三个动作按钮
	JButton jbStop=new JButton("停止");
	JButton jbStart=new JButton("开始");
	JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jpz,jpy);//创建JSplitPane
	ServerSocket ss;//声明ServerSocket引用
	ServerThread st;//声明服务器线程
	ServerAgentThread sat;//声明服务器代理线程引用
	int times=0;//辅助控制用
	public TankServer(){
		this.addComponent();//初始化窗体，将控件添加到窗体中
		this.addListener();//为相应控件注册监听器
		this.initialFrame();//初始化窗体
		jpz.setFocusable(true);
		jpz.requestFocus(true);//使左边面板获得焦点
	}
	public void setState(boolean state){//设置窗体状态的方法
		jtfPort.setEnabled(state);
		jtfNickName.setEnabled(state);
		jbNew.setEnabled(state);
		jbStop.setEnabled(!state);
		jbStart.setEnabled(!state);
	}
	public void addComponent(){//添加控件 的方法
		jpy.setLayout(null);//设为空布局
		jlPort.setBounds(10,20,40,25);
		jpy.add(jlPort);//添加"端口号"标签
		jtfPort.setBounds(55,20,100,25);
		jpy.add(jtfPort);//添加输入端口号的文本框
		jlNickName.setBounds(10,50,40,25);
		jpy.add(jlNickName);//添加"昵称"标签
		jtfNickName.setBounds(55,50,100,25);
		jpy.add(jtfNickName);//添加输入昵称的文本框
		jbNew.setBounds(20,85,60,20);
		jpy.add(jbNew);//添加"建主"按钮
		jbStop.setBounds(100,85,60,20);
		jpy.add(jbStop);//添加"停止"按钮
		jbStart.setBounds(20,120,140,30);
		jpy.add(jbStart);//添加"开始"按钮
		jsp.setDividerLocation((frame_width-frame_opration)*block_width);
		jsp.setDividerSize(4);//设置JSplitPane分割线的位置及宽度
		this.add(jsp);//添加jsp
	}
	public void initialFrame(){//初始化窗体的方法
		this.setState(true);
		Image image=new ImageIcon("ico.gif").getImage();  
		this.setIconImage(image);//设置图标
		this.setTitle("TankWar--Server");//设置标题
		this.setResizable(false);
		Dimension screenSize = //获得屏幕尺寸
		        Toolkit.getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;//计算屏幕的中心坐标
		int centerY=screenSize.height/2;
		int width=frame_width*block_width;//计算窗口的高度和宽度
		int height=frame_height*block_height;
		this.setBounds(centerX-width/2,centerY-height/2-30,width,height);//使窗体居中显示
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					if(st==null){//服务器线程为空，则直接退出
						System.exit(0);
						return;
					}
					try{
						if(sat!=null){//代理线程非空，向客户端发送离开的消息
							sat.dout.writeUTF("<#SERVER_DOWN#>");
							sat.setFlag(false);//终止代理线程
						}
						st.setFlag(false);//终止服务器线程
						ss.close();//关闭连接
					}
					catch(IOException ee){ee.printStackTrace();}
					System.exit(0);
				}
				
			}
			);
		this.setVisible(true);
	}
	public void addListener(){//为控件注册监听器
		jbNew.addActionListener(this);//为"建主"按钮注册监听器
		jbStop.addActionListener(this);//为"停止"按钮注册监听器
		jbStart.addActionListener(this);//为"开始"按钮注册监听器
		jpz.addKeyListener(this);//为主面板注册监听器
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==this.jbNew){//当按下"建主"按钮时
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
				JOptionPane.showMessageDialog(this,"端口号只能是0-65535的整数",
				                         "错误",JOptionPane.ERROR_MESSAGE);
				return;
			}
			try{
				ss=new ServerSocket(port);//创建ServerSocket
				st=new ServerThread(this,ss);//创建服务器线程
				st.start();//启动服务器线程
				JOptionPane.showMessageDialog(this,"服务器启动成功","提示",
				         JOptionPane.INFORMATION_MESSAGE);//给出成功的提示
				this.setState(false);//设置窗体状态
				this.jbStart.setEnabled(false);
			}
			catch(Exception ee){//启动失败
				JOptionPane.showMessageDialog(this,"服务器启动失败","提示",
				                    JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if(e.getSource()==this.jbStop){//当单击"停止"按钮时
			try{
				if(sat!=null){//代理线程不为空，则向客户端发出离开的消息
					sat.dout.writeUTF("<#SERVER_DOWN#>");
					sat.setFlag(false);//终止代理线程
				}
				st.setFlag(false);//终止服务器线程
				ss.close();//关闭连接
			}
			catch(IOException ee){ee.printStackTrace();}
			this.setState(true);//设置窗体状态
			jpz.setStart(false);//设置游戏状态
			jpz.initialTank();//重新初始化坦克
		}
		else if(e.getSource()==this.jbStart){//当单击"开始"按钮时
			try{sat.dout.writeUTF("<#START#>");}
			catch(IOException ee){ee.printStackTrace();}
			jpz.setStart(true);//开始游戏
			jbStart.setEnabled(false);
		}
		jpz.requestFocus(true);
	}
	public void keyReleased(KeyEvent e){
		times=0;
		if(e.getKeyCode()==32){//当为空格是，发射子弹
			jpz.hostFire();
		}
	}
	public void keyPressed(KeyEvent e){
		times++;
		int key=e.getKeyCode();
		if(times==1){//当时第一次时，更新方向
			if(key==38){
				jpz.setHostDir(1);//将方向设为上
			}
			else if(key==40){
				jpz.setHostDir(2);//将方向设为下
			}
			else if(key==37){
				jpz.setHostDir(3);//将方向设为左
			}
			else if(key==39){
				jpz.setHostDir(4);//将方向设为右
			}
		}		
		if(key==37||key==38||key==39||key==40){
			jpz.hostMove();//移动坦克
		}
	}
	public void keyTyped(KeyEvent e){}
	public static void main(String args[]){
		new TankServer();
	}	
}
