import java.awt.*;
import java.awt.Event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
//̹�˴�ս��Ϸ
public class TankFire extends JFrame implements KeyListener{

	/**
	 * @param args
	 */
	StartPanel sp=null;
	MyPanel mp=null;
	
	
	
	public TankFire(){
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

}

