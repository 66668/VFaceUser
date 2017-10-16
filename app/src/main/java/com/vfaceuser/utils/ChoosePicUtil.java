package com.vfaceuser.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.vfaceuser.R;
import com.vfaceuser.commen.MyApplication;
import com.vfaceuser.dialog.ChooseItemDialog;

import java.io.File;
import java.io.IOException;

public class ChoosePicUtil {
	
	public static final int REQUEST_CODE_PICK_PHOTO_FROM_CAMERA = 70;
	public static final int REQUEST_CODE_PICK_PHOTO_FROM_ALBUMS = 71;
	public static final int REQUEST_CODE_PICK_PHOTO_FINISH = 72;
	
	private Activity context;
	private ChoosePicCallback callBack;
	private String cameraPath;
	
	public ChoosePicUtil(Activity context,ChoosePicCallback callBack){
		this.context = context;
		this.callBack = callBack;
	}
	
	public interface ChoosePicCallback{
		
		void choosePicSuccess(String path);
		void choosePicFailed();
		void choosePicCancel();
	}
	
	// 选取照片对话框
	public void showChoosePhotoDialog() {
		String cameraPath = MyApplication.getUnhandledUserPhotoPath(context);
		showChoosePhotoDialog(cameraPath);
	}
	
	public void showChoosePhotoDialog(final String cameraPath) {
		this.cameraPath = cameraPath;
		ChooseItemDialog dialog = new ChooseItemDialog(context, "拍照", "从相册中选择", 0,
				false, new ChooseItemDialog.ChooseItemDialogCallBack() {

					@Override
					public void confirm(int whichItem) {
						if (whichItem == 0) {
							// 拍照
							File newfile = new File(cameraPath);// file
							try {
								newfile.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
							Uri outputFileUri = Uri.fromFile(newfile);
							Intent cameraIntent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
									outputFileUri);
							context.startActivityForResult(cameraIntent,
									REQUEST_CODE_PICK_PHOTO_FROM_CAMERA);
						} else {
							// 从相册中选择
							Intent pickPhoto = new Intent(Intent.ACTION_PICK,
									null);
							pickPhoto.setDataAndType(
											MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											"image/*");
							context.startActivityForResult(pickPhoto,REQUEST_CODE_PICK_PHOTO_FROM_ALBUMS);
						}
					}

					@Override
					public void cancel() {
						if(callBack != null){
							callBack.choosePicCancel();
						}			
					}
				});
		dialog.show();
	}
	
	/**
	 * 在onActivityResult里调用
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * 默认压缩
	 */
	public void onActivityResultAction(int requestCode, int resultCode, Intent data){
		onActivityResultAction(requestCode,resultCode,data,true);
	}
	/**
	 * 在onActivityResult里调用
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @param ifCompress 是否压缩
	 */
	public void onActivityResultAction(int requestCode, int resultCode, Intent data,boolean ifCompress){
		if (requestCode == REQUEST_CODE_PICK_PHOTO_FROM_ALBUMS) {
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();

				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = context.managedQuery(uri, proj, null, null, null);
				if (cursor != null) {
					try {
						int actual_image_column_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String imgPath = cursor.getString(actual_image_column_index);
						if(ifCompress){
							String compressedPath = BitmapUtil.compressPic(context, imgPath);
							callBack.choosePicSuccess(compressedPath);
						}else{
							callBack.choosePicSuccess(imgPath);
						}

					} catch (Exception e) {
						Toast.makeText(
								context,
								context.getString(R.string.please_choose_system_album_pic),
								Toast.LENGTH_SHORT).show();
						callBack.choosePicFailed();
					}
				} else {
					Toast.makeText(
							context,
							context.getString(R.string.please_choose_system_album_pic),
							Toast.LENGTH_SHORT).show();
					callBack.choosePicFailed();
				}

			}else{
				callBack.choosePicFailed();
			}
			
		} else if (requestCode == REQUEST_CODE_PICK_PHOTO_FROM_CAMERA) {
			if (resultCode == Activity.RESULT_OK) {
				if(!TextUtils.isEmpty(cameraPath)){
					if(ifCompress){
						String compressedPath = BitmapUtil.compressPic(context, cameraPath);
						callBack.choosePicSuccess(compressedPath);
					}else{
						callBack.choosePicSuccess(cameraPath);
					}
					return;
				}
				
			}
			callBack.choosePicFailed();
			
		} 
	}
	
}
