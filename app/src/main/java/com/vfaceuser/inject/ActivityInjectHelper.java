package com.vfaceuser.inject;

import android.app.Activity;
import android.view.View;

public class ActivityInjectHelper extends InjectHelper {

	public Activity getActivity(){
		return (Activity)mObject;
	}
	
	public ActivityInjectHelper(Activity activity) {
		super(activity);
	}

	@Override
	protected View findViewById(int id) {
		return getActivity().findViewById(id);
	}

}
