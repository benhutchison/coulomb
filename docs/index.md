# coulomb: Unit analysis for Scala

## Quick Start

### documentation
API
[documentation](https://www.javadoc.io/doc/com.manyangled/coulomb-docs_3/latest/index.html)
for `coulomb` is hosted at javadoc.io

### packages
Include `coulomb` with your Scala project:
```scala
libraryDependencies += "com.manyangled" %% "coulomb-core" % "@VERSION@"
libraryDependencies += "com.manyangled" %% "coulomb-units" % "@VERSION@"
```

### import statements
Import `coulomb` definitions:
```scala mdoc
// fundamental coulomb types and methods
import coulomb.*
import coulomb.syntax.*

// algebraic definitions
import algebra.instances.all.given
import coulomb.ops.algebra.all.given

// unit and value type policies for operations
import coulomb.policy.standard.given

// unit definitions
import coulomb.units.si.{*, given}
```

### examples
Use `coulomb` to do typelevel unit analysis in Scala!
```scala mdoc
// acceleration
val a = 9.8.withUnit[Meter / (Second ^ 2)]

// time or duration
val t = 10.withUnit[Second]

// velocity
val v = a * t

// position
val x = a * (t.pow[2]) / 2.withUnit[1]
```

Improper unit analysis is a compile-time failure.
```scala mdoc:fail
val time = 1.withUnit[Second]
val dist = 1.withUnit[Meter]

// Incompatible unit operations are a compile-time type error
val fail = time + dist
```