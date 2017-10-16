package com.vfaceuser.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.vfaceuser.commen.MyApplication;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * bitmap的一些处理方法,压缩、保存到本地
 * @author Fangweidong
 *
 */
public class BitmapUtil {
	/**
	 * 缩小图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap,int maxWidth,int maxHeight) {
		if(bitmap != null){
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			if(width>maxWidth){
				int pWidth = maxWidth;
				int pHeight = maxWidth*height/width;
				Bitmap result = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);
				bitmap.recycle();
				return result;
			}
			if(height>maxHeight){
				int pHeight = maxHeight;
				int pWidth = maxHeight*width/height;
				Bitmap result = Bitmap.createScaledBitmap(bitmap, pWidth, pHeight, false);
				bitmap.recycle();
				return result;
			}
		}
		return bitmap;
	}
	
	/**
	 * 保存jpg格式图片到本地，并以75压缩
	 * @param bitmap
	 * @param filePath
	 * @return
	 */
	public static boolean saveBitmapToJpegFile(Bitmap bitmap, String filePath) {
		return saveBitmapToJpegFile(bitmap, filePath, 75, CompressFormat.JPEG);
	}
	
	/**
	 * 保存图片到本地，并以75压缩
	 * @param bitmap
	 * @param filePath
	 * @return
	 */
	public static boolean saveBitmapToImgFile(Bitmap bitmap, String filePath,CompressFormat imgType) {
		return saveBitmapToJpegFile(bitmap, filePath, 75, imgType);
	}
	
	/**
	 * 保存到本地，720*1080为最大尺寸
	 * @param bitmap
	 * @param filePath
	 * @param quality
	 * @return
	 */
	public static boolean saveBitmapToJpegFile(Bitmap bitmap, String filePath,
			int quality,CompressFormat imgType) {
		int maxWidth = 720;
		int maxHeight = 1080;
		return saveBitmapToJpegFile(bitmap,filePath,quality,maxWidth,maxHeight,imgType);
	}
	
	
	/**
	 * 保存到本地
	 * @param bitmap
	 * @param filePath 保存的本地路径
	 * @param quality 压缩比例
	 * @param maxWidth 最大宽度
	 * @param maxHeight 最大高度
	 * @return
	 */
	public static boolean saveBitmapToJpegFile(Bitmap bitmap, String filePath,
			int quality,int maxWidth,int maxHeight,CompressFormat imgType) {
		try {

			FileOutputStream fileOutStr = new FileOutputStream(filePath);
			BufferedOutputStream bufOutStr = new BufferedOutputStream(fileOutStr);
			resizeBitmap(bitmap,maxWidth,maxHeight).compress(imgType, quality, bufOutStr);
			bufOutStr.flush();
			bufOutStr.close();
		} catch (Exception exception) {
			return false;
		}
		return true;
	}
	
	/**
	 * 压缩图片
	 * @param path 原始图片路径
	 * @return 压缩后的路径
	 */
	public static String compressPic(Context context,String path){
		
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
			.resetViewBeforeLoading(true).cacheOnDisc(false)
			.cacheInMemory(false).imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
			.build();

		Bitmap bitmap = ImageLoader.getInstance().loadImageSync("file://"+path,displayImageOptions);
		String fileName = Utils.md5(System.currentTimeMillis()+"");
		String compressedPath = MyApplication.getUploadPicPath(context) + "/" + fileName;
		FileOutputStream fileOutStr;
		try {
			fileOutStr = new FileOutputStream(compressedPath);
			BufferedOutputStream bufOutStr = new BufferedOutputStream(fileOutStr);
			bitmap.compress(CompressFormat.JPEG, 75, bufOutStr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return compressedPath;
	}
	
}
