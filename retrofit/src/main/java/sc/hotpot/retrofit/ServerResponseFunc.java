package sc.hotpot.retrofit;

import android.text.TextUtils;

import io.reactivex.functions.Function;
import retrofit2.Response;


public class ServerResponseFunc<T> implements Function<T, T> {

    @Override
    public T apply(T t) throws Exception {
        //对返回码进行判断，如果不是0，则证明服务器端返回错误信息了，便根据跟服务器约定好的错误码去解析异常
        if(t instanceof Response){
            if(((Response) t).code() != 200){
                //如果服务器端有错误信息返回，那么抛出异常，让下面的方法去捕获异常做统一处理
                throw new ServerException(((Response)t).code()
                        ,((Response)t).message()+"\n");
            }
            else
            {
                if (!TextUtils.isEmpty(((Response) t).message()))
                {

                    if (HttpConfigs.i_netWorkCodeError!=null)
                    {

                        HttpConfigs.i_netWorkCodeError.onNetWork200WithMSG(((Response) t).message());
                    }
                }
            }

        }

        //服务器请求数据成功，返回里面的数据实体
        return t;
    }

}
