package com.yinhan.util;

import java.util.HashMap;

public class Config {

	public static final String APKCONFIGFILE="config/apkconfig.txt";
	public static final String TESTCASECONFIGFILE="config/testcase.txt";
	public static final String ALLCONFIG="config/config.txt";
	public static final String TEMPPICPATH="config/temp.jpg";
	public static final String PICPATHPRE="config/rightpicture/";
	
	public static final int STANDARDX=1920;
	public static final int STANDARDY=1080;
	
	public static final String SEMICOLON=";";
	public static final String COMMA=",";
	public static final String COLON=":";
	public static final String TABSIGN="	";
	public static final String EQUALSIGN="=";
	public static final String CHANGELINE="\n";
	
	
	public static final String ACTIONDESCCHINESE_1="点击坐标";
	public static final String ACTIONDESCCHINESE_2="休息";
	public static final String ACTIONDESCCHINESE_3="点击button";
	public static final String ACTIONDESCCHINESE_4="点击textview";
	public static final String ACTIONDESCCHINESE_5="点击imageview";
	public static final String ACTIONDESCCHINESE_6="edittext输入";
	public static final String ACTIONDESCCHINESE_7="返回";
	public static final String ACTIONDESCCHINESE_8="键盘输入";
	public static final String ACTIONDESCCHINESE_9="滑动";
	public static final String SURFACETESTCASEPATH="D:\\myeclipse_workspace\\AppiumTest\\";
	
	
	
	public static HashMap<String,Integer> KEYCODEMAP=new HashMap<String,Integer>();
	
	
	public static final int ACTION_CLICK=1;//点击动作
	public static final int ACTION_WAIT=2;//等待
	public static final int ACTION_CLICKBUTTON=3;//控件button的点击
	public static final int ACITON_CLICKTEXT=4;//控件textview的点击
	public static final int ACTION_CLICKIMAGE=5;//控件image的点击
	public static final int ACTION_INEDITTEXT=6;//控件edittext的输入
	public static final int ACTION_BACK=7;//返回事件
	public static final int ACTION_INKEYBOARD=8;//键盘的输入
	public static final int ACTION_SWIPE=9;//滑动
	
	
	public static final int RESULT_EDITTEXTNUM=1;//控件edittext的数目对比
	public static final int RESULT_TEXTVIEWNAME=2;//控件textview显示名字的对比
	public static final int RESULT_MATCHPIC=3;//图片的对比
	
	public static int RUNNINGX;
	public static int RUNNINGY;
	public static String APKRESULTPATH;
	
	
	public static void initKeyCodeMap()
	{
		KEYCODEMAP.put("0",7);
		KEYCODEMAP.put("1",8);
		KEYCODEMAP.put("2",9);
		KEYCODEMAP.put("3",10);
		KEYCODEMAP.put("4",11);
		KEYCODEMAP.put("5",12);
		KEYCODEMAP.put("6",13);
		KEYCODEMAP.put("7",14);
		KEYCODEMAP.put("8",15);
		KEYCODEMAP.put("9",16);
		KEYCODEMAP.put("a",29);
		KEYCODEMAP.put("b",30);
		KEYCODEMAP.put("c",31);
		KEYCODEMAP.put("d",32);
		KEYCODEMAP.put("e",33);
		KEYCODEMAP.put("f",34);
		KEYCODEMAP.put("g",35);
		KEYCODEMAP.put("h",36);
		KEYCODEMAP.put("i",37);
		KEYCODEMAP.put("j",38);
		KEYCODEMAP.put("k",39);
		KEYCODEMAP.put("l",40);
		KEYCODEMAP.put("m",41);
		KEYCODEMAP.put("n",42);
		KEYCODEMAP.put("o",43);
		KEYCODEMAP.put("p",44);
		KEYCODEMAP.put("q",45);
		KEYCODEMAP.put("r",46);
		KEYCODEMAP.put("s",47);
		KEYCODEMAP.put("t",48);
		KEYCODEMAP.put("u",49);
		KEYCODEMAP.put("v",50);
		KEYCODEMAP.put("w",51);
		KEYCODEMAP.put("x",52);
		KEYCODEMAP.put("y",53);
		KEYCODEMAP.put("z",54);
		
	}
	
}
