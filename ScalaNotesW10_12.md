## Code Generation (Stack Machine) (12a)

[back-to-top](#scala)
Recall the compiler pipeline

- `Source Text` &rarr; `[Lexing]` &rarr; `[Parsing]` &rarr; `[Semantic Analysis]` &rarr; `[Optimization]` &rarr; **[Code Generation]**

We are at the last part!

### Instruction Selection

#### 3-address instruction (RISC) architecture

- PA, ARM

#### 2-address instruction (CISC) architecture

- Intel 86
- x86 is a family of rules (an instruction set) that defines how software tells a CPU what to do.

#### 1-address instruction

- Stack machine &rarr; JVM
