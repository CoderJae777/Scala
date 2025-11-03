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

### Static Semantics

### Fault Detection
