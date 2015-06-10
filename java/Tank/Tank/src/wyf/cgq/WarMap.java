
package wyf.cgq;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
public class WarMap{
	int[][] map=new int[35][40];//创建一个二维数组
	public WarMap(){
		this.initialMap();//初始化地图
	}
	public void initialMap(){//对地图进行初始化
		for(int i=7;i<12;i++){
			for(int j=7;j<12;j++){map[i][j]=1;}
		}//将左上角部分区域设为1
		for(int i=21;i<26;i++){
			for(int j=7;j<12;j++){map[i][j]=1;}
		}//将右上角部分区域设为1
		for(int i=7;i<12;i++){
			for(int j=25;j<30;j++){map[i][j]=1;}
		}//将左下角部分区域设为1
		for(int i=21;i<26;i++){
			for(int j=25;j<30;j++){map[i][j]=1;}
		}//将右下角部分区域设为1
		for(int i=14;i<19;i++){
			for(int j=15;j<20;j++){map[i][j]=1;}
		}//将中间部分区域设为1
	}
	public void draws(Graphics g){//绘制地图
		Color c=g.getColor();//获得画笔颜色
		g.setColor(Color.GRAY);//设置画笔颜色
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[i].length;j++){
				if(map[i][j]==1){//1代表有墙壁
					g.fill3DRect(j*20,i*20,20,20,false);
				}
			}
		}
		g.setColor(c);//还原画笔颜色
	}
	public int getState(int x,int y){//返回否一点的状态
		if(x>=0&&x<40&&y>=0&&y<35){return map[y][x];}
		else{return 1;}
	}
	public void setState(int x,int y){map[x][y]=0;}//设置某一点的状态
}