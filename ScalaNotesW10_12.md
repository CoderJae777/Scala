- [Name Analysis (10a)](#name-analysis-10a)
  - [Variable Scope](#variable-scope)
    - [Dynamic Scope](#dynamic-scope)
    - [Static (lexical) Scope](#static-lexical-scope)
  - [SIMP](#simp)
  - [How compilers typically implement name analysis (high-level)](#how-compilers-typically-implement-name-analysis-high-level)
- [Static Single Assignment (10b)](#static-single-assignment-10b)
  - [ϕ (phi) line](#ϕ-phi-line)
    - [General Form](#general-form)
    - [Simple Example](#simple-example)
  - [Constructing Minimal SSA (Cytron)](#constructing-minimal-ssa-cytron)
    - [Cytron’s Algorithm](#cytrons-algorithm)
  - [Constructing a SSA Example](#constructing-a-ssa-example)
    - [Step 1: Build the CFG](#step-1-build-the-cfg)
    - [Step 2: Compute Denominators + idom (needed for Dominance Frontier)](#step-2-compute-denominators--idom-needed-for-dominance-frontier)
    - [Step 3: Compute Dominance Frontier (DF)](#step-3-compute-dominance-frontier-df)
    - [Step 4: Phi (ϕ) Insertion](#step-4-phi-ϕ-insertion)
    - [Step 5: Rename (make each assignment a new version)](#step-5-rename-make-each-assignment-a-new-version)
    - [Final SSA Form](#final-ssa-form)
- [Sign Analysis and Lattice Theory (11a)](#sign-analysis-and-lattice-theory-11a)
  - [Abstract Domain (Sign Lattice)](#abstract-domain-sign-lattice)
- [Code Generation (Stack Machine) (12a)](#code-generation-stack-machine-12a)
  - [Instruction Selection](#instruction-selection)

## Name Analysis (10a)

Given a program, we have many identifiers, and we want to check:

1. Is it a variable name or a function name?
2. Is the variable name of type int or bool?
3. What is the scope of the variable?
4. Has the variable been declared before used?
5. Where is the defined variable used?

_A simple analogy_

Think of your program like a big building, and every variable name is a person’s name.

If someone shouts “Alex!”:

Name analysis figures out **_which Alex_** they meant (nearest relevant one), whether that **Alex exists in this building** section (scope) and whether Alex is **even allowed to respond here** (visibility rules).

---

### Variable Scope

The core idea behind name resolution

A variable's scope is the region of code where that variable name refers to a particular declaration

#### Dynamic Scope

Scope follows the _call stack_ &rarr; when you reference `x`, the language looks for `x` by walking the run-time call chain (who called who)

```python
def f(x) :
  return g(x)
def g(x):
  print(x)

x = 1
f(x)  # 1 is printed
```

Why is 1 printed?

- At runtime, `g` is called inside `f`
- `f` has local `x = 1`
- so `g` sees that `x` through the call chain
- _“Meaning of x depends on the path of calls that got you here.”_

This makes programs harder to reason about, because “what x means” can change depending on runtime behavior.

```mermaid
graph TD
    Main --> x1[x=-1]
    Main --> f
    f --> x2[x=1]
    f --> g
    g --> print["print(x)"]
```

---

#### Static (lexical) Scope

In static scoping, `x` is resolved based on where `x` is written/declared in the source code, not who called who.

```python

def f():
    x = 1
    return g()
def g():
    print(x)

x = -1
f() # -1 is printed
```

Why is -1 printed? (and not 1)

- `g` is defined at top level
- so `x` inside `g` refers to the top-level `x`, not `f`'s local `x`
- `f`'s `x` has nothing on `g`
- _"Meaning of x depends on the nesting struction of the code"_

```mermaid
graph TD
    Main --> x1[x=-1]
    Main --> f
    Main --> g
    f --> x2[x=1]
    g --> print["print(x)"]
```

---

### SIMP

```mathematica
// SIMP_ERR1
x = 1;
if input == 0 {
  x = 2;
} else {
  y = 1;
}
return y; // y could be undefined.

```

Potential error

- y is only assigned in the else branch.
- If input == 0 is true, we never assign y, but we still return y.
- Only works if input != 0

We don't want to leave things to chance, so we should detect error statically

---

### How compilers typically implement name analysis (high-level)

**Step A**: Build an environment (symbol table)

- As you traverse the program:
- When you see a declaration, add it to the current scope map: x ↦ (kind=var, type=int, declared_at=...)
- When you enter a new scope (block/function), push a new scope frame.
- When you exit, pop it.

**Step B**: Resolve every use

- When you see x used:
- look up x from the innermost scope outward
- if not found: error (used before declared)
- if found: link this use to that definition (this is the “where is the defined variable used?” part)

**Step C**: (Often added) definite-assignment check

- To catch return y safely, you do a lightweight dataflow idea:
- After an if/else, a variable is “definitely assigned” only if it’s assigned on all paths.
- Here, y is assigned only on one path → flag error.

---

## Static Single Assignment (10b)

SSA is an intermediate representation where

- Every variable is assigned exactly once
- If you reassign a variable, you instead create a new version (renaming/alpha renaming)
- When multiple control-flow paths re-define the same original variable, SSA uses a ϕ (phi) assignment to merge those versions at join points

**Core intuition**

SSA turns “variable = a box you keep overwriting” into “variable versions = immutable snapshots” (very FP-like once renaming is included).

---

Example &rarr; Original PA (reassigns `s` and `c`)

```mathematica
// PA1                     // SSA_PA1
1: x <- input              1: x0 <- input
2: s <- 0                  2: s0 <- 0
3: c <- 0                  3: c0 <- 0
4: t <- c < x              4: s1 <- phi(3:s0, 8:s2)
5: ifn t goto 9               c1 <- phi(3:c0, 8:c2)
6: s <- c + s                 t0 <- c1 < x0
7: c <- c + 1              5: ifn t0 goto 9
8: goto 4                  6: s2 <- c1 + s1
9: rret <- s               7: c2 <- c1 + 1
10: ret                    8: goto 4
                           9: rret <- s1
                           10: ret
```

Basically instead of `s` and `c` being overwritten over and over again, we have `s1`, `s2`, ... `s8` and `c1`, `c2`, ... `c8`

For what? So that:

- each use has exactly one definition
- the IR basically is the def-use chain
- Simpler and/or more precise analysis

From the example, return s, which s we talking about? depend on the run, but for the ssa example, we can return s1 directly, or s2 if we want or maybe s6 etc etc

---

### ϕ (phi) line

A ϕ (phi) line is SSA’s way of saying:

“At this control-flow join point, the value of this variable depends on which predecessor block we came from.”

It’s not a normal function call. It’s a _merge_

It picks the right incoming version based on which edge you took to enter the block.

#### General Form

```php
v = phi(P1: a1, P2: a2, ..., Pk: ak)
```

- If execution enters the current block from predecessor P1, then v = a1
- If it enters from P2, then v = a2
- …and so on.

#### Simple Example

```c
if cond:
   x = 2
else:
   x = 3
return x
```

SSA:

- then branch defines `x1 = 2`
- else branch defines `x2 = 3`
- join block:

```kotlin
x3 = phi(then:x1, else: x2)
return x3
```

Meaning:

- if we came from the _then_ block, `x3` becomes `x1`
- if we came from the _else_ block, `x3` becomes `x2`
  So after the join, you can just use `x3` as “the value of `x`”.

---

### Constructing Minimal SSA (Cytron)

#### Cytron’s Algorithm

1. Convert the program to a **\*CFG** (Control Flow Graph).
2. Compute **Dominance Frontiers (DF)** from the CFG.
3. Insert **phi assignments** at DF nodes for variables defined at block `v`.
4. **Rename variables** to ensure each SSA variable is assigned exactly once.

**\*CFG** (Control Flow Graph) is a graph that shows all the possible ways execution can flow through a program.

- **Nodes** = basic blocks (straight-line chunks of code with no jumps in the middle)

- **Edges** = possible jumps/flow from one block to another (like if, while, goto, fall-through)

Example CFG

```c
x = 1
if c:
    x = 2
print(x)
```

Conceptually:
B1: `x=1; if (c) goto B2 else goto B3`
B2: `x=2; goto B3`
B3: `return x`

Edges:
B1 &rarr; B2 (if true)
B1 &rarr; B3 (if false)
B2 &rarr; B3

```mermaid
flowchart TD
  B1["B1: x = 1<br/>if c goto B2 else goto B3"] -->|c = True| B2["B2: x = 2<br/>goto B3"]
  B1 -->|c = False| B3["B3: print(x)"]
  B2 --> B3
```

---

### Constructing a SSA Example

Source Program

```python
x = 0
if c:
    x = 1
else:
    x = 2
z = x + 3
return z
```

#### Step 1: Build the CFG

1. B0 (entry): `x = 0; if c goto B1 else goto B2`
2. B1 (then): `x = 1; goto B3`
3. B2 (else): `x = 2, goto B3`
4. B3 (join): `z = x + 3; return z`

```mermaid
flowchart TD
  B0["B0: x=0<br/>if c goto B1 else B2"] -->|c=True| B1["B1: x=1<br/>goto B3"]
  B0 -->|c=False| B2["B2: x=2<br/>goto B3"]
  B1 --> B3["B3: z = x + 3<br/>return z"]
  B2 --> B3
```

Phi can only happen at joins (needs ≥2 incoming edges).

---

#### Step 2: Compute Denominators + idom (needed for Dominance Frontier)

A block D dominates N if every path from the entry to N must pass through D.
_So dominators tell you what blocks are “always before” other blocks, no matter how the program branches._

B0 dominates B1, B2, B3 because you can’t reach any of them without going through B0.

B1 does not dominate B3 because you can reach B3 via B2 instead.

So the idom (the immediate dominator, i.e. the closes dominator of N) are:

- `dom(B0) = {B0}`
- `dom(B1) = {B0, B1}`
- `dom(B2) = {B0, B2}`
- `dom(B3) = {B0, B3}`

Immediate dominators (closest dominator):
`idom(B1)=B0, idom(B2)=B0, idom(B3)=B0`

---

#### Step 3: Compute Dominance Frontier (DF)

DF(X) = “join points where paths influenced by X meet paths not strictly controlled by X”.

Local rule (easy cheat):
For each edge `b -> s`, if `idom(s) != b, then s ∈ DF(b)`.

```mermaid
flowchart TD
  B0["B0: x=0<br/>if c goto B1 else B2"] -->|c=True| B1["B1: x=1<br/>goto B3"]
  B0 -->|c=False| B2["B2: x=2<br/>goto B3"]
  B1 --> B3["B3: z = x + 3<br/>return z"]
  B2 --> B3
```

Apply (do for ALL paths):

- From `B1` -> `B3`: `idom(B3)=B0 != B1` ⇒ `B3 ∈ DF(B1)`

intuitively &uarr; means the intermediate dominator of B3 is B0, not B1, therefore B3 is the first place after B1 where B1's influence stops being exclusive, because other paths can also reach there

- From `B2` -> `B3: idom(B3)=B0 !`= `B2` ⇒ `B3 ∈ DF(B2)`
- From `B0` -> `B1/B2: idom(B1)=B0` and `idom(B2)=B0` ⇒ not in DF

So:

- `DF(B1) = {B3}`
- `DF(B2) = {B3}`
- `DF(B0) = ∅`
- `DF(B3) = ∅`

---

#### Step 4: Phi (ϕ) Insertion

_This is the part where SSA decides exactly which blocks need phi, and for which variables._

```
Recall: General Form
x3 = phi(P1: x1, P2: x2, ..., Pk: xk)
```

```mermaid
flowchart TD
  B0["B0: x=0<br/>if c goto B1 else B2"] -->|c=True| B1["B1: x=1<br/>goto B3"]
  B0 -->|c=False| B2["B2: x=2<br/>goto B3"]
  B1 --> B3["B3: z = x + 3<br/>return z"]
  B2 --> B3
```

Intuitively, at B3:

- if we came from B1, then x should be 1
- if we came from B2, then x should be 2
- So we need to merge the versions of x at the join.
<br>
<hr style="width: 200px; margin-left: 0;" />

1. Find where x is assigned:

- In `B0`: `x=0`
- In `B1`: `x=1`
- In `B2`: `x=2`
- So `DefBlocks(x) = {B0, B1, B2}`
<hr style="width: 200px; margin-left: 0;" />

2. DF to find join points

- DF(B1) = {B3}
- DF(B2) = {B3}
- DF(B0) = ∅
- DF(B3) = ∅
<hr style="width: 200px; margin-left: 0;" />

3. Cytron rule (cheat):

- For each variable x, insert `phi(x)` in every block in `DF⁺(DefBlocks(x))`
  (`DF⁺` = iterated DF; we compute it via a worklist.)

Here:

- `DF(B1)` contains `B3`, `DF(B2)` contains `B3` ⇒ insert `ϕ` for `x` at `B3`.

**_Why phi at B3?_**

- B3 is a join (two predecessors: B1 and B2)
- along different incoming edges, x can be different (1 vs 2)
- SSA needs one name to use after the join → ϕ merges versions

So add at top of B3:

- `x = ϕ(B1: x_from_B1, B2: x_from_B2)`

---

#### Step 5: Rename (make each assignment a new version)

Because `x` is assigned many times, so we rename each definition once:

- In B0: `x0 = 0`
- In B1: `x1 = 1`
- In B2: `x2 = 2`
- In B3 : `x3 = [phi(B1:x1, B2:x2)]`

Then use `x3` after the join:

- `z0 = x3 + 3`
- `return z0`

---

#### Final SSA Form

Final SSA form

Blocks:

- B0: `x0=0`; if `c` goto `B1` else goto `B2`
- B1: `x1=1`; goto `B3`
- B2: `x2=2`; goto `B3`
- B3: `x3=phi(B1:x1, B2:x2)`; `z0=x3+3`; `return z0`

So what have we achieved? Recall the Source Program:

```python
x = 0
if c:
    x = 1
else:
    x = 2
z = x + 3
return z
```

Without SSA, at z = x + 3, the compiler has to figure out:

- does this x come from x=0?
- or from x=1?
- or from x=2?

SSA rewrites it so each assignment makes a new name

```python
B0: x0 = 0
    if c goto B1 else goto B2

B1: x1 = 1
    goto B3

B2: x2 = 2
    goto B3

B3: x3 = phi(B1: x1, B2: x2)
    z0 = x3 + 3
    return z0
```

---

## Sign Analysis and Lattice Theory (11a)

Analysing integer signs (positive/negative/zero)

Without running the program, infer what signs each integer variable could have at each program point `{+, 0, -, ⊤, ⊥}`

Type inference is not enough because:

- flow-insensitive (one type for the whole program)
- path-insensitive (can't distinguise different branches/loop phases)

Even a naive sign attempt on SIMP can be path-insensitive

---

### Abstract Domain (Sign Lattice)

Abstract values (VarSign):

- `+` : all positive integers
- `-` : all negative integers
- `0` : {0}
- `⊤` : any integer (may be +, -, 0)
- `⊥` : empty / no info

Order: `⊑` is set inclusion (`⊆`)

- `⊥ ⊑ +, ⊥ ⊑ -, ⊥ ⊑ 0`
- `+, -, 0 ⊑ ⊤`

---

## Code Generation (Stack Machine) (12a)

[back-to-top](#scala)
Recall the compiler pipeline

- `Source Text` &rarr; `[Lexing]` &rarr; `[Parsing]` &rarr; `[Semantic Analysis]` &rarr; `[Optimization]` &rarr; **[Code Generation]**

We are at the last part!

---

### Instruction Selection

1. **1-address instruction**

- Stack machine &rarr; JVM

```
# operands on stack; op pops args, pushes result
# MOST Complex instruction set with stack for computation
# Minimal registers needed, JVM has only 3 registers
# Requires more instructions but less registers

push 1
push 2
add
store r #load it back to variable
```

---

2.  **2-address instruction (CISC) architecture**

- Intel 86
- x86 is a family of rules (an instruction set) that defines how software tells a CPU what to do.

```
# op dst, src   (dst is both input and output)
# more complex instruction set, but fewer registers

load x 1
load y 2
add x y

# r is the same as x
```

---

3. **3-address instruction (RISC) architecture**

- ARM, Pseudo Assembly
- RISC because the number of lines of code have reduced

```
# dst <- src1 op src2
# simple instruction set BUT more registers and tempvar needed

x <- 1
y <- 2
r <- x + y

load x 1
load y 2
add r x y
```

---

### Assembly vs Machine Code

**Assembly** &rarr; human legible
&darr; Assembler + linker &darr;
**Machine** &rarr; binary (not human legible)

---

### JVM bytecode (1-address)

Goal : SIMP PA → JVM

(JVM Instructions):

- `jis ::= []`
  - Empty instruction sequence.
- `jis ::= ji jis`
  - Sequence of JVM instructions.
    (JVM Instruction):
- `ji ::= ilabel`
  - Label marking a jump target.
- `ji ::= iload n`
  - Load local variable onto stack.
- `ji ::= istore n`
  - Store top stack value into local.
- `ji ::= iadd`
  - Pop two values, push their sum.
- `ji ::= isub`
  - Pop two values, push their difference.
- `ji ::= imul`
  - Pop two values, push their product.
- `ji ::= if_icmpge`
  - Compare two; jump if first ≥ second.
- `ji ::= if_icmpne`
  - Compare two; jump if values unequal.
- `ji ::= goto l`
  - Unconditional jump to label `l`.
- `ji ::= ispush c`
  - Push constant `c` onto stack.
- `ji ::= ireturn`
  - Return integer from the method.
    (JVM local vars):
- `n ::= 1`
- `n ::= 2`
- ...
  - Local variable slot indices.
    (constant):
- `c ::= -32768`
- ...
- `c ::= 0`
- ...
- `c ::= 32767`

  - Signed 16-bit constant range.

- 1: a register for the first operand and result
- 2: a register for the second operand
- 3: a register for controlling the state of the stack operation (we can't use)

---

**An Example (PA → JVM)**

```text
   // PA1                          // JVM (illustrative)
############           ############################################################
1:  x <- input          iload 1        // push the content of input to r0
2:  s <- 0              istore 2       // pop r0's content to x
3:  c <- 0              sipush 0       // push the value 0 to r0
4:  b <- c < x          istore 3       // pop r0 to s
5:  ifn b goto 9        sipush 0       // push the value 0 to r0
6:  s <- c + s          istore 4       // pop r0 to c
7:  c <- c + 1          ilabel 11      // mark label 11
8:  goto 4              iload 4        // push the content of c to r0
9:  rret <- s           iload 2        // push the contetn of x to r1
10: ret                 if_icmpge 12   // if r0 >= r1 jump, pop both r0 r1

                        iload 4        // push the content of c to r0
                        iload3         // push the content of s to r1
                        iadd           // sum up r0 and r1 and result in r0
                        istore 3       // pop r0 to s
                        iload 4        // push the content of c to r0
                        sipush 1       // push a constant 1 to r1
                        iadd
                        istore 4       // pop r0 to c
                        igoto 11
                        ilabel 12
                        iload 3        // push the content of s to r0
                        ireturn
```

PA variables → JVM locals:

- input → 1,
- x → 2,
- s → 3,
- c → 4

PA labels → JVM labels:

- 4 → 11,
- 9 → 12

---

### Operational Semantics of JVM

We write small-step JVM configurations as:

- `J ⊢ (Δ, S, jis) → (Δ', S', jis')`

where:

- `J` is the JVM program
- `Δ` the local-variable environment &rarr; like JVM's numbered storage slots
  - If Δ(1) = 7, then local slot 1 currently holds 7
- `S` the operand stack
- `jis` (JVM instruction sequence) is the current instruction sequence.

Key small-step rules (informal):

$$
\textbf{(sJLoad1)}\qquad
J \vdash (\Delta,\ \_,\ \_,\ \text{iload } n;\ jis)\ \to\ (\Delta,\ \Delta(n),\ \_,\ jis)
$$

_Meaning:_ Load local `n` onto an empty stack.

$$
\textbf{(sJLoad2)}\qquad
J \vdash (\Delta,\ c,\ \_,\ \text{iload } n;\ jis)\ \to\ (\Delta,\ c,\ \Delta(n),\ jis)
$$

_Meaning:_ Load local `n` onto the second stack slot.

$$
\textbf{(sJPush1)}\qquad
J \vdash (\Delta,\ \_,\ \_,\ \text{sipush } c;\ jis)\ \to\ (\Delta,\ c,\ \_,\ jis)
$$

_Meaning:_ Push constant `c` onto an empty stack.

$$
\textbf{(sJPush2)}\qquad
J \vdash (\Delta,\ c_0,\ \_,\ \text{sipush } c_1;\ jis)\ \to\ (\Delta,\ c_0,\ c_1,\ jis)
$$

_Meaning:_ Push constant `c1` onto the second stack slot.

$$
\textbf{(sJLabel)}\qquad
J \vdash (\Delta,\ r_0,\ r_1,\ \text{ilabel } l;\ jis)\ \to\ (\Delta,\ r_0,\ r_1,\ jis)
$$

_Meaning:_ Labels are markers; they don’t change state.

$$
\textbf{(sJStore)}\qquad
J \vdash (\Delta,\ c,\ \_,\ \text{istore } n;\ jis)\ \to\ (\Delta \oplus (n,c),\ \_,\ \_,\ jis)
$$

_Meaning:_ Pop stack top and store into local slot `n`.

$$
\textbf{(sJAdd)}\qquad
J \vdash (\Delta,\ c_0,\ c_1,\ \text{iadd};\ jis)\ \to\ (\Delta,\ c_0 + c_1,\ \_,\ jis)
$$

_Meaning:_ Pop two integers, push their sum.

$$
\textbf{(sJGoto)}\qquad
J \vdash (\Delta,\ r_0,\ r_1,\ \text{igoto } l';\ jis)\ \to\ (\Delta,\ r_0,\ r_1,\ \text{codeAfterLabel}(J,l'))
$$

_Meaning:_ Jump to the instruction sequence after label `l'`.

$$
\textbf{(sJCmpNE1)}\qquad
\frac{c_0 \ne c_1 \qquad jis' = \text{codeAfterLabel}(J,l')}
     {J \vdash (\Delta,\ c_0,\ c_1,\ \text{if\_icmpne } l';\ jis)\ \to\ (\Delta,\ \_,\ \_,\ jis')}
$$

_Meaning:_ If values differ, jump to label `l'`.

$$
\textbf{(sJCmpNE2)}\qquad
\frac{c_0 = c_1}
     {J \vdash (\Delta,\ c_0,\ c_1,\ \text{if\_icmpne } l';\ jis)\ \to\ (\Delta,\ \_,\ \_,\ jis)}
$$

_Meaning:_ If values equal, continue to the next instruction.

---

$$
\textbf{codeAfterLabel}(\text{ireturn},\ l)\ =\ \text{error}
$$

_Meaning:_ Error if label wasn’t found before return.

$$
\textbf{codeAfterLabel}(\text{ilabel } l';\ jis,\ l)\ =\
\begin{cases}
jis & \text{if } l = l' \\
\text{codeAfterLabel}(jis,\ l) & \text{otherwise}
\end{cases}
$$

_Meaning:_ If label matches, return remaining code; else keep searching.

$$
\textbf{codeAfterLabel}(ji;\ jis,\ l)\ =\ \text{codeAfterLabel}(jis,\ l)
$$

_Meaning:_ Skip non-label instruction and continue searching for the label.

Notes:

- `codeAfterLabel(J, l)` denotes the instruction sequence of `J` starting at label `l`.
- These rules are the standard small-step behaviour used in the lecture slides.

---

### From PA to JVM

M - a mapping from PA tempvar to JVM local var

L - a mapping from PA labels (which are used as the targets in some jump instructions) to JVM labels

**3 rules**

```text
M, L ⊢ jis_PA ⇒ jis_JVM
  -- Translate PA instruction sequence `jis_PA` to JVM `jis_JVM` using mappings.

M, L ⊢ S_PA ⇒ jis_JVM
  -- Translate PA operand-stack `S_PA` into JVM pushes/instructions.

L ⊢ l_PA ⇒ jis_JVM
  -- Translate PA label `l_PA` into the JVM label and its code.
```

---

#### Example (PA &rarr; JVM)

```mathematica
1:  x <- 7
2:  t <- x < 10
3:  ifn t goto 6
4:  y <- x + 1
5:  goto 7
6:  y <- x - 1
7:  rret <- y
8:  ret
```

_t is a boolean temp in PA (result of comparison)._

_ifn t goto 6 means “if NOT t, jump to label 6”._

##### 1) Pick the mappings M and L

Variable-to-local mapping M

- `M(x) = 1`
- `M(y) = 1`

Label mapping L

- Only labels that are jump targets need JVM labels

Jump targets in PA:

- line 6 (target of goto 6)
- line 7 (target of goto 7)

So:

- L(6) = L6
- L(7) = L7

##### 2) Translate PA &rarr; JVM

The below will document the step by step translation, with justification at each step which operation semantics is being used and why as well as the updates to local variable and S

PA program:

```text
1:  x <- 7
2:  t <- x < 5
3:  ifn t goto 6
4:  y <- x + 2
5:  goto 7
6:  y <- x - 2
7:  rret <- y
8:  ret
```

Mappings:

- `M(x)=1`, `M(y)=2`
- `L(6)=L6`, `L(7)=L7`

JVM program J:

```text
sipush 7
istore 1

iload 1
sipush 5
if_icmpge L6

iload 1
sipush 2
iadd
istore 2
igoto L7

ilabel L6
iload 1
sipush 2
isub
istore 2

ilabel L7
iload 2
ireturn
```

State notation:

- `Δ` = locals map (e.g., `Δ(1)=x`, `Δ(2)=y`)
- `S` ∈ { `(_,_)`, `(c,_)`, `(c0,c1)` } (same as slide)
- I’ll show `Before → After` for `Δ` and `S` each step.

---

# Run A (x = 7): takes the ELSE path (jumps to L6)

Initial:

- Δ = {1:?, 2:?}
- S = (_, _)

## PA Line 1: `x <- 7` (emits `sipush 7; istore 1`)

1. `sipush 7`

- Rule: **(sJPush1)**
- Why this rule: stack is empty `(_,_)`, so push goes into first slot.
- Δ: {1:?, 2:?} → {1:?, 2:?}
- S: (_,_) → (7,\_)

2. `istore 1`

- Rule: **(sJStore)**
- Why this rule: `istore` requires a value on stack top `(c,_)` to store.
- Δ: {1:?, 2:?} → {1:7, 2:?}
- S: (7,_) → (_,\_)

---

## PA Lines 2–3: `t <- x < 5; ifn t goto 6`

(Compiled as “push x, push 5, then branch if x >= 5”.)

3. `iload 1`

- Rule: **(sJLoad1)**
- Why this rule: stack is empty `(_,_)`, so load fills first slot.
- Δ: {1:7, 2:?} → {1:7, 2:?}
- S: (_,_) → (Δ(1),_) = (7,_)

4. `sipush 5`

- Rule: **(sJPush2)**
- Why this rule: stack has one value `(7,_)`, so push fills second slot.
- Δ: {1:7, 2:?} → {1:7, 2:?}
- S: (7,\_) → (7,5)

5. `if_icmpge L6`

- Rule used: **same conditional-branch pattern as (sJCmpNE1)/(sJCmpNE2)**, but with predicate `>=`
- Why this “jump case”: operands are `(7,5)` and `7 >= 5` is true → jump.
- Δ: {1:7, 2:?} → {1:7, 2:?}
- S: (7,5) → (_,_)
- Control effect: next `jis` becomes `codeAfterLabel(J, L6)` (jump to ELSE block).

---

## PA Line 6: `y <- x - 2` (label + arithmetic + store)

6. `ilabel L6`

- Rule: **(sJLabel)**
- Why this rule: labels are markers; they don’t change Δ or S.
- Δ: {1:7, 2:?} → {1:7, 2:?}
- S: (_,_) → (_,_)

7. `iload 1`

- Rule: **(sJLoad1)**
- Why this rule: stack is empty `(_,_)`.
- Δ: {1:7, 2:?} → {1:7, 2:?}
- S: (_,_) → (7,\_)

8. `sipush 2`

- Rule: **(sJPush2)**
- Why this rule: stack has one value `(7,_)`.
- Δ: {1:7, 2:?} → {1:7, 2:?}
- S: (7,\_) → (7,2)

9. `isub`

- Rule used: **same stack-shape as (sJAdd)**, but compute `c0 - c1`
- Why this rule shape: `isub` consumes two stack values `(c0,c1)` and leaves one result.
- Δ: {1:7, 2:?} → {1:7, 2:?}
- S: (7,2) → (7-2,_) = (5,_)

10. `istore 2`

- Rule: **(sJStore)**
- Why this rule: `istore` pops `(c,_)` and writes into local slot 2.
- Δ: {1:7, 2:?} → {1:7, 2:5}
- S: (5,_) → (_,\_)

---

## PA Lines 7–8: `rret <- y; ret` (load then return)

11. `ilabel L7`

- Rule: **(sJLabel)**
- Why this rule: labels don’t change machine state.
- Δ: {1:7, 2:5} → {1:7, 2:5}
- S: (_,_) → (_,_)

12. `iload 2`

- Rule: **(sJLoad1)**
- Why this rule: stack is empty `(_,_)`.
- Δ: {1:7, 2:5} → {1:7, 2:5}
- S: (_,_) → (5,\_)

13. `ireturn`

- Rule: (not shown on your screenshot page)
- What happens: program halts and returns the top stack value (here `5`).
- Δ: unchanged
- S: consumed by return (conceptually returns `5` and stops)

Result of Run A: returns **5**.

---

# Run B (x = 3): takes the THEN path (falls through, uses `igoto L7`)

This run exists so you can see Line 4 and Line 5 execute with their rules.

Change only PA Line 1 to: `x <- 3` (so `3 >= 5` is false).

Initial:

- Δ = {1:?, 2:?}
- S = (_, _)

## PA Line 1: `x <- 3`

1. `sipush 3`

- Rule: **(sJPush1)** (stack empty)
- Δ: {1:?, 2:?} → {1:?, 2:?}
- S: (_,_) → (3,\_)

2. `istore 1`

- Rule: **(sJStore)**
- Δ: {1:?, 2:?} → {1:3, 2:?}
- S: (3,_) → (_,\_)

## PA Lines 2–3: compare + branch

3. `iload 1`

- Rule: **(sJLoad1)** (stack empty)
- Δ: {1:3, 2:?} → {1:3, 2:?}
- S: (_,_) → (3,\_)

4. `sipush 5`

- Rule: **(sJPush2)** (one item already)
- Δ: {1:3, 2:?} → {1:3, 2:?}
- S: (3,\_) → (3,5)

5. `if_icmpge L6`

- Rule used: **same conditional-branch pattern as (sJCmpNE1)/(sJCmpNE2)**, but with `>=`
- Why this “fall-through case”: `3 >= 5` is false → do NOT jump.
- Δ: {1:3, 2:?} → {1:3, 2:?}
- S: (3,5) → (_,_)
- Control effect: continue with the next instruction (THEN block).

## PA Line 4: `y <- x + 2`

6. `iload 1`

- Rule: **(sJLoad1)** (stack empty)
- Δ: {1:3, 2:?} → {1:3, 2:?}
- S: (_,_) → (3,\_)

7. `sipush 2`

- Rule: **(sJPush2)** (one item already)
- Δ: {1:3, 2:?} → {1:3, 2:?}
- S: (3,\_) → (3,2)

8. `iadd`

- Rule: **(sJAdd)**
- Why this rule: `iadd` requires two operands `(c0,c1)` and produces `(c0+c1,_)`.
- Δ: {1:3, 2:?} → {1:3, 2:?}
- S: (3,2) → (5,\_)

9. `istore 2`

- Rule: **(sJStore)**
- Δ: {1:3, 2:?} → {1:3, 2:5}
- S: (5,_) → (_,\_)

## PA Line 5: `goto 7`

10. `igoto L7`

- Rule: **(sJGoto)**
- Why this rule: unconditional jump replaces `jis` with `codeAfterLabel(J, L7)`.
- Δ: {1:3, 2:5} → {1:3, 2:5}
- S: (_,_) → (_,_)
- Control effect: skip ELSE block; jump to code after `ilabel L7`.

## PA Lines 7–8: return

11. `iload 2`

- Rule: **(sJLoad1)** (stack empty)
- Δ: {1:3, 2:5} → {1:3, 2:5}
- S: (_,_) → (5,\_)

12. `ireturn`

- Rule: (not shown on your screenshot page)
- What happens: halt and return top stack value `5`.

Result of Run B: returns **5**.

---

## Quick “why rule X vs rule Y” summary

- Use **(sJPush1)** when `S = (_ , _)`; use **(sJPush2)** when `S = (c , _)`.
- Use **(sJLoad1)** when `S = (_ , _)`; use **(sJLoad2)** when `S = (c , _)`.
- Use **(sJStore)** when `S = (c , _)` (needs one value to store).
- Use **(sJAdd)** when `S = (c0 , c1)` (needs two values).
- Use **(sJGoto)** for `igoto l'` to jump via `codeAfterLabel(J, l')`.
- Use **(sJLabel)** for `ilabel l` (no state change).
- Conditional `if_icmpge` follows the **same jump/fall-through structure** as **(sJCmpNE1)/(sJCmpNE2)**.

---

## Register Allocation Problem (12b)

PA is already a 3-address code

- Unlimited registers
- Unlimited tempVar
- Operation can be applied to both registers and tempVar

But in reality,

- Limited registers
- Large set of tempVar but never unlimtied
- Most operation only work on registers

Problem:

Given program `p`, allocate variables/results to `k` registers such that:

- behavior stays the same
- spilling is minimized

So its an optimisation problem

### Interference Graph

A data structure to reason able the constraints of register allocation

2 tempvar are interferring when they are both "live" at the same time in the program

#### Goal (why we build this graph)

We want to **allocate a small number of registers** to many temporaries.
Two temporaries **cannot share the same register** if they are **live at the same time**.
The **interference graph** encodes exactly those “cannot-share” constraints.

---

The PA program (PA1) + liveness info shown on the slide

```text
// PA1
1:  x    <- input      // {input}
2:  y    <- x + 1      // {x}
3:  z    <- y + 1      // {y}
4:  w    <- y * z      // {y, z}
5:  rret <- w          // {w}
6:  ret               // {}
```

Interpretation of the `{ ... }` on each line:

- It shows which temporaries are **live at that program point** (their values will be needed later).
- If **two names appear together** in the same set, they **overlap in time** → they **interfere**.

---

## 2) Build the interference graph from liveness

### Rule (what edges mean)

- Make one node per temporary: `input, x, y, z, w`.
- Add an **undirected edge** between `A` and `B` if there exists **any point** where both are live together.

### Apply the rule to PA1

Look at the sets:

- Line 1: `{input}` → no pair → no edge
- Line 2: `{x}` → no pair → no edge
- Line 3: `{y}` → no pair → no edge
- Line 4: `{y, z}` → **pair exists** → add edge **`y — z`**
- Line 5: `{w}` → no pair → no edge

So the only interference constraint here is:

- **`y` interferes with `z`**

That’s exactly the single edge drawn on the slide.

---

## 3) The interference graph (Mermaid)

```mermaid
graph TB
  input["input"]
  x["x"]
  y["y"]
  z["z"]
  w["w"]

  y --- z
```

---

## 4) Use the graph to assign registers (graph coloring idea)

### What register allocation is doing here

Think of each register as a “color”.

- If two nodes are connected by an edge, they must have **different colors**.
- If two nodes are **not** connected, they _may_ share a color/register (as long as no other overlap forces separation).

### Apply to this graph

Only `y` and `z` are adjacent, so:

- `y` must be in a different register from `z`
- everyone else can reuse either register since they don’t overlap with a conflict

One valid assignment (matching the slide’s idea):

- `z → r1`
- `y → r0`
- `input, x, w → r0` (they don’t interfere with `y` or `z` in a conflicting way)

---

## 5) Interference graph + one concrete register assignment (Mermaid)

```mermaid
graph TB
  input["input(r0)"]
  x["x(r0)"]
  y["y(r0)"]
  z["z(r1)"]
  w["w(r0)"]

  y --- z
```

---

## 6) Why your Mermaid error happened (and how this fixes it)

Mermaid is picky about node text formatting.
Using quotes like `id["text"]` is safest, especially when labels contain parentheses like `input(r0)`.
Also, put each node definition on its own line (as above).
