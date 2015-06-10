import java.awt.*;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
//̹�˴�ս��Ϸ
//////////////////////////////////////////////////////////////////////////////////////

public class TankFire extends JFrame implements KeyListener, ActionListener{

	/**
	 * @param args
	 */
	StartPanel sp=null;
	MyPanel mp=null;
	JMenuBar jmb=null;
	JMenu jm=null;
	JMenuItem jitem1=null;
	JMenuItem jitem2=null;
	JMenuItem jitem3=null;
	public TankFire(){
		//���ӹ��ܣ� ��ʼ��Ϸ �����˳�
		 jmb=new JMenuBar();//�˵���
		 jm=new JMenu("��Ϸ");
		 jitem1=new JMenuItem("��ʼ(G)");
		 jitem2=new JMenuItem("�˳���Ϸ(S)");
		 jitem3=new JMenuItem("��������(R)");
		 ////////////////////////////�¼�����
		 jitem1.setActionCommand("start");
		 jitem2.setActionCommand("signout");
		 jitem3.setActionCommand("replay");
		 jitem1.addActionListener(this);
		 jitem2.addActionListener(this);
		 jitem3.addActionListener(this);
		
		//��װ�˵�
		 jm.add(jitem1);
		 jm.add(jitem3);
		 jm.add(jitem2);
		 jmb.add(jm);
		 setJMenuBar(jmb);
		 
//		Tank tk=new Tank();
//		Thread tTk=new Thread(tk);
//		tTk.start();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�رս�����Դ �ǳ�����Ŷ
		sp=new StartPanel();
		//mp=new MyPanel();����������ʼ���Ļ� ���ڵ��˳�ʼ��������� �Զ����� �ᵼ�½�����Ϸ�� �����Լ��Ѿ�����
		this.setBounds(100,10,1024,600);
		this.add(sp);
		this.addKeyListener(this);
		Thread tsp=new Thread(sp);
		tsp.start();
		//������Ϸ���
//		this.add(mp);
//
//		this.addKeyListener(mp); //�û�����������	
//		//��������߳�
//		Thread tmp=new Thread(mp);
//		tmp.start();
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new TankFire();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode()==KeyEvent.VK_SPACE){
		
			if(sp!=null){	
				this.remove(sp);
				mp=new MyPanel();//
				mp.setBounds(0, 0, 600, 400);
				//System.out.println("��Ϸ��ʽ��ʼ��");
				this.add(mp);
				this.addKeyListener(mp); //�û�����������	
				Thread tmp=new Thread(mp);//��������߳�					
				tmp.start();
				sp=null;
				this.setVisible(true);
		}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getActionCommand().equals("start")){
			if(sp!=null){	
				this.remove(sp);
				mp=new MyPanel();//
				mp.setBounds(0, 0, 600, 400);
				//System.out.println("��Ϸ��ʽ��ʼ��");
				this.add(mp);
				this.addKeyListener(mp); //�û�����������	
				Thread tmp=new Thread(mp);//��������߳�					
				tmp.start();
				sp=null;
				this.setVisible(true);
		}
			

			
		}
		if(arg0.getActionCommand().equals("signout")){
			//System.out.println("�˳�");
			System.exit(0);
		}
		
		
	}

}

