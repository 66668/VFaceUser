package com.vfaceuser.utils;


import com.vfaceuser.commen.MyHttpException;

import org.json.JSONArray;
import org.json.JSONObject;

public class APIUtils {
	
	/**
	 * Convert json to HttpResult 
	 * @param jsonObject
	 * @return
	 * @throws MyHttpException
	 */
	public static HttpResult ToHttpResult(JSONObject jsonObject) throws MyHttpException {
		HttpResult result = new HttpResult();
		try {
			result.returnObject = jsonObject;
			result.Status = JSONUtils.getInt(jsonObject,"code");
			if(jsonObject.has("result")){
				Object dataObject = jsonObject.get("result");
				if(dataObject instanceof JSONObject){
					result.Data = (JSONObject)dataObject;
				}else if(dataObject instanceof JSONArray){
					result.DataList = (JSONArray)dataObject;
				} 
			}
			result.Message = JSONUtils.getString(jsonObject, "message"); 
		} catch (Exception e) {
			throw new MyHttpException(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 返回对象
	 * @param url
	 * @param parameters
	 * @return
	 */
	public static HttpResult postForObject(String url,
			HttpParameter parameters) throws MyHttpException {
		return postForObject(url, parameters, true);
	}
	
	/**
	 * 返回对象
	 * @param url
	 * @param parameters
	 * @return
	 */
	public static HttpResult postForObject(String url,
			HttpParameter parameters, boolean withLogin) throws MyHttpException {
		try { 
			JSONObject jsonObject = HttpUtils.getInstance().postForm(
					url, parameters, withLogin); 
			LogUtil.d("HTTP",jsonObject.toString());
			return ToHttpResult(jsonObject); 
		}
		catch(MyHttpException e){
			throw e;
		}
		catch (Exception e) { 
			e.printStackTrace();
			return HttpResult.createError(e);
		}
	}
	
	/**
	 * 返回对象
	 * @param url
	 * @param parameters
	 * @return
	 */
	public static HttpResult postForList(String url,
			HttpParameter parameters, boolean withLogin) throws MyHttpException {
		try { 
			JSONObject jsonObject = HttpUtils.getInstance().postForm(
					url, parameters, withLogin); 
			return ToHttpResult(jsonObject); 
		}
		catch(MyHttpException e){
			throw e;
		}
		catch (Exception e) { 
			e.printStackTrace();
			return HttpResult.createError(e);
		}
	}
	
	/**
	 * 返回对象
	 * @param url
	 * @param parameters
	 * @return
	 */
	public static HttpResult postForList(String url,
			HttpParameter parameters) throws MyHttpException { 
		return postForList(url, parameters, true);
	}


    /**
     * get获取数据
     * @param url
     * @return
     * @throws MyHttpException
     */
    public static HttpResult getResult(String url) throws MyHttpException{

        try {
            JSONObject jsonObject = HttpUtils.getInstance().getJSONObject(
                    url);
            return ToHttpResult(jsonObject);
        }
        catch(MyHttpException e){
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            return HttpResult.createError(e);
        }

    }


}
