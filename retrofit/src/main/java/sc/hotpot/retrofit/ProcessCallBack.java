package sc.hotpot.retrofit;

public abstract class ProcessCallBack {
    public int tempPer=0;
    public boolean success;
    public boolean uploading;
    public abstract void onProcess(int per);
    public abstract void onSucess(Object object);
    public abstract void onFailed();
    public int getPer()
    {
        if (tempPer>100||success)
            return 100;
        return tempPer;
    }
}
