package sc.hotpot.retrofit;

public class SSLBean {
    public String assetBKSFile;
    public String password;
    public String assetCRTFile;

    public SSLBean(String assetBKSFile, String password, String assetCRTFile) {
        this.assetBKSFile=assetBKSFile;
        this.password=password;
        this.assetCRTFile=assetCRTFile;
    }

}
