package dev.terrell.sinksensor;

import android.content.Intent;
import android.util.Log;

import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.CardType;
import com.nxp.nfclib.ultralight.UltralightFactory;
import com.nxp.nfclib.ultralight.UltralightC;
import com.nxp.nfclib.desfire.DESFireFactory;
import com.nxp.nfclib.desfire.IDESFireEV1;
import java.util.Base64;

public class Guest {
    private static final String LogTag = "SinkSensor";

    public static String getIdentity(NxpNfcLib nfc, Intent intent) {
        String tagName = "unknown";
        try {
            CardType cardType = nfc.getCardType(intent);
            tagName = cardType.getTagName();
        } catch (Exception e) {

        }
        Log.d(LogTag, "Tag Type: " + tagName);
        switch (tagName) {
            case "DESFire EV1":
                return getDESFireEV1Identity(nfc);
            case "Ultralight C":
                return getUltralightCIdentity(nfc);
            default:
                return "unknown";
        }
    }

    private static String getDESFireEV1Identity(NxpNfcLib nfc) {
        IDESFireEV1 card = DESFireFactory.getInstance().getDESFire(nfc.getCustomModules());
        card.getReader().connect();
        String identity = "unknown";
        try {
            byte[] data = card.getUID();
            identity = Base64.getEncoder().encodeToString(data);
            Log.d(LogTag, identity);
        } catch (Exception e) {
            Log.e(LogTag, "DESFire EV1 Read Error: " + e);
        }
        return identity;
    }

    private static String getUltralightCIdentity(NxpNfcLib nfc) {
        UltralightC card = UltralightFactory.getInstance().getUltralightC(nfc.getCustomModules());
        card.getReader().connect();
        String identity = "unknown";
        try {
            byte[] data = card.getUID();
            identity = Base64.getEncoder().encodeToString(data);
            Log.d(LogTag, identity);
        } catch (Exception e) {
            Log.e(LogTag, "Ultralight C Read Error: " + e);
        }
        return identity;
    }
}
