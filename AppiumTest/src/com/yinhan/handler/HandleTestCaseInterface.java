package com.yinhan.handler;
/**
 * 
 * @author susi
 *
 */
public interface HandleTestCaseInterface {
	//开始跑测试用例的入口
	void startRunning();
	//解析测试用例文件
	void parseTestCase();
	//执行测试用例
	void excuteAction(String[] actions);
	//得到结果
	boolean getTestResult(String[] result);

}
