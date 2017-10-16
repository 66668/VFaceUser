package com.vfaceuser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vfaceuser.bizmodel.MemberModel;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.ImageLoadingConfig;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.DatePickerDialog;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.ConfigHelper;
import com.vfaceuser.helper.UserHelper;
import com.vfaceuser.inject.ViewInject;
import com.vfaceuser.utils.LogUtil;
import com.vfaceuser.utils.UpdateAvatarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateUserActivity extends BaseActivity implements
		UpdateAvatarUtil.ChoosePicCallBack {

	@ViewInject(id = R.id.imgBack, click = "imgBackClick")
	View imgBack;

	@ViewInject(id = R.id.btnSave, click = "btnSaveClick")
	View btnSave;

	@ViewInject(id = R.id.imgPhoto, click = "imgPhotoClick")
	ImageView imgPhoto;

	@ViewInject(id = R.id.txtUsername)
	EditText txtUsername;

	@ViewInject(id = R.id.txtNickname)
	EditText txtNickname;

	@ViewInject(id = R.id.radioGender)
	RadioGroup radioGender;

	@ViewInject(id = R.id.txtId)
	EditText txtId;

	@ViewInject(id = R.id.txtBirthday)
	TextView txtBirthday;

	@ViewInject(id = R.id.spinnerProvince)
	Spinner spinnerProvince;

	@ViewInject(id = R.id.spinnerCity)
	Spinner spinnerCity;

    @ViewInject(id = R.id.spinnerArea)
    Spinner spinnerArea;

	@ViewInject(id = R.id.txtAddress)
	EditText txtAddress;

	@ViewInject(id = R.id.txtPhone)
	TextView txtPhone;

	@ViewInject(id = R.id.txtEmail)
	EditText txtEmail;

    @ViewInject(id = R.id.ifUpdateAllCheckBox)
    CheckBox ifUpdateAllCheckBox;

	private UpdateAvatarUtil updateAvatarUtil;
	private DisplayImageOptions imgOption;
	private ImageLoader imgLoader;

	private final int GET_PROVINCE = 2001;
	private final int GET_CITY = 2002;
	final int SUCESS = 2003;
    private static final int GET_AREA =2004 ;

	List<JSONObject> provinceList;
	List<JSONObject> cityList;
    List<JSONObject> areaList;
    String avatar = "";
    String areaId = "";
	String cityId = "";
    String provinceId="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_user);
		updateAvatarUtil = new UpdateAvatarUtil(this, handler, this, false);
		imgLoader = ImageLoader.getInstance();
		imgLoader.init(ImageLoaderConfiguration.createDefault(this));
		imgOption = ImageLoadingConfig
				.generateDisplayImageOptionsNoCatchDisc(R.drawable.photo);
		Loading.run(this, new Runnable() {

			@Override
			public void run() {
				try {
					JSONArray priovinceArray = ConfigHelper
							.getProvinceList(CreateUserActivity.this);
					sendMessage(GET_PROVINCE, priovinceArray);
				} catch (MyHttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					sendToastMessage(e.getMessage());
				}
			}
		});

		spinnerProvince.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				try {
                    provinceId = provinceList.get(position).getString(
							"ProvinceId");
					getCity(provinceId);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		spinnerCity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					cityId = cityList.get(position).getString("CityId");
                    getArea(cityId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				cityId = "";

			}
		});

        spinnerArea.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(areaList != null ){
                        areaId = areaList.get(position).getString("DistrictId");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

		txtBirthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog dialog = new DatePickerDialog(
						CreateUserActivity.this, txtBirthday.getText()
								.toString(), new DatePickerDialog.DatePickerDialogCallBack() {

							@Override
							public void confirm(String date) {
								txtBirthday.setText(date);
							}
						});
				dialog.show();
			}
		});
		initMemeberView();
	}

	void initMemeberView() {
		try {
			MemberModel memberModel = UserHelper.getCurrentUser();
			imgLoader.displayImage(memberModel.getPicPath(), imgPhoto,
					imgOption);
			txtUsername.setText(memberModel.getFullName());
            txtNickname.setText(memberModel.getNickName());
            if (memberModel.getSex() == 0) {
                ((RadioButton) radioGender.getChildAt(1)).setChecked(true);
            }
            txtId.setText(memberModel.getIdNumber());
            txtBirthday.setText(memberModel.getBirthday());
            txtAddress.setText(memberModel.getAddress());

            String phoneNum = memberModel.getPhoneNumber();
            txtPhone.setText(phoneNum);
            txtEmail.setText(memberModel.getEmail());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case SUCESS:
			sendToastMessage("更新成功！");
			this.finish();
			startActivity(MainActivity.class);
			break;
		case GET_PROVINCE:
			JSONArray jsonArray = (JSONArray) msg.obj;
			bindProvince(jsonArray);
			break;
		case GET_CITY:
			JSONArray jsonCityArray = (JSONArray) msg.obj;
			bindCity(jsonCityArray);
			break;
        case GET_AREA:
            JSONArray jsonAreaArray = (JSONArray) msg.obj;
            bindArea(jsonAreaArray);
            break;
		default:
			break;
		}
	}

	protected void getCity(final String provinceId) {
		Loading.run(this, new Runnable() {

			@Override
			public void run() {
				try {
					JSONArray cityArray = ConfigHelper.getCityList(
							CreateUserActivity.this, provinceId);
					sendMessage(GET_CITY, cityArray);
				} catch (MyHttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					sendToastMessage(e.getMessage());
				}
			}
		});
	}

    protected void getArea(final String cityId) {
        Loading.run(this, new Runnable() {

            @Override
            public void run() {
                try {
                    JSONArray areaArray = ConfigHelper.getDistrictList(
                            CreateUserActivity.this, cityId);
                    sendMessage(GET_AREA, areaArray);
                } catch (MyHttpException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    sendToastMessage(e.getMessage());
                }
            }
        });
    }

	private void bindProvince(JSONArray jsonArray) {
		provinceList = new ArrayList<JSONObject>();
		ArrayList<String> provinceStringList = new ArrayList<String>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				provinceList.add(jsonArray.getJSONObject(i));
				provinceStringList.add(jsonArray.getJSONObject(i).getString(
						"ProvinceName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_my, provinceStringList);
		spinnerProvince.setAdapter(arrayAdapter);

        try {
            spinnerProvince.setSelection(provinceStringList.indexOf(UserHelper.getCurrentUser().getProvinceName()));
        }catch (Exception e){
            e.printStackTrace();
        }
	}

	private void bindCity(JSONArray jsonArray) {
		cityList = new ArrayList<JSONObject>();
		ArrayList<String> citys = new ArrayList<String>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				cityList.add(jsonArray.getJSONObject(i));
				citys.add(jsonArray.getJSONObject(i).getString("CityName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_my, citys);
		spinnerCity.setAdapter(arrayAdapter);

        try {
            spinnerCity.setSelection(citys.indexOf(UserHelper.getCurrentUser().getCityName()));
        }catch (Exception e){
            e.printStackTrace();
        }
	}
    private void bindArea(JSONArray jsonArray) {
        areaList = new ArrayList<JSONObject>();
        ArrayList<String> areas = new ArrayList<String>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                areaList.add(jsonArray.getJSONObject(i));
                areas.add(jsonArray.getJSONObject(i).getString("DistrictName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_my, areas);
        spinnerArea.setAdapter(arrayAdapter);

        try {
            spinnerArea.setSelection(areas.indexOf(UserHelper.getCurrentUser().getDistrictName()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

	public void btnSaveClick(View v) {
		Loading.run(this, new Runnable() {

			@Override
			public void run() {
				HashMap<String, String> hashMap = new HashMap<String, String>();

                hashMap.put("MemberCode", UserHelper.getCurrentUser()
                        .getMemberCode() + "");

                if(ifUpdateAllCheckBox.isChecked()){
                    hashMap.put("isUpdateAll", "true");
                }else {
                    hashMap.put("isUpdateAll", "false");
                }

                if (UserHelper.getCurrentUser().getPicPath() != null) {
                    hashMap.put("oldPicPath", UserHelper.getCurrentUser()
                            .getPicPath());
                } else {
                    hashMap.put("oldPicPath", "");
                }

                hashMap.put("picStream", avatar);
                hashMap.put("FullName", txtUsername.getText().toString());
                hashMap.put("NickName", txtNickname.getText().toString());

				hashMap.put(
						"Sex",
						radioGender.getCheckedRadioButtonId() == R.id.radio0 ? "1"
								: "0");
				hashMap.put("IdNumber", txtId.getText().toString());
                hashMap.put("ProvinceId", provinceId);
				hashMap.put("CityId", cityId);
                hashMap.put("AreaId",areaId);
				hashMap.put("Address", txtAddress.getText().toString());
				hashMap.put("Birthday", txtBirthday.getText().toString());
				hashMap.put("Email", txtEmail.getText().toString());

				try {
					UserHelper.editVFaceMember(CreateUserActivity.this, hashMap);
					sendMessage(SUCESS);
				} catch (MyHttpException e) {
					e.printStackTrace();
					sendToastMessage(e.getMessage());
				}
			}
		});
		// this.finish();
	}

	public void imgPhotoClick(View v) {
		updateAvatarUtil
				.showChoosePhotoDialog(UpdateAvatarUtil.IMG_TYPE_AVATAR);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		updateAvatarUtil.onActivityResultAction(requestCode, resultCode, data);
	}

	public void imgBackClick(View v) {
		this.finish();
	}

	@Override
	public void updateAvatarSuccess(int updateType, String avatar,
			String avatarBase64) {
		LogUtil.d(avatar);
		imgPhoto.setImageResource(R.drawable.login_logo);
		if (avatar.toLowerCase().startsWith("http")) {
			ImageLoader.getInstance().displayImage(avatar, imgPhoto, imgOption);
		} else {
//			imgPhoto.setImageURI(Uri.fromFile(new File(avatar)));
            ImageLoader.getInstance().displayImage("file://"+avatar, imgPhoto, imgOption);
		}

		this.avatar = avatarBase64;
	}

	@Override
	public void updateAvatarFailed(int updateType) {
		Toast.makeText(this, R.string.upload_avatar_failed, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void cancel() {

	}

}
