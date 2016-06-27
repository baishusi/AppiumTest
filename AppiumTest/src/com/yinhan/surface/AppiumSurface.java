package com.yinhan.surface;

import java.applet.Applet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import com.yinhan.data.SurConfigBean;
import com.yinhan.data.TestCaseBean;
import com.yinhan.util.Config;
import com.yinhan.util.HelpUtil;

@SuppressWarnings("serial")
public class AppiumSurface extends Applet {

	ArrayList<PanelBean> panelBeanList = new ArrayList<PanelBean>();

	private ArrayList<SurConfigBean> surConfigList = new ArrayList<SurConfigBean>();
	
	private JButton addPanelBtn;
	private JButton saveBtn;
	private JButton runBtn;
	private JMenu menu;
	private JMenuItem createTestcase;
	private JMenu choseTestcase;
	private JTextField descTextField;
	private ArrayList<JMenuItem> chooseJMenuItemList=new ArrayList<JMenuItem>();
	
	private boolean isCreateFile=false;
	
	

	private void initialSurConfig() {
		Config.log.info("initial surconfig file");
		try {
			ArrayList<String[]> tempList = HelpUtil.ReadFileManager(
					Config.SURFACETESTCASEPATH + Config.SURFACECONFIGFILE,
					Config.TABSIGN);
			for (int i = 0; i < tempList.size(); i++) {
				SurConfigBean surConfig = new SurConfigBean();
				surConfig.setSurConfigId(i);
				surConfig.setTestcaseDesc(tempList.get(i)[0]);
				surConfig.setTestcasefileName(tempList.get(i)[1]);
				surConfigList.add(surConfig);
			}
			
			HelpUtil.copyFile(Config.SURFACETESTCASEPATH
					+ Config.ALLTESTCAEFILEPATHPRE
					+ surConfigList.get(0).getTestcasefileName(),
					Config.SURFACETESTCASEPATH + Config.TESTCASECONFIGFILE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initialSurface() {
		Config.log.info("initial surface show");
		 addPanelBtn = new JButton();
		 saveBtn = new JButton();
		 runBtn = new JButton();
		
		choseTestcase = new JMenu("选择测试用例");
		menu = new JMenu("选项");
		createTestcase = new JMenuItem("新建测试用例");
		menu.add(createTestcase);
		menu.add(choseTestcase);

		createTestcase.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createTestcaseActionPerformed(evt);
			}
		});

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);

