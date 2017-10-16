package com.vfaceuser.commen;

/**
 * Web API URL
 * 
 * @author don
 * 
 */
public class WebAPI {

	public static final String DOMAIN = "pss100.com"; 
 
	//test
//	public static final String DOMAIN = "newaysoft.com";

	/**
	 * 接口根地址
	 */
	private static final String API_URL = "http://openapi." + DOMAIN + "/openapi/";


	public class VipCard {
		
		//会员卡信息(列表)
        public static final String GET_MEMBER_CARD_LIST_URL = API_URL + "StoreMember/GetMemberCardList/";//{VFACEMemberCode}

        //获取会员卡可用服务列表
        public static final String GET_AVAILABLE_SERVICE_URL = API_URL + "User/GetAvailableService?";
        //storeMemberId=0ce97f1b-d16c-4b45-b62f-102069b1a5db
        //&expenseType=4
        //&storeId=b3b3b802-dce6-43bd-a5d9-eac9938ba293
		
        public static final String GET_EXPENSE_HISTORY_URL = API_URL + "User/GetServiceExpenseHistory?";
//        		+ "storeMemberId=648541a0-ba8b-470b-80b1-ff8856858c88
//        &expenseType=4
//        &storeId=25fc3071-db62-11e4-b995-a0d3c1f0ddf4
//        &serviceId=3a9a5d5c-c72f-4a61-8c6e-5a3adc2518fe";
        
        public static final String GET_STORE_EXPENSE_LIST_URL = API_URL + "User/ExpenseListIncludeSubStore?";
//        		+ "vfaceMemberCode=20150607134247"
//        		+ "&storeId=887ef9eb-5318-4990-ad35-7aef0899fa67"
//        		+ "&endDate=2016-6-22"
//        		+ "&expenseType=0"
//        		+ "&pageIndex=1"
//        		+ "&pageSize=10"
//        		+ "&startDate=2015-6-8";
        
	}
	
	public class User {

        //获取用户消费记录
        public static final String GET_USER_EXPENSE_LIST_URL = API_URL + "User/ExpenseList?";
//                "vfaceMemberCode=201504121219122
//                  &expenseType=1
//                  &pageIndex=1
//                  &pageSize=10";
        
//        "http://v_openapi.newaysoft.com/openapi/User/ExpenseList?"
//        + "vfaceMemberCode=20150607134247"
//        + "&expenseType=0"
//        + "&pageIndex=1"
//        + "&pageSize=10"
//        + "&storename"
//        + "&startDate=2015-6-9"
//        + "&endDate=2016-6-9";


        //完善个人信息
        public static final String EDIT_VFACE_MEMBER_URL = API_URL + "VFaceMember/EditVFaceMember";//{MemberCode}

        //会员登陆
        public static final String MLogin_URL = API_URL + "VFaceMember/MLogin";
        
        //获取会员账户信息
        public static final String GET_MEMBER_BALANCE_URL = API_URL + "VFaceMember/GetMemberBalance/";

        //修改密码
        public static final String CHANGE_MEMBER_PASSWORD_URL = API_URL + "VFaceMember/ChangeMemberPassword";

        //找回密码,发送验证码
        public static final String SEND_AUTH_CODE_FOR_RESET_PWD_URL = API_URL + "VFaceMember/SendAuthCodeForReSetPwd";

        //注册接口/找回密码,重置密码
        public static final String SET_MEMBER_PASSWORD_URL = API_URL + "VFaceMember/SetMemberPassword";

        //发送注册手机验证码短信
        public static final String SEND_AUTH_CODE_URL = API_URL + "VFaceMember/SendAuthCode";

        
    }
	
	public class Expense{
		
		public static final String SHOP_CONSUME_DETAIL_URL = API_URL + "Store/ExpenseDetail?";
		
		public static final String COUNTS_CONSUME_DETAIL_URL = API_URL + "User/GetCountExpenseInfo?";
		//orderNumber=JC20150520160429
		//&storeId=b91bbd48-28e5-44d7-b254-b949f7be884e
		
		public static final String TIME_CONSUME_DETAIL_URL = API_URL + "User/GetTimeExpenseInfo?";
		//orderNumber=20150518225038
		//&storeId=887ef9eb-5318-4990-ad35-7aef0899fa67
		
	}
	
	public class Common{

        //检测是否有可升级的版本 GET
        public static final String CLIENT_UPGRADE_URL = API_URL + "Upgrade/ClientUpgradeNew/";//{deviceType}/{clientType}

        //发送推送Key
        public static final String CLIENT_SET_PUSH_KEY_URL = API_URL + "VFaceMember/ClientSetPushKey/";

        public static final String GET_STORE_BUSINESS_TYPE_URL = API_URL + "Common/GetStoreBusinessType/";

	}

    public class Profile{

        //省份 GET
        public static final String GET_PROVINCE_URL = API_URL + "Common/GetProvince/";

        //城市 GET
        public static final String GET_CITY_URL = API_URL + "Common/GetCity/";//{ProvinceId}

        //区县 GET
        public static final String GET_DISTRICT_URL = API_URL + "Common/GetDistrict/";//{CityId}

    }

    public class Shop{

        //附近商家(列表)
        public static final String GET_NEARBY_STORE_URL = API_URL + "Store/getnearbystore/";//{pageIndex}/{pageSize}/{lng}/{lat}/{BusinessType}

        //商家详细信息 GET
        public static final String SHOP_DETAIL_INFO = API_URL + "Store/GetStoreById/";//{StoreId}

    }

    public class Config{

        //欢迎语设置
        public static final String CONFIG_WELCOME_VOICE_URL = API_URL + "VFaceMember/ClientSetCustomWelcome/";

    }
 

}
