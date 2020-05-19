package sc.hotpot.retrofit.rxutil;

import android.widget.Toast;

import com.github.qingmei2.retry.RetryConfig;

import org.json.JSONException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.HttpException;
import sc.hotpot.retrofit.HttpConfigs;

public class RxUtils {

    private static final int STATUS_UNAUTHORIZED = 401;

    public static <T > GlobalErrorTransformer<T> handleGlobalError() {

        return new GlobalErrorTransformer<T>(

                // 通过onNext流中数据的状态进行操作
                new Function<T, Observable<T>>() {
                    @Override
                    public Observable<T> apply(T it) throws Exception {
                        Throwable err=HttpConfigs.i_netWorkCodeError.onDateFilter(it);
                        if (err!=null)
                        {

                            return Observable.error(err);
                        }

                        return Observable.just(it);
                    }
                },

                // 通过onError中Throwable状态进行操作
                new Function<Throwable, Observable<T>>() {
                    @Override
                    public Observable<T> apply(Throwable error) throws Exception {
                        if (error instanceof HttpException )
                        {
                            if(((HttpException) error).code()==STATUS_UNAUTHORIZED)
                            {
                                if (HttpConfigs.i_netWorkCodeError!=null)
                                {
                                    HttpConfigs.i_netWorkCodeError.onNetWork401("HTTP ERROR:401");
                                }

                            }
                            else
                            {
                                if (HttpConfigs.i_netWorkCodeError!=null)
                                {
                                    HttpConfigs.i_netWorkCodeError.onNetWorkOther(((HttpException) error).code(),((HttpException) error).getMessage());
                                }
                            }
                        }
                        return Observable.error(error);
                    }
                },

                new Function<Throwable, RetryConfig>() {
                    @Override
                    public RetryConfig apply(Throwable error) throws Exception {

//                        if (error instanceof ConnectFailedAlertDialogException) {
//                            return new RetryConfig(
//                                    new Suppiler<Single<Boolean>>() {
//                                        @Override
//                                        public Single<Boolean> call() {
//                                            return RxDialog.showErrorDialog(activity, "ConnectException")
//                                                    .flatMap(new Function<Boolean, SingleSource<? extends Boolean>>() {
//                                                        @Override
//                                                        public SingleSource<? extends Boolean> apply(Boolean retry) throws Exception {
//                                                            return Single.just(retry);
//                                                        }
//                                                    });
//                                        }
//                                    });
//                        }



                        return new RetryConfig();   // 其它异常都不重试
                    }
                },

                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof JSONException) {

                            if (HttpConfigs.i_netWorkCodeError!=null)
                            {
                                HttpConfigs.i_netWorkCodeError.onJsonError(throwable.getMessage());
                            }
                        }
                    }
                }
        );
    }
}