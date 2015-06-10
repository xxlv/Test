///////////////////////////////////////////////////////////////////////////////
//记录着 记录游戏数据走这里
public class Recorder {
	
	
	public static int ets=30;//敌人的数量
	public static int ts=3;//我的生命
	public static boolean game=true;
	public static boolean gameFailed=false;
	public static int getCredit()
	{
		return  ets*10-(3-ts)*5;
	}
}
