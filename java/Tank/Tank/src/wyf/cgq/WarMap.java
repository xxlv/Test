
package wyf.cgq;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
public class WarMap{
	int[][] map=new int[35][40];//����һ����ά����
	public WarMap(){
		this.initialMap();//��ʼ����ͼ
	}
	public void initialMap(){//�Ե�ͼ���г�ʼ��
		for(int i=7;i<12;i++){
			for(int j=7;j<12;j++){map[i][j]=1;}
		}//�����Ͻǲ���������Ϊ1
		for(int i=21;i<26;i++){
			for(int j=7;j<12;j++){map[i][j]=1;}
		}//�����Ͻǲ���������Ϊ1
		for(int i=7;i<12;i++){
			for(int j=25;j<30;j++){map[i][j]=1;}
		}//�����½ǲ���������Ϊ1
		for(int i=21;i<26;i++){
			for(int j=25;j<30;j++){map[i][j]=1;}
		}//�����½ǲ���������Ϊ1
		for(int i=14;i<19;i++){
			for(int j=15;j<20;j++){map[i][j]=1;}
		}//���м䲿��������Ϊ1
	}
	public void draws(Graphics g){//���Ƶ�ͼ
		Color c=g.getColor();//��û�����ɫ
		g.setColor(Color.GRAY);//���û�����ɫ
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[i].length;j++){
				if(map[i][j]==1){//1������ǽ��
					g.fill3DRect(j*20,i*20,20,20,false);
				}
			}
		}
		g.setColor(c);//��ԭ������ɫ
	}
	public int getState(int x,int y){//���ط�һ���״̬
		if(x>=0&&x<40&&y>=0&&y<35){return map[y][x];}
		else{return 1;}
	}
	public void setState(int x,int y){map[x][y]=0;}//����ĳһ���״̬
}