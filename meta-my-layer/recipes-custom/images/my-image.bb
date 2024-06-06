SUMMARY = "My image for learning purposes."
 
IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_INSTALL:append = " nano"

IMAGE_INSTALL:append = " linux-firmware-bcm43455 bcm2835-dev i2c-tools mpu6050-kermod" 
IMAGE_INSTALL:append = " wpa-supplicant resolvconf openssh packagegroup-base"

IMAGE_INSTALL:append = " python3 packagegroup-core-buildessential"

IMAGE_INSTALL:append = " my-scripts mpu-start"

IMAGE_FEATURES += "read-only-rootfs"
EXTRA_IMAGE_FEATURES += "package-management"


DISTRO_FEATURES += "wifi"
MACHINE_FEATURES += "wifi"
LICENSE = "MIT"

inherit core-image

