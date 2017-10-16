package com.vfaceuser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.ImageLoadingConfig;
import com.vfaceuser.commen.MyApplication;
import com.vfaceuser.widget.ClipView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 照片截取页面
 */
public class UserUploadPhotoClipPictureActivity extends BaseActivity implements OnTouchListener
{
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	
	//图片部分
	private int userPhotoBgSampleWidth;
	private int userPhotoBgSampleHeight;
	private String imgPath;
	private Bitmap modelMap;
	
	private ImageView srcPic;//原图显示view
	private ClipView clipview;//截取框
	private LinearLayout userPhotoLoadingBg;//刚进入此页面时，加载提示
	
	private Handler handler = new Handler();
	
	private int MOBILE_STATUS_BAR_HEIGHT = 38;//状态栏高度
	private int TOP_BAR_HEIGHT;//顶部导航栏高度
	
	private ImageLoader imgLoader;
	@SuppressWarnings("unused")
	private DisplayImageOptions imgOption;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip_picture);
		imgLoader = ImageLoader.getInstance();
		imgLoader.init(ImageLoaderConfiguration.createDefault(this));
		imgOption = ImageLoadingConfig.generateDisplayImageOptions(R.drawable.photo);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		userPhotoBgSampleWidth = metrics.widthPixels;
		userPhotoBgSampleHeight = metrics.heightPixels;
		
		TOP_BAR_HEIGHT = getResources().getDimensionPixelSize(R.dimen.common_topbar_height);
		
		try {  
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");  
            Object object = clazz.newInstance();  
            Field field = clazz.getField("status_bar_height");  
            int height = Integer.parseInt(field.get(object).toString());  
            MOBILE_STATUS_BAR_HEIGHT = getResources().getDimensionPixelSize(height); 
        } catch (Exception e) {  
        	e.printStackTrace();
        }
		
		initViewBtn();

		srcPic = (ImageView) this.findViewById(R.id.src_pic);
		srcPic.setOnTouchListener(this);
		
		Intent intent = getIntent();
		if(intent!=null){
			imgPath = intent.getStringExtra("img_path");
		}
		
		if(TextUtils.isEmpty(imgPath)){
			imgPath = MyApplication.getUnhandledUserPhotoPath(UserUploadPhotoClipPictureActivity.this);//Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.jpg";
		}
		
		File fi = new File(imgPath);
		if(!fi.exists()){
			Toast.makeText(getApplicationContext(), "请选择系统相册图片", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		userPhotoLoadingBg = (LinearLayout)findViewById(R.id.user_photo_loading_bg);
		
		new saveToLocalThread().start();
		
	}
	
	public void initViewBtn(){

        ImageView back = (ImageView) findViewById(R.id.back_btn);
		ImageButton next = (ImageButton)findViewById(R.id.top_right_btn);
		TextView title = (TextView)findViewById(R.id.title_textview);
		
//		next.setText(R.string.top_bar_btn_finish);
		next.setVisibility(View.VISIBLE);
		title.setText("用户");
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				generateUploadUserPhotoToLocal();
				
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
	}
	
	// 保存缓存到本地
	private class saveToLocalThread extends Thread{
		
		@Override
		public void run() {
			
			ImageSize targetSize = new ImageSize(userPhotoBgSampleWidth, userPhotoBgSampleHeight); // result Bitmap will be fit to this size
			 
			modelMap = imgLoader.loadImageSync("file://"+imgPath, targetSize,
					ImageLoadingConfig.generateDisplayImageOptionsNoCatchDisc());
			
			handler.post(new Runnable() {
				@Override
				public void run() {
					
					if(modelMap != null && !modelMap.isRecycled()){
						srcPic.setImageBitmap(modelMap);
						
						//Get the image's rect
						RectF drawableRect = new RectF(0, 0, modelMap.getWidth(), modelMap.getHeight());
						       
						//Get the image view's rect
						RectF viewRect = new RectF(0, 0, srcPic.getWidth(),srcPic.getHeight());
								
						//draw the image in the view
						matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
						
						if(modelMap.getWidth() > modelMap.getHeight()){
							float nowImgHeight = ((float)srcPic.getWidth() / modelMap.getWidth()) * modelMap.getHeight();
							float sx = (float)(srcPic.getWidth()/nowImgHeight);
							matrix.postScale(sx, sx,srcPic.getWidth()/2,srcPic.getHeight()/2);
						}
						
						srcPic.setImageMatrix(matrix);
						srcPic.invalidate();
						userPhotoLoadingBg.setVisibility(View.GONE);
					}else{
						Toast.makeText(getApplicationContext(), "图片处理失败", Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			});
			
		}
	}
	
	/*这里实现了多点触摸放大缩小，和单点移动图片的功能，参考了论坛的代码*/
	public boolean onTouch(View v, MotionEvent event){
		ImageView view = (ImageView) v;
		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK)
			{
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix);
				// 設置初始點位置
				start.set(event.getX(), event.getY());
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				if (oldDist > 10f)
				{
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG)
				{
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);
				} else if (mode == ZOOM)
				{
					float newDist = spacing(event);
					if (newDist > 10f)
					{
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
			}

		view.setImageMatrix(matrix);
		return true; // indicate event was handled
	}

	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event){
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event){
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	
	
	private void generateUploadUserPhotoToLocal(){
		
		clipview = (ClipView)this.findViewById(R.id.clipview);
		clipview.setVisibility(View.INVISIBLE);
		Bitmap screenShoot = takeScreenShot();
		clipview.setVisibility(View.VISIBLE);
		int width = clipview.getWidth();
		int height = clipview.getHeight();
		
		//yStart 相对于整个屏幕高度
		int yStart = (height - width)/2 + (MOBILE_STATUS_BAR_HEIGHT + TOP_BAR_HEIGHT);
		
		Bitmap finalBitmap = Bitmap.createBitmap(screenShoot, 1, yStart + 1  , width - 1, width - 1);
		
		int compressDegree = 60;//压缩比例
		
		saveBitmapToLocalFile(MyApplication.getHandledUserPhotoPath(UserUploadPhotoClipPictureActivity.this), finalBitmap, compressDegree, false);
		
		if(finalBitmap != null && !finalBitmap.isRecycled()){
			finalBitmap.recycle();
			finalBitmap = null;
		}
				
	}
	
	//保存本地
	public void saveBitmapToLocalFile(String filePath , Bitmap sourceBitmap , int compressDegree , boolean isSourceBitmapRecycle){
		
		FileOutputStream stream = null;
		
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		
		try {
			stream = new FileOutputStream(file);
			if(sourceBitmap != null && !sourceBitmap.isRecycled()){
				sourceBitmap.compress(CompressFormat.JPEG, compressDegree, stream);//大小压缩
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stream != null) {
					stream.flush();
					stream.close();
				}
				
				if(isSourceBitmapRecycle && sourceBitmap != null && !sourceBitmap.isRecycled()){
					sourceBitmap.recycle();
					sourceBitmap = null;
				}
			} catch (IOException e2) {
			}
		}
		
	}
	
	// 获取Activity的截屏
	private Bitmap takeScreenShot(){
		View view = this.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		return view.getDrawingCache();
	}

}