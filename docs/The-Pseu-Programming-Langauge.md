# The Pseu programming language

## Overview

The purpose of the pseu language is to provide a compact notation for describing algorithms at a high level of abstraction.

For example types such as sets, sequences, tuples, and maps are built-in data types.

## Some examples

The examples may use syntax that is beyond the core. The extra syntax used is described in the document "syntaticSugarAndOtherExtensions".

This procedure will find all permutations of a set of integers

```
  fun perms( S : Set[Int] ) : Set[Seq[Int]]
      var P : Set[Seq[Int]]
      if #S = 0 then
          P := { [ ] }
      else
          val a := s.any()
          var P0 : Set[Seq[Int]]
          P0 := perms( S-{a} )
          P := { }
          for seq <- P0 do
               for i <- [0 ,.. #S] do
                   P := P union { seq[0,..i] ^ a ^ seq[i,..] }
     return P
 end perms
```

Here is a blow by blow explanation of the program. 

The first line declares that `perms` is a procedure. It has an input parameter `S` of type `Set[Int]` (i.e. `S` represents a set of integers). 

The declaration `var P : Set[Seq[Int]]` declares a variable `P` of type `Set[Seq[Int]]`. Which means `P` represents a set of sequences of integers.

The expression `#S = 0` is true if and only if the size of `S` is 0.  When this true `p` is assigned a set containing only the empty sequence `[ ]`.

After the `else`, there is a declaration of a local constant `a`, which is initialized with an arbitrary member of set `S`. Variable `P0` is declared to have the same type as `P`; it is not initialized. The next line is a recursive call to procedure `perms`.  The argument `S-{a}` is the set of all think in `S` other than `a`.

Next, `P` is assigned the empty set `{ }`. 
The following lines define two nested for loops that add values to set `P`. 

The second last line indicates that the result of the function is the value of variable `P`.

The final line marks the end of the function definition.

It should be noted that sets and sequences in Pseu are mathematical in nature. That is, they are immutable values. Our variable `P` refers to different values at different times rather than always referring to one object whose value changes.

## Some general remarks on syntax

The syntax of the language is mostly conventional.  It uses `:=` for assignment and initialization and `=` for equality.  It is block structured.   It requires, a semicolon `;` after each command or declaration, except for the last one in a block.

There are some unusual rules about indentation, similar to, but different from, Python.  These are beyond the core and are described in the document "syntaticSugarAndOtherExtensions".

## Data types and data values

The language has at least the following data types and values

