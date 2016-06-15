package com.enumeration.example;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.MyEnumAdapterFactory;

public class Test {
	public static void main(String[] args) {
		List<Student> stus = new ArrayList<Student>();
		
		Teacher mTeacher = new Teacher();
		mTeacher.setStudents(stus);;
		for (int i = 0; i < 5; i++) {
			Student stu = new Student();
			stu.setStuId(i);
			stu.setStuName("stu" + i);
			stu.setmDegree(Degree.Low);
			stu.setmSex(Sex.Women);
			 mTeacher.getStudents().add(stu);
//			stus.add(stu);
		}
		mTeacher.setStudentCount(5);

		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapterFactory(new MyEnumAdapterFactory());
		Gson gson = gb.create();

		String mJsonStr = gson.toJson(mTeacher);
		// Log.d("json is====>", mJsonStr); //��ӡ��ת�����Jsonֵ

		System.out.println("json is====>" + "  " + mJsonStr);
		Teacher teacher = gson.fromJson(mJsonStr, Teacher.class);
		// Log.d("DeSerilize ok===>", "Sex is:"+
		// teacher.getStudents().get(0).getmSex()+ " Degree is:"+
		// teacher.getStudents().get(0).getmDegree());
		System.out.println("DeSerilize ok===>" + "Sex is:" + "   "
				+ teacher.getStudents().get(0).getmSex() + " Degree is:"
				+ teacher.getStudents().get(0).getmDegree());
	}
}
