package com.yinhan.surface;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import com.yinhan.data.TestCaseBean;
import com.yinhan.util.Config;
import com.yinhan.util.HelpUtil;

public class AppiumSurface extends Applet{

	ArrayList<PanelBean> panelBeanList = new ArrayList<PanelBean>();
	
    public void init(){
    	
    	JButton addPanelBtn = new JButton();
    	JButton saveBtn = new JButton();
    	
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
         
    	add(addPanelBtn);
    	add(saveBtn);
    	
    	
    	
    	ArrayList<String[]> tempTestCaseList;
		try {
			tempTestCaseList = HelpUtil.ReadFileManager(
					Config.SURFACETESTCASEPATH+Config.TESTCASECONFIGFILE, Config.TABSIGN);
			for (int i = 0; i < tempTestCaseList.size(); i++) {
				TestCaseBean testCaseBean=new TestCaseBean();
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

		
		
        setSize(1144,990);
    }
    
    

    private void addPanelBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    	addPanel(new TestCaseBean());
		validate();
    }                                       

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {   
    	
    	System.out.println("save testcase file!!");
        // TODO add your handling code here:
    	
    	
    	
    	boolean isAppendWrite = false;
    	for(int i =0;i<panelBeanList.size();i++)
    	{
    		panelBeanList.get(i).updateData();
    		
    		String testCaseString="";
    		if (i > 0) {
				isAppendWrite = true;
			}
    		String actionString="";
    		for(int j=0;j<panelBeanList.get(i).getTestcaseBean().getAcitons().length;j++)
    		{
    			if(j==panelBeanList.get(i).getTestcaseBean().getAcitons().length-1)
    			{
    				actionString=actionString+panelBeanList.get(i).getTestcaseBean().getAcitons()[j];
    			}else
    			{
    				actionString=actionString+panelBeanList.get(i).getTestcaseBean().getAcitons()[j]+Config.SEMICOLON;
    			}
    			 
    		}
    		
    	
    		testCaseString=panelBeanList.get(i).getTestcaseBean().getId()+Config.TABSIGN
    				+panelBeanList.get(i).getTestcaseBean().getInstruction()+Config.TABSIGN
    				+actionString+Config.TABSIGN
    				+panelBeanList.get(i).getTestcaseBean().getTestResult()[0]+Config.COLON
    						+panelBeanList.get(i).getTestcaseBean().getTestResult()[1]
    				;
    		
    		
    		
    		// 写入结果
			try {
				HelpUtil.WriteFileManager(Config.SURFACETESTCASEPATH+Config.TESTCASECONFIGFILE, testCaseString, isAppendWrite);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    	
    } 
    
    private void addPanel(TestCaseBean testCaseBean)
    {
    	PanelBean panelBean = new PanelBean();
    	panelBean.setTestcaseBean(testCaseBean);
    	panelBean.initComponents();
		add(panelBean);
		panelBeanList.add(panelBean);
    }
    
}
