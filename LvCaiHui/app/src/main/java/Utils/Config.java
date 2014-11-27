package Utils;

/**
 * Created by Administrator on 2014/10/29.
 */
public class Config {

    public static final String HOST_WITHOUT_PROTOCAL = "182.92.235.86";
    public static final String HOST = "http://" + HOST_WITHOUT_PROTOCAL;
    public static final String VERSION = "1.0.0";

    //RESTully APIs
    public static final String URL_REGISTER = HOST + "/exp/Reg.do?v=" + VERSION;
    public static final String URL_OBTAINVERIFYCODE = HOST + "/comm/Verify.do?v=" + VERSION;
    public static final String URL_LOGIN = HOST + "/exp/Login.do?v=" +VERSION;

}
