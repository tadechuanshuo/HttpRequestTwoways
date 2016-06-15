package com.example.qiuqing.net;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JSONClient
{
	private static final String TAG = JSONClient.class.getName();
	private static JSONClient jsonClient;
	private final ExecutorService m_sendPool;
	private static final int connect_out_time = 30 * 1000;
	private static final int read_out_time = 40 * 1000;
	public static final String pwd_of_des_secret = "ceshi";

	/**
	 * Constructor
	 */
	private JSONClient()
	{
		m_sendPool = Executors.newCachedThreadPool();
//		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//		System.setProperty("sun.net.client.defaultReadTimeout", "30000");
	}

	public static JSONClient getInstance()
	{
		if(jsonClient == null)
			jsonClient = new JSONClient();
		return jsonClient;
	}

	public void cancelInstance()
	{
		if(jsonClient != null) jsonClient = null;
		if(m_sendPool != null && !m_sendPool.isShutdown()) m_sendPool.shutdown();
	}

	/**
	 * 取缓存
	 */
	private void sendCacheRequest(JSONRequest request,IJSONResponseCallback handler,Context con) throws Exception
	{
		Log.i(TAG, "sendCacheRequest取缓存");
		JSONObject jsonData = null;
//		String[] jsonString = CacheViewUtil.getCache(request.getMethod());
//
//		if (jsonString != null && jsonString[0] != null && jsonString[0].length() > 0)
//			jsonData = new JSONObject(jsonString[0]);
//		else
//			jsonData = new JSONObject();

		handler.handleResponse(jsonData);
	}

	/**
	 * 请求网络
	 */
	private void sendNetWorkRequest(JSONRequest request,IJSONResponseCallback callback,Context con) throws Exception
	{
		Log.i(TAG, "sendNetWorkRequest");
		ByteArrayOutputStream baos = null;
		InputStream is = null;
		String url = null;
		boolean isPost = request.isM_isPost();

		try
		{
			url = request.getMethod();

			if(!isPost)
			{
				url = url + setParameters(request,con);
			}

			Log.i(TAG, "url = " + url);

			URL url2 = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)url2.openConnection();
			conn.setConnectTimeout(connect_out_time);
			conn.setReadTimeout(read_out_time);
			conn.setRequestMethod(isPost ? "POST" : "GET");
			conn.setRequestProperty("Charset", "UTF-8");
//				conn.setRequestProperty("User-agent","Mozilla/4.0");
//				conn.setRequestProperty("Connection", "Keep-Alive");
//			if (!isWifiEnable(con) && conn != null)
//			{
//				conn = setHttpProxy(conn,con);
//			}

			if(isPost)
			{
				conn.setDoInput(true);
				conn.setDoOutput(true);
				String params = setParametersBySecret(request, con);
				params = params.replace("?", "&");
				conn.connect();

				PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"GB2312"));
				out.print(params);
				out.flush();
				out.close();
			}

			conn.connect();

			Log.i(TAG, "waiting for connection response!");

			// 返回成功
			if (conn != null && conn.getResponseCode() == HttpStatus.SC_OK)
			{
				Log.i(TAG, "connection response is ok!");

				byte[] data = new byte[1024];
				int count = 0;
				is = conn.getInputStream();
				baos = new ByteArrayOutputStream();

				while ((count = is.read(data)) > 0)
				{
					baos.write(data, 0, count);
				}
			}
			// 返回失败
			else
			{
				// call onError() with the return code. Kari130709
				Log.i(TAG, "sendNetWorkRequest onError()");

				if (callback != null)
				{
					callback.handleError(0);
				}

				return;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
//				setBiaduConnection(false);
			if (!(e instanceof InterruptedException))
			{
				if (callback != null)
				{
					Log.i(TAG, "网络连接错误！错误代码 -1! " + e.getMessage());
					callback.handleError(-1);
				}
				return;
			}
		}
		finally
		{
			if (baos != null)
				baos.close();

			if (is != null)
				is.close();
		}

		if (baos == null)
		{
			Log.i(TAG, "返回失败 baos == null");
			if (callback != null)
				callback.handleError(-1);
			return;
		}

		try
		{
			String responseString = baos.toString();
			Log.i(TAG, "调用接口成功！返回数据：responseString:" + responseString);
			responseString = responseString.trim();

			if(!request.isM_Secert() && request.isM_isPost())
			{
				JSONObject jObject = xml2JSON(responseString);
				jObject.put("code","1");
				callback.handleResponse(jObject);
			}
			else
			{
				if (responseString.indexOf("{") < 0)
				{
					callback.handleError(-1);
				}
				else
				{
//					CLog.info(con, "!!!!!!!!!    baos != null  =  " + baos.toString());
					String ret = responseString.substring(responseString.indexOf("{"));
					JSONObject jsonResponse = new JSONObject(ret);
					callback.handleResponse(jsonResponse);
				}
			}
		}
		catch (JSONException e)
		{
			Log.i(TAG, "sendNetWorkRequest bottom JSONException error! " + e.getMessage());
			if (callback != null)
				callback.handleError(-1);
			return;
		}
		catch (Exception e)
		{
			Log.i(TAG, "sendNetWorkRequest bottom error! " + e.getMessage());
		}
	}

	/**
	 * 图片上传
	 */
	private void uploadRequest(JSONRequest request, IJSONResponseCallback callback, Context con) throws Exception
	{
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716";
		try {
			Log.i(TAG, "url = "+request.getMethod());
			URL url = new URL(request.getMethod() + setParameters(request,con));
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(connect_out_time);
			conn.setReadTimeout(read_out_time);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());

			Map<String,String> fileMap = request.getFileMap();

			// file
			if (fileMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append(
							"\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					out.write(strBuf.toString().getBytes());

					File file = new File(inputValue);

					DataInputStream in = new DataInputStream( new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。");
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}

		Log.i(TAG, "upload response data == "+res);

		if(res != null && res.length() > 0 && res.indexOf("{") != -1)
		{
			JSONObject jsonResponse = new JSONObject(res);
			callback.handleResponse(jsonResponse);
		}
		else
		{
			callback.handleError(-1);
		}
	}

//	private void uploadRequest(JSONRequest request,IJSONResponseCallback callback,Context con) throws Exception
//	{
//		CLog.info(con, "uploadRequest");
//		String urlStr = request.getMethod() + setParameters(request,con);
//		HttpPost httpPost = new HttpPost(urlStr);
//
//		httpPost.setHeader("User-Agent","SOHUWapRebot");
//		httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.5");
//		httpPost.setHeader("Accept-Charset","UTF-8;q=0.7,*;q=0.7");
//		httpPost.setHeader("Connection","keep-alive");
//
//		InputStreamBody inputStreamBody = new InputStreamBody(request.getInputStream(),"android_inputstream.png");
//
//		MultipartEntity mEntity = new MultipartEntity();
//		mEntity.addPart("file", inputStreamBody);
//
//		HttpClient httpClient = new DefaultHttpClient();
//		httpPost.setEntity(mEntity);
//		HttpResponse httpResponse = httpClient.execute(httpPost);
//		HttpEntity httpEntity =  httpResponse.getEntity();
//		String responseString = EntityUtils.toString(httpEntity);
//		final int statusCode = httpResponse.getStatusLine().getStatusCode();
//
//		if (statusCode == HttpStatus.SC_OK)
//		{
//			try
//			{
//				responseString = responseString.trim();
//
//				if(!request.isM_Secert() && request.isM_isPost())
//				{
//					JSONObject jObject = MethodUtil.xml2JSON(responseString);
//					jObject.put("code","1");
//					callback.handleResponse(jObject);
//				}
//				else
//				{
//					if (responseString.indexOf("{") < 0)
//					{
//						callback.handleError(-1);
//					}
//					else
//					{
//						CLog.info(con, "!!!!!!!!!    baos != null  =  " + responseString);
//						String ret = responseString.substring(responseString.indexOf("{"));
//						JSONObject jsonResponse = new JSONObject(ret);
//						callback.handleResponse(jsonResponse);
//					}
//				}
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		}
//
//		String CONTENT_TYPE = "multipart/form-data";//"application/octet-stream";
//		ByteArrayOutputStream baoStream = null;
//		InputStream is = null;
//		String urlStr = null;
//
//		try
//		{
//			urlStr = request.getMethod() + setParameters(request,con);
//			URL url = new URL(urlStr);
//			CLog.info(con, "url = " + url);
//			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//			conn.setConnectTimeout(connect_out_time);
//			conn.setReadTimeout(read_out_time);
//			conn.setDoInput(true);
//			conn.setDoOutput(true);
//			conn.setUseCaches(false);
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Charset", "UTF-8");
//			conn.setRequestProperty("Content-Type", CONTENT_TYPE);
//
//			File file = new F
//
//			MultipartEntity multipartEntity = new MultipartEntity();
//
//
//			conn.connect();
//
//			DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
//			InputStream input = request.getInputStream();
//
//			outStream.writeBytes("--" + boundary + "\r\n");
//			outStream.writeBytes("Content-Disposition: form-data; name=\"" + "file-name"
//					+ "\"; filename=\"" + "fname" + "\"\r\n");
//			outStream.writeBytes("Content-Type: " + CONTENT_TYPE + "\r\n");
//			outStream.writeBytes("\r\n");
////			outStream.write(getBytes(value));
////			outStream.writeBytes("\r\n");
//
//
//			int size = 1024;
//			byte[] buffer = new byte[size];
//			int length = -1;
//			while((length = input.read(buffer)) != -1)
//			{
//				outStream.write(buffer, 0, length);
//			}
//
//			if(input != null)
//			{
//				try
//				{
//					input.close();
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//
//			if(outStream != null)
//			{
//				try
//				{
//					outStream.flush();
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//
//			try
//			{
//				int retCode = conn.getResponseCode();
//
//				if(retCode == 200)
//				{
//					CLog.error(con, "url连接成功!!");
//					byte[] data = new byte[1024];
//					is = conn.getInputStream();
//					baoStream = new ByteArrayOutputStream();
//
//					int count = 0;
//					while ((count = is.read(data)) > 0)
//					{
//						baoStream.write(data, 0, count);
//					}
//				}
//			}
//			catch(Exception e)
//			{
//				//写的时候没有异常  读的时候发生异常，此处认为上传成功
//				JSONObject jsonResponse = new JSONObject();
//				jsonResponse.put("rsp", 0);
//				jsonResponse.put("msg", "success");
//				callback.handleResponse(jsonResponse);
//				return;
//			}
//
//			outStream.close();
//			System.gc();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			callback.handleError(-1);
//		}
//		finally
//		{
//			if (baoStream != null)
//			{
//				CLog.error(con, "baos != null");
//				try
//				{
//					baoStream.close();
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//			if (is != null)
//			{
//				try
//				{
//					is.close();
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//		}
//
//		if (baoStream == null)
//		{
//			CLog.error(con, "baos == null");
//			callback.handleError(-1);
//			return;
//		}
//
//		try
//		{
//			String responseString = baoStream.toString();
//			responseString = responseString.trim();
//
//			if(!request.isM_Secert() && request.isM_isPost())
//			{
//				JSONObject jObject = MethodUtil.xml2JSON(responseString);
//				jObject.put("code","1");
//				callback.handleResponse(jObject);
//			}
//			else
//			{
//				if (responseString.indexOf("{") < 0)
//				{
//					callback.handleError(-1);
//				}
//				else
//				{
//					CLog.info(con, "!!!!!!!!!    baos != null  =  " + baoStream.toString());
//					String ret = responseString.substring(responseString.indexOf("{"));
//					JSONObject jsonResponse = new JSONObject(ret);
//					callback.handleResponse(jsonResponse);
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private String setParametersByQQ(JSONRequest request)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

		for (String key : request.getM_parameters().keySet())
		{
			params.add(new BasicNameValuePair(key, (String) request.getM_parameters().get(key)));
		}

		return URLEncodedUtils.format(params, "UTF-8");
	}

	private String setParametersBySecret(JSONRequest request,Context con)
	{
		String string = "?";
		try
		{
			if (request.getM_parameters().size() > 0)
			{
				for (String key : request.getM_parameters().keySet())
				{
//					String value = URLEncoder.encode((String) request.getM_parameters().get(key), "UTF-8");
					String value = (String) request.getM_parameters().get(key);
					Log.i(TAG, "key = " + key + "  value = " + value);
					string = string + key + "=" + value +"&";
				}
			}

			if(string.lastIndexOf("&") != -1)
				string = string.substring(0,string.lastIndexOf("&"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return string.trim().replace(" ", "%20");
	}

	private String setParameters(JSONRequest request,Context con)
	{
		String st = "?v=";

		try
		{
			String string = "";

			if (request.getM_parameters().size() > 0)
			{
				for (String key : request.getM_parameters().keySet())
				{
//					String value = URLEncoder.encode((String) request.getM_parameters().get(key), "UTF-8");
					String value = (String) request.getM_parameters().get(key);
					Log.i(TAG, "key = " + key + "  value = " + value);
					string = string + key + "=" + value +"&";
				}
			}

			if(string.lastIndexOf("&") != -1)
			{
				string = string.substring(0, string.lastIndexOf("&"));
			}

			string = removeStringFormatEmoji(string);
//			string = string.trim().replace(" ", "%20");
			if(request.isM_Secert())
			{
				Log.i(TAG, "加密前 = "+string);
				string = DES.encrypt(string, pwd_of_des_secret).replace(" ", "").replace("+", "%2B");
				Log.i(TAG, "加密后 = "+string);
			}
			else
			{
				st = "?";
			}

			st = st + string;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return st;
	}

	// support wap connection
	private boolean isWifiEnable(Context ctx)
	{
		boolean ret = true;
		WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager == null || !wifiManager.isWifiEnabled())
			ret = false;
		return ret;
	}

	/**
	 * 设置代理
	 */
	private HttpURLConnection setHttpProxy(HttpURLConnection conn,Context con)
	{
		Log.i(TAG, "setHttpProxy android wifi is enable");

		Cursor mCursor = null;

		try
		{
			Uri uri = Uri.parse("content://telephony/carriers/preferapn"); // get current APN access point
			mCursor = con.getContentResolver().query(uri, null, null, null, null);

			if (mCursor != null && mCursor.moveToFirst()) // move the cursor to the first record ,only one of course
			{
				String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
				String portStr = mCursor.getString(mCursor.getColumnIndex("port"));

				if (proxyStr != null && proxyStr.trim().length() > 0)
				{
					conn.setRequestProperty(ConnRouteParams.DEFAULT_PROXY,portStr);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(mCursor != null)
			{
				mCursor.close();
			}
		}

		return conn;
	}

	public void sendRequest(JSONRequest request,IJSONResponseCallback callback,Context con) throws InterruptedException
	{
		m_sendPool.submit(new sendRunnable(request,callback,con));
	}

	/**
	 * 发送请求
	 */
	private class sendRunnable implements Runnable
	{
		JSONRequest request = null;
		IJSONResponseCallback callback = null;
		Context con;

		public sendRunnable(JSONRequest request, IJSONResponseCallback callback,Context con)
		{
			this.request = request;
			this.callback = callback;
			this.con = con;
		}

		public void run()
		{
			try
			{
				if(request != null)
				{
					// 如果要取缓存，则去取缓存内容,随后刷新，并通知界面加载数据，如果没有缓存，返回可以解析的空的JSONObject。
					if (request.isM_isCache())
						sendCacheRequest(request,callback,con);
					else if(request.isM_isUpload() && request.isM_isUpload() && request.getFileMap() != null && request.getFileMap().size() > 0)
						uploadRequest(request,callback,con);
					else
						sendNetWorkRequest(request,callback,con);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Log.i(TAG, "sendRunnable run() error!");
			}
		}
	}
	
	/**
	 * 转换一个xml格式的字符串到json格式
	 *
	 * @param xml xml格式的字符串
	 * @return 成功返回json 格式的字符串;失败反回null
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject xml2JSON(String xml)
	{
		JSONObject jsonObj = null;

		try
		{
			jsonObj = XML.toJSONObject(xml);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return jsonObj;
	}
	
	/**
	 * 替换字符串中的表情符号
	 */
	public static String removeStringFormatEmoji(String source) {

		if (!containsEmoji(source)) {
			return source;
		}
		//到这里铁定包含
		StringBuilder buf = null;

		int len = source.length();

		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);

			if (isEmojiCharacter(codePoint)) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}

				buf.append(codePoint);
			}
			else if(i%2==0)
			{
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}

				buf.append("☺");
			}
		}

		if (buf == null) {
			return source;//如果没有找到 emoji表情，则返回源字符串
		} else {
			if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
				buf = null;
				return source;
			} else {
				return buf.toString();
			}
		}
	}
	
	/**
	 * 是否包含表情字符
	 */
	private static boolean containsEmoji(String source) {
		if (isStringEmpty(source)) {
			return false;
		}

		int len = source.length();

		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);

			if (isEmojiCharacter(codePoint)) {
				//do nothing，判断到了这里表明，确认有表情字符
				return true;
			}
		}

		return false;
	}
	
	/**
	 * 判断字符串是否是空或空串
	 */
	public static boolean isStringEmpty(String str)
	{
		boolean isEmpty = true;

		if(str != null)
		{
			str = str.trim();

			if(str.length() > 0)
			{
				isEmpty = false;
			}
		}

		return isEmpty;
	}
	
	/**
	 * 是否是表情字符
	 */
	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) ||
				(codePoint == 0x9) ||
				(codePoint == 0xA) ||
				(codePoint == 0xD) ||
				((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
				((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
				((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}
}