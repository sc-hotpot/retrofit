package sc.hotpot.retrofit;


import android.content.Context;
import android.util.Log;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import sc.hotpot.retrofit.rxutil.RxUtils;

/**
 * @author unicorn
 * @date 18-5-11
 * description:
 * 设置retrofit 配置参数，创建网络请求管理类
 */

public final class RetrofitManager {
    /**
     * 日志打印TAG
     */
    private static final String TAG = "OKHttp";

    private static Retrofit S_RETROFIT;
    private static RetrofitManager S_INSTANCE;

    private static Retrofit NO_SSL_RETROFIT;
//    private static RetrofitManager NO_SSL_INSTANCE;

    /**
     * 默认时间
     */
    private static final int DEFAULT_TIMEOUT = 60;

    /**
     * 获取单列
     *
     * @return
     */
    public static RetrofitManager getInstance() {
        synchronized (RetrofitManager.class) {
            if (S_INSTANCE == null) {
                S_INSTANCE = new RetrofitManager();
            }
            return S_INSTANCE;
        }
    }

    public <T> T getApiService(Context context, Class<T> tClass) {
        return getsRetrofit(context).create(tClass);
    }

    public <T> T getNoSSLApiService(Context context, Class<T> tClass) {
        return getNoSSlRetrofit(context).create(tClass);
    }

    public static <T> void subscribe(Flowable<T> flowable, DisposableSubscriber<T> subscriber){
        if(flowable != null){
            flowable
                    .compose(RxUtils.<T>handleGlobalError()).
                    subscribeOn(Schedulers.io())//io线程发起请求
                    .observeOn(AndroidSchedulers.mainThread())//将结果回调到主线程
                    .map(new ServerResponseFunc<T>())
                    .subscribe(subscriber);
        }
    }

    private static Retrofit getsRetrofit(Context context) {
        synchronized (RetrofitManager.class) {
            if (S_RETROFIT == null) {
                OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                        .addInterceptor(new HeaderInterceptor())
                        .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log( String message) {
                                Log.i(TAG, message);
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .cookieJar(new CookieJarManager(context));
                //如果是生产环境 则加上证书验证
                if (HttpConfigs.configs.isProduction()) {
                    HttpsUtils.SSLParameter sslParameter = HttpsUtils.getSslSocketFactory(context);
                    builder.sslSocketFactory(sslParameter.sslSocketFactory, sslParameter.trustManager)
                            .hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);

                }
                OkHttpClient client = builder.build();

                S_RETROFIT = new Retrofit.Builder()
                        .baseUrl(HttpConfigs.configs.getBaseUrl())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(client)
                        .build();
            }
            return S_RETROFIT;
        }

    }

    private static Retrofit getNoSSlRetrofit(Context context) {
        synchronized (RetrofitManager.class) {
            if (NO_SSL_RETROFIT == null) {
                OkHttpClient.Builder builder =
                        new OkHttpClient().newBuilder()
//                getUnsafeOkHttpClient()
                        .addInterceptor(new HeaderInterceptor())
                        .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log( String message) {
                                Log.i(TAG, message);
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .cookieJar(new CookieJarManager(context));
                OkHttpClient client = builder.build();

                NO_SSL_RETROFIT = new Retrofit.Builder()
                        .baseUrl(HttpConfigs.configs.getBaseUrl())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(client)
                        .build();
            }
            return NO_SSL_RETROFIT;
        }

    }
    private static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
