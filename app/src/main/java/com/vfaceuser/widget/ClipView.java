package com.vfaceuser.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 画截图框
 * @author tangjie
 *
 */
public class ClipView extends View
{
	
	public ClipView(Context context)
	{
		super(context);
	}

	public ClipView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}


	public ClipView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		/*这里就是绘制矩形区域*/
		int width = this.getWidth();
		int height = this.getHeight();
		
		Paint paint = new Paint();
		paint.setColor(Color.BLACK); //0xaa000000
		paint.setAlpha(99);
		paint.setAntiAlias(true);
		
//		Paint paint2 = new Paint();
//		paint2.setColor(Color.WHITE);
//		paint2.setAlpha(0);
		
		Paint linePaint = new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setAntiAlias(true); 
		
		int yStart = (height - width)/2; //start和end都是相对于当前视图的坐标点
		int yEnd = yStart + width;
		
		//center
//		canvas.drawRect(0, height/4, width,height/4 + Global.CUT_USER_PHOTO_SIZE , paint2);
		
		//画遮挡阴影
		//top
		canvas.drawRect(0, 0, width, yStart, paint);
		
		//bottom
		canvas.drawRect(0, yEnd, width, height, paint);
		
		//画线
		//top
		canvas.drawLine(0, yStart, width, yStart, linePaint);
		//left
		canvas.drawLine(0, yStart, 1, yEnd, linePaint); 
		//right
		canvas.drawLine(width-1, yStart, width, yEnd, linePaint);
		//bottom
		canvas.drawLine(0, yEnd, width, yEnd, linePaint);
		
	}
	
}
