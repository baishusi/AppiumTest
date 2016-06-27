package com.yinhan.surface;

import com.yinhan.util.Config;
import com.yinhan.util.HelpUtil;

public class RunThread extends Thread{
	private String name;
    public RunThread(String name) {
       this.name=name;
    }
	public void run() {
		Config.log.info("begin run command " +name);
       HelpUtil.RunCommand(name);
       
	}
}