# Syntactic sugar and other extensions

This document contains ideas on how to extend the syntax from the core language to the full language.

Some of these ideas will be implemented by me before the end of 9874.  Some are only half baked and need some thought and experimentation.

In any case, 9874 students won't have to implement any of these ideas, they should stick to implementing the core language.  So they shouldn't spend a lot of time (if any) reading this document.

This section contains suggestions for how the syntax can be extended beyond the core to make the language more flexible and appealing to use.

## Purely syntactic changes

Many of these additions are purely syntactic and involve little or no additional abstract syntax.

### If syntax

[I do plan to implement this for 9874.]

The following sort of if syntax will be allowed.

~~~
   if X then A
   elsif Y then B
   elsif Z then C
   end if
~~~

This is equivalent to 

~~~
   if X then A
   else if Y then B
        else if Z then Z
             else
             end if
        end if
   end if
~~~
     
### Fancy operator syntax

[This is now implemented.]

An expression `E + F` abbreviates `(E).binary+ (F)`.  An expression `- E` abbreviates `(E).unary- ()`.  The default precedence of binary operators is given by

| operator | precedence |
------------------|---------------------|
| implies    | 100            |
| or            |  200            |
| and            |  300            |
| =              | 400            |
| <            |  400            |
| >           |  400            |
| >_           |  400            |
| _<           |  400            |
| \le          |  400            |
| \ge         |  400           |
| union     |  500   |
| \cup     |  500   |
| +            |  500   |
| -            |  500   |
| *      |  600   |
| intersection     |  600   |
| \cap | 600 |
| /     |  600   |
| div     |  600   |
| mod     |  600   |
| ^      |  700   |
| other operators | 1000000 |

The other operators come in two flavours.

* Any string of one or more characters from the set { `~`, `!`,  `@`, `#`,`$`, `%`, `^`, `&`,  `*`,`-`, `_`, `+`, `=`, `|`, `/`, `<`, `>`, `?`, `\` } otherwise used in the grammar.

* A backslash `\` followed by one or more letters is also considered an operator. This is useful for translating Pseu into LaTeX.

Binary operators at the same precedence level always associate to the left, so for example `a ^ b ^ c` is the same as `(a ^ b) ^ c` and ultimately the same as `(a.^(b)).^(c)`.

Unary operators have higher precedence than binary operators, but lower precedence than send operations and other postfix operations. So `- a + - b.f()` is the same as `a. unary-() + b.f().unary-()`, which is the same as

~~~
   a. unary-() .binary+( b.f().unary-() )
~~~


### Ranges

[ I don't plan to implement this soon.]

Make `,..` and `,..,` be tokens.

Define `[x,..y]` to mean `x.upTo(y)`; define `[x,..,y]` to mean `x.through(y)`; define `{x,..y}` to mean `x.upTo(y).toSet()`; and define `{x,..,y}` to mean `x.through(y).toSet()`.

### Range application

Define `s(x,..y)` to mean `s.subSeq(x, y)`.  If `s` is a sequence and `x` and `y` are integers.

Similarly for `,..,`.


### <a id="brackets"></a> Brackets

Allow square brackets for application.  Some people might prefer to use `s[5]` to `s(5)`. 

This conflicts somewhat with the allowing the parentheses to be dropped. Does `s[5]` really mean `s(5)` or `s([5])`.  Similarly, does `s[1,2,3]` mean `s(1,2,3)` or `s([1,2,3])`.

With this proposal, we pretty much have to pick the first.  Without this proposal, we have to pick the second.

Similarly define `s[x,..y]` to mean `s(x,..y)`.  Note that when `x` equals `y`,  `s[x]` and `s[x,..y]` have different meanings, whereas (if they have any meaning at all) `s([x])` and `s([x,..y])` have the same meaning.  Presumably the meaning of `s([x,..y])` would be the same as `s(x,..y)`.

### Function declaration

#### Directly declaring a function

[This is implemented.]

A declaration

~~~
	val f := fun (a : t0, b: t1) : u do ... end fun
~~~

can be abbreviated by

~~~
    fun f(a : t0, b: t1) : u do ... end fun
~~~

#### Procedures

[This is implemented.]

An expression

~~~
	fun (a : t0, b: t1) : Unit do ... ; () end fun
~~~

can be abbreviated by

~~~
	proc (a : t0, b: t1) do ... end proc
~~~

A declaration

~~~
	val p := proc (a : t0, b: t1) do ... end proc
~~~

can be abbreviated by

~~~
    proc p(a : t0, b: t1) do ... end proc
~~~


### Short form lambda expressions

[This is now implemented.]


The expression

~~~
   (a: t0, b: t1) -> E
~~~

where E is a simple expression, abbreviates

~~~
    fun ( a: t0, b: t1) do return E end fun
~~~

The precedence of the `(...) ->` prefix is the same as unary operators.  So you can write

~~~
   () -> - a
~~~

But

~~~
   () -> a + b
~~~

means

~~~
   ( () -> a ) + b
~~~

Short form lambda expressions can not be used when there is an explicit result type. E.g.

~~~
   () : Int -> a    // This is an error.
~~~

### Indentation rules

[I might implement this for 9874.]

The full syntax includes indentation restrictions. In return the various "end" markers can be omitted as can semicolons at the ends of lines.

The basic restriction is that each command or declaration in a block must begin at the same column or on the same line. For example

```
      a := 1 ; b := 2
```

can be a block because both commands start on the same line.  And

```
     a := 1 ;
     b := 2 ;
```

can be a block because both commands start at the same column.  But

```
     a := 1 ;
         b := 2 ;
```

can not be a block. Nor can

```
     a := 1 ;
   b := 2 ;
```

Furthermore, the first token of each block must begin at a column number larger than the first token of its containing command or declaration. For example

```
    if a < b then
       a := c
    else  b := c
```

is allowed because the block between `then` and `else` starts at a larger column number than the keyword `if` and likewise the  block that comes after `else`.

Blocks can be optionally ended with `end`-markers.  For example, the we could write

```
    if a < b then
       a := c
    else 
        b := c
    end if
```

### Omitting semicolons at the ends of lines.

[I might implement this for 9874.]

If we have the implementation rules as about and also adopt the convention that each expression should not have any tokens at or to the left of the first statement of any block it is in. Then we can make semi-colons at the ends of lines optional. For example

~~~
   a := f.foo
            ( 123 ) 
   b := 456
~~~

Here the argument is line after the function. No semicolon is inferred at the end of the first line.

Since the `b` on the third line is indented the same as the  first, it must be part of the next command. That the closing `)` is the last token of the first command can be inferred. The parser acts as if here were a `;` at the end of the second line.


## Deeper enhancements

### Others

[None of these will be implemented until after 9874.]

* Compile-time type checking
* User defined classes and interfaces
* Inheritance and overriding
* Generic classes and functions.
* Function overloading of some kind
* Algebraic data types and pattern matching
* Mutable sets and sequences
* out parameters and arrays???
* Pre and post conditions
* Automated verification

### Implicit typing

[I do not plan to implement this for 9874, as it requires type checking.]

~~~
    var x := E
~~~

abbreviates

~~~
    var x : T := E
~~~

where T is the most specific type that can be inferred for E.

Similarly for val.  The core syntax allows the type to be omitted from a val.  In the absence of compile time type checking, we can use replace the missing type with Any.  However with compile time type checking, it is better to replace the missing type with the type of E.

Similarly with functions, the core syntax allows the 
