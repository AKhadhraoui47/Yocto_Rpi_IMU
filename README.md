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
  
This overview for Compilation Process and Debugging tools is a solid foundation to go further in understanding another key element of our code which is **Libraries**  
  
##Libraries
  
### 1. Static Libraries  
  
Static libraries are collections of object files linked into the program at compile time. They become part of the final executable, making it self-contained but larger in size. To create a static library let's consider **denombrement.c factorial.c** under **Makefile** folder, we start by:    
  
* **Compiling to Object files**
  

  

