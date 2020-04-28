# PSAM

PSAM is the Pseu abstract machine. It is a virtual machine designed to execute Pseu programs.


## The memory structure of the virtual machine

The machine has 4 kinds of memory.

* There is a stack of values, types, and locations called the *value stack* (*v-stack* for short).
* The *return stack*, called the *r-stack* for short.
* A list of code segments called the *code store*
* There is a collection of *frames*. Frames are described below.

A frame is a run-time representation of a block. Each frame on the frame stack has an optional reference to another frame (called the frame's parent) and also contains list of  *locations*.

A *location*  is the run-time representation of a variable. Each location has four parts: The run-time representation of a type. A boolean that indicates whether or not the variable is writable, a boolean that indicates whether or not the variable is readable, the current value of the location.

Each item on the v-stack is either a (reference to a) value or one of a few other value-like things: (references to) locations and (references to) types.

Each code segment in the code store is an array of *instructions*. 

Two registers represent the next instruction to be executed. These are the *segment counter* and the *program counter*. The segment counter indicates which segment is currently being executed and the program counter indicates the address of the next instruction to be executed in that segment. Both these registers are initially 0.

There is a *frame pointer* register that contains a reference to a frame.

There is a register *running*, which is a boolean, initially true.

Each item of the return stack is a record containing.

* A segment number.
* An instruction number.
* A number representing the height of the v-stack.
* A reference to a frame.
* A boolean value for the running register.
* A result type, for dynamic checking of function results.

## Fetch, execute cycle

The basic algorithm of the machine is that the machine looks up the next instruction using the segment counter and the program counter, executes that expression, and then starts again. The cycle ends as soon as the *running* register is false or there is a run-time error.

## Initial state

A *program* is a list of segments.

After loading a program the initial state of the machine is this: 

* The v-stack is empty.
* The frame pointer points to the global frame. This is the frame for built in identifiers such as "print".
* The code store contains the loaded program.
* The r-stack contains one record in which the value for the running flag is false, the result type is "Any", and all numbers are 0.
* The segment counter is 0 and the program counter is 0.


## The frame chain

Starting with the frame pointer and then following parent links, we find that there is a chain (or stack) of frames.  Consider the following code. The top of this stack is the frame that the machines frame pointer points to.

~~~
   var x : Bool := true
   val y : String := "hello"
   if x then         // 1
         var v : Int := 6
         var w : Int
         print x    
         if x then // 2
             var t : String 
             var u : String
             print y  //3
         else
         end if
   end if
~~~

When execution reaches point 1, the top frame on the frame stack has locations representing `x` and `y`.  When execution reaches point 2,  the top of frame stack has locations representing `v` and `w`.  The frame representing `x` and `y` will be second from the top.  At point 3, the top frame has locations representing `t` and `u`; the frame with the locations representing `x` and `y` is now 3rd from the top.

In the machine code, variable references are given by two numbers: the depth of the frame and the position within the frame.  For example

* the reference to `x` at point 1 is represented by (0,0). The depth is 0 since the frame is on top of the stack. Then the location is 0, since x is declared first in the block.
* The reference to `x` at point 2 is represented by (1,0), since the frame is now 1 from the top of the frame stack.
* The reference to `y` on line 3 is (2,1) since the frame is 2 from the top of the frame stack and because `y` is the second variable defined in the block.

At this point (line 3) the frame stack looks like this:

The frame pointer points to

|   | name | value | type | readable | writable |
|---|----------|----------|-------------|----------------|-----------|
| 0|  "t"   |  ??    |  String |   false     | true |
| 1|  "u"   |   ??  |  String |   false     | true |

That frame's parent is

|   | name | value | type | readable | writable |
|---|----------|----------|-----------|----------------|-------------|
| 0|  "v"   |  6     |  Int    |   true      | true |
| 1|  "w"   |   ??  |  Int    |   false     | true |

That frame's parent is

|   | name | value | type | readable | writable |
|---|----------|----------|-----------|----------------|-----------------|
| 0|  "x"   |  true|  Bool  |   true      | true |
| 1|  "y"   |  "hello"|  String |   true | false |

The parent of this frame is the global frame.

Of course chasing pointers is potentially slow, since blocks might be nested deeply.

There is an optimization to allow location lookups to happen in O(1) time.  Instead of a parent pointer, each frame has an array of ancestors. Ancestor 0 is the frame itself; ancestor 1 is the parent frame; ancestor 2 is that frames parent and so on.  The frame pointer does not point to the current frame, but rather to its ancestor array. Now to look up location (2,1) the machine:

*  follows the frame pointer to find the current frame's ancestor array;
*  gets item 2 out of that array, which points to the frame's grandparent;
*  gets location 1 of that frame.


## Instructions

Most instructions cause the program counter to increase by 1.  I'll only mention the exceptions.

Each instruction is associated with a source code coordinate. This is useful for debugging the compiler, for run time error reporting, and possibly other uses in the future.

### Value making instructions

~~~
   pushBool( boolean b )
   pushInt( String str )
   pushString( String str )
~~~

pushes the appropriate boolean value, integer, or string value on to the v-stack.

It is a precondition of `pushInt`, that the string contains only ascii digits.

~~~
   makeTuple( int count )
   makeSet( int count )
   makeSeq( int count )
~~~

Each pops `count` items off the v-stack. These must be values. It constructs a tuple, set, or sequence and pushes the result onto the v-stack. The order of the items in the tuple of sequence is from bottom to top. I.e., when count > 0, the last item of the sequence will be the former top of the stack.

It is a precondition of all that count is not negative. Furthermore, for `makeTuple`, count will not be 1.

~~~
   makeClosure(int count)
~~~

pops 2*count + 2 items off the stack. These must be (from bottom to top).
	
* A string representing the name of parameter 0
* A type representing the type of parameter 0.
* A string representing the name of parameter 1.
* A type representing the type of parameter 1.
* ....
* A string representing the name of the parameter count-1.
* A type representing the type of parameter count-1.
* A type representing the result type of the 
* An integer representing the code segment in which the functions block may be found.

### Location manipulation instructions
~~~
   pushLocation(int depth, int number)
~~~

pushes a reference to a location onto the v-stack. The location is found in a frame, so first the machine finds the frame.  If `depth` is 0, the frame is the one on the top of the frame stack. If `depth` is 1, it is the parent of that frame. If `depth` is 2, it is the parent of the parent of the frame at the top of the f-stack.

(Note that`depth` indicates the number of parent links to follow on the chain of frames that starts with the top of the frame stack.  All frames on the f-stack other than the top frame are ignored.)

Once a frame is found, the `number` indicates which location within that frame should be pushed.

When the location is found, a reference to the location is pushed onto the v-stack.

~~~
   fetch()
~~~

fetches a value from a location.  The top of the stack is popped off. It must be a reference to a location. If that location is not readable, it is a run-time error.  Otherwise the value of the location is pushed onto the v-stack.

~~~ 
   store( int n )
~~~

stores to one or more locations.  When `n ` is one, there the v-stack should look like this (from bottom to top)

~~~
   ... loc, val
~~~

These two items are popped.  If the location is not writable, it is a run-time error. If the value is not a member of the location's type, it is a run-time error.  Otherwise the location's value is updated with the value and the location's `readable` flag is set to true.  Finally the value is pushed back onto the stack:

~~~
   ... val
~~~

If `n` is more than 1, the v-stack should look like this 

~~~
   ... loc_0, loc_1, ... loc_n-1, val
~~~

All these items are popped off the v-stack.  In this case, the value must be an n-tuple.  If it is, then the locations are updated as above.

Precondition: n must be 1 or more.

~~~
   lockLocation()
   unlockLocation()
~~~

The top of the stack should be a location. This location is popped and its writable flag is set to false or true, depending on the instruction.

These instructions are useful for initializing `val` and `var` variables. First the variable is unlocked, then is initialized. Finally, for `val` variables, it is locked.

### Stack instruction

~~~
   pop(int n)
~~~

Precondition: n must be 0 or more.

The instruction simply pops `n` items off the v-stack.

Runtime error if there are fewer than n things on the v-stack.

~~~
   duplicate()
~~~

Duplicate the item on the top of the v-stack.

[Note:  I don't think dup will be needed for compiling Pseu. It is included for completeness.]

Runtime error if the v-stack is empty.
~~~
   rotateUp(int n)
   rotateDown(int n)
~~~

Precondition: n must be 1 or more.

Runtime error if the v-stack size is less than n .

rotateUp moves the item on the top of the v-stack to position n-1 from the top, pushing n-1 items up.  So rotateUp(1) should have no effect. rotateUp(2) swaps the top two items of the stack. rotateUp(6) changes a stack

~~~
   ... u v w x y z
~~~

to

~~~
   ... z u v w x y 
~~~

rotateDown( n ) does is the inverse operation to rotateUp( n ).

[Note:  I don't think either rotateUp or rotateDown will be needed for compiling Pseu. They are included for completeness.]

### Lookup instruction

~~~
   lookup( )
~~~

The v-stack should look like this 

~~~
   ... val0 val1
~~~

the 2 items are popped from the stack. `val1` should be a string. If it is not, it is an error.   Every value implements a `lookup` method which maps strings to values.  The string represented by `val1` is sent to the `lookup` function of `val0`.  If that lookup fails, it is a run time error. If the lookup succeeds, then the value resulting from the lookup is pushed onto the v-stack.

Typically the value simply passes the lookup operation on to a type. For example, the lookup operation for integer values simply passes the lookup request to the "Int" type, but the value adds itself to the message.

A list of lookups is described in section: [Predefined Values].

### Apply and return instructions

~~~
   apply()
~~~

The v-stack should look like this

~~~
   ... val0 val1
~~~

the 2 items are popped from the stack. 

The apply instruction applies the function represented by `val0` to the argument represented by `val1`.

If `val0` is an integer value, string value, boolean value, tuple value, or set value there is a run-time error.

If `val0` is a sequence value, it is an error unless `val1` is an integer greater or equal to 0 and less than the length of the sequence.  Then the appropriate item of the sequence is pushed onto the stack.

If `val0` is a built-in function value either an error is reported or the built-in function computes a value and pushes that value onto the stack.

The most complicated case is when `val0` is a closure value.

A closure consists of a description of a sequence of 0 or more (descriptions of) parameters, a reference to a frame, a result type, and the number of a code segment.

The apply method for the closure acts on the virtual machine as follows.

1. A new return record is created. In this record, the machine stores
    * The current segment number.
    * The current value of the program counter + 1.
    * The height of the v-stack.
    * The current value of the frame pointer.
    * The return type from the closure
2. This record is pushed onto the r-stack.
3. A new frame is made based on the parameter information of the closure. The frame consists of one location per parameter. The parent of this frame will be the frame referenced in the closure. 
4. The `val1` value is used to initialize the parameters and then all parameters are set to be readable, but not writeable.  Initialization is similar to assignment, so it is a run time error if there are 0 parameters and the `val1` is not an empty tuple.  And it is an error if there is more than 1 parameter and `val` is not a tuple of the same size.
5. The parent of the new frame is the frame reference found in the closure.
6. The new frame is pushed onto the f-stack.
7. The program counter is set to 0.
8. The segment counter is set to the segment in the closure.

That ends the apply operation for closures. The next instruction to be executed will be the first instruction of the function.

### Return

~~~
   returnNow( )
~~~

The top of the stack should contain a value; call it `val`. That value is popped from the stack. 

The r-stack should also not be empty. The top record on the r-stack is popped off. Call it `r`.

It is a run-time error if `val` is not a member of the result type stored in `r`.

The state is restored from `r`:

1. The size of the v-stack is cut down to the size recorded in `r`.
2. `val` is pushed onto the v-stack.
3. The registers are restored from `r`.

### Jumps

~~~
   jump( int i )
~~~

Instead of adding one to the program counter, `i` is added to the program counter.

~~~
   jumpOnFalse( int i )
   jumpOnTrue( int i )
~~~

The top of the v-stack should be a boolean value. If not, there is a run-time type error.

The v-stack is popped. Depending on the value, either `1` or `i` is added to the program counter.

### Frame instructions

~~~
   newFrame(int count)
~~~

The top of the stack should contain `2*n` values.  Like this, from bottom to top:

* A string value representing the name of variable 0
* A type representing the type of parameter 0.
* A string value representing the name of variable 1.
* A type representing the type of variable 1.
* ....
* A string value representing the name of the variable count-1.
* A type representing the type of variable count-1.

All these items are popped off the stack and a new frame is made. The frame will contain `count` locations.  The names and types of the locations come from the information on the stack. Both the readable and writable flags are set to false. The initial value of the locations does not matter since they will not be until the first assignment to the variable.

The parent of the new frame will be the current value of the frame pointer. The new frame becomes the new value of the frame pointer.

~~~
   popFrame()
~~~

The new value of the frame pointer will be the parent of the frame the frame pointer points to.

It is a run-time error to pop a frame with no parent. (The only frame that will have no parent will be the global frame.)


### Type making instructions

At run-time, types are treated as objects that we can put on the v-stack.  The set of types available is not extensible.

The instruction

~~~
   constructType(String name, int n)
~~~

expects the top `n` items on the v-stack to be types:

~~~
  ty_0, ty1, ..., ty_n-1
~~~

All these values are popped. The machine attempts to make a type and, if successful pushes it onto the v-stack.

The following argument combinations should succeed if the top `n` items on the v-stack are types.

|  name | n |
|----|----|
| Unit | 0 |
| Bool | 0 |
| Int | 0 |
| String | 0 |
| Any | 0 |
| None | 0 |
| Seq | 1 |
| Set | 1 |
| Fun | 2 |
| Product | any number > 1 |

For example the type `Seq[Int] * Bool -> Unit` can be constructed by 

~~~
      constructType( "Int", 0 )
      constructType( "Seq", 1 )
      constructType( "Product", 2 )
      constructType( "Bool", 0 )
      constructType( "Fun", 2 )
~~~   

## Predefined Values

In the global frame there are 3 predefined val variables.  

|  name | address in frame | type | meaning |
|----|----|-----|---|
| print | 0 | Fun[Any, Unit] | Print a representation of the value
| readInt | 1 | Fun[Unit, Int] | Read an integer from the input
| readString | 2 | Fun[Unit, String] | Read a line from the input

For integer values there are the following attributes

|  name | type | meaning |
|----|----|-----|
| binary(=) | Fun[Any, Bool] | equality
| binary(!=) | Fun[Any, Bool] | inequality
| binary(/=) | Fun[Any, Bool] | inequality
| binary(+) | Fun[Int, Int] | addition
| binary(-) | Fun[Int, Int] | subtraction
| binary(*) | Fun[Int, Int] | multiplication
| binary(/) | Fun[Int, Int] | integer division
| binary(div) | Fun[Int, Int] | same as binary(/)
| binary(mod) | Fun[Int, Int] | remainder
| binary(<) | Fun[Int, Bool] | less than
| binary(>) | Fun[Int, Bool] | greater than
| binary(_<) | Fun[Int, Bool] | less than or equal to
| binary(<=) | Fun[Int, Bool] | less than or equal to
| binary(\\le) | Fun[Int, Bool] | less than or equal to
| binary(>_) | Fun[Int, Bool] | greater than or equal to
| binary(>=) | Fun[Int, Bool] | greater than or equal to
| binary(\\ge) | Fun[Int, Bool] | greater than or equal to
| unary(-) | Fun[Unit, Int] | negation 

For boolean values there are the following attributes

|  name | type | meaning |
|----|----|-----|
| binary(=) | Fun[Any, Bool] | equality
| binary(!=) | Fun[Any, Bool] | inequality
| binary(/=) | Fun[Any, Bool] | inequality
| binary(implies) | Fun[Bool, Bool] | implication
| binary(==>) | Fun[Bool, Bool] | implication
| binary(or) | Fun[Bool, Bool] | disjunction (or)
| binary(and) | Fun[Bool, Bool] | conjunction
| binary(not) | Fun[Unit, Bool] | negation

For sequences there are the following attributes

| name | type | meaning |
|----|----|-----|
| length | Int | length of the sequence |

For sets there are the following attributes

| name | type | meaning |
|----|----|-----|
| size | Int | length of the sequence |

## Examples

###An empty program

An empty block should have a value of (), so the code for a completely empty program would be either.

~~~
Segment  0:
     newFrame(0)
     makeTuple(0)
     popFrame()
     returnNow() 
~~~

Or simply

~~~
Segment  0:
     makeTuple(0)
     returnNow() 
~~~

### Applying built in functions

Consider the program

~~~
   print "Hello world!" ;
   2 + 1 ;
~~~

This is a block. Inside the block is are two expression commands. The first applies a built in function. The second applies a property of a number.  (Note that the parser will treat "2+1" as if it was written "2.binary(+) ( 1 )".)

~~~
Segment  0:
    newFrame(0)
    // New frame with 0 variables 
    // Now the frame pointer point to a frame
    // containing 0 locations. Its parent is the
    // global frame
    
    pushLocation(1, 0)
    // since 0 is the position of the "print" variable in the global frame.
    // v-stack:  The location of "print"
    fetch()
    // v-stack: The value in that location
    // This value is a built-in function value
    pushString( "Hello world!" )
    // v-stack: The value of location (1,n), "Hello world!"
    apply( )
    // v-stack: the empty tuple
    // Because the value returned by the built in is that tuple.
    
    
    // The value of the first command is not needed, so we should discard it.
    pop( 1 )
    
    // v-stack is empty
    pushInt( 2 ) ;
    // v-stack: 2
    pushString( "binary(+)" )
    // v-stack: 2, "binary(+)" 
    lookup()
    // v-stack: The value of the "binary(+)" property for the number 2
    pushInt(1)
    // v-stack: The value of the "binary(+)" property for the number 2, 1
    apply()
    // v-stack: 3
    popFrame() // Because it's the end of the block
    returnNow() 
~~~

An optimization is to omit the first and last instructions. Then the pushLocation instruction would have parameters 0 and n.  This optimization is possible whenever a block contains no variable declarations.

### Variable declarations

Here is a program with some variables

~~~
   var x : Int := 100 ;
   val y := x + 1 ;
   var z : Int ;
   z := y + 1 ;
   print z
~~~

The machine code could look like this

~~~
Segment  0:
   // The next 5 instructions represent the start of
   // the block.
   pushString( "x" )
   constructType("Int", 0)
   pushString( "y" )
   constructType("Any", 0)
   pushString( "z" )
   constructType("Int", 0)
   // v-stack: "x", Int, "y", Any, "z", Int
   newFrame( 3 ) ; 
   // v-stack: empty
   // Now the frame pointer point to a frame
   // containing 3 locations. Its parent is the
   // global frame.
   // All 3 locations are not writable and not readable
   
   // var x : Int := 100
   // The initialization of x
   pushLocation( 0, 0 )  // prepare to assign to x
      pushInt( 100 ) 
   // v-stack: a location, 100
   pushLocation( 0, 0 ) 
   // v-stack: a location, 100, the location again
   unlockLocation() // Now location 0 is writable, but not readable.
   // v-stack: a location, 100
   store( 1 ) 
   // v-stack: 100
   // After the store, the location is both readable and writable.
   
   // Since this is not the last command or declaration in the block, the value is not needed.
   pop(1)
   // v-stack: empty
   
   // val y := x + 1
  
   // The initialization of y
   pushLocation( 0, 1 ) // prepare to assign to y
   // v-stack: location
   // The expression x + 1
      pushLocation( 0, 0 ) // x
      fetch()
      // v-stack: location, 100
      pushString( "binary(+)" )   
      // v-stack: location, 100, "binary(+)"
      lookup()
      // v-stack: location, a built-in function
      pushInt( 1 )
      // v-stack: location, a built-in function, 1
      apply()
      // v-stack: location, 101
   pushLocation( 0, 1 ) 
   unlockLocation() // Now location 1 is writable, but not readable.
   store( 1 ) 
   // Now it is both readable and writable.
   // v-stack: 101   
   pushLocation( 0, 1 ) 
   lockLocation()
   // Now it is readable but not writable.
   // v-stack: 101
   
   // Since this is not the last command or declaration in the block, the value is not needed.
   // pop(1)
   // v-stack: empty
   
   // var z : Int
   pushLocation( 0, 2 ) 
   unlockLocation() // Now location 2 is writable, but not readable.
   makeTuple( 0 ) 
   // v-stack: the empty tuple
   
   pop(1)
   
   // x := y + 1 ;
   pushLocation(0, 0)  // x
   // v-stack: location of x
      pushLocation(0, 1)  // y
      // v-stack: location of x, location of y
      fetch( )
      // v-stack: location of x, 101
      pushString( "binary(+)" )
      // v-stack: location of x, 101, "binary(+)"
      lookup()
      // v-stack: location of x, 101, a function
      pushInt( "1" ) 
      apply()  // Add
   // v-stack: location of x, 102
   store()   // Store into x
   // v-stack: 102
   
   pop(1)
   
   // print z
   pushLocation(1, n)
   // where n is is the address of "print"
   fetch()
   pushLocation(0, 2)
   fetch()
   apply()
   
   // The end of the block
   popFrame()
   returnNow() 
~~~

The sequence 

~~~
   makeTuple(0)
   pop(1)
~~~

could be optimized out.  It might simplify writing the code generator, though, if every command and declaration leaves a value on the stack and the code generation routine for blocks emits a pop instruction after compiling each block item except the last.  That's why I did the example this way.

### Defined function.

Consider this function definition and application

~~~
   fun square( x : Int ) : Int do
       x * x 
   end fun
   print( square(42) ) 
~~~

Possible Machine code is as follows:  The block that makes up the body of the function is put in a separate code segment.  In this case, I'll assume it is segment 1.

~~~
Segment  0:
   pushString( "square" )
   constructType( "Any", 0 )
   newFrame( 1 ) 
   
   // The initialization of square
   pushLocation(0, 0)
      // Make a closure
      pushString( "x" )        // Name of parameter
      constructType( "Int", 0) // Type of parameter
      constructType( "Int", 0) // Type of result
      pushInt( 1 )             // The segment of the code
      makeClosure( 1 ) // There is one parameter
   pushLocation(0, 0)
   unlockLocation( )
   store( 1) // Do the initialization
   pushLocation(0, 0)
   lockLocation( )
   
   pop( 1 )
   
   // print( square(42) ) 
   pushLocation( 1, 0 ) // Since 0 is the position of print in the global frame
   fetch() 
   pushLocation( 0, 0 ) // The location of square
   fetch()
   pushInt( "42" )
   apply()
   apply() 
   popFrame()
   returnNow() 
 
 
Segment  1:
   // Segment 1 represents the body of the function
   newFrame( 0 )
   pushLocation( 1, 0 ) // The location of x
   fetch()
   pushString( "binary(*)" ) 
   lookUp( )
   pushLocation( 1, 0 ) // The location of x
   fetch()
   apply() 
   popFrame()
   returnNow() 
~~~

In the above code, I put in a frame for the body of the function, even though there are no variables declared in it. An optimization is to omit the `newFrame` and `popFrame` instructions. In that case the top frame would represent the parameters and the location of `x` would be represented by (0,0) instead of (1,0).

### A recursive function

~~~
   fun fib( n : Int ) : Int do
       if n > 2 then 1
       else fib(n-1) + fib(n-2)
   end fun
   fib( 42 ) 
~~~

The code for the body of the function could be this.

~~~
 Segment 1:
      // if n > 2
        pushLocation( 0, 0 )  // Location of n
        fetch()               // Fetch n
        pushString( "binary(>)" )
        lookup()
        pushInt( 2 )
        apply()
        jumpOnFalse( 3 ) 
     // Then
         pushInt( "1" ) 
     // Else
     jump( 22 )
         // fib (n-1)
         pushLocation( 1, 0 ) // Location of fib
         fetch()
            // n-1
            pushLocation( 0, 0 )  // Location of n
            fetch()               // Fetch n
            pushString( "binary(-)" )
            lookup()
            pushInt( 1 )
            apply()
         apply()
         
         pushString( "binary(+)" )
         lookup()
         
         // fib (n-2)
         pushLocation( 1, 0 ) // Location of fib
         fetch()
            // n-1
            pushLocation( 0, 0 )  // Location of n
            fetch()               // Fetch n
            pushString( "binary(-)" )
            lookup()
            pushInt( 2 )
            apply()
         apply()
         
         apply() // Do the addition
      // end if
      returnNow()      
~~~

This time, I didn't make a frame for the body of the function. Nor for the blocks representing the two tines of the if command. This is possible because there are no variables declared in any of these blocks.  For this reason the parameter `n` is at location `(0,0)` and `fib` is at location `(1,0)` meaning that it is the first variable in the frame representing the block that contains the function.

If there were new frames made for each of these three blocks, the first fetch of `n` would use location `(1,0)`, the other fetches of `n` being one block deeper, would use location `(2,0)`.  The fetches of `fib` would use location `(3,0)`.  Like this

~~~
Segment 1:
      newFrame( 0 ) 
      // if n > 2
        pushLocation( 1, 0 )  // Location of n
        fetch()               // Fetch n.
     ...
     jump( 24 )
         newFrame()
         pushLocation( 3, 0 ) // Location of fib
         fetch()
            // n-1
            pushLocation( 2, 0 )  // Location of n
            fetch()               // Fetch n
            ...
         
         // fib (n-2)
         pushLocation( 3, 0 ) // Location of fib
         fetch()
            // n-1
            pushLocation( 2, 0 )  // Location of n
            fetch()               // Fetch n
         ...
         popFrame()
      // end if
      popFrame() 
      returnNow()
         
~~~

### A function returning a function

Consider this function that returns a function as its value

~~~
    fun twice( f : Any -> Any ) : Any -> Any do
       fun ( x : Any ) : Any do f(f(x)) end fun
    end fun ;
    twice( 3.binary *)( 7 )
~~~

This is a rather strange way of calculating 3 * 3 * 7.  Here `3.binary *` is a built-in function that multiplies its argument by 3.  Then `twice( 3.binary *)` is a function that multiplies its argument by 9.  And `twice( 3.binary *)( 7 )` computes 9*7.

We'll make no frames for blocks with no variable declarations.

The main segment represents a block with a declaration of `twice` and then an expression command.

~~~
    pushString( "twice" )
    constructType( "Any", 0 ) 
    newFrame( 1 ) 
    
    // val fun := ... 
    pushLocation( 0, 0 ) // Location of twice
    
        // Construct a closure
           // Name of parameter
              pushString( "f" )  
           // Type of parameter
              constructType( "Any", 0 )
              constructType( "Any", 0 )
              constructType( "Fun", 2 )
           // Type of result
              constructType( "Any", 0 )
              constructType( "Any", 0 )
              constructType( "Fun", 2 )
           // Code in segment 1
              pushInt( 1 )
           makeClosure( 1 ) // One parameter
    pushLocation( 0, 0 ) // Location of twice
    unlockLocation() 
    store( 1 ) 
    pushLocation( 0, 0 ) // Location of twice
    lockLocation() 
    
    pop( 1 )
    
    // twice( 3.binary* ) ( 7 )
    pushLocation( 0, 0 ) // Location of twice
    fetch()
    pushInt( 3 )
    pushString( "binary(*)" )
    lookup()
    apply()
    pushInt( 7 )
    apply
    
    popFrame()
    returnNow()
~~~

The segment for the `twice` function makes a closure and returns. It would look like this. 

~~~
Segment 1:
    pushString( "x" )
    constructType( "Any", 0 )  // Type of x
    constructType( "Any", 0 ) //   Result type
    pushInt( 2 ) // Code in segment 2
    makeClosure( 1 ) // 1 parameter
    returnNow()
~~~

The segment for the inner function's body simply computes `f( f x )`. When it executes, `x` will be in the parameter frame, while `f` will be in the frame outside of that.

~~~
Segment 2:
    // f( f x ) )
    pushLocation( 1, 0 )  // Location of f
    fetch()
    pushLocation( 1, 0 ) // Location of f
    fetch()
    pushLocation( 0, 0 ) // Location of x
    apply()
    apply()
    returnNow( )
~~~

### Another function that returns a function

Consider this pseu program.

```
fun f( x : Int ) do 
    return fun( y : Int ) : Int do x+y end
end ;
val g : Int -> Int := f 2 ;
val h : Int -> Int := f 3;
(g 5, h 5)
```

The machine code could be

Segment 0

```
// Make a frame with 3 variables, f, g, h.
pushString("f")
constructType("Int", 0)
constructType("Any", 0)
constructType("Fun", 2)
pushString("g")
constructType("Int", 0)
constructType("Int", 0)
constructType("Fun", 2)
pushString("h")
constructType("Int", 0)
constructType("Int", 0)
constructType("Fun", 2)
newFrame( 3)

// fun f( x : Int ) do 
//     return fun( y : Int ) : Int do x+y end
// end
pushLocation( 0, 0)
								

pushString("x")
constructType("Int", 0)
constructType("Int", 0)
pushInt("1")
makeClosure(1)
pushLocation( 0, 0)
unlockLocation()
store(1)
								
pushLocation( 0, 0)
lockLocation()
								
pop(1	)
								
// val g : Int->Int := f(2) ;
pushLocation( 0, 1)	

pushLocation( 0, 0)
fetch()
pushInt( "2")
apply()
								
pushLocation( 0, 1)
unlockLocation()
store(1)
								
pushLocation( 0, 1)
lockLocation()
								
pop(1	)
								
// val h : Int->Int := f(3) ;
pushLocation( 0, 2)		

pushLocation( 0, 0)
fetch()
pushInt( "3")
apply()
								
pushLocation( 0, 2)
unlockLocation()
store(1)
								
pushLocation( 0, 2)
lockLocation()
								
pop(1	)
								
// (g(5), h(5))
pushLocation( 0, 1)
fetch()
pushInt( "5")
apply()
								
pushLocation( 0, 2)
fetch()
pushInt( "5")
apply()
								
makeTuple(2)
				
popFrame()  // Not needed, but the compiler might make it
returnNow()
```

Segment 1:

```
// The body of function f
// return fun( y : Int ) : Int do x+y end
newFrame(0)
								
pushString("y")
constructType("Int", 0)
constructType("Int", 0)
pushInt("2")
makeClosure(1)
returnNow()
popFrame()
```

Segment 2: 

```
// The body of the anonymous function
//  x+y
newFrame(0)
								
pushLocation( 3, 0)  // x
fetch()
pushString("binary(+)")
lookup()
pushLocation(1, 0)  / y
fetch()
apply()
								
returnNow()
popFrame()
```