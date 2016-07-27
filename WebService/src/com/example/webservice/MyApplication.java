package com.example.webservice;

import android.app.Application;

/**
 * 
 * @Description:  
 * @author 
 * @date 
 *
 */
public class MyApplication extends Application{
	
    public static MyApplication myApplication;
	
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        myApplication = this;
    }
    
    // 单例模式中获取唯一的WomiApplication实例
    public static MyApplication getInstance() {
        if (null == myApplication) {
        	myApplication = new MyApplication();
        }
        return myApplication;
    }
}
