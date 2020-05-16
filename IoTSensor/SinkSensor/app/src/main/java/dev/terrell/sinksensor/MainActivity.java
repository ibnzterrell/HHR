package dev.terrell.sinksensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nxp.nfclib.NxpNfcLib;

public class MainActivity extends AppCompatActivity {
    private NxpNfcLib nfc;
    private static final String LogTag = "SinkSensor";

    private void initNFC()
    {
        nfc = NxpNfcLib.getInstance();
        try {
            nfc.registerActivity(this, Secrets.MifareKey);
        }catch (Exception e){
            Log.e(LogTag, e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNFC();
    }

    @Override
    protected void onResume ()
    {
        nfc.startForeGroundDispatch();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        nfc.stopForeGroundDispatch();
        super.onPause();
    }

    @Override
    public void onNewIntent(final Intent intent)
    {
        Log.d(LogTag, "Intent Received");
        String guestIdentity = Guest.getIdentity(nfc, intent);
        if (guestIdentity != "unknown") {
            Log.d(LogTag, "Guest Identified: " + guestIdentity);
        }
        else {
            Log.w(LogTag, "Unable to Read Tag");
        }
        super.onNewIntent(intent);
    }
}
