package com.enumeration.example;

public enum Degree implements IEnum {
	High(1), Middle(2), Low(3);

	private int value;

	private Degree(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	
	
	
	@Override
	public void setValue(int value) {
		this.value = value;
	}
}
