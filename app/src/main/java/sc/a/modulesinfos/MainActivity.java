package sc.a.modulesinfos;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Request;
import sc.hotpot.retrofit.HttpConfigs;
import sc.hotpot.retrofit.SSLBean;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            public  Throwable onDateFilter(Object result) {
                return null;
            }
        });
    }
}