* `Bool` the boolean values `true` and `false`.
* `Int` integers of any size.
* `String` immutable sequences of characters of any length
* `Set[T]` immutable finite sets containing only items of type `T`. E.g. `Set[Set[Int]]`
* `Seq[T]` immutable finite sequences containing only items of type `T`. E.g. `Seq[Bool]`.
*  `T * U` pairs of values. The first value is from `T`, the second is from `U`.  Similarly `T * U * V` is the type of triples of values and so on for longer tuples. These [product types can also be written as `Product[T,U,V]` where the number of type arguments is at least 2.
*  `Unit` is a type that only has one value, namely the empty tuple.
* `T -> U` functions from `T` to `U`. This can also be written as `Fun[T,U]`.
* `Any`  All values have type `Any`
* `None` No values have type `None`.

Note there are no length 1 tuples.   (In a sense every value is a length 1 tuple.)

Implementation note: It is important that the set of types and the set of values be easily expandable.

## Constants 

The keywords `true` and `false` .

Integer constants are sequences of 1 or more decimal digits. Underscores can be used to ease readability. E.g., `0`, `123`, `123_456_789_012_345`.

String constants are enclosed by double quotes. E.g. `""`, `"abc"`, ... .  The syntax for strings is borrowed from Java, so you can write things like "\"\n\\" which is a string of 3 characters, as in Java.

## Constructors

We can construct a set from a sequence of expression using the syntax. `{X, Y, Z}` where `X`, `Y`, and `Z` are expressions.

We can construct a sequence from a sequence of expression using the syntax. `[X, Y, Z]` where `X`, `Y`, and `Z` are expressions.

Pairs, triples, etc. are made using parentheses and. E.g. `(X,Y,Z)` makes a triple. `()` makes the 0-tuple. There are no 1-tuples in the language, so `(X)` just means `X`.

Functions are made using lambda expressions.  An example of a lambda expression is `fun( x : Int ) x .binary+ 1 end fun`

## Lookup

For each value, there are other values associated with it via names. For example, the sequence `[99,98,97]` is associated with the number 3 via the name `length`.  We can lookup such values, using dot (`.`) notation. For example `[99,98,97].length` is a complicated way of writing `3`.

Often the result of a lookup is a function value.  For example `[1,2,3].cat` might result in a function that concatenates `[1,2,3]`sequence with its argument  so that `[1,2,3].cat( s )` concatenates `[1,2,3]` with the value of `s`.

## Operator syntax

Uses of binary operators such as `123 - x` will be turned into combination of lookups and application by the parser. The example expression is equivalent to `123.binary-(x)`.  Here the name is the two token sequence `binary -`.

Likewise uses of unary operators such as `-x` are turned into combinations of lookups and applications by the parser. The example is equivalent to `x.unary- ()`. 

## Application

Applications can have more than one or less than one argument.  These cases are equivalent to sending a single argument. Again, this is a trivial syntactic matter.  An expression `E()` is equivalent to the expression `E(())`; that is, the argument will be the empty tuple.  An expression `E(F,G,H)` is equivalent to an expression `E((F,G,H))`; so the single argument value will be a triple.

Conversely in most cases, the parentheses are not needed. For example `f 123` applies function `f` to argument `123`.

## Sending messages

Message sending actually has two steps. Consider the expression `123.binary +(321)`

* The first step is look-up. This step looks up the name in the recipient and results in a function value.  For example the result of `123.binary +` is a function value.
* The second step is application. This step applies the resulting function to an argument.

These steps can actually be separated. The following code should work

~~~
     val f : Int -> Int := 123.binary + ;
     print( f(321) )
~~~

The first line gives the name `f` to the result of the lookup. The second line applies that function.


## Type checking

[For the purposes of the 9874 project, type checking is only done at runtime. This includes checking the number of arguments passed to a function call. For example, the expression `12 + "abc"` will result in no error at compile time, but will result in an error at run time, since the `binary(+)` operation for integers is not prepared to accept a string as the argument.  Similarly an expression `12 .binary +(34, 56)` would not be an error at compile time, but will result in an error at run time. In this case the argument is a pair and the `binary(+)` operation for integer values is not defined when the argument is a pair.]

The following type checks need to be made at run-time. 

* When a variable (val or var) is assigned, the value must be in the type of the variable. Otherwise, it is a run-time error.
* When a user defined function with one parameter is applied, the value passed as argument must be the same type as that parameter.
* When a user defined function with 0 parameters is applied, the argument value must be the empty tuple.
* When a user defined function with 2 or more parameters is applied, the argument must be a tuple of the same length and each item of that tuple must be in the type of the corresponding parameter
* Similar checks for built-in functions.
* The result of a user defined function should be in return type of the function.
* In a lookup expression, such as `x.y`, the name `y` must be mapped to something for the value `x`. E.g., `123.binary+` is ok since `binary(+)` is defined for all integers.  But `123.foobar` should probably result in a run-time error.

[As a bonus goal, you can add compile time type checking.]

## Core syntax

The following describes the core syntax of the language. Constructs that use the full syntax can be rewritten to use the core syntax, so we will start by implementing the core and add elements of the full syntax as needed.

The syntax in this section is given using extended CFG notation.  Terminals of the language are written in double quotes or as lower case words.  The meaning of these words is discussed in the Section called Lexical rules. Nonterminals are written as words.   The right hand side of each production is written with the following conventions:
 
 >  X Y
  
 means X followed by Y;
 
 > X | Y
 
 means either X or Y;
 
 > X?
 
 means an optional X;
 
 > X*
 
 means zero or more X;
 
 > X+
 
means one or more X.  Unquoted parentheses are used for grouping.
 
~~~
 Block --> ( BlockItem (";" BlockItem)* )? (";")?
 
 BlockItem ->  Command | Declaration 
 
 Command -->  LHSs ":=" Exps
           |  "if" Exp "then" Block "else" Block "end" "if"
           |  "while" Exp "do" Block "end" "while"
           |  "for" id "<-" Exp "do" Block "end" "for"
           |  "return" ( Exp )?
           | "begin" Block "end"
           | ExpCommand
   
ExpCommand --> Exp

PostfixOp --> "." Name | Arg
 
LHSs --> LHS ("," LHS)*
 
Exps --> Exp  ("," Exp)*
 
LHS --> id
 
Declaration --> "var" id ":" Type (":=" Exp)?
              | "val" id (":" Type)? ":=" Exp

Name --> id | "unary" Op | "binary" Op

Exp --> SimpleExp
     |  Exp PostfixOp

SimpleExp --> "true"
         | "false"
         | stringLiteral
         | intLiteral
         | id
         | "{" (Exp ("," Exp)* )? "}"
         | "[" (Exp ("," Exp)* )? "]"
         | "fun" "(" Parameters  ")" (":" Type)? "do" Block "end" "fun"
         | ParenthizedExp
 
ParenthizedExp --> "(" (Exp ("," Exp)* )? ")"
         
Arg -->  SimpleExp 

Op --> "*"
      | "mod"
      | "div"
      | "union" | "intersection"
      | "and" | "or" | "not" | "implies"
      | otherOp
      | latexOp

Type --> ProductType 
         | Type "->" ProductType

ProductType --> SimpleType ("*" SimpleType)*

SimpleType --> id
             | id "[" Types "]"
             | "(" Type ")"

Types --> Type ("," Type)*

Parameters --> (Parameter ("," Parameter)*)?

Parameter --> id ":" Type

~~~

## Meaning

### Blocks
 
 A block of commands and declarations is executed from left to right.  All the variables (whether val or var) declared (directly) within the block are scoped to the block.  
 
~~~
    val f := fun () : Int do return x end do ;
    val x : Int := 42 ;
    print( f() ) 
~~~

should print 42.

However, it is not allowed (by a run time check) for a location to be read or written before it has been declared and initialized.  For example:
 
~~~
    val y : Int := x + 1 ; // Allowed by compiler. But causes a run time error.
    val x : Int := 42 ;
    print( f() ) 
~~~

Code in inner blocks can access variables declared in outer blocks.  For example

~~~
   val x : Int := 42 ;
   var y : Int := 10 ;
   while y.binary /= ( 0 ) do
       print(x) ;
       y := y.unary -( )
   end
~~~

Should compile and run just fine.

Variable resolution should be done at compile time. Thus the program

~~~
    print(x)
~~~

should cause a compile time error, since `x` is not declared.  (The reference to the `print` variable is no problem, since `print` is a built-in variable which is implicitly declared at block level 0.)

The run-time analogue of a block is a **frame**. A frame is a array of locations and a pointer to a parent frame.

Blocks, command, and declarations all evaluate to a value. The value of a nonempty block is the value of its last command or declaration.  The value of an empty block is the empty tuple.  This convention is useful so we don't need a return at the end of function. E.g. these two expression have the same meaning.

~~~
    fun (k : Int) do k.binary*(k) end
    fun (k : Int) do return k.binary*(k) end
~~~
  

### Declarations

A declaration

~~~
   val x : T := E
~~~

declares a variable `x`, having type `T`, initialized by expression `E`.  Since the variable is declared as a `val`, it can't be assigned to and the compiler should report as errors any assignments to `val` variables.


~~~
   val x := E
~~~

declares a variable `x` initialized with expression `E`.  Since the variable is declared as a `val`, it can't be assigned to and the compiler should report as errors any assignments to `val` variables.

If the type is missing in a val declaration, the missing type can be filled in with Any. E.g. these two declarations mean the same thing

~~~
   val x := E
   val x : Any := E
~~~


A var declaration 

~~~
   var x : T := E
~~~

is similar to a val declaration, but assignments to `x` are allowed.

~~~
   var x : T 
~~~

is similar, except that the variable is initially uninitialized. Any fetch from the variable before it is assigned a value is a run-time error.

Variables remain uninitialized until their declaration is encountered. For example

~~~
   val x : Int := y ; // Ok at compile time
   var y : Int := x
~~~

The use of `y` in the first declaration is permitted at compile time because the scope of `y` covers the whole block. However, at run time it is an error to fetch from `y` or assign to `y` until its declaration has been executed.

Similarly, the following code has a run time error.

~~~
   y := 2 // Ok at compile time
   var y : Int := 1
~~~

The run-time analogue to a variable is a location. Locations are located in a frame.

The value of each declaration is the value of the initialization expression if it is present. Otherwise the value is the empty tuple.

### Commands

#### Assignment commands

An assignment

~~~
   x := E
~~~

changes the value of `x` to the value of `E`.

The right hand side can contain 2 or more expressions separated by commas.  The parser treats these as if they were a tuple.

~~~
   x := E, F, G
~~~

abbreviates

~~~
   x := (E,F,G)
~~~

Both assign `x` a value that is a triple (i.e., a 3-tuple).

The left hand side can consist of 2 or more variables separated by commas.

~~~
   x, y, z := E
~~~

In this case, the expression should evaluate to a triple --- this should be checked at run time, not compile time.  If `E` does evaluate to a triple, then the three components are assigned (from left to right) to the three variables.

There is a run-time check that variables can only be assigned values of the appropriate type.

For example:

~~~
    var x : Int := "123" // Run-time error
~~~

and 

~~~
    var y : Seq[Int] ;
    y := 123 // Run-time error
~~~

The value of each assignment command is the value of the expression on the right-hand-side.

#### If commands

~~~
   if E then B0 else B1 end if
~~~

means what you would expect.

There should be a run-time check that the expression evaluates to a boolean value (either true or false).

The value of an if command the value of the block that gets executed.

#### While commands

~~~
   while E do B end while
~~~

means what you would expect, again there is a requirement that the expression is boolean.

The value of each while command is the empty tuple.

#### Begin command

A begin command is just a block nested within another block. This allows the scope of certain variables to be limited.

The value of each begin command is the empty value of its block.

#### For command

A for command

~~~
   for x <- E do B end for
~~~

is exactly equivalent to the following code

~~~
    begin
       val it := (E).iterator() ;
       while it.notEmpty() do
          val x := it.next() ;
          begin
             B
          end
        end while
    end
~~~

where `begin` and `end` mark the start and end of internal blocks and where `it` is actually a variable name that is guaranteed not to clash with any other variable in the program. This can be accomplished by actually using a name like `#7548`. This works because the language does allow `#` in a variable name. 

The value of each for command is the empty tuple.

#### Return command

`return E` returns from a function invocation; the value of the invocation will be the value of `E`.

A return command without the expression. Is equivalent to `return ()`.

The value of return command is the empty tuple.  (However there is no way to use this value, so it actually doesn't matter what its is.)

#### Expression commands

Any simple expression may be used as a command.

The value of an expression command is the value of the expression.

### Expressions

#### Literals

Int literals like `1234` evaluate to the corresponding integer value.  Similarly for string literals, and `true` and `false`.

Set and sequence literals are evaluated by evaluating the subexpressions and then creating the corresponding set or sequence values.

####  Parenthesized expressions

* If there are 0 expressions within the parentheses, the result is the empty tuple.
* If there is 1 expression within the parentheses, expression is evaluated and the value of the  parenthesized expression is the value of that expression.
* If there are 2 or more expressions in the parentheses, these expressions are evaluated and the result is a tuple made from these expressions.

#### Function expressions

A function expression evaluates to a closure value. A closure value consists of four things.

* The code for the block.
* Information about the parameters. The number of them, their names, and their types.
* The return type.
* A reference to the frame in which the closure is evaluated. (A frame is the run-time representation of a block.)

If the result type is missing from a function expression, it can be treated as if it were Any.  For example the expressions

~~~
    fun ( a : Int, b : String ) do b + a end fun
    fun ( a : Int, b : String ) : Any do b + a end fun
~~~

Mean the same thing and can compile to the same code.

#### Lookup

A lookup looks like `E.n` where E is an expression and `n` is a name.  The lookup is resolved at run-time.  For each value, there is a set of names that can be looked up.  If `n` is in that set, the lookup succeeds and the result is some value.  Usually this value is a built in function value. For example, consider the lookup `123.binary+`. Here is what will likely happen at run time

* First, the expression `123` is evaluated o yield a value.
* Then the lookup happens. The virtual machine sends a `lookup("binary(+)")` message to that value. (The parser will turn the `binary +` construct into the string `"binary(+)"`.)
* The value sends a message to its type ---`Int`--- which includes a reference to itself.
*  The type creates a built-in function value of type `Int->Int`, which will add 123 to its argument.
*  That built-in function value is the result of the lookup expression.

Now what about `123.foobarbang`?

*  First, the expression `123` is evaluated o yield a value.
* Then the lookup happens. The virtual machine sends a `lookup("foobarbang")` message to that value. 
* The value sends a message to its type ---`Int`--- which includes a reference to itself.
*  Nothing is found, so the machine reports a run-time error.

If we had compile-time checking, then the compile-time type of `E` would allow us to report at compile-time lookups that might fail.  For those sure to succeed, we would get a type for the function returned.  E.g. the type of the expression `123.binary +` would be `Int -> Int`.

#### Application

An application expression `E F` means the value of `E` is to be applied to the value of `F`.  

Again, this requires run-time type checking.  E.g. `123(456)` would compile fine, but should result in an error at run-time since integers don't support application.

The following values support application.

* Sequences.
* Built-in function values.
* Defined function values (also known as "closures").

For example

~~~
   val s := [10,11,12] ;
   print( s 2 )
~~~

should print 12.  The value of `s` is a sequence. It is applied to 2 and the result is 12.  (Sequences are indexed from 0.)  The value of variable `print` is a built-in function. It is applied to 12. The effect is that 12 is printed to the output channel. (Changing the 2 to any value other than 0, 1, or 2 would result in a run-time error.)

Consider 

~~~
   val x := 123 ;
   print( x.binary+( 765 ) )
~~~

The result of the lookup operation `x.binary+` is a built-in function value. This is applied to 765 and the result is 888.

Consider 

~~~
   val f := fun (y:Int, z:Int) : Int
                if x.binary<( y ) then return x 
                else return y
                end if
            end fun ;
              
   print( f(123,456) )
~~~

In this case the value of `f` is a defined function value (or closure).  The argument is a pair (or 2-tuple) (123,456). 

The evaluation of the expression `f(123,456)` is as follows

* The expressions `f` and `(123,456)` are evaluated. The former evaluates to a closure value.  The later evaluates to a 2-tuple of two integers.
* The closure value contains 4 things.
    * One is a reference to frame. This is the frame in which the function value was declared.
    * The second is a description of the parameters.
    * The third is the type of the result, in this case Int.
    * The fourth is a reference to the code segment that contains the machine code for the functions block.
* Then the application operation starts.
* A new memory frame is created. A memory frame is the run-time representation of a block. This frame has two locations, one for `x` and one for `y`. The parent of this frame comes from the closure.
* The items of the argument tuple are divided up between the parameters of the function. The two locations of the new frame are initialized with 123 and 456 respectively.
* The body of the function is executed in the context of the new frame.
* On the return command being executed, the function invocation is returned from and the value of the application is the same as the value of the expression in the return command.

## Compile time errors.

The following errors should be caught by the compiler. (Only the first error in each file needs to be reported, though.)

###Use of an undeclared variable

For example:

~~~
   x + 1 ;
~~~

In this program x is never declared. If we assume that there is no global variable called "x", this is a compile-time error.

Another example:

~~~
   x := 42 // Error here.
~~~

### Declaring a variable twice in one block

For example

~~~
    val x : Int := 1 ;
    val x : String := "abc"  // Error here.
~~~

The following is NOT an error

~~~
    val x : Int := 1 ;
    if 1 = 2 then
        val x : String := "abc" ; // No error here.
        print x ; // print "abc" ;
    else
    end if ;
    print x // prints 1.
~~~

### Assignment to a "val" variable or a parameter

Both of these are compile-time errors

~~~
   val x := 1
   x := 2  // Error on this line
~~~

~~~
   fun (a : String)
       a := "abc"  // Error on this line
   end fun
~~~

## Run-time errors

This list might not be exhaustive

* Reading a location, not yet stored to.
* Storing to a location before it is declared.
* Storing to a location that represents a parameter (other than its initialization).
* Storing to a location that represents a val variable  (other than its initialization).
* Assigning (or initializing) a variable with a value not in its type.
* Assigning to multiple (2 or more) variables when the right hand side value is not a tuple or a tuple of a different length.
* Returning from a user defined function with a value not in its result type.
* Looking up an attribute that is not an attribute.
* Calling a function with an argument of the wrong type.
* Applying something that is not a function or sequence.


## Lexing

[Note for 9874: You don't need to implement any of this, but it's good to know for testing.]

Before lexing happens, Java-style unicode escapes are processed.   This lets you use characters not in ascii while keeping the file in ascii.
For example the input `val \u0061 : Int := 1` is treated as if the input were `val a : Int := 1`.  This is most useful inside string literals.  (An unfortunate side effect of this design decision is that the column numbers are thrown off in the source coordinates.   So I should really either fix the calculation of column numbers --doable but tricky--- or change this design decision.

Comments are as in C++, starting with two slashes and ending with the end of the line.  

Spaces, carriage returns, and newlines are skipped, but can be useful to separate tokens.

Identifiers (id) must start with a letter and may contain letters, digits, and underscores. Identifiers must end with a letter or digit. Reserved words, such as "if" and "fun" are not considered identifiers. Case matters, so "IF" is an identifier and "A" is a different identifier from "a".

[Currently only the 52 upper and lowercase ascii letters are allowed and only the 10 ordinary decimal digits.]

String literals (stringLiteral) are the same as in Java, so double quotes must be used and escape sequences such as \n, \r, \\, and \" may be used.

Integer literals (intLiteral) are made up of decimal digits. Underscores may be used internally, but not at the start or end.

Any nonempty sequence of characters from the set { `~`, `!`,  `@`, `#`,`$`, `%`, `^`, `&`,  `*`,`-`, `_`, `+`, `=`, `|`, `/`, `<`, `>`, `?`, `\` } is considered an other operator (otherOp), unless it is used for something else in the grammar.  So `+` is an other operator as is `/\` and `_<`.  However `->` is not, since it has other uses in the grammar.  Essentially `->` is a reserved operator, as are `<-`, `:=` and `*`.

However, // can not be used as an operator.  Nor can //+\\ or anything else starting with two slashes.  An operator can contain double slashes as long as they aren't at the start.  E.g. \\+// is an operator.

LaTeX operators (latexOp) consist of a backslash followed by one or more letters.  E.g. `\le`. These are useful for expressing operators that aren't in the ascii character set without having to leave the ascii character set.

Lexing follows the "maximal munch" rule meaning that as the input is processed from left to right, the characters are packed into tokens in a greedy fashion; i.e. as many characters are stuck together to make a token as possible before a token is produced.  For example

* `\le123` is 2 tokens (`\le` and `123`) since a LaTeX operator can not contain digits
* `abc_123` is one token --- an identifier.
* `abc _123` is three tokens (`abc`, `_` and `123`). 
* `a+-b` is three tokens (`a`, `+-`, and `b`)
* `a+ -b` is four tokens (`a`, `+`, `-`, and `b`)



