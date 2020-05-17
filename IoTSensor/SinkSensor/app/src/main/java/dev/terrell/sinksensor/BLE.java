package dev.terrell.sinksensor;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.UUID;

public class BLE {
    private static BLE instance;
    private AdvertiseSettings settings;
    private AdvertiseData advertiseData;
    private AdvertiseData scanResponseData;
    private AdvertiseCallback advertiseCallback;
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;
    private BluetoothGattServerCallback bluetoothGattServerCallback;
    private BluetoothGattService bluetoothGattService;
    private BluetoothGattServer bluetoothGattServer;
    private BluetoothGattCharacteristic identityCharacteristic;
    private BluetoothManager bluetoothManager;
    private static final String serviceUUID = "37b3c7d1-97e1-11ea-bb37-0242ac130002";
    private static final ParcelUuid serviceParcelUUID = new ParcelUuid(UUID.fromString(serviceUUID));
    private static final String identityCharacteristicUUID = "37b3c7d2-97e1-11ea-bb37-0242ac130002";
    private static final ParcelUuid identityCharacteristicParcelUUID = new ParcelUuid(
            UUID.fromString(identityCharacteristicUUID));

    private static final String TAG = "SinkSensor";

    private BLE(Context context) {
        initAdvertising(context);
        initGATTServer();
        startGATTServer(context);
        startAdvertising();
    }

    private void initAdvertising(Context context) {
        settings = new AdvertiseSettings.Builder().setConnectable(true).build();

        advertiseData = new AdvertiseData.Builder().setIncludeDeviceName(true).setIncludeTxPowerLevel(true).build();

        scanResponseData = new AdvertiseData.Builder().addServiceUuid(serviceParcelUUID).setIncludeTxPowerLevel(true)
                .build();

        advertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.d(TAG, "BLE advertisement added successfully");
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e(TAG, "Failed to add BLE advertisement, reason: " + errorCode);
            }
        };
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothLeAdvertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
    }

    private void startAdvertising() {
        bluetoothLeAdvertiser.startAdvertising(settings, advertiseData, scanResponseData, advertiseCallback);
    }

    private void initGATTServer() {
        bluetoothGattServerCallback = new BluetoothGattServerCallback() {
            @Override
            public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset,
                                                    BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            }

            @Override
            public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId,
                    BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded,
                    int offset, byte[] value) {
                super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded,
                        offset, value);
            }

            @Override
            public void onDescriptorWriteRequest(BluetoothDevice device, int requestId,
                                                 BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset,
                                                 byte[] value) {
                super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset,
                        value);
            }

            @Override
            public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset,
                    BluetoothGattDescriptor descriptor) {
                super.onDescriptorReadRequest(device, requestId, offset, descriptor);
            }

        };
        identityCharacteristic = new BluetoothGattCharacteristic(identityCharacteristicParcelUUID.getUuid(),
                BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ);
    }

    private void startGATTServer(Context context) {
        bluetoothGattServer = bluetoothManager.openGattServer(context, bluetoothGattServerCallback);
        bluetoothGattService = new BluetoothGattService(serviceParcelUUID.getUuid(), BluetoothGattService.SERVICE_TYPE_PRIMARY);

        bluetoothGattService.addCharacteristic(identityCharacteristic);

        bluetoothGattServer.addService(bluetoothGattService);
    }

    public static BLE getInstance(Context context) {
        if (instance == null) {
            instance = new BLE(context);
        }
        return instance;
    }
}
