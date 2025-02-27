# coulomb-refined

The `coulomb-refined` package defines policies and utilities for integrating the
[refined](https://github.com/fthomas/refined#refined-simple-refinement-types-for-scala)
typelevel libraries with `coulomb`.

## Quick Start

### documentation

You can browse the `coulomb-refined` policies
[here](https://www.javadoc.io/doc/com.manyangled/coulomb-docs_3/latest/coulomb/policy/overlay/refined.html).

### packages

Include `coulomb-refined` with your Scala project:

```scala
libraryDependencies += "com.manyangled" %% "coulomb-core" % "@VERSION@"
libraryDependencies += "com.manyangled" %% "coulomb-refined" % "@VERSION@"
```

### import

To import the standard coulomb policy with the refined overlay:

```scala mdoc
// fundamental coulomb types and methods
import coulomb.*
import coulomb.syntax.*

// common refined definitions
import eu.timepit.refined.*
import eu.timepit.refined.api.*
import eu.timepit.refined.numeric.*

// algebraic definitions
import algebra.instances.all.given
import coulomb.ops.algebra.all.{*, given}

// standard policy for spire and scala types
import coulomb.policy.standard.given
import scala.language.implicitConversions

// overlay policy for refined integrations
import coulomb.policy.overlay.refined.algebraic.given

// coulomb syntax for refined integrations
import coulomb.syntax.refined.*
```

### examples

Examples in this section will use the following workaround as a replacement for
[refineMV](https://github.com/fthomas/refined/issues/932)
until it is ported forward to Scala 3.

```scala mdoc
// a workaround for refineMV not being available in scala3
// https://github.com/fthomas/refined/issues/932
object workaround:
    extension [V](v: V)
        def withRP[P](using Validate[V, P]): Refined[V, P] =
            refineV[P].unsafeFrom(v)

import workaround.*
```

The `coulomb-refined` package supports `refined` predicates that are algebraically well-behaved for applicable operations.
Primarily this means the predicates `Positive` and `NonNegative`.
For example, the positive doubles are an additive semigroup and multiplicative group,
as the following code demonstrates.

@:callout(info)
The
[table][algebraic-policy-table]
below summarizes the full list of supported `refined` predicates and associated algebras.
@:@

```scala mdoc
import coulomb.units.si.{*, given}
import coulomb.units.us.{*, given}

val pos1 = 1d.withRP[Positive].withUnit[Meter]
val pos2 = 2d.withRP[Positive].withUnit[Meter]
val pos3 = 3d.withRP[Positive].withUnit[Second]

// positive doubles are an additive semigroup
pos1 + pos2

// also a multiplicative semigroup
pos1 * pos2
pos2.pow[2]

// also a multiplicative group
pos2 / pos3
pos2.pow[0]
```

The standard `refined` function for refining values with run-time checking is `refineV`,
which returns an `Either`.
The `coulomb-refined` package supplies a similar variation `refinedVU`.
These objects are also supported by algebras.

```scala mdoc
// This refinement succeeds, and returns a Right value
val pe1 = refineVU[Positive, Meter](1)

// This refinement fails, and returns a Left value
val pe2 = refineVU[Positive, Meter](0)

// positives are an additive semigroup
pe1 + pe1

// algebras operating on Left values result in a Left
pe1 + pe2
```

## Policies

### policy overlays

The `coulomb-refined` package currently provides a single "overlay" policy.
An overlay policy is designed to work with any other policies currently in scope,
and lift them into another abstraction;
in this case, lifting policies for value type(s) `V` into `Refined[V, P]`.
The `Refined` abstraction guarantees that a value of type `V` satisfies some predicate `P`,
and the semantics of `V` remain otherwise unchanged.

For example, given any algebra in scope for a type `V` that defines addition,
the `coulomb-refined` overlay defines the corresponding `Refined[V, P]` addition
like so:
```scala
plus(x: Refined[V, P], y: Refined[V, P]): Refined[V, P] =
// (x.value + y.value) refined by P
```

@:callout(info)
Because the refined algebraic policy is an overlay,
you can use it with your choice of base policies,
for example with
[core policies](concepts.md#coulomb-policies)
or
[spire policies](coulomb-spire.md#policies).
@:@

### algebraic policy table

The following table summarizes the "algebraic" overlay policy.
Examples of Fractional value types include Double, Float, BigDecimal, spire Rational, etc.
Integral value types include Int, Long, BigInt, etc.

| Value Type | Predicate | Add Alg | Mult Alg | `+` | `*` | `/` | `pow` (exponent) |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Fractional | Positive | semigroup | group | Y | Y | Y | Y (rational) |
| Fractional | NonNegative | semigroup | semigroup | Y | Y | N | Y (pos int) |
| Integral | Positive | semigroup | semigroup | Y | Y | N | Y (pos int) |
| Integral | NonNegative | semigroup | semigroup | Y | Y | N | Y (pos int) |

@:callout(info)
The table above also applies to `Either` objects returned by `refineVU` as discussed
in the examples section above.
@:@
