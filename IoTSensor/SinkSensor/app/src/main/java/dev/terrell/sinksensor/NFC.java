package dev.terrell.sinksensor;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.CardType;
import com.nxp.nfclib.ultralight.UltralightFactory;
import com.nxp.nfclib.ultralight.UltralightC;
import com.nxp.nfclib.desfire.DESFireFactory;
import com.nxp.nfclib.desfire.IDESFireEV1;
import java.util.Base64;

public class NFC {
    private static final String TAG = "SinkSensor";
    private static NFC instance;
    private NxpNfcLib lib;

    private NFC(Activity a) {
        lib = NxpNfcLib.getInstance();
        try {
            lib.registerActivity(a, Secrets.MifareKey);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void onResume() {
        lib.startForeGroundDispatch();
    }

    public void onPause() {
        lib.stopForeGroundDispatch();
    }

    public static NFC getInstance(Activity a) {
        if (instance == null) {
            instance = new NFC(a);
        }
        return instance;
    }

    public String getIdentity(Intent intent) {
        String tagName = "unknown";
        try {
            CardType cardType = lib.getCardType(intent);
            tagName = cardType.getTagName();
        } catch (Exception e) {

        }
        Log.d(TAG, "Tag Type: " + tagName);
        switch (tagName) {
            case "DESFire EV1":
                return getDESFireEV1Identity();
            case "Ultralight C":
                return getUltralightCIdentity();
            default:
                return "unknown";
        }
    }

    private String getDESFireEV1Identity() {
        IDESFireEV1 card = DESFireFactory.getInstance().getDESFire(lib.getCustomModules());
        card.getReader().connect();
        String identity = "unknown";
        try {
            byte[] data = card.getUID();
            identity = Base64.getEncoder().encodeToString(data);
            Log.d(TAG, identity);
        } catch (Exception e) {
            Log.e(TAG, "DESFire EV1 Read Error: " + e);
        }
        return identity;
    }

    private String getUltralightCIdentity() {
        UltralightC card = UltralightFactory.getInstance().getUltralightC(lib.getCustomModules());
        card.getReader().connect();
        String identity = "unknown";
        try {
            byte[] data = card.getUID();
            identity = Base64.getEncoder().encodeToString(data);
            Log.d(TAG, identity);
        } catch (Exception e) {
            Log.e(TAG, "Ultralight C Read Error: " + e);
        }
        return identity;
    }
}
