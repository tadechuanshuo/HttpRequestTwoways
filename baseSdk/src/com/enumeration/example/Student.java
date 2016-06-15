package com.enumeration.example;

public class Student {
	 private String stuName;
     private int stuId;
     public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public int getStuId() {
		return stuId;
	}
	public void setStuId(int stuId) {
		this.stuId = stuId;
	}
	public Sex getmSex() {
		return mSex;
	}
	public void setmSex(Sex mSex) {
		this.mSex = mSex;
	}
	public Degree getmDegree() {
		return mDegree;
	}
	public void setmDegree(Degree mDegree) {
		this.mDegree = mDegree;
	}
	private Sex mSex;
     private Degree mDegree;

}
