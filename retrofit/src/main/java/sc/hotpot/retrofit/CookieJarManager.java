package sc.hotpot.retrofit;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @author by unicorn
 * @date 2018/5/14.
 * description:
 */
public class CookieJarManager implements CookieJar {

    private static final String TAG = "CookieJarManager";
    private Context mContext;
    private static PersistentCookieStore cookieStore;

    /**
     *  constructor
     */
    public CookieJarManager(Context context) {
        mContext = context;
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(mContext);
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

    public void removeAll() {
        cookieStore.removeAll();
    }

}
