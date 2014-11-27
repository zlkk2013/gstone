package com.greenstone.lvcaihui;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import Utils.Config;
import Utils.CryptoUtility;
import Utils.HttpUtility;
import Utils.Utility;


public class Login extends Activity implements View.OnClickListener{

    private Button loginBtn;
    private Button goRegister;
    private EditText edtPhoneNum;
    private EditText edtPwd;
    private TextView forgetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button)findViewById(R.id.login_button);
        loginBtn.setOnClickListener(this);
        goRegister = (Button)findViewById(R.id.login_go_register_btn);
        goRegister.setOnClickListener(this);
        edtPhoneNum = (EditText)findViewById(R.id.login_phoneNum);
        edtPwd = (EditText)findViewById(R.id.longin_pwd);
        forgetPwd = (TextView)findViewById(R.id.login_forget_pwd_tv);
        forgetPwd.setText(getSpannableString(forgetPwd.getText().toString()));
        forgetPwd.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private SpannableStringBuilder getSpannableString(String str){
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //跳转到忘记密码页面
//                Intent intent = new Intent(getApplicationContext(),RetrievePassword.class);
//                startActivity(intent);
            }
        };
        builder.setSpan(clickableSpan,0,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                login();
                break;
            case R.id.login_go_register_btn:

        }

    }
    private boolean inputIsOk(){
        String phoneNum = edtPhoneNum.getText().toString().trim();
        String pwd = edtPwd.getText().toString().trim();
        if (phoneNum.equals("")||pwd.equals("")){
            Toast.makeText(getApplicationContext(), "账号或密码不能无空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login(){
        AsyncHttpClient client = HttpUtility.getClient();
        String phoneNum = edtPhoneNum.getText().toString().trim();
        String pwd = edtPwd.getText().toString().trim();
        String url = Config.URL_LOGIN  ;
        Header[] headers = {
                CryptoUtility.genAuthHeader(url, phoneNum, Utility.encripher(pwd), "GET")};
        client.get(getApplicationContext(), url, headers, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode == 200){
                    try {
                        int code = response.getInt("c");
                        Toast.makeText(getApplicationContext(), Utility.getErrorCodeDescription(code), Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = response.getJSONObject("u");
                        User.save(jsonObject.getString("k"),jsonObject.getInt("uid"),jsonObject.getInt("t"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.v("response", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.v("login_error", responseString);
                Toast.makeText(getApplicationContext(),Utility.getErrorCodeDescription(statusCode), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
