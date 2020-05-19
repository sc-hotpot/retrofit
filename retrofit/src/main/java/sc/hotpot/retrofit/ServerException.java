package sc.hotpot.retrofit;




public class ServerException extends Exception {
    private static final int TOKEN_INVALID = 401;//token过期
    private String msg;
    public ServerException(int errorCode, String errorText) {
        msg=errorText;
        if (HttpConfigs.i_netWorkCodeError!=null)
        {

            if(errorCode == TOKEN_INVALID){
                HttpConfigs.i_netWorkCodeError.onNetWork401(msg);
            }else {

                HttpConfigs.i_netWorkCodeError.onNetWorkOther(errorCode,msg);
            }
        }


    }

    @Override
    public String getMessage() {
        return msg;
    }
}
