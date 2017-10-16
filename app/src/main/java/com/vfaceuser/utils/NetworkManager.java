package com.vfaceuser.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class NetworkManager {

	private NetworkManager(){}
	
	public static boolean isNetworkAvailable(Context context) {
		boolean result = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			result = false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED
							&& (info[i].getTypeName()
									.equalsIgnoreCase("MOBILE") || info[i]
									.getTypeName().equalsIgnoreCase("WIFI"))) {
						
						result = true;
						break;
					}
				}
			}
		}
		return result;
	} 

	public static boolean download(final String httpURL,final String fileName,final String path){
		int count = 0;
		byte data[] = new byte[1024];
		try {
			URL url = new URL(httpURL);
			URLConnection conection = url.openConnection();
			conection.setConnectTimeout(5000);
			conection.setReadTimeout(5000);
			conection.connect();
			InputStream input = new BufferedInputStream(url.openStream());
			File file = new File(path + fileName);
			if(file.exists()){
				file.delete();
			}
			OutputStream output = new FileOutputStream(path + fileName);
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}
			output.flush();
			output.close();
			input.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
    public static Bitmap getBitmap(String imageURL) {  
        Bitmap bitmap = null;  
        try {  
            URL url = new URL(imageURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);  
            int state = conn.getResponseCode();  
            if (state == 200) {    
                int length = (int) conn.getContentLength();
                InputStream is = conn.getInputStream();  
                if (length != -1) {  
                    byte[] imgData = new byte[length];  
                    byte[] temp = new byte[512];  
                    int readLen = 0;  
                    int destPos = 0;  
                    while ((readLen = is.read(temp)) > 0) {  
                        System.arraycopy(temp, 0, imgData, destPos, readLen);  
                        destPos += readLen;  
                    }  
                    bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                    //is.close();
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();
        }  
        return bitmap;  
    }  

}
