package sc.hotpot.retrofit;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class UploadBody {
    byte[] bytes;
    public UploadBody()
    {

    }
    public UploadBody(byte[] bytes)
    {
        this.bytes=bytes;
    }
    public  RequestBody createProgressRequestBody(final MediaType contentType, final File file, final UploadBodyCallBack callback) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return bytes==null?file.length():bytes.length;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;

                Log.e("debug", " UploadBody writeTo " );
                try {
                    if (bytes==null)
                    {
                        source = Okio.source(file);
                        Buffer buf = new Buffer();
                        long remaining = contentLength();
                        long current = 0;
                        for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                            sink.write(buf, readCount);
                            current += readCount;
                            //callback 进度通知
                            if (callback!=null){

                                callback.onLoading(remaining,current);
                                Log.e("debug", String.format(" UploadBody writeTo %s/%s",""+current,""+remaining) );
                            }
                        }
                    }
                    else
                    {
                        source =Okio.source(new ByteArrayInputStream(bytes));
                        Buffer buf = new Buffer();
                        long remaining = contentLength();
                        long current = 0;
                        for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                            sink.write(buf, readCount);
                            current += readCount;
                            //callback  进度通知
                            if (callback!=null){

                                callback.onLoading(remaining,current);
                                Log.e("debug", String.format(" UploadBody writeTo %s/%s",""+current,""+remaining) );
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
    }
    public interface  UploadBodyCallBack
    {
        void onLoading(long total, long now);
    }
}
