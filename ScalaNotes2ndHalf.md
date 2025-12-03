# Scala

_Lecture 6b onwards_

---

_This markdown file documents everything learned in 50.054 Compiler Design and Program Analysis related to Scala_

- [Pseudo Assembly](#pseudo-assembly)
  - [SIMP](#simp-simple-imperative-programming-language)

---

## Pseudo Assembly

Recall the compiler pipeline

- `Source Text` &rarr; `[Lexing]` &rarr; `[Parsing]` &rarr; `[Semantic Analysis]` &rarr; `[Optimization]` &rarr; `[Code Generation]`

We are at **Semantic Analysis** now

### SIMP (Simple Imperative Programming language)

- Tiny imperative language with assignments

Syntax

```mathematica
Statements (S):
  S ::= X = E ;
     | return X ;
     | nop ;
     | if E { S } else { S }
     | while E { S }
     | S S          // sequencing (two statements in a row)

Expressions (E):
  E ::= E OP E
     | X
     | C
     | ( E )

Operators (OP):
  OP ::= + | − | * | < | ==

Constants (C):
  C ::= 0 | 1 | 2 | ... | true | false

Variables (X):
  X ::= a | b | c | d | ...

```

Example

```mathematica
x = input;
s = 0;
c = 0;
while c < x {
s = c + s;
c = c + 1;
}
return s;

// When input = 10, the above should return 45
// 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9
```

### Maximal Munch &rarr; SIMP to PA

Converting to Pseudo Assembly &rarr; 3-address code with labeled instructions

How? Two mutually defined judgments:

`Gs(S) ⊢ lis` &rarr; compile a statement S into a list of labeled PA instructions lis.

`Ga(X)(E) ⊢ lis` &rarr; compile an expression E so that its value ends up in destination X (a temp or variable).

```mathematica
1: x <- input
2: s <- 0
3: c <- 0
4: t <- c < x
5: ifn t goto 9
6: s <- c + s
7: c <- c + 1
8: goto 4
9: rret <- s
10: ret
```

---

## Semantic Analysis (8a)

- Syntax Analysis &rarr; verify given code conforms to grammar rules
- Semantic Analysis &rarr; verify code behave according to human expectation &rarr; how does the program get executed, what does the program compute/return?
  - Goal #1: Optimization
  - Goal #2: Fault Detection

### Dynamic Semantic Analysis

1. Testing
2. Run-time verification &rarr; finding bugs by log
3. Fuzzing

---

### Static Semantics Analysis

- **Type checking**

```java
b = true;
c = b + 1
// Static error: Type mismatch
```

- **Name analysis** &rarr; is it a function?

```java
y = x + 1;
return y;
// Static error: x is undefined
```

- **Control flow analysis** &rarr;“which statements can execute?”, “where are loops/back-edges?”, and “is code unreachable?”.
- **Data flow analysis** &rarr; how data flow
- **Model checking** &rarr; step by step input-output checking
- **Abstract intepretation** &rarr; Approximate program behavior using simpler values to prove properties.
- **Symbolic Execution**

---

### Fault Detection

```java
x = input;
while (x>=0){
    x = x - 1
}
y = Math.sqrt(x)
return y;

// Will raise error because if x = 0, you will be square rooting -1
```

```mathematica
1: x <- input
2: t1 <- 0 < x
3: t2 <- 0 == x
4: t3 <- t1 + t2 // t1 or t2
5: ifn t3 goto 8
6: x = x - 1
7: goto 2
8: y <- Math.sqrt(x) // x is definitely negative
9: rret <- y
10: ret
```

### Optimizationgit

```java
x = input;
y = 0;
s = 0;
while (y < x) {
    y = y + 1;
    t = s; // t is not used.
    s = s + y;
}
return s;
```

```mathematica
1: x <- input
2: y <- 0
3: s <- 0
4: b <- y < x
5: ifn b goto 10
6: y <- y + 1
7: t <- s // t is not used
8: s <- s + y
9: goto 4
10: rret <- s
11: ret

```

---

### Rice Theorem

_All non-trivial semantic properties of programs are undecidable, i.e. there exists no algorithm that can decide all semantic properties fro all given programs_

Example: There exists **NO** algorithm in the world that can determine if the x is 1 or -1 without running it

&rarr; the Halting Problem

```python
def f(path):
    p = open(path, "r")
    x = 1
    if eval(p):
        x = -1
    return x
```

- `eval()` &rarr; evaluates a python expression and returns its value

```python
eval("2 + 3")
#5

f = eval("lambda x: x*x")
f(3)
# 9
```

---

## Code Generation (Stack Machine) (12a)

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