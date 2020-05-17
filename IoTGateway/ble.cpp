#include "ble.h"
#include <memory>

#include <QObject>
#include <QBluetoothDeviceDiscoveryAgent>

std::shared_ptr<BLE> BLE::instance;

BLE::BLE(QObject *parent)
{
    parentQObject = parent;
    initConnection();
}

std::shared_ptr<BLE> BLE::getInstance(QObject *parent)
{
    if (BLE::instance == NULL)
    {
        BLE::instance = std::make_shared<BLE>(parent);
    }
    return BLE::instance;
}

void BLE::initConnection()
{
    deviceDiscoveryAgent = std::make_shared<QBluetoothDeviceDiscoveryAgent>(parentQObject);
    deviceDiscoveryAgent->setLowEnergyDiscoveryTimeout(5000);
}
