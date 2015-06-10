import java.awt.*;
import java.awt.Event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
//坦克大战游戏
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
		//增加游戏面板
		this.add(mp);
		this.setVisible(true);
		this.addKeyListener(mp); //让画板监这个窗体	
		//启动面板线程
		Thread tmp=new Thread(mp);
		tmp.start();
	}
	
	public static void main(String[] args) {
		new TankFire();
	}

}

