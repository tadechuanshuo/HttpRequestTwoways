package com.enumeration.example;

public enum Sex implements IEnum {
	Man(1), Women(2);

	private int value;

	private Sex(int value) {
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


