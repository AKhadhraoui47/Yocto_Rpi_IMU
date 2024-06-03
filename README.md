# Yocto_Rpi_IMU
This repository chronicles my journey of learning Embedded Linux and Yocto from the ground up. 
It concludes with the development of a custom-built image for a Raspberry Pi, designed to read and log data from an MPU6050 sensor.

Exploring **Yocto** and the basics of **Embedded Linux** from scratch can seem to be a heavy task to handle, therefore to have a steady progress, it is preferable to break it down into manageable topics.

## The Compilation Process

The compilation process of C code involves several steps, each transforming the source code into executable machine code. Here are the key steps:

### 1.Preprocessing

The preprocessor handles directives (lines starting with #) in the source code. It performs tasks such as:  
     - Including header files ( #include )  
     - Macro substitution ( #define )  
     - Conditional compilation ( #ifdef, #ifndef, etc.)  
