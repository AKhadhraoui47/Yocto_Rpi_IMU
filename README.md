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
  
A Makefile is a special file used by the make build automation tool to compile and link programs. It defines how to derive the target program from the source files, automating the build process.  
  
### 1. Basic Structure of a Makefile  
  
```
target: dependencies
    command
```  
  
* **Target**: typically the name of the file that you want to create/label for a group of commands  
* **Dependencies**: files that the target depends on, source files/other targets  
* **Command**: commands that are executed to build the target  



## References  
  

   


  

  

