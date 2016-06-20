package com.yinhan.main;

import io.appium.java_client.android.AndroidDriver;

import java.net.URL;
import java.util.ArrayList;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.yinhan.handler.HandleTestCase;
import com.yinhan.handler.HandleTestCaseInterface;
import com.yinhan.util.Config;
import com.yinhan.util.HelpUtil;
/**
 * 入口
 * @author susi
 *
 */
public class StartTest {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		
		//解析文件获得要测试的apk信息，包名和路径
		ArrayList<String[]> apkconfigList = HelpUtil.ReadFileManager(
				Config.APKCONFIGFILE, Config.TABSIGN);
		Config.initKeyCodeMap();
		
		ArrayList<String[]> allConfigList=HelpUtil.ReadFileManager(Config.ALLCONFIG, Config.EQUALSIGN);
		for(int i=0;i<allConfigList.size();i++)
		{
			if("runningX".equals(allConfigList.get(i)[0]))
			{
				Config.RUNNINGX=Integer.parseInt(allConfigList.get(i)[1]);
			}else if("runningY".equals(allConfigList.get(i)[0]))
			{
				Config.RUNNINGY=Integer.parseInt(allConfigList.get(i)[1]);
			}else if("apkResultPath".equals(allConfigList.get(i)[0]))
			{
				Config.APKRESULTPATH=allConfigList.get(i)[1];
			}
		}
		
		AndroidDriver driver = null;
		DesiredCapabilities capabilities = new DesiredCapabilities();
		for(int i=0;i<apkconfigList.size();i++)
		{
			//安装apk
//			HelpUtil.RunCommand("adb install "+apkconfigList.get(i)[0]);
			
			//设置机器
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("deviceName", "vivo X5Pro D");
			capabilities.setCapability("platformVersion", "5.0");
			capabilities.setCapability("appPackage",
					apkconfigList.get(i)[1]);
			capabilities.setCapability("appActivity",
					"com.yinhan.yh01.sdk.YinHanSDK");
			capabilities.setCapability("unicodeKeyboard", true);
			capabilities.setCapability("resetKeyboard", true);
			
			driver= new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			
			//开始跑测试用例
			HandleTestCaseInterface handleYhTestCase = new HandleTestCase();
			((HandleTestCase) handleYhTestCase).setDriver(driver);
			((HandleTestCase) handleYhTestCase).setResultPath(Config.APKRESULTPATH+HelpUtil.GetFileName(apkconfigList.get(i)[0]));
			handleYhTestCase.startRunning();
			
			//跑完了结束掉机器
			driver.quit();
			
			//卸载程序
//			HelpUtil.RunCommand("adb uninstall "+apkconfigList.get(i)[1]);
			
			//导出报告
			HelpUtil.ExportReport(Config.APKRESULTPATH+HelpUtil.GetFileName(apkconfigList.get(i)[0]));
			
		}
		
	}

	
	
	
//	 ArrayList<String[]>
//	 apkconfigtempList=HelpUtil.ReadFileManager(Config.APKCONFIGFILE, "");
//	 apkconfigtempList.remove(0);
//	 if(apkconfigtempList.size() == 0)
//	 {
//	 HelpUtil.WriteFileManager(Config.APKCONFIGFILE, "",false);
//	 }else
//	 {
//	 for(int i =0;i<apkconfigtempList.size();i++)
//	 {
//	 if(i==0)
//	 {
//	 HelpUtil.WriteFileManager(Config.APKCONFIGFILE,
//	 apkconfigtempList.get(i)[0],false);
//	 }
//	 else
//	 {
//	 HelpUtil.WriteFileManager(Config.APKCONFIGFILE,
//	 apkconfigtempList.get(i)[0],true);
//	 }
//	
//	 }
//	 }
	
	
}
