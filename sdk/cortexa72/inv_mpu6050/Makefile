#obj-m += aht10.o
obj-m += inv-mpu6050-i2c.o
inv-mpu6050-i2c-y := inv_mpu_i2c.o inv_mpu_acpi.o

KERNEL_DIR = /home/tsarbomba/Documents/sdk/cortexa72/sysroots/cortexa72-poky-linux/lib/modules/5.15.92-v8/build

all:
	make -C $(KERNEL_DIR) M=/home/tsarbomba/Documents/sdk/cortexa72/inv_mpu6050 modules

clean:
	make -C $(KERNEL_DIR) clean

