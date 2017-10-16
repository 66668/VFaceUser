package com.vfaceuser.utils;

import com.vfaceuser.R;
import com.vfaceuser.commen.MyApplication;
import com.vfaceuser.commen.MyException;
import com.vfaceuser.commen.MyHttpException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;


public class HttpUtils {

	private static HttpUtils httpUtils = null;
	/**
	 * 超时设置
	 */
	public static final int TIMEOUT = 10000;

	private HttpUtils() {
	}

	public static HttpUtils getInstance() {
		if (null == httpUtils) {
			httpUtils = new HttpUtils();
		}
		return httpUtils;
	}

	/**
	 * 检查网络
	 * 
	 * @throws MyException
	 */
	void checkNetwork() throws MyHttpException {
		if (!NetworkManager.isNetworkAvailable(MyApplication.getInstance())) {
			throw new MyHttpException(R.string.network_invalid);
		}
	}

	/**
	 * get jsonObject based on url
	 * 
	 * @param url
	 * @return int
	 */
	public int getIntegerResult(final String url) {
		LogUtil.d("HTTP", "GET: " + url);
		int result = 0;
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		try {
			MyHttpGet get = new MyHttpGet(url);
			HttpResponse response = client.execute(get);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				result = Integer.parseInt(EntityUtils.toString(response
						.getEntity()));
				LogUtil.d("HTTP", "Result: " + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * get jsonObject based on url
	 * 
	 * @param url
	 * @return jsonObject
	 * @throws MyException
	 */
	public JSONObject getJSONObject(final String url) throws MyException {
		checkNetwork();
		LogUtil.d("HTTP", "GET: " + url);
		JSONObject jsonObject = null;
		StringBuilder result = new StringBuilder();

		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		try {
			MyHttpGet get = new MyHttpGet(url);
			HttpResponse response = client.execute(get);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				for (String responseBody = reader.readLine(); responseBody != null; responseBody = reader
						.readLine()) {
					result.append(responseBody);
				}
				reader.close();
				LogUtil.d("HTTP", "Result: " + result.toString());
				jsonObject = new JSONObject(result.toString());
				return jsonObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.d("HTTP", "ERROR:" + e.getMessage()
					+ e.getStackTrace().toString());
		}
		return null;
	}

	/**
	 * get JSONArray function
	 * 
	 * @param url
	 * @return jsonArray
	 */
	public JSONArray getJSONArray(final String url) {
		LogUtil.d("HTTP", "GET: " + url);
		JSONArray jsonArray = null;
		StringBuilder result = new StringBuilder();
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		try {
			MyHttpGet get = new MyHttpGet(url);
			HttpResponse response = client.execute(get);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				for (String responseBody = reader.readLine(); responseBody != null; responseBody = reader
						.readLine()) {
					result.append(responseBody);
				}
				reader.close();
				LogUtil.d("HTTP", "Result: " + result.toString());
				jsonArray = new JSONArray(result.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray;
	}

	/**
	 * post JSON data to back end service
	 * 
	 * @param url
	 * @param json
	 * @return boolean
	 */
	public boolean postJSON(final String url, final String json) {
		LogUtil.d("HTTP", "POST: " + url + " \r\n " + json);
		HttpResponse response = null;
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT);
		HttpConnectionParams.setSoTimeout(client.getParams(), TIMEOUT);
		try {
			MyHttpPost post = new MyHttpPost(url);
			StringEntity entity = new StringEntity(json, HTTP.UTF_8);
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(entity);
			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 提交数据
	 * 
	 * @param url
	 * @param formData
	 * @return
	 * @throws MyException
	 */
	public JSONObject postForm(final String url,
			final HashMap<String, String> formData) throws MyException {
		return postForm(url, formData, true);
	}

	/**
	 * 使用UrlEncodedForm提交数据
	 * 
	 * @param url
	 * @param formData
	 * @param withLogin
	 * @return
	 * @throws MyException
	 */
	public JSONObject postForm(String url, HashMap<String, String> formData,
			boolean withLogin) throws MyException {
		if (formData == null) {
			formData = new HashMap<String, String>();
		}
		if (!formData.containsKey("device_id")) {
			formData.put("device_id", Utils.getDeviceId());
		}
		JSONObject jsonObject = postUrlEncodedForm(url, formData);
		if (withLogin) {
			try {
				if (jsonObject != null && jsonObject.has("status")) {
					int status= jsonObject.getInt("status");
			
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonObject;
	}

	/**
	 * 提交数据
	 * 
	 * @param url
	 * @param formData
	 *            是否自动登录，若为true，则会在session过期后自动登录
	 * @return
	 * @throws MyException
	 */
	protected JSONObject postUrlEncodedForm(final String url,
			final HashMap<String, String> formData) throws MyHttpException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		LogUtil.d("HTTP","URL:"+url);
		for (String key : formData.keySet()) {
			NameValuePair pair = new BasicNameValuePair(key, formData.get(key));
			list.add(pair);
			LogUtil.d("HTTP",key+":"+formData.get(key));
		}
		UrlEncodedFormEntity ety;
		try {
			ety = new UrlEncodedFormEntity(list, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}
		JSONObject jsonObject = postForm(url, ety);
		return jsonObject;
	}

	/**
	 * 提交数据
	 * 
	 * @param url
	 * @param formData
	 *            是否自动登录，若为true，则会在session过期后自动登录
	 * @return
	 * @throws MyException
	 */
	protected JSONObject postMultipartForm(final String url,
			final HashMap<String, String> formData) throws MyException {
		MultipartEntity ety = new MultipartEntity();
		if (formData != null) {
			try {
				LogUtil.d("HTTP", "POST: " + url);
				for (String key : formData.keySet()) {
					ety.addPart(key, new StringBody(formData.get(key)
							.toString(), Charset.forName("UTF-8")));
					LogUtil.d("HTTP", "POST: " + key + " "
							+ formData.get(key).toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		JSONObject jsonObject = postForm(url, ety);
		return jsonObject;
	}

	public JSONObject postForm(final String url,
			final UrlEncodedFormEntity formEntity) throws MyHttpException {
		checkNetwork();
		HttpResponse response = null;
		JSONObject jsonObject = null;
		StringBuilder result = new StringBuilder();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT);
		HttpConnectionParams.setSoTimeout(client.getParams(), TIMEOUT);
		try {
			MyHttpPost post = new MyHttpPost(url);
			post.setEntity(formEntity);
			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				InputStream is = response.getEntity().getContent();
				org.apache.http.Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					is = new GZIPInputStream(new BufferedInputStream(is));
				}
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				for (String responseBody = reader.readLine(); responseBody != null; responseBody = reader
						.readLine()) {
					result.append(responseBody);
				}
				reader.close();
				LogUtil.d("HTTP", "Result: " + result.toString());
				jsonObject = new JSONObject(result.toString());
				return jsonObject;
			}
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			throw new MyHttpException(R.string.network_invalid);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new MyHttpException(R.string.data_error);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MyHttpException(R.string.connect_host_fail);
		} finally {
			// client.getConnectionManager().shutdown();
		}
		return null;
	}

	public JSONObject postForm(final String url, final HttpEntity formEntity)
			throws MyException {
		checkNetwork();
		HttpResponse response = null;
		JSONObject jsonObject = null;
		StringBuilder result = new StringBuilder();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT);
		HttpConnectionParams.setSoTimeout(client.getParams(), TIMEOUT);
		try {
			MyHttpPost post = new MyHttpPost(url);
			post.setEntity(formEntity);
			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				InputStream is = response.getEntity().getContent();
				org.apache.http.Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					is = new GZIPInputStream(new BufferedInputStream(is));
				}
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				for (String responseBody = reader.readLine(); responseBody != null; responseBody = reader
						.readLine()) {
					result.append(responseBody);
				}
				reader.close();
				LogUtil.d("HTTP", "Result: " + result.toString());
				jsonObject = new JSONObject(result.toString());
				return jsonObject;
			}
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			throw new MyException(R.string.network_invalid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MyException(R.string.connect_host_fail);
		} finally {
			// client.getConnectionManager().shutdown();
		}
		return null;
	}

	/**
	 * 对JSON结果统一处理
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static String handleResult(JSONObject jsonObject,
			Runnable successRunnable, Runnable errorRunnable) {
		String result = "";
		try {
			String status = jsonObject.getString("status");
			result = jsonObject.getString("message");
			if (status.equals("1")) {
				if (successRunnable != null) {
					successRunnable.run();
				}
			} else {
				if (errorRunnable != null) {
					errorRunnable.run();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			result = MyApplication.getInstance().getString(
					R.string.common_error);
		}
		return result;
	}

	/**
	 * 获取附加头信息
	 * 
	 * @return
	 */
	public HashMap<String, String> getHeaders() {
		HashMap<String, String> map = new HashMap<String, String>();
		return map;
	}
}
