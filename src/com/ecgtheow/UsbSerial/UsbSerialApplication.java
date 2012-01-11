package com.ecgtheow.UsbSerial;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

public class UsbSerialApplication extends Application {
	private static final String TAG = "UsbSerialApplication";
	private static final String PREFS = "UsbSerialPrefs";
	
	// Intent actions
	public static final String ACTION_USB_DEVICE_ATTACHED = "com.ecgtheow.UsbSerial.UsbAttached.ACTION_USB_DEVICE_ATTACHED";
	
	private static UsbSerialApplication instance = null;
	
	private static boolean main_activity_started = false;
	
	public static SharedPreferences getPreferences() {
		checkInstance();
		
		return instance.getSharedPreferences(PREFS, MODE_PRIVATE);
	}
	
	public static void setMainActivityStarted() {
		UsbSerialApplication.main_activity_started = true;
	}
	
	public static boolean getMainActivityStarted() {
		return main_activity_started;
	}
	
	public static Resources getAppResources() {
		checkInstance();
		
		return instance.getResources();
	}
	
	private static void checkInstance() {
		if(instance == null) {
			throw new IllegalStateException("Application not created yet");
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		instance = this;
		
		Log.d(TAG, "Application created");
	}
}
