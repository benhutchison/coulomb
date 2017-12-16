/*
Copyright 2017 Erik Erlandson

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import spire.math._

package object coulomb {
  /**
   * An "infix" type alias for [[Quantity]]
   * @tparam N The numeric representation type of the quantity value
   * @tparam U The unit type of the quantity
   * {{{
   * import com.manyangled.coulomb._
   * import SIBaseUnits._
   * def f(v: Double WithUnit (Meter %/ Second)) = v * 60D.withUnit[Second]
   * }}}
   */
  type WithUnit[N, U] = Quantity[N, U]

  /**
   * An "infix" type alias for [[Temperature]]
   * @tparam N The numeric representation type of the temperature value
   * @tparam U The unit type of the temperature
   * {{{
   * import com.manyangled.coulomb._
   * import SIBaseUnits._
   * def f(t: Double WithTemperature Kelvin) = t + 100D.withUnit[Kelvin]
   * }}}
   */
  type WithTemperature[N, U] = Temperature[N, U]

  /** enhances numeric types with utility methods for `coulomb` */
  implicit class CoulombExtendWithUnits[N](val v: N) extends AnyVal with Serializable {
    /** create a new unit Quantity of type U with the value of `this` */
    def withUnit[U]: Quantity[N, U] = new Quantity[N, U](v)

    /** create a new unit Temperature of type U with numeric value of `this` */
    def withTemperature[U](implicit t2k: DerivedTemp[U]): Temperature[N, U] =
      new Temperature[N, U](v)
  }

  def coefficient[U1, U2](implicit cu: ConvertableUnits[U1, U2]): Rational = cu.coef
}