package com.yinhan.data;


public class TestCaseBean {

	private String id;
	private String instruction;
	private String[] acitons;
	private String[] testResult;



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String[] getAcitons() {
		return acitons;
	}

	public void setAcitons(String[] acitons) {
		this.acitons = acitons;
	}

	public String[] getTestResult() {
		return testResult;
	}

	public void setTestResult(String[] testResult) {
		this.testResult = testResult;
	}

}
