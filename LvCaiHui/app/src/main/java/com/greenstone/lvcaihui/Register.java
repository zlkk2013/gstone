package com.greenstone.lvcaihui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import Utils.Config;
import Utils.HttpUtility;
import Utils.Utility;


public class Register extends Activity implements View.OnClickListener{

    private EditText edtPhoneNum;
    private EditText edtPwd;
    private EditText edtRePwd;
    private EditText edtVerfiyCode;
    private Button btnRigester;
    private Button btnLogin;
    private Button btnGetVerfiyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRigester = (Button)findViewById(R.id.register_ok_btn);
        btnRigester.setOnClickListener(this);
        btnGetVerfiyCode = (Button)findViewById(R.id.register_get_verifycode_btn);
        btnGetVerfiyCode.setOnClickListener(this);
        btnLogin = (Button)findViewById(R.id.register_login_btn);
        btnLogin.setOnClickListener(this);
        edtPhoneNum = (EditText)findViewById(R.id.register_phoneNum);
        edtPwd = (EditText)findViewById(R.id.register_pwd);
        edtRePwd = (EditText)findViewById(R.id.register_repwd);
        edtVerfiyCode = (EditText)findViewById(R.id.register_verifycode);

    }
    @Override
    public void onClick(View view) {
        String phoneNum = edtPhoneNum.getText().toString().trim();
        switch (view.getId()){
            case R.id.register_get_verifycode_btn:
                if(phoneNum.equals("")){
                    Toast.makeText(getApplicationContext(),"请输入手机号码",Toast.LENGTH_SHORT).show();
                }else {
                    HttpUtility.getSMVerifyCode(phoneNum, getApplicationContext());
                }
                break;
            case R.id.register_ok_btn:
                if (inputIsOK()){
                    register();
                }
                break;
            case R.id.register_login_btn:
        }
    }


    private void register(){
        AsyncHttpClient client = HttpUtility.getClient();
        String phoneNum = edtPhoneNum.getText().toString().trim();
        String pwd = edtPwd.getText().toString().trim();
        String verifyCode = edtVerfiyCode.getText().toString().trim();
        String equipmentID = Utility.getEquipmentID(getApplicationContext());
        int type = 0;
        RequestParams params = new RequestParams();
//        if (!Utility.getUerPhoneNum(getApplicationContext()).equals(phoneNum)){
//
//        }
        params.put("pn",phoneNum);
        params.put("t",type);
        params.put("epid",equipmentID);
        params.put("vc",verifyCode);
        params.put("pwd",Utility.encripher(pwd));
        params.setUseJsonStreamer(true);
        String url = Config.URL_REGISTER;
        client.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int code = response.getInt("c");
                    Toast.makeText(getApplicationContext(),Utility.getErrorCodeDescription(code), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("response",response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),Utility.getErrorCodeDescription(statusCode), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean inputIsOK(){
        String phoneNum = edtPhoneNum.getText().toString().trim();
        String pwd = edtPwd.getText().toString().trim();
        String rePwd = edtRePwd.getText().toString().trim();
        String verifyCode = edtVerfiyCode.getText().toString().trim();

        if (phoneNum.equals("") || pwd.equals("") || rePwd.equals("")){
            Toast.makeText(getApplicationContext(),"输入不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (!Utility.getUerPhoneNum(getApplicationContext()).equals(phoneNum)){
//            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.register_linearLayout);
//            linearLayout.setVisibility(View.VISIBLE);
//        }

        if (verifyCode.equals("")){
            Toast.makeText(getApplicationContext(),"验证码不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!pwd.equals(rePwd)){
            Toast.makeText(getApplicationContext(),"密码不一致!", Toast.LENGTH_SHORT).show();
            edtPwd.setText("");
            edtRePwd.setText("");
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
