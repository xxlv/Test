
package wyf.cgq;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;
public class TankServer extends JFrame implements ActionListener,KeyListener{
	public static final int frame_width=50;//����Ŀ��
	public static final int frame_height=35;//����ĸ߶�
	public static final int frame_opration=10;//��������Ŀ��
	public static final int block_width=20;//��λ���
	public static final int block_height=20;//��λ�߶�
	ServerWar jpz=new ServerWar(this);//�����������������
	JPanel jpy=new JPanel();//���������������		
	JLabel jlPort=new JLabel("�˿ں�");//������ʾ�û�����˿ںŵı�ǩ
	JTextField jtfPort=new JTextField("9999");//������������˿ںŵ��ı���Ĭ����9999
	JLabel jlNickName=new JLabel("��    ��");//����������ʾ�����ǳƵı�ǩ
	JTextField jtfNickName=new JTextField("Player2");//�������������ǳƵ��ı���
	JButton jbNew=new JButton("����");//����"����","ֹͣ","��ʼ"����������ť
	JButton jbStop=new JButton("ֹͣ");
	JButton jbStart=new JButton("��ʼ");
	JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jpz,jpy);//����JSplitPane
	ServerSocket ss;//����ServerSocket����
	ServerThread st;//�����������߳�
	ServerAgentThread sat;//���������������߳�����
	int times=0;//����������
	public TankServer(){
		this.addComponent();//��ʼ�����壬���ؼ���ӵ�������
		this.addListener();//Ϊ��Ӧ�ؼ�ע�������
		this.initialFrame();//��ʼ������
		jpz.setFocusable(true);
		jpz.requestFocus(true);//ʹ�������ý���
	}
	public void setState(boolean state){//���ô���״̬�ķ���
		jtfPort.setEnabled(state);
		jtfNickName.setEnabled(state);
		jbNew.setEnabled(state);
		jbStop.setEnabled(!state);
		jbStart.setEnabled(!state);
	}
	public void addComponent(){//��ӿؼ� �ķ���
		jpy.setLayout(null);//��Ϊ�ղ���
		jlPort.setBounds(10,20,40,25);
		jpy.add(jlPort);//���"�˿ں�"��ǩ
		jtfPort.setBounds(55,20,100,25);
		jpy.add(jtfPort);//�������˿ںŵ��ı���
		jlNickName.setBounds(10,50,40,25);
		jpy.add(jlNickName);//���"�ǳ�"��ǩ
		jtfNickName.setBounds(55,50,100,25);
		jpy.add(jtfNickName);//��������ǳƵ��ı���
		jbNew.setBounds(20,85,60,20);
		jpy.add(jbNew);//���"����"��ť
		jbStop.setBounds(100,85,60,20);
		jpy.add(jbStop);//���"ֹͣ"��ť
		jbStart.setBounds(20,120,140,30);
		jpy.add(jbStart);//���"��ʼ"��ť
		jsp.setDividerLocation((frame_width-frame_opration)*block_width);
		jsp.setDividerSize(4);//����JSplitPane�ָ��ߵ�λ�ü����
		this.add(jsp);//���jsp
	}
	public void initialFrame(){//��ʼ������ķ���
		this.setState(true);
		Image image=new ImageIcon("ico.gif").getImage();  
		this.setIconImage(image);//����ͼ��
		this.setTitle("TankWar--Server");//���ñ���
		this.setResizable(false);
		Dimension screenSize = //�����Ļ�ߴ�
		        Toolkit.getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;//������Ļ����������
		int centerY=screenSize.height/2;
		int width=frame_width*block_width;//���㴰�ڵĸ߶ȺͿ��
		int height=frame_height*block_height;
		this.setBounds(centerX-width/2,centerY-height/2-30,width,height);//ʹ���������ʾ
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					if(st==null){//�������߳�Ϊ�գ���ֱ���˳�
						System.exit(0);
						return;
					}
					try{
						if(sat!=null){//�����̷߳ǿգ���ͻ��˷����뿪����Ϣ
							sat.dout.writeUTF("<#SERVER_DOWN#>");
							sat.setFlag(false);//��ֹ�����߳�
						}
						st.setFlag(false);//��ֹ�������߳�
						ss.close();//�ر�����
					}
					catch(IOException ee){ee.printStackTrace();}
					System.exit(0);
				}
				
			}
			);
		this.setVisible(true);
	}
	public void addListener(){//Ϊ�ؼ�ע�������
		jbNew.addActionListener(this);//Ϊ"����"��ťע�������
		jbStop.addActionListener(this);//Ϊ"ֹͣ"��ťע�������
		jbStart.addActionListener(this);//Ϊ"��ʼ"��ťע�������
		jpz.addKeyListener(this);//Ϊ�����ע�������
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==this.jbNew){//������"����"��ťʱ
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
				JOptionPane.showMessageDialog(this,"�˿ں�ֻ����0-65535������",
				                         "����",JOptionPane.ERROR_MESSAGE);
				return;
			}
			try{
				ss=new ServerSocket(port);//����ServerSocket
				st=new ServerThread(this,ss);//�����������߳�
				st.start();//�����������߳�
				JOptionPane.showMessageDialog(this,"�����������ɹ�","��ʾ",
				         JOptionPane.INFORMATION_MESSAGE);//�����ɹ�����ʾ
				this.setState(false);//���ô���״̬
				this.jbStart.setEnabled(false);
			}
			catch(Exception ee){//����ʧ��
				JOptionPane.showMessageDialog(this,"����������ʧ��","��ʾ",
				                    JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if(e.getSource()==this.jbStop){//������"ֹͣ"��ťʱ
			try{
				if(sat!=null){//�����̲߳�Ϊ�գ�����ͻ��˷����뿪����Ϣ
					sat.dout.writeUTF("<#SERVER_DOWN#>");
					sat.setFlag(false);//��ֹ�����߳�
				}
				st.setFlag(false);//��ֹ�������߳�
				ss.close();//�ر�����
			}
			catch(IOException ee){ee.printStackTrace();}
			this.setState(true);//���ô���״̬
			jpz.setStart(false);//������Ϸ״̬
			jpz.initialTank();//���³�ʼ��̹��
		}
		else if(e.getSource()==this.jbStart){//������"��ʼ"��ťʱ
			try{sat.dout.writeUTF("<#START#>");}
			catch(IOException ee){ee.printStackTrace();}
			jpz.setStart(true);//��ʼ��Ϸ
			jbStart.setEnabled(false);
		}
		jpz.requestFocus(true);
	}
	public void keyReleased(KeyEvent e){
		times=0;
		if(e.getKeyCode()==32){//��Ϊ�ո��ǣ������ӵ�
			jpz.hostFire();
		}
	}
	public void keyPressed(KeyEvent e){
		times++;
		int key=e.getKeyCode();
		if(times==1){//��ʱ��һ��ʱ�����·���
			if(key==38){
				jpz.setHostDir(1);//��������Ϊ��
			}
			else if(key==40){
				jpz.setHostDir(2);//��������Ϊ��
			}
			else if(key==37){
				jpz.setHostDir(3);//��������Ϊ��
			}
			else if(key==39){
				jpz.setHostDir(4);//��������Ϊ��
			}
		}		
		if(key==37||key==38||key==39||key==40){
			jpz.hostMove();//�ƶ�̹��
		}
	}
	public void keyTyped(KeyEvent e){}
	public static void main(String args[]){
		new TankServer();
	}	
}
