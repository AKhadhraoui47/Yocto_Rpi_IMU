SUMMARY = "My image for learning purposes."

#Boot 
IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

#Text Editor
IMAGE_INSTALL:append = " nano"

#Wireless Connection 
IMAGE_INSTALL:append = " linux-firmware-bcm43455 bcm2835-dev i2c-tools mpu6050-kermod" 
IMAGE_INSTALL:append = " wpa-supplicant resolvconf openssh packagegroup-base"

#On Target Dev Modules
IMAGE_INSTALL:append = " python3 packagegroup-core-buildessential kernel-devsrc"
IMAGE_INSTALL:append = " kernel-dev kernel-modules linux-libc-headers-dev lmsensors"

#My Shell Scripts mpu-start
IMAGE_INSTALL:append = " my-scripts mpu-start"
#Read-Only RFS
IMAGE_FEATURES += "read-only-rootfs"
EXTRA_IMAGE_FEATURES += "package-management"


#################################
DISTRO_FEATURES += "wifi"
MACHINE_FEATURES += "wifi"
LICENSE = "MIT"
#################################

inherit core-image

