package com.ecgtheow.UsbSerial;

import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.ecgtheow.UsbSerialLib.UsbSerialDeviceFactory;

public class UsbAttached extends Activity {
	private static final String TAG = "UsbAttached";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "UsbAttached onCreate");
		
		Intent attach_intent = getIntent();
		Parcelable intent_data = attach_intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
		UsbDevice device = (UsbDevice)intent_data;
		if(device != null && UsbSerialDeviceFactory.knownDevice(device)) {
			Log.d(TAG, String.format("Got known device [0x%04X:0x%04X]", device.getVendorId(), device.getProductId()));

			try {
				if(UsbSerialApplication.getMainActivityStarted()) {
					/* Just need to tell the main activity about the new device */
					Log.d(TAG, "Main activity running!");
					Intent intent = new Intent();
					intent.setAction(UsbSerialApplication.ACTION_USB_DEVICE_ATTACHED);
					intent.putExtra(UsbManager.EXTRA_DEVICE, intent_data);
					sendBroadcast(intent);
				} else {
					/* Creating the main activity will cause a scan of devices, and will pick up this device then */
					Log.d(TAG, "Need to start main activity!");
					Intent intent = new Intent(this, UsbSerial.class);
					startActivity(intent);
				}
			} catch (Exception e) {
				Log.d(TAG, "Exception thrown", e);
			}
		}
		finish();
	}

}
