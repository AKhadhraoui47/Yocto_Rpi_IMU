SUMMARY = "Python module for mpu9250 imu sensor"
SECTION = "custom"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SRC_URI  = "file://mpu9250.py"

RDEPENDS:python3-mpu9250 += "python3-smbus"

do_install() {
    install -d ${D}/home/root/python-modules/mpu9250
    install -m 0755 ${WORKDIR}/mpu9250.py ${D}/home/root/python-modules/mpu9250
}

FILES:${PN} += "/home/root/python-modules/mpu9250/mpu9250.py"

