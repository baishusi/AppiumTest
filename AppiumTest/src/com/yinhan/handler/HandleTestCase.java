package com.yinhan.handler;

import io.appium.java_client.android.AndroidDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import com.yinhan.data.TestCaseBean;
import com.yinhan.util.Config;
import com.yinhan.util.HelpUtil;

public class HandleTestCase implements HandleTestCaseInterface {
	@SuppressWarnings("rawtypes")
	private AndroidDriver driver;
	private ArrayList<TestCaseBean> testCaseList = new ArrayList<TestCaseBean>();
	private String resultPath, resultPicPath, resultTxtPath, onePicPath;
	private int ratioX, ratioY;

	private void handleClick(String clickType, String[] argumentArr) {
		WebElement elText = null;
		if ("n".equals(argumentArr[0])) {
			for (int i = 0; i < driver.findElementsByClassName(clickType)
					.size(); i++) {
				elText = (WebElement) driver.findElementsByClassName(clickType)
						.get(i);
				if (argumentArr[1].equals(elText.getText())) {
					elText.click();
					break;
				}
			}
		} else {
			int textviewTag = Integer.parseInt(argumentArr[0]);
			if (textviewTag < 0) {
				textviewTag = driver.findElementsByClassName(clickType).size()
						+ textviewTag;
			}
			elText = (WebElement) driver.findElementsByClassName(clickType)
					.get(textviewTag);
			elText.click();
		}

	}

