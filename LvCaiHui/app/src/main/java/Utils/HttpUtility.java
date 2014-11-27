package Utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by gzc on 14-10-28.
 */
public class HttpUtility {

    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        setUserAgent(client);
    }

    public static void setUserAgent(AsyncHttpClient c) {
        /*
        Log.v("OS Version", System.getProperty("os.version"));
        Log.v("MODEL", Build.MODEL);
        Log.v("SERIAL", Build.SERIAL);
        */
        c.setUserAgent("exp/" + Config.VERSION + "/" + System.getProperty("os.version") + "/" + Build.MODEL + "/" + Build.SERIAL);
    }

    public static AsyncHttpClient getClient() {
        return client;
    }

    public static AsyncHttpClient createClient() {
        AsyncHttpClient c = new AsyncHttpClient();
        setUserAgent(c);
        return c;
    }

    public static void uploatFiles(String url, String filePath, final Context context){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setUserAgent(Utility.USER_AGENT);
        File mFile = new File(filePath);
        String dataID = Utility.getMD5(mFile);
        String uploadUrl = url + "&id="+dataID;
        RequestParams params = new RequestParams();
        try {
            params.put(dataID, mFile);
            asyncHttpClient.post(uploadUrl,params,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.v("upload", "failed");
                    switch (statusCode) {
                        case 400:
                            Toast.makeText(context, "请求格式错误！", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(context, "服务器内部错误！", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Log.v("statusCode", String.valueOf(statusCode));
                            break;
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void downloadFile(String url, final Context context){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {

            }
        });

    }
    public static void getSMVerifyCode(String phoneNum, final Context context){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setUserAgent(Utility.USER_AGENT);
        String url = Config.URL_OBTAINVERIFYCODE + "&pn=" + phoneNum;
//        String url = "http://182.92.235.86/comm/Verify.do?v=1.0.0&pn=" + phoneNum;
        Log.v("url",url);
        client.get(context, url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(context,"获取成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(context,"获取失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class JsonRequestParams extends RequestParams {
        public JsonRequestParams() {
            super();
            this.setUseJsonStreamer(true);
        }

        public JsonRequestParams(Map<String, String> source) {
            super(source);
            this.setUseJsonStreamer(true);
        }

        public JsonRequestParams(String key, String value) {
            super(key, value);
            this.setUseJsonStreamer(true);
        }

        public JsonRequestParams(Object... keysAndValues) {
            super(keysAndValues);
            this.setUseJsonStreamer(true);
        }
    }
}
