
SUMMARY = "Script to start mpu6050 datat reading"
SECTION = "custom"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302" 

SRC_URI  = "file://mpu"

inherit update-rc.d

INITSCRIPT_NAME = "mpu"

do_install() {
  install -d ${D}${sysconfdir}
  install -d ${D}${sysconfdir}/init.d
  install -m 755 ${WORKDIR}/mpu ${D}${sysconfdir}/init.d/mpu
}
