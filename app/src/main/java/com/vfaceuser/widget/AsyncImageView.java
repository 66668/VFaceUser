package com.vfaceuser.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;

import com.vfaceuser.R;
import com.vfaceuser.utils.ImageManager;
import com.vfaceuser.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 异步图片控件 使用：new AsyncImageView().asyncLoadBitmapFromUrl("http://xxxx","缓存路径"){
 * 
 */
public class AsyncImageView extends android.support.v7.widget.AppCompatImageView {

	/**
	 * 异步task加载器
	 */
	private AsyncLoadImage mAsyncLoad;

	/**
	 * 下载回来的图片缓存存活时间，单位：秒(s),默认30分钟
	 */
	private long mCacheLiveTime = 0;

	/**
	 * 圆角尺寸，小于等于0时不切圆角
	 */
	private int mRoundPx = 10;

	/**
	 * 是否转成圆形图
	 */
	private boolean mIsCircle = false;
	
	/**
	 * 设置显示为圆图
	 * 
	 * @param circle
	 */
	public void setIsCircle(boolean circle) {
		mIsCircle = circle;
	}

	private int mAdjustWidth = 0, mAdjustHeight = 0;

	/**
	 * 调整后的宽度、高度
	 * 
	 * @param adjustWidth
	 */
	public void setAdjustWidthHeight(int adjustWidth, int adjustHeight) {
		this.mAdjustWidth = adjustWidth;
		this.mAdjustHeight = adjustHeight;
	}

	/**
	 * 设置圆角
	 * 
	 * @param r
	 */
	public void setRoundPx(int r) {
		mRoundPx = r;
	}

	public AsyncImageView(Context context) {
		super(context);
	}

	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context 
                .obtainStyledAttributes(attrs, R.styleable.AsyncImageView);
		mIsCircle = a.getBoolean(R.styleable.AsyncImageView_isCircle, false);
        a.recycle();
	}

	public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context 
                .obtainStyledAttributes(attrs, R.styleable.AsyncImageView);
		mIsCircle = a.getBoolean(R.styleable.AsyncImageView_isCircle, false);
        a.recycle();
	}

	/**
	 * 
	 */
	@Override
	public void setImageDrawable(Drawable drawable) {
		if (mAsyncLoad != null) {
			mAsyncLoad.cancel(true);
			mAsyncLoad = null;
		}
		super.setImageDrawable(drawable);
	}

	/**
	 * 重写下面几个设置图片资源的方法，目地是取消网络加载
	 */
	@Override
	public void setImageResource(int resId) {
		cancelLoad();
		super.setImageResource(resId);
	}

	@Override
	public void setImageURI(Uri uri) {
		cancelLoad();
		super.setImageURI(uri);
	}

	@Override
	public void setImageBitmap(Bitmap bitmap) {
		cancelLoad();
		super.setImageBitmap(bitmap);
	}

	/**
	 * 取消正在进行的异步task
	 */
	public void cancelLoad() {
		if (mAsyncLoad != null) {
			mAsyncLoad.cancel(true);
			mAsyncLoad = null;
		}
	}

	/**
	 * 设置图片存活时间
	 * 
	 * @param second
	 *            存活时间，单位【秒】，如果等于0或null，则不缓存
	 */
	public void setCacheLiveTime(long second) {
		if (second == 0) {
			this.mCacheLiveTime = 0;
		} else if (second >= 0) {
			this.mCacheLiveTime = second * 1000;
		}
	}

	public void LoadBitmapFromFile(String filePath) {

		AsyncImageView.this.setImageBitmap(bitmapAdjust(BitmapFactory
				.decodeFile(filePath)));
	}

	private String lastUrl = "";

	/**
	 * 从网络异步加载
	 * 
	 * @param url
	 * @param saveFileName
	 */
	public void asyncLoadBitmapFromUrl(String url, String saveFileName) {
		if (lastUrl.equals(url))
			return;
		if (mAsyncLoad != null) {
			mAsyncLoad.cancel(true);
		}
		// 先从本地读取，如果读取失败，则启动异步线程从网络读取
		if (!loadFromLocal(saveFileName)) {
			// AsyncTask不可重用，所以每次重新实例
			mAsyncLoad = new AsyncLoadImage();
			lastUrl = url;
			mAsyncLoad.execute(url, saveFileName);
		}
	}

	/**
	 * 从本地读取
	 * 
	 * @param saveFileName
	 * @return
	 */
	private boolean loadFromLocal(String saveFileName) {
		File file = new File(saveFileName);
		if (file.isFile()) {
			Bitmap result = BitmapFactory.decodeFile(file.getAbsolutePath());
			if (result == null)
				return false;
			AsyncImageView.this.setImageBitmap(result);
			return true;
		}
		return false;
	}

	/**
	 * 异步加载器
	 */
	private class AsyncLoadImage extends AsyncTask<String, Integer, Bitmap> {
		/**
		 * 是否取消
		 */
		private boolean isCancel = false;

		@Override
		protected Bitmap doInBackground(String... params) {
			if (isCancel) {
				return null;
			}
			String url = params[0];
			String fileName = params[1];
			try {
				return getBitmap(url, fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			System.out.println("async load imgae cancel");
			isCancel = true;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (!isCancel && result != null) {
				AsyncImageView.this.setImageBitmap(result);
			}
		}
	}

	/**
	 * 按变量配置调整图片
	 * 
	 * @param bitmap
	 * @return
	 */
	Bitmap bitmapAdjust(Bitmap bitmap) {
		if (bitmap == null)
			return null;
		if (mIsCircle) {
			return ImageManager.toCicleBitmap(bitmap, mAdjustWidth,
					mAdjustHeight);
		} else {
			return ImageManager.toRoundCorner(bitmap, mRoundPx, mAdjustWidth,
					mAdjustHeight);
		}
	}

	/**
	 * 下载图片
	 * 
	 * @param urlString
	 *            url下载地址
	 * @param fileName
	 *            缓存文件路径
	 * @throws IOException
	 */
	private Bitmap getBitmap(String urlString, String fileName)
			throws IOException {
		LogUtil.d("AsyncImage getBitmap UrlString:" + urlString);
		if (fileName == null || fileName.trim().equals("")) {
			InputStream input = getBitmapInputStreamFromUrl(urlString);
			return bitmapAdjust(BitmapFactory.decodeStream(input));
		}

		File file = new File(fileName);
		if (!file.isFile()
				|| (mCacheLiveTime > 0 && (System.currentTimeMillis()
						- file.lastModified() > mCacheLiveTime))) {
			InputStream input = getBitmapInputStreamFromUrl(urlString);
			/** change start **/
			// file = saveImage(input, fileName);
			// // 如果文件结构创建失败，则直接从输入流解码图片
			// if (file == null || !file.exists() || !file.canWrite()
			// || !file.canRead()) {
			// return bitmapAdjust(BitmapFactory.decodeStream(input));
			// }
			/** change to **/
			Bitmap bitmap = bitmapAdjust(BitmapFactory.decodeStream(input));
			ImageManager.savePNG_After(bitmap, fileName);
			return bitmap;
			/** changed **/
		}
		return BitmapFactory.decodeFile(file.getAbsolutePath());
	}

	/**
	 * 下载图片，输入InputStream
	 * 
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	private InputStream getBitmapInputStreamFromUrl(String urlString)
			throws IOException {
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(8000);
		connection.setReadTimeout(8000);
		LogUtil.d("getBitmapInputStreamFromUrl:" + urlString);
		return connection.getInputStream();
	}

	/**
	 * 从输入流保存图片到文件系统
	 * 
	 * @param fileName
	 * @param input
	 * @return
	 */
	@SuppressLint("NewApi")
	private File saveImage(InputStream input, String fileName) {
		if (fileName.trim().isEmpty() || input == null) {
			return null;
		}
		File file = new File(fileName);
		OutputStream output = null;
		try {
			file.getParentFile().mkdirs();
			if (file.exists() && file.isFile()) {
				file.delete();
			}
			if (!file.createNewFile()) {
				return null;
			}
			output = new FileOutputStream(file);
			byte[] buffer = new byte[4 * 1024];
			do {
				// 循环读取
				int numread = input.read(buffer);
				if (numread == -1) {
					break;
				}
				output.write(buffer, 0, numread);
			} while (true);
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return file;
	}

}
