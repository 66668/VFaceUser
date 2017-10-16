package com.vfaceuser.inject;

import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;

import com.vfaceuser.commen.BaseActivity;

import java.lang.reflect.Field;

public abstract class InjectHelper {
	Object mObject; 

	public InjectHelper(Object obj ) {
		this.mObject = obj; 
	}
	
	protected abstract View findViewById(int id);

	public void initView() {
		Field[] fields = mObject.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) { 
			for (Field field : fields) { 
				try {
					field.setAccessible(true);

					if (field.get(mObject) != null)
						continue;

					//View v = findViewById(viewId);
					//if(v instanceof TextView){
					//PageUtil.DisplayToast(field.getName()+"+id---"+field.);
					//}
					
					ViewInject viewInject = field
							.getAnnotation(ViewInject.class);
					if (viewInject != null) {
						int viewId = viewInject.id();
						field.set(mObject, findViewById(viewId)); 
						setListener(field, viewInject.click(), BaseActivity.Method.Click);
						setListener(field, viewInject.longClick(),
								BaseActivity.Method.LongClick);
						setListener(field, viewInject.itemClick(),
								BaseActivity.Method.ItemClick);
						setListener(field, viewInject.itemLongClick(),
								BaseActivity.Method.itemLongClick);

						Select select = viewInject.select();
						if (!TextUtils.isEmpty(select.selected())) {
							setViewSelectListener(field, select.selected(),
									select.noSelected());
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setViewSelectListener(Field field, String select,
			String noSelect) throws Exception {
		Object obj = field.get(mObject);
		if (obj instanceof View) {
			((AbsListView) obj).setOnItemSelectedListener(new EventListener(
					mObject).select(select).noSelect(noSelect));
		}
	}

	private void setListener(Field field, String methodName, BaseActivity.Method method)
			throws Exception {
		if (methodName == null || methodName.trim().length() == 0)
			return;

		Object obj = field.get(mObject);

		switch (method) {
		case Click:
			if (obj instanceof View) {
				((View) obj).setOnClickListener(new EventListener(mObject)
						.click(methodName));
			}
			break;
		case ItemClick:
			if (obj instanceof AbsListView) {
				((AbsListView) obj).setOnItemClickListener(new EventListener(
						mObject).itemClick(methodName));
			}
			break;
		case LongClick:
			if (obj instanceof View) {
				((View) obj).setOnLongClickListener(new EventListener(mObject)
						.longClick(methodName));
			}
			break;
		case itemLongClick:
			if (obj instanceof AbsListView) {
				((AbsListView) obj)
						.setOnItemLongClickListener(new EventListener(mObject)
								.itemLongClick(methodName));
			}
			break;
		default:
			break;
		}
	}

}
