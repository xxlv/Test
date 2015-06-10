import java.awt.*;
import java.awt.Event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
//坦克大战游戏
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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭进程资源 非常有用哦
		sp=new StartPanel();
		//mp=new MyPanel();如果在这里初始化的话 由于敌人初始化工作完成 自动运行 会导致进入游戏后 可能自己已经死了
		this.setBounds(100,10,1024,600);
		this.add(sp);
		this.addKeyListener(this);
		Thread tsp=new Thread(sp);
		tsp.start();
		//增加游戏面板
//		this.add(mp);
//
//		this.addKeyListener(mp); //让画板监这个窗体	
//		//启动面板线程
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
				//System.out.println("游戏正式开始！");
				this.add(mp);
				this.addKeyListener(mp); //让画板监这个窗体	
				Thread tmp=new Thread(mp);//启动面板线程					
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

