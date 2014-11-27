package Utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by gzc on 14-10-26.
 */
public class Utility {
    private static Pattern pattern;
    private static Matcher matcher;

    public static final String USER_AGENT = "jlt/" + Config.VERSION + "/" + System.getProperty("os.version") + "/" + Build.MODEL + "/" + Build.SERIAL;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String CELLPHONE_PATTERN = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
    public static boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isNotBlank(String txt){

        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    public static String getEquipmentID(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
    /**
     * 验证手机号码
     *
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189
     *
     * @param cellphone
     * @return
     */
    public static boolean checkCellphone(String cellphone) {
        pattern = Pattern.compile(CELLPHONE_PATTERN);
        matcher = pattern.matcher(cellphone);
        return matcher.matches();
    }

    public static boolean checkPwd(String pwd){
        int pwdlen = pwd.length();
        if(pwdlen<6||pwdlen>16){
            return false;
        }
        return true;
    }
    public static byte[] HmacSHA1Encrypt(String encryptText,String encryptKey) throws Exception {

        byte[] data = encryptKey.getBytes("UTF-8");
        SecretKey secretKey = new SecretKeySpec(data,"HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        byte[] text = encryptText.getBytes("UTF-8");
        return mac.doFinal(text);

    }
    public static String getBase64(byte[] data) throws UnsupportedEncodingException {

        return Base64.encodeToString(data, Base64.DEFAULT);

    }
    
    public static String encripher(String inStr){
        MessageDigest messageDigest = null;
        String outStr = "";
        String temp = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] digest = messageDigest.digest(inStr.getBytes());
            for (int i = 0; i <digest.length; i++ ){
                temp = Integer.toHexString(digest[i] & 0XFF);
                if(temp.length()<1){
                    outStr+="0";
                }
                outStr += temp;
            }
            //outStr = bytetoString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return outStr.toUpperCase();
    }


    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    private static byte charToByte(char c){
        return (byte)"0123456789ABCDEF".indexOf(c);
    }

    //获取文件MD5值
    public static String getMD5(File file){

        FileInputStream fileInputStream = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fileInputStream.read(buffer))!= -1){
                messageDigest.update(buffer, 0, length);
            }
            byte[]bytes = messageDigest.digest();
            return byteToHexSting(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    public static String byteToHexSting(byte[] bytes) {

        int j = bytes.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = bytes[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }
    public static String getErrorCodeDescription(int statusCode){
        Map<Integer,String> errorCode = new HashMap<Integer, String>();
        errorCode.put(200,"成功");
        errorCode.put(400,"客户端格式错误");
        errorCode.put(500,"服务器内部错误");
        errorCode.put(1000,"成功");
        errorCode.put(1001,"短信验证码错误");
        errorCode.put(1002,"需要图形验证码");
        errorCode.put(1003,"图形验证码错误");
        errorCode.put(1004,"用户已注册");
        errorCode.put(1005,"认证失败");
        errorCode.put(1006,"旧密码错误");
        errorCode.put(1007,"账号被冻结");
        errorCode.put(1008,"数据文件不存在");
        errorCode.put(1009,"权限不足");

        return errorCode.get(statusCode);
    }

    public static String getLawyerGrade(int grade){
        Map<Integer,String> lawyerGrade = new HashMap<Integer, String>();
        lawyerGrade.put(1,"律师");
        lawyerGrade.put(2,"合伙人");
        lawyerGrade.put(3,"高级合伙人");
        return lawyerGrade.get(grade);
    }

    public static String getUerPhoneNum(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }
}
