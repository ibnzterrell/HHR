#include "mainwindow.h"

#include <QApplication>
#include <memory>
#include "ble.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    std::shared_ptr<BLE> ble = BLE::getInstance(&a);
    MainWindow w;
    w.show();
    return a.exec();
}
