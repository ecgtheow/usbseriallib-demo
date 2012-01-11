package com.ecgtheow.UsbSerial;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ecgtheow.UsbSerialLib.UsbDeviceConnectionEvent;
import com.ecgtheow.UsbSerialLib.UsbDeviceReadEvent;
import com.ecgtheow.UsbSerialLib.UsbSerialDevice;
import com.ecgtheow.UsbSerialLib.UsbSerialLib;

public class UsbSerial extends Activity implements UsbDeviceConnectionEvent, UsbDeviceReadEvent {
	private static final String TAG = "UsbSerial";
	
	private UsbSerialLib usb;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		IntentFilter permission_filter = new IntentFilter();
		permission_filter.addAction(UsbSerialApplication.ACTION_USB_DEVICE_ATTACHED);
		registerReceiver(usb_receiver, permission_filter);
		
        try {
        	Log.w(TAG, "Starting USB!");
        	usb = new UsbSerialLib(this, this, this);
        	usb.connectDevices();
        } catch (Exception e) {
        	Log.e(TAG, "Exception thrown", e);
        	Toast.makeText(this, e.getClass().getName() + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        UsbSerialApplication.setMainActivityStarted();
        
        setContentView(R.layout.main);
    }
    
	private final BroadcastReceiver usb_receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(TAG, String.format("Received action: %s", action));
			
			if(UsbSerialApplication.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
				Log.d(TAG, "Got usb attach");
				UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				if(device != null) {
					usb.connectDevice(device);
				}
			}
		}
	};
    
    public void onUsbDeviceConnected(UsbSerialDevice device) {
    	Toast.makeText(this, device.getName() + " connected!", Toast.LENGTH_LONG).show();
    	//device.stop();
    }

    public void onReadData(UsbSerialDevice device, byte[] buf) {
    	String str = new String(buf);
    	Log.d(TAG, String.format ("Read data: %s (len %d)", str, buf.length));
    }
}