package sc.hotpot.retrofit;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class HttpFileUtils {



    public static MultipartBody.Part getFileBody(File file, final ProcessCallBack callBack)
    {
        return  getFileBody(file,"file",callBack);
    }

    public static MultipartBody.Part getFileBody(File file, String filekey, final ProcessCallBack callBack)
    {
        if (callBack!=null)
        {

            callBack.tempPer=0;
        }
        MultipartBody.Part body = MultipartBody.Part.createFormData(filekey,file.getName(), new UploadBody().<RequestBody>createProgressRequestBody(MediaType.parse("multipart/form-data"), file, new UploadBody.UploadBodyCallBack() {


            @Override
            public void onLoading(long total, long progress) {
                int per=(int)(progress*100/total);
                if (callBack!=null)
                {
                    if (per>callBack.tempPer)
                    {
                        callBack.tempPer=per;
                        callBack.onProcess(per);

                    }
                }


            }
        }));
        return  body;
    }

    public static MultipartBody.Part getFileBody(byte[] bytes,final ProcessCallBack callBack)
    {
        return  getFileBody(bytes,"file",callBack);
    }

    public static MultipartBody.Part getFileBody(byte[] bytes, String fileKey, final ProcessCallBack callBack)
    {
        if (callBack!=null)
        {

            callBack.tempPer=0;
        }
        MultipartBody.Part body = MultipartBody.Part.createFormData(fileKey,"xx.jpg",
                new UploadBody(bytes).<RequestBody>createProgressRequestBody(MediaType.parse("multipart/form-data"), null, new UploadBody.UploadBodyCallBack() {


            @Override
            public void onLoading(long total, long progress) {
                int per=(int)(progress*100/total);
                if (callBack!=null)
                {
                    if (per>callBack.tempPer)
                    {
                        callBack.tempPer=per;
                        callBack.onProcess(per);

                    }
                }


            }
        }));
        return  body;
    }




}
