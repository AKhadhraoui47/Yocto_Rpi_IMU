# Yocto_Rpi_IMU
This repository chronicles my journey of learning Embedded Linux and Yocto from the ground up. 
It concludes with the development of a custom-built image for a Raspberry Pi, designed to read and log data from an MPU6050 sensor.

> Exploring **Yocto** and the basics of **Embedded Linux** from scratch can seem to be a heavy task to handle, therefore to have a steady progress, it is preferable to break it down into manageable topics.

## Compilation Process

The compilation process of C code involves several steps, each transforming the source code into executable machine code. To explore practically the process we will be using the **GNU Compiler Collection GCC**. Here are the key steps:

### 1. Preprocessing

The preprocessor handles directives (lines starting with #) in the source code. It performs tasks such as:  
  
* Including header files ( #include )  
* Macro substitution ( #define )  
* Conditional compilation ( #ifdef, #ifndef, etc.)  
  
The output is an expanded source code file.  
  
```console  
ak47@ak47:~$ gcc -o file.i -E file.c    
```  
### 2. Compilation

The compiler translates the preprocessed code into assembly code. This step includes:  
  
* Syntax and semantic analysis  
* Intermediate code generation  
* Optimization (depending on compiler settings)  
  
The result is an assembly file.  
  
```console  
ak47@ak47:~$ gcc -o file.s -S file.i
``` 
  
### 3. Assembly
  
The assembler converts the assembly code into machine code, producing an object file. This file contains binary code that the machine can execute, but it is not yet a complete program.  
  
The result is an object file.  
  
```console
ak47@ak47:~$ gcc -o file.o -c file.s
```  
  
### 4. Linking  
  
The linker combines one or more object files with libraries, resolving references to external symbols (functions and variables) to produce an executable. This step includes:  
  
* Linking standard libraries (e.g., C standard library)  
* Handling static and dynamic linking  
* Relocating code and data addresses  
  
The output is the final executable program.  
  
```console
ak47@ak47:~$ gcc -o exe file.o 
```  
    
> Going through all these steps is not mandantory to get the executable. These steps can be summarized in single command: **ak47@ak47:~$ gcc -o exe file.c**  
  
## Debugging 
  
Debugging C code involves using tools and techniques to find and fix errors or bugs in your program. The **GNU Debugger GDB** is a powerful tool for debugging C programs. Here are the basic steps to use GDB:  
  
### 1. Compile with Debug Information
  
This time we will be compiling our source code with the **-g** option to include debug information.  
  
```console
ak47@ak47:~$ gcc -g -o exe file.c
```
  
### 2. Start GDB 
  
Once we compiled our C program with debugging information, we can start GDB with our executable. We will see the GDB prompt **(gdb)**. From here, we can use various commands to debug our program.
  
```console
ak47@ak47:~$ gdb exe  
(gdb) 
```
  
Let's consider **file.c** has the source code below:  
  
```c
#include <stdio.h>

void func(int x) {  
    printf("x = %d\n", x);  
}  
  
int main() {  
    int a = 5;  
    printf("a = %d\n", a);    
    func(a);  
    return 0;  
}  
```
  
To insert a breakpoint we use **break** command  
  
```
(gdb) break 10
Breakpoint inserted in file.c: line 8
(gdb) break func
Breakpoint inserted in file.c: func
```
  
To run the code normally the **run** command  
  
```
(gdb) run
a = 5
Code stopped due to breakpoint at line 10
```
  
To check the value of a variable 
  
```  
(gdb) print a
5
```
   
To resume the program after stopping by a breakpoint  
   
```
(gdb) continue
Continues till end of program/Next breakpoint  
Code stopped due to breakpoint at entry point of func  
(gdb) step  
Line by line execution  
```
  
This overview for Compilation Process and Debugging tools is a solid foundation to go further in understanding another key element of our code which is **Libraries**.  
  
## Libraries  
    
### 1. Static Libraries  
  
Static libraries are collections of object files linked into the program at compile time. They become part of the final executable, making it self-contained but larger in size. To create a static library let's consider [denombrement.c](Makefile/denombrement.c) and [factorial.c](Makefile/factorial.c), we start by:    
  
* **Compiling to Object files**  
  
```console
ak47@ak47:~$ gcc -o denombrement.o -c denombrement.c  
ak47@ak47:~$ gcc -o factorial.o -c factorial.c  
```  
  
* **Create the Library Archive**  
  
```console
ak47@ak47:~$ ar rcs libmylib.a factorial.o denombrement.o  
```  
  
> **r** (replace): Insert the files into the archive, replacing any existing files with the same name.
**c** (create): Create the archive if it does not already exist.
**s** (index): Create an index for the archive, allowing for faster symbol lookup.
  
* **Link the static Library when compiling**  
  
```console
ak47@ak47:~$ gcc file.c -L. -lmylib -o exe
```  
  
> **-L** directs linker to look for the lib in specified path. **-l** indicates the lib name.  
  
### 2. Dynamic Libraries  
  
Dynamic libraries are linked at runtime, not at compile time. This allows for smaller executable sizes and the possibility to update libraries without recompiling programs that depend on them. To create a dynamic library let's consider [denombrement.c](Makefile/denombrement.c) and [factorial.c](Makefile/factorial.c), and we start by:  
  
* **Compiling to Object files**

```console
ak47@ak47:~$ gcc -fPIC -o denombrement.o -c denombrement.c
ak47@ak47:~$ gcc -fPIC -o factorial.o -c factorial.c
```  
  
> **-fPIC** is used to generate machine code that is position-independent, meaning it can be loaded at any memory address without modification, essential for for shared libs often loaded at different memory addresses.  
  
* **Create the dynamic Library**

```console
ak47@ak47:~$ gcc -shared -o libmylib.so factorial.o denombrement.o
```  
  
* **Link the dynamic Library**

```console
ak47@ak47:~$ gcc file.c -L. -lmylib -o exe           
```   
  
* **Set the Library Path**: Ensure the runtime linker can find the shared library:     
  
```console
ak47@ak47:~$ export LD_LIBRARY_PATH=.:$LD_LIBRARY_PATH
ak47@ak47:~$ sudo cp libmylib.so /usr/lib
```
  
> The first approach makes the lib ***temporarily*** reachable, once the session is closed the path is lost. Second command makes a copy of lib under the **/usr/lib** path, always visible by the runtime linker.  
  
Up to now we have managed creating executables and libraries with a limited number of files but for projects with numerous files, manually repeating compilation commands can be tedious and error-prone. To efficiently manage and automate the process of compiling and linking these files into libraries or executables, using a **Makefile** is essential.  
  
## Makefile  
  
A Makefile is a special file used by the ***make*** build automation tool to compile and link programs. It defines how to derive the target program from the source files, automating the build process.  
  
### 1. Basic Structure of a Makefile  
  
```
target: dependencies
    command
```  
  
* **Target**: typically the name of the file that you want to create/label for a group of commands  
* **Dependencies**: files that the target depends on, source files/other targets  
* **Command**: commands that are executed to build the target  
  
### 2. Makefile Example  
  
To get familiar with makefiles and their structure i have crafted makefiles to streamline the process of creating **Static** and **Dynamic** libraries from source codes under the same directory, [dynamicLibGen](Makefile/dynamicLibGen) and [staticLibGen](Makefile/staticLibGen). Let's take for example the [staticLibGen](Makefile/staticLibGen) and understand its composition:  
    
* Variable definition for defining the compiler **CC**, options **OPTIONS COMPILESTAGE NAME**.
  
```
CC:=gcc
STAT:=ar
OPTIONS:=rcs
COMPILESTAGE:=-c
NAME:=-o
```  
  
* Creating variables to store all source code files **SRCS** and the objects files **OBJ** needed ~~already existing/created~~. 
  
> In a Makefile, a **wildcard** is a feature that allows you to specify a pattern to match multiple files.
      
```
SRCS:= $(wildcard *.c)
OBJ:= $(SRCS:%.c=%.o)
```
  
* Variable containing the output desired after the execution of this makefile.
  
```
TARGET:= newlib.a
```  
  
* Specifying the targets to build in the standard target **all**. 
  
```
all: $(TARGET) clean
```  
  
* Compiling available source files into object files  
  
> **$@** is a standard reference to the target of the rule. **$<** is a standard reference to the dependency of the rule.**@echo** used to execute echo command without displaying on terminal.
   
```
%.o:%.c
	@echo "Target" $@ "Prereq" $<
	@echo "Executed Command: " $(CC) $(NAME) $@ $(COMPILESTAGE) $<
	$(CC) $(NAME) $@ $(COMPILESTAGE) $<
```  
  
* Building the target to create the static library.
  
```
$(TARGET): $(OBJ)
$(STAT) $(OPTIONS) $@ $(OBJ)
```
  
* Cleaning the directory from object files  
  
```
clean:
	rm *.o
```  
  
After going over the key-elements of Embedded-Linux and getting familiar with its tools practically, fasten your seatbelts to delve into the world of **Yocto**  
    
## Yocto Project  
  
Yocto is an open-source project designed to create custom Linux systems for embedded devices. It offers tools and resources to build efficient and optimized Linux distributions tailored to your specific needs.  
Yocto regroups a set of components, some of which are:  
  
### 1. Recipes  
  
Files that contain instructions on how to build individual software packages. Each recipe specifies the source code location, dependencies, configuration options, and build steps needed to compile and install the package.  
  
### 2. Classes  
  
Reusable components that encapsulate common build tasks and functionality. Classes can be inherited by recipes to avoid code duplication and streamline the build process.  
  
### 3. Layers  
  
A modular way to extend and customize the build system. Layers contain collections of related metadata, such as recipes, configurations, and classes. Layers can be modified to include additional features, support specific hardware, or apply custom configurations.  
   
### 4. Bitbake  
  
A build engine that is the core of the Yocto Project's build system. It interprets metadata, applies configurations, and executes tasks to produce the desired software images.
  
> We will go through some other key components and elements of Yocto project while advancing in my project analysis.  
  
## Project Study  
  
My project consists of building a Raspberry Pi 4 custom linux image with Yocto that ensures connectivity over Wi-Fi and communication with the MPU6050 sensor. All these tasks are desired to be launched automatically so we will go through the major steps to achieve these requirements.  
  
### No RaspberryPi, No Party   
  
So my Yocto workspace layout looks as shown below:  
  
```
yocto_ws -- layers
         -- builds
         -- sstate-cache
         -- downloads
```  
  
> **layers/** regroups all needed layers for my projects for different targets  
  **builds/** regroups the different images built unsder one directory  
  **downloads/** stores everything automatically downloaded by Yocto when interpreting recipes and can be shared between different builds.  
  **sstate-cache/** used by BitBake to save compilation fragments (object files, archives, etc.), can be reused later and shared among different builds 
  
First of all we need to download the reference distribution of Yocto which is Poky. Poky combines BitBake and OpenEmbedded-Core with configurations and scripts. It serves as a starting point for developing custom embedded Linux distributions.  
  
```console
ak47@ak47:~$ cd yocto_ws/layers/  
ak47@ak47:~$ git clone git://git.yoctoproject.org/poky -b kirkstone
```
First thing to do to be able to generate a compatible image with the RaspberryPi hardware specifications is to download the Rpi layer which describes them in recipes configuration files and archives:  
  
```console
ak47@ak47:~$ git clone git://git.yoctoproject.org/meta-raspberrypi -b kirkstone  
```  
  
After setting the Rpi layer we need to download a crucial set of layers present under **meta-openembedded**. This set regroups thousands of recipes necessary for any Embedded Linux application.  
  
```console
ak47@ak47:~$ git clone git://git.yoctoproject.org/meta-openembedded -b kirkstone  
```  
  
After setting the layers we need to set our building environment. The **oe-init-env** script provided by **poky** will do the trick, we just need to specify the build directory:  
  
```console
ak47@ak47~:$ source yocto_ws/layers/poky/oe-init-env yocto_ws/builds/build-rpi/
```  
  
After setting the build environment, if we check the layers used by **bitbake** during the **build** we will only find dafault layers, and the rpi layer we downloaded is missing, so we should add:  
  
```console  
ak47@ak47~:$ bitbake-layers show-layers
NOTE: Starting bitbake server... 
layer                   path
======================================================================
meta                    /home/ak47/yocto_ws/layers/poky/meta
meta-poky               /home/ak47/yocto_ws/layers/poky/meta-poky
meta-yocto-bsp          /home/ak47/yocto_ws/layers/poky/meta-yocto-bsp  

ak47@ak47~:$ bitbake-layers add-layer yocto_ws/layers/meta-rapberrypi
```  
   
Now that our build environment is "set" we can start configuring our image, and the first file we will apply changes to, is the [local.conf](build-rpi/conf/local.conf).  
> The **local.conf** file is a key configuration file where we define settings specific to our build environment and preferences such as the target machine and additional features.    
  
Let's take a look at our file and understand some of its components:  
  
* **Specifying the Target**: The Machine variable assigns the target of the build  
  
```
MACHINE = "raspberrypi4-64"
```    
  
* **Specifying the downloads and sstate-cache directories**: These folders contain essential files for the build
  
```
DL_DIR = "${TOPDIR}/../../downloads"
SSTATE_DIR = "${TOPDIR}/../../sstate-cache"
```  
  
* **Specifying the image packaging format**  
  
```
IMAGE_FSTYPES = "rpi-sdimg"
```   
  
* **Adding a Root user**
> Extrausers class is used to manage user and group creation within the built image.
  
```
INHERIT += "extrausers" 
EXTRA_USERS_PARAMS += "usermod -p '\$5\$f0r5NbGw3PeHlbq/\$qUkA2Kq72d/zCro3vj9UVtONjMjm7EL1RIaKmyO7G2B' root;"
```  
  
Now that we adapted our [local.conf](build-rpi/conf/local.conf) file, we will create our [meta-my-layer](meta-my-layer/) which will host our custom recipes.  
A key recipe in our layer is the custom [image](meta-my-layer/recipes-custom/images/my-image.bb) describing the features and packages present in our project:  
> Recipes describing a custom image should be stored under an **images/** named directory  
  
* **Adding my fav text editor and different tools for on-target development**
> The feature **packagegroup-core-buildessential** is used to install **make gcc gdb g++ etc.**  
  
```
IMAGE_INSTALL:append = " nano"  
IMAGE_INSTALL:append = " python3 packagegroup-core-buildessential"
```  
  
* **Adding the Wi-Fi and I2C modules firmwares**
> i2c-tools  is a set of tools needed to analyze the i2c bus, necessary when we will be working the mpu6050  
  mpu6050-kermod is a cross-compiled kernel module that we will talk about later    
  
```
IMAGE_INSTALL:append = " linux-firmware-bcm43455 bcm2835-dev i2c-tools mpu6050-kermod"
```
  
* **Installing my custom shell scripts**  
  
```
IMAGE_INSTALL:append = " my-scripts mpu-start"
```
  
* **For enhanced security aspects we will make our filesystem Read-Only**
  
```
IMAGE_FEATURES += "read-only-rootfs"
```
  


## References  
  

   


  

  

