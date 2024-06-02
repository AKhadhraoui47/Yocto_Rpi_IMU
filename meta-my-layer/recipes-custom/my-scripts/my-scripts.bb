SUMMARY = "Custom scripts for rfs rw/ro manipulation"
SECTION = "custom"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI  = "file://ro"
SRC_URI += "file://rw"
SRC_URI += "file://mpulaunch"

do_install() {
     install -d ${D}${sbindir}
     install -m 0755 ${WORKDIR}/ro ${D}${sbindir}
     install -m 0755 ${WORKDIR}/rw ${D}${sbindir}
     install -m 0755 ${WORKDIR}/mpulaunch ${D}${sbindir}
}

FILES:${PN} += "${sbindir}/ro"
FILES:${PN} += "${sbindir}/rw"
FILES:${PN} += "${sbindir}/mpulaunch"
