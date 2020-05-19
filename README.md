# retrofit
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.sc-hotpot:retrofit:0.1'
	}
  
step 3. init before use 
  
        HttpConfigs.init(new HttpConfigs.I_HttpConfig() {
            @Override
            public String getBaseUrl() {
                return null;
            }

            @Override
            public SSLBean bindNetWorkSSL() {
                return null;
            }

            @Override
            public boolean isProduction() {
                return false;
            }

            @Override
            public void bindNetworkHeader(Request.Builder builder) {

            }
        }, new HttpConfigs.I_NetWorkCodeError() {
            @Override
            public void onNetWork401(String msg) {

            }

            @Override
            public void onNetWorkOther(int code, String msg) {

            }

            @Override
            public void onNetWork200WithMSG(String msg) {

            }

            @Override
            public void onJsonError(String msg) {

            }

            @Override
            public Throwable onDateFilter(Object result) {
                return null;
            }
        });
        
step4, create the Api interface and manage
    =============================interface========================
    
    public interface Api {
    
    @GET("xx/xx")
    Flowable<Object> dosome(@Query("phone") String phone);
    
    }
    
    =============================get interface===========================
    
    public class ApiManager {
    private ApiManager()
    {
    }
    public static Api getApi()
    {
        return RetrofitManager.getInstance().getApiService(Application.context,Api.class);
    }
    }
    
    
    =============================start response===========================
    
     public static void dosome(String phone ,DisposableSubscriber subscriber) {
        RetrofitManager.subscribe(ApiManager.getApi().dosome(phone,applyCode), subscriber);
    }
    
    
    
    
    
