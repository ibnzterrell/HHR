#ifndef BLE_H
#define BLE_H

#include <memory>
#include <QObject>
#include <QBluetoothDeviceDiscoveryAgent>

class BLE : public QObject
{
    Q_OBJECT
public:
    BLE(QObject *parent);
    static std::shared_ptr<BLE> getInstance(QObject *parent);

private:
    static std::shared_ptr<BLE> instance;
    QObject *parentQObject;
    std::shared_ptr<QBluetoothDeviceDiscoveryAgent> deviceDiscoveryAgent;
    void initConnection();
};

#endif // BLE_H
