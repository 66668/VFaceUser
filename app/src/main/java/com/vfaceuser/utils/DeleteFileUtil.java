package com.vfaceuser.utils;

import java.io.File;


public class DeleteFileUtil {
	
    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录
     *@return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String sPath) {

    	try {
    		File targetFile = new File(sPath);
    		if(targetFile.exists()){
    			String[]entries = targetFile.list();
    			for(String s: entries){
    				File currentFile = new File(targetFile.getPath(),s);
    				if(currentFile.isDirectory()){
    					deleteFolder(currentFile.getAbsolutePath());
    				}else{
    					currentFile.delete();
    				}
    			}
    		}
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return false;
    }
    
    
	
}
