package dev.terrell.sinksensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String LogTag = "SinkSensor";
    private NFC nfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfc = NFC.getInstance(this);
    }

    @Override
    protected void onResume() {
        nfc.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        nfc.onPause();
        super.onPause();
    }

    @Override
    public void onNewIntent(final Intent intent) {
        Log.d(LogTag, "Intent Received");
        String guestIdentity = nfc.getIdentity(intent);
        if (guestIdentity != "unknown") {
            Log.d(LogTag, "Guest Identified: " + guestIdentity);
        } else {
            Log.w(LogTag, "Unable to Read Tag");
        }
        super.onNewIntent(intent);
    }
}
