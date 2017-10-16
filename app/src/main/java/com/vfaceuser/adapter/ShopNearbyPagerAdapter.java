package com.vfaceuser.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vfaceuser.ShopsNearbyListFragment;
import com.vfaceuser.bizmodel.StoreBusinessTypeModel;

import java.util.ArrayList;

public class ShopNearbyPagerAdapter extends FragmentPagerAdapter{

	private ArrayList<StoreBusinessTypeModel> shopTypes = new ArrayList<StoreBusinessTypeModel>();
	
	public ShopNearbyPagerAdapter(FragmentManager fm,ArrayList<StoreBusinessTypeModel> shopTypes) {
		super(fm);
		this.shopTypes.clear();
		this.shopTypes.addAll(shopTypes);
	}

	@Override
	public Fragment getItem(int position) {

		return ShopsNearbyListFragment.newInstance(shopTypes.get(position));
		
	}
	
    @Override
    public CharSequence getPageTitle(int position) {
        return shopTypes.get(position).getKeyName();
    }

	@Override
	public int getCount() {
		if(shopTypes != null){
			return shopTypes.size();
		}
		return 0;
	}

}
