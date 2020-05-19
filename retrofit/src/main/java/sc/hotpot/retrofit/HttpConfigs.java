package sc.hotpot.retrofit;



import okhttp3.Request;


public  class HttpConfigs {
    public  static  I_HttpConfig configs;
//    public  static Context context;

    public  static I_NetWorkCodeError i_netWorkCodeError;
//    public static I_HttpConfig getInstance(){
//        return  configs;
//    }
    public static void init(I_HttpConfig configs, I_NetWorkCodeError i_netWorkCodeError){
        HttpConfigs.configs=configs;
        HttpConfigs.i_netWorkCodeError=i_netWorkCodeError;
//        HttpConfigs.context=context;
    }

    public interface I_HttpConfig{

        /**
         *
         * @return  获取URL
         */
         String getBaseUrl();

        /**
         *
         * @return 绑定与服务器通讯证书
         */
         SSLBean bindNetWorkSSL();

        /**
         *
         * @return  判断是否是生产环境，影响网络是否使用证书
         */
         boolean isProduction();

        /**
         *
         * @param builder 绑定http header
         *                例如 builder.addHeader(HttpConstant.KEY_VERSION, AppManager.getAppVersionName())
         * @return
         */
         void bindNetworkHeader(Request.Builder builder);
    }

    public interface I_NetWorkCodeError <T>{
        void onNetWork401(String msg);
        void onNetWorkOther(int code,String msg);
        void onNetWork200WithMSG(String msg);
        void onJsonError(String msg);
        Throwable  onDateFilter(T result);
    }
}
