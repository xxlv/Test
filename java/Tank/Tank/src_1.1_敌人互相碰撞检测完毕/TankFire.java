import java.awt.*;
import java.awt.Event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
//̹�˴�ս��Ϸ
public class TankFire extends JFrame{

	/**
	 * @param args
	 */
	
	MyPanel mp=null;
	
	
	public TankFire(){
//		Tank tk=new Tank();
//		Thread tTk=new Thread(tk);
//		tTk.start();
		mp=new MyPanel();
		this.setBounds(100,10,1024,600);
		//������Ϸ���
		this.add(mp);
		this.setVisible(true);
		this.addKeyListener(mp); //�û�����������	
		//��������߳�
		Thread tmp=new Thread(mp);
		tmp.start();
	}
	
	public static void main(String[] args) {
		new TankFire();
	}

}

