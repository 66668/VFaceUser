package com.vfaceuser.helper;

import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by HuBin on 15/5/12.
 */
public class GetPwdHelper {

    private static GetPwdHelper instance;
    public static int resendVerificationCodeTime = 0;

    private ArrayList<WaitResendObserver> waitResendObservers;
    private Handler handler = new Handler();

    public GetPwdHelper(){

        waitResendObservers = new ArrayList<WaitResendObserver>();

    }

    public static GetPwdHelper getInstance(){

        if(instance == null){
            instance = new GetPwdHelper();
        }

        return instance;

    }

    public void startWaitForResend(){
        if(resendVerificationCodeTime > 0){
            return;
        }
        resendVerificationCodeTime = 60;
        new WaitResendThread().start();
    }

    public void registerObserver(WaitResendObserver observer){
        waitResendObservers.add(observer);
    }

    public void removeObserver(WaitResendObserver observer){
        int i = waitResendObservers.indexOf(observer);
        if(i >= 0)
            waitResendObservers.remove(i);
    }


    private void notifyObservers(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (WaitResendObserver observer:waitResendObservers){
                    observer.updateResendVCodeTime(resendVerificationCodeTime);
                }
            }
        });

    }


    //等待重新发送验证码倒计时线程
    class WaitResendThread extends Thread{

        @Override
        public void run() {

            while(resendVerificationCodeTime > 0){
                try {
                    notifyObservers();
                    sleep(1000);
                    resendVerificationCodeTime--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    notifyObservers();
                }
            }
            notifyObservers();
        }

    }



    public interface WaitResendObserver{

        void updateResendVCodeTime(int resendVerificationCodeTime);

    }

}