	@Override
	public void excuteAction(String[] actions) {
		int actionNum = Integer.parseInt(actions[0]);
		String[] argumentArr = null;
		
		if(actions.length>1)
		{
			argumentArr = actions[1].split(Config.COMMA);
		}
		
		try {
			switch (actionNum) {
			case Config.ACTION_CLICK:
				HashMap<String, Double> tap = new HashMap<String, Double>();
				tap.put("tapCount", new Double(2));
				tap.put("touchCount", new Double(1));
				tap.put("duration", new Double(0.5));
				tap.put("x", new Double(argumentArr[0]) * ratioX);
				tap.put("y", new Double(argumentArr[1]) * ratioY);
				driver.executeScript("mobile: tap", tap);
				break;
			case Config.ACTION_WAIT:
				Thread.sleep(Integer.parseInt(actions[1]) * 1000);
				break;
			case Config.ACTION_CLICKBUTTON:
				handleClick("android.widget.Button", argumentArr);
				break;
			case Config.ACITON_CLICKTEXT:
				handleClick("android.widget.TextView", argumentArr);
				break;
			case Config.ACTION_CLICKIMAGE:
				handleClick("android.widget.ImageView", argumentArr);
				break;
			case Config.ACTION_INEDITTEXT:
				@SuppressWarnings("unchecked")
				List<WebElement> textFieldsList = driver
						.findElementsByClassName("android.widget.EditText");
				for (int i = 0; i < argumentArr.length; i++) {
					if (i % 2 == 0) {
						textFieldsList.get(Integer.parseInt(argumentArr[i]))
								.sendKeys(argumentArr[i + 1]);
					}

				}

				break;
			case Config.ACTION_BACK:
				driver.pressKeyCode(4);
				break;
			case Config.ACTION_INKEYBOARD:
				for (int i = 0; i < actions[1].length() - 1; i++) {
					driver.pressKeyCode(Config.KEYCODEMAP.get(actions[1]
							.substring(i, i + 1)));
				}

				break;
			case Config.ACTION_SWIPE:
				driver.swipe(Integer.parseInt(argumentArr[0]) * ratioX,
						Integer.parseInt(argumentArr[1]) * ratioY,
						Integer.parseInt(argumentArr[2]) * ratioX,
						Integer.parseInt(argumentArr[3]) * ratioY, 1000);
				break;

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean getTestResult(String[] result) {
		// TODO Auto-generated method stub
		int resultNum = Integer.parseInt(result[0]);
		String[] resultArr = result[1].split(Config.COMMA);
		boolean isSuccess = false;
		switch (resultNum) {
		case Config.RESULT_EDITTEXTNUM:
			if ("n".equals(resultArr[0])) {
				if (driver.findElementsByClassName("android.widget.EditText")
						.size() != Integer.parseInt(resultArr[1])) {
					isSuccess = true;
				}
			} else {
				if (driver.findElementsByClassName("android.widget.EditText")
						.size() == Integer.parseInt(resultArr[0])) {
					isSuccess = true;
				}
			}

			break;
		case Config.RESULT_TEXTVIEWNAME:
			if ("n".equals(resultArr[0])) {
				int textViewTag = Integer.parseInt(resultArr[1]);
				if (textViewTag < 0) {
					textViewTag = textViewTag
							+ driver.findElementsByClassName(
									"android.widget.TextView").size();
				}
				if (resultArr[2].equals(driver.findElementsByClassName(
						"android.widget.TextView").get(textViewTag))) {
					isSuccess = true;
				}
			}else
			{
				int textViewTag = Integer.parseInt(resultArr[0]);
				if (textViewTag < 0) {
					textViewTag = textViewTag
							+ driver.findElementsByClassName(
									"android.widget.TextView").size();
				}
				if (resultArr[1].equals(driver.findElementsByClassName(
						"android.widget.TextView").get(textViewTag))) {
					isSuccess = true;
				}
			}
			

			break;
		case Config.RESULT_MATCHPIC:
			HelpUtil.CutPic(onePicPath, Config.TEMPPICPATH,
					Integer.parseInt(resultArr[1]),
					Integer.parseInt(resultArr[2]),
					Integer.parseInt(resultArr[3]),
					Integer.parseInt(resultArr[4]));
			isSuccess = HelpUtil.matchPic(Config.PICPATHPRE + resultArr[0],
					Config.TEMPPICPATH);

			break;
		}
		return isSuccess;
	}

	@Override
	public void startRunning() {
		// TODO Auto-generated method stub
		resultPicPath = resultPath + "\\pic";
		resultTxtPath = resultPath + "\\testresult.txt";
		HelpUtil.RemoveAndMakeDir(resultPath);
		HelpUtil.RemoveAndMakeDir(resultPicPath);

		ratioX = Config.RUNNINGX / Config.STANDARDX;
		ratioY = Config.RUNNINGY / Config.STANDARDY;

		parseTestCase();
		String resultString = "success";
		boolean isAppendWrite = false;
		try {
			for (int i = 0; i < testCaseList.size(); i++) {
				Config.runlog.info("running the " + i+" testcase");
				if (i > 0) {
					isAppendWrite = true;
				}
				// 跑测试用例
				for (int j = 0; j < testCaseList.get(i).getAcitons().length; j++) {
					Config.runlog.info("running the "+ j +" action");
					excuteAction(testCaseList.get(i).getAcitons()[j]
							.split(Config.COLON));
				}
				
				Config.runlog.info("begin catch picture");
				// 截图
				onePicPath = resultPicPath + "\\" + testCaseList.get(i).getId()
						+ ".jpg";
				HelpUtil.SnapShot((TakesScreenshot) driver, onePicPath);
				
				Config.runlog.info("get the result");
				// 获得结果
				if (getTestResult(testCaseList.get(i).getTestResult())) {
					resultString = "success";
				} else {
					resultString = "failed";
				}

				Config.runlog.info("write the result");
				// 写入结果
				HelpUtil.WriteFileManager(resultTxtPath, testCaseList.get(i)
						.getId()
						+ Config.TABSIGN
						+ testCaseList.get(i).getInstruction()
						+ Config.TABSIGN
						+ resultString, isAppendWrite);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void parseTestCase() {
		// TODO Auto-generated method stub
		try {
			ArrayList<String[]> tempTestCaseList = HelpUtil.ReadFileManager(
					Config.TESTCASECONFIGFILE, Config.TABSIGN);
			for (int i = 0; i < tempTestCaseList.size(); i++) {
				TestCaseBean testCaseBean = new TestCaseBean();

				testCaseBean.setId(tempTestCaseList.get(i)[0]);
				testCaseBean.setInstruction(tempTestCaseList.get(i)[1]);
				testCaseBean.setAcitons(tempTestCaseList.get(i)[2]
						.split(Config.SEMICOLON));
				testCaseBean.setTestResult(tempTestCaseList.get(i)[3]
						.split(Config.COLON));
				testCaseList.add(testCaseBean);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("rawtypes")
	public AndroidDriver getDriver() {
		return driver;
	}

	@SuppressWarnings("rawtypes")
	public void setDriver(AndroidDriver driver) {
		this.driver = driver;
	}

	public String getResultPath() {
		return resultPath;
	}

	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}

}