		for (int i = 0; i < surConfigList.size(); i++) {
			final int testCaseNum = surConfigList.get(i).getSurConfigId();
			JMenuItem testcaseList= new JMenuItem(surConfigList.get(i)
					.getTestcaseDesc());
			
			testcaseList.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					chooseTestCaseActionPerformed(evt, testCaseNum);
				}
			});
			choseTestcase.add(testcaseList);
			
			chooseJMenuItemList.add(testcaseList);
		}
		addPanelBtn.setText("add");
		addPanelBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addPanelBtnActionPerformed(evt);
			}
		});

		saveBtn.setText("save");
		saveBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveBtnActionPerformed(evt);
			}
		});

		runBtn.setText("run");
		runBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				runBtnActionPerFormed(evt);
			}
		});

		
		descTextField=new JTextField(20);
		descTextField.setText(surConfigList.get(0).getTestcaseDesc());
		
		add(menuBar);
		add(addPanelBtn);
		add(saveBtn);
		add(runBtn);
		add(descTextField);
		
		
		
	}

	public void init() {
		Config.log.setLevel(Level.INFO); 
		initialSurConfig();

		initialSurface();

		updateTestCaseSurface(); 

		setSize(1144, 990);
	}
	
	public void stop() {
		Config.log.info("stop applet and save surconfig file");
		for(int i =0;i<surConfigList.size();i++)
		{
			boolean isAppend=false;
			if(i>0)
				isAppend=true;
			String surConfigString="";
			surConfigString=surConfigString+surConfigList.get(i).getTestcaseDesc()+Config.TABSIGN+surConfigList.get(i).getTestcasefileName();
			try {
				HelpUtil.WriteFileManager(Config.SURFACETESTCASEPATH + Config.SURFACECONFIGFILE, surConfigString, isAppend);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void updateTestCaseSurface() {
		Config.log.info("update the detail testcase list");
		ArrayList<String[]> tempTestCaseList;
		try {
			tempTestCaseList = HelpUtil.ReadFileManager(
					Config.SURFACETESTCASEPATH + Config.TESTCASECONFIGFILE,
					Config.TABSIGN);
			for (int i = 0; i < tempTestCaseList.size(); i++) {
				TestCaseBean testCaseBean = new TestCaseBean();
				testCaseBean.setId(tempTestCaseList.get(i)[0]);
				testCaseBean.setInstruction(tempTestCaseList.get(i)[1]);
				testCaseBean.setAcitons(tempTestCaseList.get(i)[2]
						.split(Config.SEMICOLON));
				testCaseBean.setTestResult(tempTestCaseList.get(i)[3]
						.split(Config.COLON));

				addPanel(testCaseBean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addPanelBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		addPanel(new TestCaseBean());
		validate();
	}

	private void createTestcaseActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		isCreateFile=true;
		descTextField.setText("");
		try {
			HelpUtil.RemoveAndMakeFile(Config.SURFACETESTCASEPATH
					+ Config.TESTCASECONFIGFILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < panelBeanList.size(); i++) {
			remove(panelBeanList.get(i));
			validate();
		}
		panelBeanList.clear();
	}

	private void updateSurConfigList(int testCaseNum)
	{
		Config.log.info("change the chose item and first item content");
		if(testCaseNum == 0)
			return;
		String tempString=surConfigList.get(0).getTestcaseDesc();
		surConfigList.get(0).setTestcaseDesc(surConfigList.get(testCaseNum).getTestcaseDesc());
		surConfigList.get(testCaseNum).setTestcaseDesc(tempString);
		
		tempString=surConfigList.get(0).getTestcasefileName();
		surConfigList.get(0).setTestcasefileName(surConfigList.get(testCaseNum).getTestcasefileName());
		surConfigList.get(testCaseNum).setTestcasefileName(tempString);
		
		int tempInt = surConfigList.get(0).getSurConfigId();
		surConfigList.get(0).setSurConfigId(surConfigList.get(testCaseNum).getSurConfigId());
		surConfigList.get(testCaseNum).setSurConfigId(tempInt);		
	}
	
	private void chooseTestCaseActionPerformed(java.awt.event.ActionEvent evt,
			int testCaseNum) {
		// TODO add your handling code here:
		
		for(int i = 0;i<surConfigList.size();i++)
		{
			if(surConfigList.get(i).getSurConfigId()==testCaseNum)
			{
				testCaseNum=i;
				break;
			}
		}
		Config.log.info("choose testcase"
				+ surConfigList.get(testCaseNum).getTestcasefileName());
		
		isCreateFile=false;
		try {
			HelpUtil.copyFile(Config.SURFACETESTCASEPATH
					+ Config.ALLTESTCAEFILEPATHPRE
					+ surConfigList.get(testCaseNum).getTestcasefileName(),
					Config.SURFACETESTCASEPATH + Config.TESTCASECONFIGFILE);
			
			for (int i = 0; i < panelBeanList.size(); i++) {
				remove(panelBeanList.get(i));
				validate();
			}
			panelBeanList.clear();
			
			
			updateSurConfigList(testCaseNum);
			descTextField.setText(surConfigList.get(0).getTestcaseDesc());
			
			updateTestCaseSurface();
			validate();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void runBtnActionPerFormed(java.awt.event.ActionEvent evt) {
		RunThread mTh1=new RunThread("cmd /c start "+Config.SURFACETESTCASEPATH+"运行Appium服务器.bat");
		RunThread mTh2=new RunThread("cmd /c start "+Config.SURFACETESTCASEPATH+"运行测试用例.bat");
		mTh1.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTh2.start();
		
	}
	
	private void showDiaglog(String content)
	{
			JDialog dlg = new JDialog(); 
			dlg.getContentPane().add(new JButton(content));
			dlg.setSize(160,80);
			dlg.setLocation(450,450);
			dlg.show();
			return;
	}
	

	private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {
		
		// TODO add your handling code here:

		boolean isAppendWrite = false;
		if(panelBeanList.size()==0)
		{
			showDiaglog("没填完整内容啊");
			return;
		}
		if("".equals(descTextField.getText()))
		{
			showDiaglog("没填完整内容啊");
			return;
		}
		for (int i = 0; i < panelBeanList.size(); i++) {
			panelBeanList.get(i).updateData();

			if(panelBeanList.get(i).getTestcaseBean().getAcitons() == null)
			{
				showDiaglog("没填完整内容啊");
				return;
			}
			if(panelBeanList.get(i).getTestcaseBean().getTestResult() == null)
			{
				showDiaglog("没填完整内容啊");
				return;
			}
			
			String testCaseString = "";
			if (i > 0) {
				isAppendWrite = true;
			}
			String actionString = "";
			for (int j = 0; j < panelBeanList.get(i).getTestcaseBean()
					.getAcitons().length; j++) {
				if (j == panelBeanList.get(i).getTestcaseBean().getAcitons().length - 1) {
					actionString = actionString
							+ panelBeanList.get(i).getTestcaseBean()
									.getAcitons()[j];
				} else {
					actionString = actionString
							+ panelBeanList.get(i).getTestcaseBean()
									.getAcitons()[j] + Config.SEMICOLON;
				}

			}

			testCaseString = panelBeanList.get(i).getTestcaseBean().getId()
					+ Config.TABSIGN
					+ panelBeanList.get(i).getTestcaseBean().getInstruction()
					+ Config.TABSIGN + actionString + Config.TABSIGN
					+ panelBeanList.get(i).getTestcaseBean().getTestResult()[0]
					+ Config.COLON
					+ panelBeanList.get(i).getTestcaseBean().getTestResult()[1];

			// 写入结果
			try {
				HelpUtil.WriteFileManager(Config.SURFACETESTCASEPATH
						+ Config.TESTCASECONFIGFILE, testCaseString,
						isAppendWrite);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		try {
			HelpUtil.copyFile(Config.SURFACETESTCASEPATH
					+ Config.TESTCASECONFIGFILE, Config.SURFACETESTCASEPATH+Config.ALLTESTCAEFILEPATHPRE+surConfigList.get(0).getTestcasefileName());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		if(isCreateFile)
		{
			SurConfigBean surConfig=new SurConfigBean();
			surConfig.setSurConfigId(surConfigList.size());
			surConfig.setTestcaseDesc(descTextField.getText());
			surConfig.setTestcasefileName("testcase"+(surConfigList.size()+1)+".txt");
			surConfigList.add(surConfig);
			
			try {
				HelpUtil.copyFile(Config.SURFACETESTCASEPATH
								+ Config.TESTCASECONFIGFILE, Config.SURFACETESTCASEPATH+Config.ALLTESTCAEFILEPATHPRE+"testcase"+surConfigList.size()+".txt");
				updateSurConfigList(surConfigList.size()-1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isCreateFile=false;
		}
		
	}

	private void addPanel(TestCaseBean testCaseBean) {
		PanelBean panelBean = new PanelBean();
		panelBean.setTestcaseBean(testCaseBean);
		panelBean.initComponents();
		add(panelBean);
		panelBeanList.add(panelBean);
	}

}
