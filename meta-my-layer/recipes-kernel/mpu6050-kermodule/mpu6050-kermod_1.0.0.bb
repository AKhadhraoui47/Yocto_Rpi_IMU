SUMMARY = "Kernel module for mpu6050 imu sensor"
SECTION = "custom"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SRC_URI  = "file://inv-mpu6050-i2c.ko"


do_install() {
	install -d ${D}/home/root/inv_mpu6050
	install -m 0755 ${WORKDIR}/inv-mpu6050-i2c.ko ${D}/home/root/inv_mpu6050/
}

FILES:${PN} += "/home/root/inv_mpu6050/inv-mpu6050-i2c.ko"
