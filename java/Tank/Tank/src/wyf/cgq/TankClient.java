
package wyf.cgq;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;
public class TankClient extends JFrame implements ActionListener,KeyListener{
	public static final int frame_width=50;//����Ŀ��
	public static final int frame_height=35;//����ĸ߶�
	public static final int frame_opration=10;//��������Ŀ��
	public static final int block_width=20;//��λ���
	public static final int block_height=20;//��λ�߶�
	ClientWar jpz=new ClientWar(this);//�����ͻ��������
	JPanel jpy=new JPanel();//���������������	
	JLabel jlHost=new JLabel("������");//��ʾ��������ip�ı�ǩ
	JTextField jtfHost=new JTextField("127.0.0.1");//��������ip���ı���Ĭ����127.0.0.1
	JLabel jlPort=new JLabel("�˿ں�");//��ʾ����˿ںŵı�ǩ
	JTextField jtfPort=new JTextField("9999");//����˿ںŵ��ı���Ĭ����9999
	JLabel jlNickName=new JLabel("��    ��");//��ʾ�����ǳƵı�ǩ
	JTextField jtfNickName=new JTextField("Player");//�����ǳƵ��ı���Ĭ����player
	JButton jbConnect=new JButton("����");//����"����"��ť
	JButton jbDisConnect=new JButton("�Ͽ�");//����"�Ͽ�"��ť
	JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jpz,jpy);//����JSplitPane
	Socket sc;//����Socket����
	ClientAgentThread cat;//�����ͻ��˴����̵߳�����
	int times=0;//����������
	public TankClient(){//������
		this.addComponent();//��ʼ�����壬���ؼ���ӵ�������
		this.addListener();//Ϊ��Ӧ�ؼ�ע�������
		this.initialFrame();//��ʼ������
		jpz.setFocusable(true);
		jpz.requestFocus(true);//ʹ�������ý���
	}
	public void setState(boolean state){//���ô���״̬�ķ���
		jtfHost.setEnabled(state);
		jtfPort.setEnabled(state);
		jtfNickName.setEnabled(state);
		jbConnect.setEnabled(state);
		jbDisConnect.setEnabled(!state);
	}
	public void addComponent(){//��ӿؼ� �ķ���
		jpy.setLayout(null);//��Ϊ�ղ���
		jlHost.setBounds(10,30,40,25);
		jpy.add(jlHost);//���"������"��ǩ
		jtfHost.setBounds(55,30,100,25);
		jpy.add(jtfHost);//��������������ı�ǩ
		jlPort.setBounds(10,60,40,25);
		jpy.add(jlPort);//���"�˿ں�"��ǩ
		jtfPort.setBounds(55,60,100,25);
		jpy.add(jtfPort);//�������˿ںŵı�ǩ
		jlNickName.setBounds(10,90,40,25);
		jpy.add(jlNickName);//���"�ǳ�"��ǩ
		jtfNickName.setBounds(55,90,100,25);
		jpy.add(jtfNickName);//��������ǳƵ��ı���
		jbConnect.setBounds(20,120,60,20);
		jpy.add(jbConnect);//���"����"��ť
		jbDisConnect.setBounds(100,120,60,20);
		jpy.add(jbDisConnect);//���"�Ͽ�"��ť
		jsp.setDividerLocation((frame_width-frame_opration)*block_width);
		jsp.setDividerSize(4);//����JSplitPane�ָ��ߵ�λ�ü����
		this.add(jsp);//���jsp
	}
	public void initialFrame(){//��ʼ������ķ���
		this.setState(true);
		Image image=new ImageIcon("ico.gif").getImage(); 
		this.setIconImage(image); //����ͼ��
		this.setTitle("TankWar--Client");//���ñ���
		this.setResizable(false);
		Dimension screenSize = //�����Ļ�ߴ�
		        Toolkit.getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;//������Ļ��������
		int centerY=screenSize.height/2;
		int width=frame_width*block_width;//���㴰��ĸߺͿ�
		int height=frame_height*block_height;
		this.setBounds(centerX-width/2,centerY-height/2-30,width,height);//ʹ���������ʾ
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					if(cat==null){//��������߳��ǿ���ֱ���˳�
						System.exit(0);
						return;
					}
					try{
						cat.dout.writeUTF("<#LEAVE#>");//�����̲߳��ǿգ���������˷����뿪����Ϣ
						cat.setFlag(false);
						sc.close();//�ر�socket
					}
					catch(IOException ea){ea.printStackTrace();}
					System.exit(0);
				}
			}
			);
		this.setVisible(true);
	}
	public void addListener(){//Ϊ�ؼ�ע��ʱ�������
		jbConnect.addActionListener(this);
		jbDisConnect.addActionListener(this);
		jpz.addKeyListener(this);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==this.jbConnect){//���û�����"����"ʱ
			int port=0;
			try{//����û�����Ķ˿ں�
				port=Integer.parseInt(this.jtfPort.getText().trim());
			}
			catch(Exception ee){//������������������ʾ 
				JOptionPane.showMessageDialog(this,"�˿ں�ֻ��������","����",
				                                JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(port>65535||port<0){//�ж϶˿ں��Ƿ�����Ч��Χ��
				JOptionPane.showMessageDialog(this,"�˿ں�ֻ����0-65535������","����",
				                                JOptionPane.ERROR_MESSAGE);
				return;
			}
			String name=this.jtfNickName.getText().trim();//����ǳ�
			if(name.length()==0){//�ǳ�Ϊ���������ʾ��Ϣ
				JOptionPane.showMessageDialog(this,"�����������Ϊ��","����",
				                                 JOptionPane.ERROR_MESSAGE);
				return;
			}
			try{
				this.setState(false);
				sc=new Socket(this.jtfHost.getText().trim(),port);//����Socket��������
				cat=new ClientAgentThread(this,sc);	//���ӳɹ������������߳�
				cat.start();//���������߳�
				JOptionPane.showMessageDialog(this,"�����ӵ�������","��ʾ",
				         JOptionPane.INFORMATION_MESSAGE);//�������ӳɹ�����ʾ
			}
			catch(Exception ee){
				JOptionPane.showMessageDialog(this,"���ӷ�����ʧ��","����",
				          JOptionPane.ERROR_MESSAGE);//����ʧ�ܣ�������ʾ
				this.setState(true);
				return;
			}
		}
		else if(e.getSource()==this.jbDisConnect){//���û�����"�Ͽ�"��ťʱ
			try{
				cat.dout.writeUTF("<#LEAVE#>");//��������˷����뿪����Ϣ
				cat.setFlag(false);//���������߳�
				sc.close();//�ر�����
			}
			catch(IOException ea){ea.printStackTrace();}
			jpz.setStart(false);
			jpz.initialTank();//���³�ʼ��̹��
			this.setState(true);
		}
		jpz.requestFocus(true);
	}
	public void keyReleased(KeyEvent e){
		times=0;
		if(e.getKeyCode()==32){//��Ϊ�ո��ǣ������ӵ�
			jpz.clientFire();
		}
	}
	public void keyPressed(KeyEvent e){
		times++;
		int key=e.getKeyCode();
		if(times==1){//��ʱ��һ��ʱ�����·���
			if(key==38){
				jpz.setClientDir(1);//��������Ϊ��
			}
			else if(key==40){
				jpz.setClientDir(2);//��������Ϊ��
			}
			else if(key==37){
				jpz.setClientDir(3);//��������Ϊ��
			}
			else if(key==39){
				jpz.setClientDir(4);//��������Ϊ��
			}
		}
		if(key==37||key==38||key==39||key==40){
			jpz.clientMove();//�ƶ�̹��
		}
	}
	public void keyTyped(KeyEvent e){}
	public static void main(String args[]){
		new TankClient();
	}
}