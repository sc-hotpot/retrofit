package sc.hotpot.retrofit;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by unicorn
 * @date 2018/8/16.
 * description:
 */
class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();


       HttpConfigs.configs.bindNetworkHeader(builder);


        return chain.proceed(builder.build());
    }


}
