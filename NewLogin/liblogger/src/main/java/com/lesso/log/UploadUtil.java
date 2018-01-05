package com.lesso.log;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 上传工具类
 * Created by it026 on 2017/8/23.
 */
public class UploadUtil {
    public static final String TAG = "UploadUtil";

    private static OkHttpClient mOkHttpClient;

    static {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
    }

    /**
     * 上传文件
     * @param requestUrl
     * @param filePath
     * @param callBack
     */
    public static void upLoadFile(String requestUrl, String filePath, final ReqCallBack callBack) {
        try {

            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);

            // 参数
            File file = new File(filePath);
            builder.addFormDataPart("file", file.getName(), RequestBody.create(null, file));

            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            //单独设置参数 比如读取超时时间
            final Call call = mOkHttpClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, e.toString());
                    if(callBack != null){
                        callBack.onReqFailed(e.getMessage());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "Upload response ----->" + string);
                        if(callBack != null){
                            callBack.onReqSuccess();
                        }
                    } else {
                        if(callBack != null){
                            callBack.onReqFailed("请求失败");
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ReqCallBack {
        /**
         * 响应成功
         */
        void onReqSuccess();

        /**
         * 响应失败
         */
        void onReqFailed(String errorMsg);
    }

}
