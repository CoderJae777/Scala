# Scala

_Lecture 6b onwards_

---

_This markdown file documents everything learned in 50.054 Compiler Design and Program Analysis related to Scala_

- [Scala](#scala)
  - [Pseudo Assembly (6B)](#pseudo-assembly-6b)
    - [SIMP (Simple Imperative Programming language)](#simp-simple-imperative-programming-language)
    - [Pseudo Assembly Syntax](#pseudo-assembly-syntax)
    - [Maximal Munch (SIMP to PA)](#maximal-munch-simp-to-pa)
    - [Maximal Munch Example Deep Dive](#maximal-munch-example-deep-dive)
    - [Issues with Maximal Munch](#issues-with-maximal-munch)
  - [Semantic Analysis (8A)](#semantic-analysis-8a)
    - [Dynamic Semantic Analysis](#dynamic-semantic-analysis)
    - [Static Semantics Analysis](#static-semantics-analysis)
    - [Goal #1: Fault Detection](#goal-1-fault-detection)
    - [Goal #2: Optimization](#goal-2-optimization)
    - [Rice Theorem](#rice-theorem)
  - [Dynamic Semantics (8B)](#dynamic-semantics-8b)
    - [Types of Dynamic Semantics](#types-of-dynamic-semantics)
      - [Small Step (Operational Semantics)](#small-step-operational-semantics)
        - [All SIMP Small Step inference rules](#all-simp-small-step-inference-rules)
        - [Example of Small Step with SIMP](#example-of-small-step-with-simp)
      - [Big Step (Operational Semantics)](#big-step-operational-semantics)
        - [All SIMP Big-step inference rules](#all-simp-big-step-inference-rules)
  - [Code Generation (Stack Machine) (12a)](#code-generation-stack-machine-12a)
    - [Instruction Selection](#instruction-selection)
      - [3-address instruction (RISC) architecture](#3-address-instruction-risc-architecture)
      - [2-address instruction (CISC) architecture](#2-address-instruction-cisc-architecture)
      - [1-address instruction](#1-address-instruction)

---

## Pseudo Assembly (6B)

[back-to-top](#scala)

Recall the compiler pipeline

- `Source Text` &rarr; `[Lexing]` &rarr; `[Parsing]` &rarr; **`[Semantic Analysis]`** &rarr; `[Optimization]` &rarr; `[Code Generation]`

### SIMP (Simple Imperative Programming language)

[back-to-top](#scala)

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
}
return s
```

---

### Pseudo Assembly Syntax

[back-to-top](#scala)

(Instruction) i ::= _d &larr; s_ `(move)` | _d &larr; s op s_ `(binary)`| _ret_ `return` | _ifn s goto l_ `(if not s go to l)` | _goto l_ `(unconditional jump)`

(Operand) _d,s_ ::= r | c | t
(Temp Var) _t_ ::= x | y | ...
(Label) _l_ ::= 1 | 2 |...
(Operator) _op_ ::= + | - | \* |...
(Constant) _c_ ::= 0 | 1 | 2 | ... `All natural numbers`
(Register) _r_ ::= rret | r1 | r2 `rret: return register, stores the return statement`

Example

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

### Maximal Munch (SIMP to PA)

[back-to-top](#scala)

Maximal Munch &rarr; rules that tell you how to generate code

_Always grab the biggest SIMP chunk you can turn into PA in one go_

Converting SIMP to Pseudo Assembly

```mathematica

SIMP                                           Psuedo Assembly
#############                                  ################
x = input;                                     1: x <- input
s = 0;                                         2: s <- 0
c = 0;                                         3: c <- 0
while c < x {        -> Maximal Munch ->       4: t <- c < x
  s = c + s;                                   5: ifn t goto 9
  c = c + 1;                                   6: s <- c + s
}                                              7: c <- c + 1
return s;                                      8: goto 4
                                               9: rret <- s
                                               10: ret
```

How? Two mutually defined judgments:

- Statements: use `G_s(S) ⊢ lis` to turn a SIMP statement into labeled PA instructions.
- Expressions: use `G_a(X)(E) ⊢ lis` to compute an expression into destination `X`.

**Assignment rule:**

$$
\frac{G_a(X)(E) \vdash \text{lis}}{G_s(X = E) \vdash \text{lis}} \quad (\text{mAssign})
$$

**Constant rule (with fresh label $l$):**

$$
\frac{c = \operatorname{conv}(C)}{G_a(X)(C) \vdash [\, l : X \leftarrow c\, ]} \quad (\text{mConst})
$$

where

$$
\operatorname{conv}(\text{true}) = 1, \quad
\operatorname{conv}(\text{false}) = 0, \quad
\operatorname{conv}(C) = C.
$$

**Variable rule:**

$$
\frac{l \text{ fresh}}{G_a(X)(Y) \vdash [\, l : X \leftarrow Y \,]} \quad (\text{mVar})
$$

**Binary-operator rule:**

$$
\frac{\begin{array}{c}
t_1 \text{ fresh},\; G_a(t_1)(E_1) \vdash \text{lis}_1 \\
t_2 \text{ fresh},\; G_a(t_2)(E_2) \vdash \text{lis}_2 \\
l \text{ fresh}
\end{array}}{G_a(X)(E_1\,\text{OP}\,E_2) \vdash \text{lis}_1 + \text{lis}_2 + [\, l : X \leftarrow t_1\, \text{OP} \, t_2 \,]} \quad (\text{mOp})
$$

**Parentheses rule:**

$$
\frac{G_a(X)(E) \vdash \text{lis}}{G_a(X)((E)) \vdash \text{lis}} \quad (\text{mParen})
$$

**Return rule:**

$$
\frac{G_a(r_{ret})(X) \vdash \text{lis}\quad l\text{ fresh}}{G_s(\text{return } X) \vdash \text{lis} + [\, l : \text{ret}\, ]} \quad (\text{mReturn})
$$

**Sequence rule:**

$$
\frac{\forall i \in \{1,\dots,n\},\; G_s(S_i) \vdash \text{lis}_i}{G_s(S_1;\dots;S_n) \vdash \text{lis}_1 + \cdots + \text{lis}_n} \quad (\text{mSequence})
$$

**No-op rule:**

$$
G_s(\text{nop}) \vdash [\,] \quad (\text{mNOp})
$$

- `mAssign`: compile the expression with `G_a(X)(E)` so its value lands in `X`, then reuse that instruction list for the statement `X = E`.
- `mConst`: when the expression is a literal `C`, emit a fresh instruction `l: X <- conv(C)` moving the constant directly into `X`.
- `mReturn`: evaluate `X` into the special register `r_ret`, then append a fresh `ret` so returning statements terminate with that value.
- `mSequence`: translate each statement `S_i` in order and concatenate their instruction lists to preserve sequential execution.
- `mNOp`: do nothing—`nop` deliberately produces an empty instruction list.

- `mVar`: load a variable `Y` into destination `X` by emitting a single move instruction with a fresh label.
- `mOp`: evaluate both operands into temporary destinations, then emit an instruction that combines them with the operator and stores the result in `X`.
- `mParen`: parentheses don’t change code generation, so just reuse the instructions produced for the inner expression `E`.

---

### Maximal Munch Example Deep Dive

[back-to-top](#scala)

Converting SIMP to Pseudo Assembly

```mathematica

SIMP                                           Psuedo Assembly
#############                                  ################
x = input;                                     1: x <- input
s = 0;                                         2: s <- 0
c = 0;                                         3: c <- 0
while c < x {        -> Maximal Munch ->       4: t <- c < x
  s = c + s;                                   5: ifn t goto 9
  c = c + 1;                                   6: s <- c + s
}                                              7: c <- c + 1
return s;                                      8: goto 4
                                               9: rret <- s
                                               10: ret
```

Mapping SIMP → PA with rule invocations:

| SIMP fragment                  | PA line(s)        | Rule invocation                                                       | Key details                                                                                         |
| ------------------------------ | ----------------- | --------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------- |
| `x = input;`                   | `1: x <- input`   | `G_s(x = input)` uses `mAssign`; expression side runs `G_a(x)(input)` | `input` expression already matches a PA move; result stored directly in destination `x`.            |
| `s = 0;`                       | `2: s <- 0`       | `G_a(s)(0)` hits `mConst` inside `mAssign`                            | `conv(0) = 0`, so the constant rule emits a fresh labeled instruction moving literal 0 into `s`.    |
| `c = 0;`                       | `3: c <- 0`       | `G_a(c)(0)` with `mConst`                                             | Same `conv(0) = 0` behavior; fresh label for `c`.                                                   |
| `while c < x { … }` guard eval | `4: t <- c < x`   | `G_s(while …)` emits guard using `G_a(t)(c < x)` with `mOp` + `mVar`  | Guard label `4` is “fresh” so the loop can jump back; `t` stores comparison result.                 |
| `while` guard branch           | `5: ifn t goto 9` | `G_s(while …)` loop rule                                              | Conditional branch exits loop when guard fails; target label `9` is preallocated for the loop exit. |
| `s = c + s;` (body)            | `6: s <- c + s`   | `G_a(s)(c + s)` via `mAssign` + `mOp` + `mVar`                        | Binary-op rule evaluates operands, then emits instruction storing `c + s` back to `s`.              |
| `c = c + 1;` (body)            | `7: c <- c + 1`   | `G_a(c)(c + 1)` using `mOp` + `mConst`                                | Uses fresh temps plus `conv(1)=1` to increment `c`; still part of loop body sequence.               |
| loop back-edge                 | `8: goto 4`       | `G_s(while …)` loop tail                                              | Implements the back-edge: unconditional jump to guard label `4` to re-test the loop condition.      |
| `return s;` (move)             | `9: rret <- s`    | `G_s(return s)` invokes `mReturn` with `G_a(r_ret)(s)` (`mVar`)       | Return rule first moves `s` into `r_ret`; this is analogous to `G_a(r_ret)(s)` using `mVar`.        |
| `return s;` (ret)              | `10: ret`         | `mReturn`                                                             | Final `ret` instruction (with fresh label) completes the return statement.                          |

1. `x = input`

- Treat as an assignment statement
- Applies `(mAssign)`: call `G_a(x)(input)` to emit code that computes `input` into dest `x`
- _When G_a(X)(E) finishes, the computed value of expression E must be stored in X. So X is simply the place you’ve chosen to hold the result_
- The expression `input` already corresponds to a PA instruction, so the result is `1: x <- input`

<br>

2. `s = 0`

- Applies `(mAssign)`: call `G_a(s)(0)`
- Constant `0` hits `(mConst)` with fresh label `2` and `conv(0) = 0`
- _conv(0) = 0 means if the SIMP constant is 0, the instruction should write 0 into the destination._
- Emit `2: s <- 0`

<br>

3. `c = 0`

- Applies `(mAssign)`: call `G_a(c)(0)`
- Constant `0` hits `(mConst)` with fresh label `3` and `conv(0) = 0`
- Emit `3: s <- 0`

<br>

4. `while c < x { body }`

- `G_s` sees a loop, so it emits a label for the guard
- _When compiling a while loop, the guard (the Boolean test) needs a jump target so execution can return to it after each iteration. “Emit a label for the guard” means: insert a numbered label on the PA line where the guard expression is evaluated (e.g., 4:). Later, after the loop body, the generated code includes goto 4, sending control back to that labeled guard line to re-test the condition._
- Guard: `4: t <- c < x` &rarr; the thing that terminates the while loop basically
- Conditional jump: `5: ifn t goto 9` (exit label)

<br>

5. Loop body statement `s = c + s`

- `(mAssign)` with expression `c + s`
- `G_a(s)(c + s)` emits `6: s <- c + s`

6. Next body statement `c = c + 1`

- `(mAssign)` with expression `c + 1`
- `G_a(c)(c + 1)` emits `7: c <- c + 1`

7. Loop Tail

- After the body, `G_s` emits a goto back to the guard
- emits `8: goto 4`

8. After loop, `return s`

- Return becomes move in `rret` plus `ret`
- emits `9: rret < - s` and `10: ret`

---

### Issues with Maximal Munch

[back-to-top](#scala)

- too many temp variables

Example

```
z = x - (y + 1) --> (apply mAssign) --> G_a(z)(x - (y + 1))

3: t1 <- x,
4: t3 <- y,
5: t4 <- 1,
6: t2 <- t3 + t4 (y+1)
7: z  <- t1 - t2 (x- (y+1))
```

There is too many temp var like t1, t2, t3, t4

Optimised Encoding

```
3: t1 <- y + 1
4: t2 <- x - t1
5: z  <- t2
```

---

## Semantic Analysis (8A)

- `Source Text` &rarr; `[Lexing]` &rarr; `[Parsing]` &rarr; **`[Semantic Analysis]`** &rarr; `[Optimization]` &rarr; `[Code Generation]`

[back-to-top](#scala)

- Syntax Analysis &rarr; verify given code conforms to grammar rules
- Semantic Analysis &rarr; verify code behave according to human expectation &rarr; how does the program get executed, what does the program compute/return?
  - Goal #1: Optimization
  - Goal #2: Fault Detection

---

### Dynamic Semantic Analysis

[back-to-top](#scala)

The program's behavior when it _runs_. How does it _execute_? What does it _compute/return_?
Approaches:

1. Testing
2. Run-time verification &rarr; finding bugs by log
3. Fuzzing

---

### Static Semantics Analysis

[back-to-top](#scala)

What the program MUST satisfy BEFORE excution

Approaches:

1. Type rules checking
2. Name rules analysis
3. Flow rules

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

### Goal #1: Fault Detection

Divide by 0 math error

[back-to-top](#scala)

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

### Goal #2: Optimization

[back-to-top](#scala)

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

[back-to-top](#scala)
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

## Dynamic Semantics (8B)

[back-to-top](#scala)

A formal way to specify a language's run-time behavior: how programs execute and what they return

### Types of Dynamic Semantics

1. **Operational Semantics**

- Define execution as **term rewriting** (rules that transform program states)
- Small-step &rarr; one micro step at a time until you reach a normal form (or continue forever)
- Big-step &rarr; jump directly to results

2. **Denotational Semantics**

- Map programs to mathematical objects (meanings)

3. **Translational Semantics**

- Translate one language to another

---

#### Small Step (Operational Semantics)

[back-to-top](#scala)

Good for debugger mode: shows every intermediate configurations. It’s for giving an unambiguous “official recipe” for how a program runs, one tiny step at a time

A small-step semantics is a relation (often written as: &rarr;) defined by **inference rules**

Inference rules defines the small-steps:

$$\frac{\text{premises}}{\text{conclusion}}$$

Meaning: if the `premises` is true, you may apply the rule to conclude the bottom `conclusion` (one step done)

##### All SIMP Small Step inference rules:

[back-to-top](#scala)

$$\text{\textbf{(sVar)}}{\Delta \vdash X \longrightarrow \Delta(X)}$$

_Meaning: look up variable `X` in the store and replace it with its value._

<br>

$$\text{\textbf{(sOp1)}}\quad \frac{\Delta \vdash E_1 \longrightarrow E_1'}{\Delta \vdash E_1\;\mathrm{OP}\;E_2 \longrightarrow E_1'\;\mathrm{OP}\;E_2}$$

_Meaning: take one evaluation step in the left operand of a binary operation._

<br>

$$\text{\textbf{(sOp2)}}\quad \frac{\Delta \vdash E_2 \longrightarrow E_2'}{\Delta \vdash C_1\;\mathrm{OP}\;E_2 \longrightarrow C_1\;\mathrm{OP}\;E_2'}$$

_Meaning: when the left operand is already a constant, evaluate the right operand next._

<br>

$$\text{\textbf{(sOp3)}}\quad \frac{C_3 = C_1\;\mathrm{OP}\;C_2}{\Delta \vdash C_1\;\mathrm{OP}\;C_2 \longrightarrow C_3}$$

_Meaning: if both operands are constants, compute the primitive result immediately._

<br>

$$\text{\textbf{(sParen1)}}\quad \frac{\Delta \vdash E \longrightarrow E'}{\Delta \vdash (E) \longrightarrow (E')}$$

_Meaning: evaluate inside parentheses (propagate a step into the grouped expression)._

<br>

$$\text{\textbf{(sParen2)}}\quad {\Delta \vdash (c) \longrightarrow c}$$

_Meaning: parentheses around a constant disappear — the constant is the result._

<br>

$$\text{\textbf{(sAssign1)}}\quad \frac{\Delta \vdash E \longrightarrow E'}{(\Delta,\; X = E\;;) \longrightarrow (\Delta,\; X = E'\;;)}$$

_Meaning: evaluate the right-hand side expression before performing the assignment._

<br>

$$\text{\textbf{(sAssign2)}}\quad \frac{\Delta' = \Delta \oplus (X,C)}{\quad(\Delta,\; X = C\;;) \longrightarrow (\Delta',\; \text{nop})}$$

_Meaning: when RHS is a constant, update the store with `X = C` and replace the statement with `nop`._

<br>

$$\text{\textbf{(sIf1)}}\quad \frac{\Delta \vdash E \longrightarrow E'}{(\Delta,\; \text{if } E \{S_1\} \text{ else } \{S_2\}) \longrightarrow (\Delta,\; \text{if } E' \{S_1\} \text{ else } \{S_2\})}$$

_Meaning: evaluate the `if` condition expression before choosing a branch._

<br>

$$\text{\textbf{(sIf2)}}{(\Delta,\; \text{if true } \{S_1\} \text{ else } \{S_2\}) \longrightarrow (\Delta,\; S_1)}$$

_Meaning: if the condition is `true`, take the then-branch `S_1`._

<br>

$$\text{\textbf{(sIf3)}}{(\Delta,\; \text{if false } \{S_1\} \text{ else } \{S_2\}) \longrightarrow (\Delta,\; S_2)}$$

_Meaning: if the condition is `false`, take the else-branch `S_2`._

<br>

$$
	{\textbf{(sWhile)}}\bigl(\Delta,\;\text{while } E\;\{S\} \longrightarrow (\Delta,\;\text{if } E\;\{\,S;\;\text{while } E\;\{S\}\,\}\;\text{else}\;\{\text{nop}\})\bigr)
$$

_Meaning: unroll the `while` loop as an equivalent `if` that repeats the body when the guard holds._

<br>

$$\text{\textbf{(sWhile1)}}\quad \frac{\Delta \vdash E \longrightarrow E'}{(\Delta,\; \text{while } E \{S\}) \longrightarrow (\Delta,\; \text{while } E' \{S\})}$$

_Meaning: take a step to evaluate the loop condition expression._

<br>

$$\text{\textbf{(sWhile2)}}{(\Delta,\; \text{while true } \{S\}) \longrightarrow (\Delta,\; S\; ;\; \text{while true } \{S\})}$$

_Meaning: if the guard is `true`, execute the body `S` then loop again._

<br>

$$\text{\textbf{(sWhile3)}}{(\Delta,\; \text{while false } \{S\}) \longrightarrow (\Delta,\; \text{nop})}$$

_Meaning: if the guard is `false`, exit the loop (do nothing)._

<br>

$$\text{\textbf{(sNopSeq)}}(\Delta,\; \text{nop} ; \bar{S}) \longrightarrow (\Delta,\; \bar{S})$$

_Meaning: drop a leading `nop` and continue with the remaining sequence._

<br>

$$\text{\textbf{(sSeq)}}\quad \frac{S \neq \text{nop} \quad (\Delta,\; S) \longrightarrow (\Delta',\; S')}{(\Delta,\; S ; SS) \longrightarrow (\Delta',\; S' ; SS)}$$

_Meaning: evaluate the left statement `S` first; when it steps to `S'` keep the sequence order._

</div>

---

**Legend (symbols & notation)**
[back-to-top](#scala)

| Symbol                                      | Meaning                                                                                |
| ------------------------------------------- | -------------------------------------------------------------------------------------- |
| Δ                                           | Store / environment mapping variables to constants (memory)                            |
| ⊢ (turnstile)                               | Judgment turnstile; used to relate a store and an expression/statement to a transition |
| →                                           | One small-step transition (single computation step)                                    |
| →\*                                         | Multi-step (reflexive-transitive) closure of `→` (zero or more steps)                  |
| overline / axiom (e.g. `\overline{(... )}`) | A rule with no premises (an axiom) — shows the conclusion directly                     |
| `nop`                                       | No-op statement (does nothing)                                                         |
| (Δ, S)                                      | Configuration: store Δ together with statement `S` (program state)                     |
| C                                           | Constant literal (e.g., 0, true, false)                                                |
| X                                           | Variable name                                                                          |
| E, E'                                       | Expressions (possibly reducible)                                                       |

Notes:

- When a rule uses a condition like `C_3 = C_1 OP C_2`, it means the result of applying `OP` to the constants `C_1` and `C_2` produces `C_3` (a primitive computation).
- Sequencing `S ; SS` means `S` followed by the rest `SS` (the semantics reduce `S` until `nop`, then continue with `SS`).

---

##### Example of Small Step with SIMP

[back-to-top](#scala)

Goal: with small-step we take the SIMP program and turn it into a chain like:

(Δ₀, S₀) → (Δ₁, S₁) → (Δ₂, S₂) → …

where each arrow is one rule application (one legal next move)

Now given this example below, we want to formally justify the execution story:

```c
x = input;
x = x + 1;
return x;
```

- when the `return` statement carries a constant value we consider the program terminated with value $v+1$ (sometimes modeled by a special halt configuration or by setting a return register).
- Final store: $\Delta_2$ with $x \mapsto v+1$.
- Program result: value $v+1$.

Compact chain (configuration sequence):

```text
(Δ₀, x = input; x = x + 1; return x)
→ (Δ₀, x = v; x = x + 1; return x)         (sAssign1) — evaluate RHS `input` to value `v`
→ (Δ₁, nop; x = x + 1; return x)           (sAssign2) — update store `x ↦ v` and replace assignment with `nop`
→ (Δ₁, x = x + 1; return x)                (sNopSeq)  — drop leading `nop` and continue
→ (Δ₁, x = v+1; return x)                  (sAssign1) — evaluate RHS `x + 1` to constant `v+1` (via sVar, sOp1, sOp3)
→ (Δ₂, nop; return x)                      (sAssign2) — update store `x ↦ v+1` and replace assignment with `nop`
→ (Δ₂, return x)                           (sNopSeq)  — drop leading `nop` and continue
→ halt with result v+1                     (return evaluation / halt) — program terminates with value `v+1`
```

---

#### Big Step (Operational Semantics)

[back-to-top](#scala)

---

##### All SIMP Big-step inference rules:

**Legend (Big-step symbols & notation)**

| Symbol                              | Meaning                                                                                       |
| ----------------------------------- | --------------------------------------------------------------------------------------------- |
| Δ                                    | Store / environment mapping variables to constants (memory)                                   |
| ⊢ (turnstile)                        | Judgment turnstile; relates a store and an expression/statement to an evaluation relation     |
| ⇓                                    | Big-step evaluation relation: evaluates an expression/statement to a result in one big step   |
| `(Δ, S) ⇓ (Δ', r)`                   | Configuration evaluation: statement `S` in store `Δ` evaluates to new store `Δ'` and result `r`|
| `no-return`                          | Marker meaning the statement completed normally with no return value                          |
| `r`                                   | Result of evaluation — either a constant value or a `return` value                            |
| C                                    | Constant literal (e.g., `0`, `true`, `false`)                                                   |
| X                                    | Variable name                                                                                  |

Notes:

- `no-return` indicates normal completion (no `return` was executed). When a rule yields a return value, `r` holds that value.
- Big-step rules summarize whole computations in a single inference; they are complementary to the small-step `→` relation.

$$\text{\textbf{(bConst)}}\qquad \Delta \vdash C \Downarrow C$$

_Meaning: a literal constant evaluates to itself._

$$\text{\textbf{(bVar)}}\qquad \Delta \vdash X \Downarrow \Delta(X)$$

_Meaning: look up a variable `X` in the store and return its value._

$$\text{\textbf{(bOp)}}\quad \frac{\Delta \vdash E_1 \Downarrow C_1\quad \Delta \vdash E_2 \Downarrow C_2\quad C_3 = C_1\;\mathrm{OP}\;C_2}{\Delta \vdash E_1\;\mathrm{OP}\;E_2 \Downarrow C_3}$$

_Meaning: evaluate both operands to constants then compute the primitive operator result._

$$\text{\textbf{(bParen)}}\quad \frac{\Delta \vdash E \Downarrow C}{\Delta \vdash (E) \Downarrow C}$$

_Meaning: parentheses do not change the evaluated result (evaluate inner expression)._

$$\text{\textbf{(bAssign)}}\quad \frac{\Delta \vdash E \Downarrow C}{(\Delta,\; X = E) \Downarrow (\Delta \oplus (X\mapsto C),\; \text{no-return})}$$

_Meaning: evaluate the RHS to a constant, update the store with `X = C`, assignment completes in one big step._

$$\text{\textbf{(bIf1)}}\quad \frac{\Delta \vdash E \Downarrow \text{true}\quad (\Delta, S_1) \Downarrow (\Delta', r)}{(\Delta,\; \text{if } E \{S_1\} \text{ else } \{S_2\}) \Downarrow (\Delta', r)}$$

_Meaning: if the guard evaluates to true, evaluate the `then` branch to completion._

$$\text{\textbf{(bIf2)}}\quad \frac{\Delta \vdash E \Downarrow \text{false}\quad (\Delta, S_2) \Downarrow (\Delta', r)}{(\Delta,\; \text{if } E \{S_1\} \text{ else } \{S_2\}) \Downarrow (\Delta', r)}$$

_Meaning: if the guard evaluates to false, evaluate the `else` branch to completion._

$$\text{\textbf{(bWhile1)}}\quad \frac{\Delta \vdash E \Downarrow \text{true}\quad (\Delta, S) \Downarrow (\Delta_1,\; \text{no-return})\quad (\Delta_1,\; \text{while } E \{S\}) \Downarrow (\Delta_2, r)}{(\Delta,\; \text{while } E \{S\}) \Downarrow (\Delta_2, r)}$$

_Meaning: when the guard is true, execute the body then repeat the loop (big-step unrolling)._

$$\text{\textbf{(bWhile2)}}\quad \frac{\Delta \vdash E \Downarrow \text{false}}{(\Delta,\; \text{while } E \{S\}) \Downarrow (\Delta,\; \text{no-return})}$$

_Meaning: when the guard is false the loop terminates immediately (no effect on the store)._

$$\text{\textbf{(bNop)}}\quad \frac{}{(\Delta,\; \text{nop}) \Downarrow (\Delta,\; \text{no-return})}$$

_Meaning: `nop` does nothing and leaves the store unchanged._

$$\text{\textbf{(bSeq)}}\quad \frac{(\Delta, S) \Downarrow (\Delta',\; \text{no-return})\quad (\Delta', S_s) \Downarrow (\Delta'', r)}{(\Delta, S; S_s) \Downarrow (\Delta'', r)}$$

_Meaning: execute the left statement to completion (no return), then execute the right; the final result is the right-hand result._

---

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
