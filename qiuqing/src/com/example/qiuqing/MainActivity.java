package com.example.qiuqing;

import org.json.JSONObject;

import com.example.qiuqing.net.IJSONResponseCallback;
import com.yunmall.ymsdk.net.callback.ResponseCallbackImpl;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				CaseManager.verifyCode(MainActivity.this, new ResponseCallbackImpl<PhoneICEntity>() {
//					
//					@Override
//					public void onSuccess(PhoneICEntity baseData) {
//						if (baseData.statusCode.equals("200")){
//                            Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(MainActivity.this, baseData.message.toString(), Toast.LENGTH_SHORT).show();
//                        }						
//					}
//					
//					@Override
//					public void onFailed(Throwable error, int statusCode) {
//						Toast.makeText(MainActivity.this, "网络异常1"+statusCode, Toast.LENGTH_SHORT).show();
//					}
//				});
				
//				CaseManager.sendMsg(phone, code, callback, con, isCache)
//				Toast.makeText(MainActivity.this, "dddd", Toast.LENGTH_SHORT).show();
				
//				http://192.168.16.184:8921/v2.1/rest/util/verifyCode/15507487039/1234?api_key=wvP4Egy79SqChhZm
				
				CaseManager.sendMsg("18310391875", "6067", new IJSONResponseCallback() {
		            @Override
		            public void handleResponse(JSONObject response) {
		            	Log.i("AAAA", "response:"+response.toString());
		            }

		            @Override
		            public void handleError(int code) {
		            	Log.i("AAAA", "ssssssssssss");
		            }
		        }, MainActivity.this, false);
			}
		});
    }
}
