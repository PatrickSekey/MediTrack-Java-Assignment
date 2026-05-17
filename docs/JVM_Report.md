# JVM Architecture Report

## Class Loader
- Loading, Linking (Verification, Preparation, Resolution), Initialization
- Types: Bootstrap, Extension, Application ClassLoaders
- Delegation Hierarchy Model

## Runtime Data Areas
1. **Heap**: All objects, arrays, shared across threads, Garbage Collection
2. **Stack**: Method invocation frames, local variables, operand stack
3. **Method Area**: Class metadata, static variables, constants
4. **PC Register**: Current executing instruction address
5. **Native Method Stack**: Native method execution

## Execution Engine
- **Interpreter**: Executes bytecode line by line (slower startup)
- **JIT Compiler**: Compiles hot methods to native code (optimized)
- **Garbage Collector**: Automatic memory management

## "Write Once, Run Anywhere"
- Java bytecode vs native code
- JVM as abstraction layer
- Platform independence achieved through JVM implementations