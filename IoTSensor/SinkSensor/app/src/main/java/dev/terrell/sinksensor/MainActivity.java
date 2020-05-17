package dev.terrell.sinksensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SinkSensor";
    private NFC nfc;
    private BLE ble;
    private TextView guestText;
    private TextView scanText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfc = NFC.getInstance(this);
        ble = BLE.getInstance(this.getApplicationContext());
        guestText = (TextView) findViewById(R.id.guestText);
        scanText = (TextView) findViewById(R.id.scanText);
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
        Log.d(TAG, "Intent Received");
        String guestIdentity = nfc.getIdentity(intent);
        if (guestIdentity != "unknown") {
            Log.d(TAG, "Guest Identified: " + guestIdentity);
        } else {
            Log.w(TAG, "Unable to Read Tag");
        }
        guestText.setText("GuestID: " + guestIdentity);
        Date currentTime = Calendar.getInstance().getTime();
        scanText.setText("Last Scan: " + currentTime.toString());
        super.onNewIntent(intent);
    }
}
