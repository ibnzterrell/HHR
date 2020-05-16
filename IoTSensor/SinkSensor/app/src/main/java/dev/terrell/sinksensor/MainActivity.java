package dev.terrell.sinksensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.CardType;

public class MainActivity extends AppCompatActivity {
    private NxpNfcLib nfc;
    private String LogTag = "SinkSensor";
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
        detectNFC(intent);
        super.onNewIntent(intent);
    }

    private void detectNFC(final Intent intent)
    {
        CardType cardType = nfc.getCardType(intent);
        Log.d(LogTag, "Card Type Detected: " + cardType.getTagName());
    }
}
