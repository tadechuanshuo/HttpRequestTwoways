package com.example.qiuqing.request;

public interface CommandInterface {
	
	String E_UPLOADING_CASE_IMAGE = "eyisheng/againdiagnosisapi/uploadcaseimage";//图片上传接口
	String E_UPLOADING_CASE_HISTORY = "eyisheng/againdiagnosisapi/diagnosis";//文字上传接口
	String E_CHOICE_DEPARTMENT = "eyisheng/patientapi/getdepartment";//获取科室列表
	String E_SUBSEQUENT_VISIT = "eyisheng/againdiagnosisapi/caselist";//获取病例复诊列表
//	                               eyisheng/recorddoctor/caselist
	String E_CLINICAL_RECEPTION = "eyisheng/againdiagnosisapi/accepts";//接诊
	String E_MY_SUBSEQUENT_VISIT = "eyisheng/againdiagnosisapi/mycaselist";//我的复诊
	String E_PATIENT_DETAILS = "eyisheng/againdiagnosisapi/caseinfo";//患者详情
	String E_CASECLIP_LIST = "eyisheng/recorddoctor/recordlist"; // 病历夹列表
	String E_CASECLIP_REMINDER = "eyisheng/recorddoctor/promptbox";//病历夹上方提示数据
	String E_CASECLIP_TYPE_SUM = "eyisheng/recorddoctor/topscreeningnum";//病历夹各种类型数量
	String E_SAVE_PATIENTS_INFO = "eyisheng/thirdphase/savepatientsinfo";//获取病例信息
	String E_ADD_CASE_TITLE = "eyisheng/thirdphase/recordtitle";
	String E_CASE_DETAILS = "eyisheng/thirdphase/myrecordlistinfo"; //病例详情
	String E_ADD_DISEASE_COURSE = "eyisheng/thirdphase/savediseasecourse";//添加病程
	String E_ADD_DISEASE_COURSE_VOICE = "eyisheng/thirdphase/saveupload";//添加音频和图片
	String E_DEL_DISEASE_COURSE_VOICE = "eyisheng/thirdphase/delimgovi"; //删除音频或图片
	String E_SAVE_DIAGNOSE = "eyisheng/thirdphase/savediagnosis";//保存诊断
	String E_SAVE_ILLNESS = "eyisheng/thirdphase/saveillness";//保存基本病情
	
	String E_CASECIRCLE_SUM = "eyisheng/recorddoctor/recordnum";//病历圈数量
	String E_CASECIRCLE_LIST = "eyisheng/recorddoctor/caselist";//病历圈列表
	
	String E_BANNER_INFO = "eyisheng/patientapi/advertising";//AD轮播图
	String E_CASE_DISEASE = "eyisheng/recorddoctor/diagnosis";//填写诊断
	String E_CASE_SUBMIT = "eyisheng/recorddoctor/submitdisease";//提交病例
	
	
	String E_HANXIN_ACCOUNTANDPASSWORD = "eyisheng/im/getimreguser"; //获取环信的账号密码
    String E_HANXIN_DOCTOR_ACCOUNTANDPASSWORD = "eyisheng/im/getimreguserdoctor"; //获取环信的账号密码
    String E_HANXIN_CONTACTLIST = "eyisheng/im/getdoctorcontactList"; //获取环信的好友列表
    
    String E_SCHEDULING_INFO = "eyisheng/doctor/appschedule";
    String E_EDIT_SCHEDULING = "eyisheng/doctor/scheduleedit";
    
    String EDIT_SCHEDULING = "util/verifyCode/18310391875/5328";
}
