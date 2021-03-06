/*                                                                      *\
** Squants                                                              **
**                                                                      **
** Scala Quantities and Units of Measure Library and DSL                **
** (c) 2013-2015, Gary Keorkunian                                       **
**                                                                      **
\*                                                                      */

package squants.experimental

/**
 * A Unit of Measure is used to define the scale of a quantity measurement
 *
 * @author  garyKeorkunian
 * @since   0.1
 *
 * @tparam A The type of Quantity being measured
 */
trait UnitOfMeasureX[A <: QuantityX[A]] extends Serializable {
  /**
   * Factory method for creating instances of a Quantity in this UnitOfMeasure
   * @param n N - the Quantity's value in terms of this UnitOfMeasure
   * @return
   */
  def apply[N](n: N)(implicit num: Numeric[N]): A

  /**
   * Extractor method for getting the Numeric value of a Quantity in this UnitOfMeasure
   * @param q A - The Quantity being matched
   * @return
   */
  def unapply(q: A) = Some(q.to(this))

  /**
   * Symbol used when representing Quantities in this UnitOfMeasure
   * @return
   */
  def symbol: String

  /**
   * Defines a signature for converting a quantity from this UOM to the Value UOM
   * @return
   */
  protected def converterFrom: Double ⇒ Double

  /**
   * Defines a signature for converting a quantity to this UOM from the Value UOM
   * @return
   */
  protected def converterTo: Double ⇒ Double

  /**
   * Applies the converterTo method to a value
   * @param n N value in terms of teh ValueUnit
   * @param num Numeric[N]
   * @tparam N Type
   * @return
   */
  final def convertTo[N](n: N)(implicit num: Numeric[N]) = converterTo(num.toDouble(n))

  /**
   * Applies the converterFrom method to a value
   *
   * @param n N value in terms of this Unit
   * @param num Numeric[N]
   * @tparam N Type
   * @return
   */
  final def convertFrom[N](n: N)(implicit num: Numeric[N]) = converterFrom(num.toDouble(n))
}

/**
 * A Unit of Measure that require a simple multiplier for converting to and from the underlying value's unit
 */
trait UnitConverterX { uom: UnitOfMeasureX[_] ⇒

  /**
   * Defines a multiplier value relative to the Quantity's [[squants.PrimaryUnit]]
   *
   * @return
   */
  protected def conversionFactor: Double

  /**
   * Implements the converterTo method as a simple quotient of the value and the multiplier
   * @return
   */
  protected def converterTo: Double ⇒ Double = value ⇒ value / conversionFactor

  /**
   * Implements the converterFrom method as a simple product of the value and the multiplier
   * @return
   */
  protected def converterFrom: Double ⇒ Double = value ⇒ value * conversionFactor
}

/**
 * Identifies the Unit of Measure used for storing the quantity's underlying value
 *
 * Each Quantity should have one and only one ValueUnit
 */
trait ValueUnitX extends UnitConverterX { uom: UnitOfMeasureX[_] ⇒

  /**
   * Implements the converterTo method to just return the underlying value
   * @return
   */
  override final def converterTo: Double ⇒ Double = value ⇒ value

  /**
   * Implements the converterFrom method to just return the underlying value
   * @return
   */
  override final def converterFrom: Double ⇒ Double = value ⇒ value

  /**
   * Value unit multiplier is always equal to 1
   */
  final val conversionFactor = 1d
}

/**
 * A marker trait identifying SI Base Units
 */
trait BaseUnitX { uom: UnitOfMeasureX[_] ⇒ }
