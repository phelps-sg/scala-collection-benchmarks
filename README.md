Scala collection benchmarks
==============

Scala offers a very rich collections API for both mutable an immutable collections. These micro benchmarks examine the
performance for common collection types during appending and subsequent traversal of N elements

Setup
-----

1. Clone this repo
2. Navigate to the directory and run `./activator` (or `sbt` if you have sbt) to enter the console
3. Type `clean`
4. Type `jmh:run`

Code structure
----------------------------

The benchmark tests themselves are in package uk.co.tobyhobson.benchmarks, the benchmarks do very little processing
themselves and it's important to keep it this way (see below).

The actual "work" is performed by classes in package uk.co.tobyhobson.fixtures

JMH complications
--------------

I've used JMH and the [SBT-JMH](https://github.com/ktoso/sbt-jmh) plugin to run these benchmarks. JMH is a great tool but
it uses the benchmark code to generate other source code which is then compiles and run (or rather the plugin does) -
this complicates things.

For this reason it's important to keep the actual code under test outside the benchmark classes to avoid profiling generated
source code which may skew the figures

JIT complications
-------------------

The JIT compiler is VERY clever, and it will do all sorts of clever things. One challenge when writing micro benchmarks
is ensuring that the JIT compiler will not strip out our "dead" code. For example this is dead code:

```
public int doStuff() {
  int firstNumber = 10;
  int secondNumber = getSecondNumber(); // dead code (assuming no side effects)
  return firstNumber;
}
```

Whilst we would hopefully not write dead code in the real world we may do this during a profiling run simply to measure
the start and end times so we need to trick the JIT into thinking that we actually need the result of the particular operation.
See [this post](http://java-performance.info/jmh/) for more information about the challenges of micro profiling. JIT
magic is easy to spot though - If your profiling results look amazing the JIT has probably dropped your code

Results with Scala 2.13
-----------------------

~~~
[info] Benchmark                                    Mode  Cnt          Score        Error  Units
[info] AppendBenchmarks.appendToArray              thrpt   50      60748.454 ±    191.629  ops/s
[info] AppendBenchmarks.appendToArrayBuffer        thrpt   50     179135.326 ±   3195.279  ops/s
[info] AppendBenchmarks.appendToArrayBuilder       thrpt   50     249371.425 ±   1152.550  ops/s
[info] AppendBenchmarks.appendToArrayUsingView     thrpt   50      77005.826 ±    298.027  ops/s
[info] AppendBenchmarks.appendToImmutableList      thrpt   50        406.710 ±      0.684  ops/s
[info] AppendBenchmarks.appendToListBuffer         thrpt   50     140080.485 ±    496.790  ops/s
[info] AppendBenchmarks.appendToMutableQueue       thrpt   50     190109.014 ±   3212.148  ops/s
[info] AppendBenchmarks.appendToQueue              thrpt   50     104061.204 ±    332.211  ops/s
[info] AppendBenchmarks.appendToVector             thrpt   50      65754.282 ±    341.401  ops/s
[info] AppendBenchmarks.prependToArrayBuffer       thrpt   50      18890.325 ±     39.502  ops/s
[info] AppendBenchmarks.prependToImmutableList     thrpt   50     107858.982 ±    440.788  ops/s
[info] AppendBenchmarks.prependToListBuffer        thrpt   50      64593.775 ±    237.541  ops/s
[info] AppendBenchmarks.prependToMutableQueue      thrpt   50     127299.984 ±    511.963  ops/s
[info] AppendBenchmarks.prependToQueue             thrpt   50      49250.590 ±    251.216  ops/s
[info] AppendBenchmarks.prependToVector            thrpt   50      59239.229 ±   3066.640  ops/s
[info] LinearAccessBenchmarks.iterateArray         thrpt   50     234774.225 ±    678.668  ops/s
[info] LinearAccessBenchmarks.iterateArrayBuffer   thrpt   50     992184.833 ±   4935.051  ops/s
[info] LinearAccessBenchmarks.iterateArrayBuilder  thrpt   50     356895.533 ±   2318.851  ops/s
[info] LinearAccessBenchmarks.iterateList          thrpt   50     496485.761 ±   9340.274  ops/s
[info] LinearAccessBenchmarks.iterateListBuffer    thrpt   50     281767.500 ±   6014.532  ops/s
[info] LinearAccessBenchmarks.iterateMutableQueue  thrpt   50     852726.262 ±  97599.451  ops/s
[info] LinearAccessBenchmarks.iterateQueue         thrpt   50     214332.149 ±   1842.947  ops/s
[info] LinearAccessBenchmarks.iterateVector        thrpt   50    1462368.176 ±  82910.925  ops/s
[info] RandomAccessBenchmarks.accessArrayBuffer    thrpt   50  114411085.895 ± 305936.085  ops/s
[info] RandomAccessBenchmarks.accessList           thrpt   50     466454.915 ±    689.573  ops/s
[info] RandomAccessBenchmarks.accessListBuffer     thrpt   50    1599624.072 ±   4022.776  ops/s
[info] RandomAccessBenchmarks.accessMutableQueue   thrpt   50  104386784.596 ± 146811.892  ops/s
[info] RandomAccessBenchmarks.accessQueue          thrpt   50     513446.301 ±    402.624  ops/s
[info] RandomAccessBenchmarks.accessVector         thrpt   50   95962852.554 ± 166793.396  ops/s
[success] Total time: 29069 s (08:04:29), completed 5 Jun 2022, 18:32:16
~~~
