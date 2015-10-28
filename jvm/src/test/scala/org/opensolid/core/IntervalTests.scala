package org.opensolid.core

import org.scalacheck._
import org.scalacheck.Prop.BooleanOperators

object IntervalGenerators {
  val singletonInterval: Gen[Interval] = Arbitrary.arbitrary[Double].map(Interval(_))

  val closedInterval: Gen[Interval] = for {
    firstValue <- Arbitrary.arbitrary[Double]
    secondValue <- Arbitrary.arbitrary[Double]
  } yield Interval(firstValue.min(secondValue), firstValue.max(secondValue))

  val negativeHalfOpenInterval: Gen[Interval] =
    Arbitrary.arbitrary[Double].map(Interval(Double.NegativeInfinity, _))

  val positiveHalfOpenInterval: Gen[Interval] =
    Arbitrary.arbitrary[Double].map(Interval(_, Double.PositiveInfinity))

  implicit val arbitraryInterval: Arbitrary[Interval] = Arbitrary(Gen.frequency(
    1 -> Interval.Empty,
    1 -> Interval.Whole,
    1 -> Interval(0.0),
    1 -> Interval(1.0),
    1 -> Interval(-1.0),
    2 -> negativeHalfOpenInterval,
    2 -> positiveHalfOpenInterval,
    2 -> singletonInterval,
    10 -> closedInterval))
}

object IntervalSpec extends Properties("Interval") {
  import IntervalGenerators._

  property("equals") = Prop.forAll {(interval: Interval) => interval == interval}

  property("interpolated") = Prop.forAll(closedInterval, Gen.chooseNum(0.0, 1.0)) {
    (interval: Interval, value: Double) => {
      val interpolated = interval.interpolated(value)
      interval.contains(interpolated, 2.0 * Interval.ulp(interval))
    }
  }

  property("randomValue") = Prop.forAll(closedInterval) {
    (interval: Interval) => {
      val randomValue = interval.randomValue()
      interval.contains(randomValue, 2.0 * Interval.ulp(interval))
    }
  }

  property("bisected") = Prop.forAll {
    (interval: Interval) => {
      val (lower, upper) = interval.bisected
      if (interval.isEmpty) {
        lower.isEmpty && upper.isEmpty
      } else {
        val basicChecks = (lower.upperBound == upper.lowerBound) &&
          (lower.lowerBound == interval.lowerBound) &&
          (upper.upperBound == interval.upperBound)
        val mid = lower.upperBound
        
        if (interval.width.isZero(DefaultPrecision)) {
          basicChecks &&
            mid >= interval.lowerBound &&
            mid <= interval.upperBound
        } else {
          basicChecks &&
            mid > interval.lowerBound &&
            mid < interval.upperBound
        }
      }
    }
  }

  property("hull(Double)") = Prop.forAll {
    (interval: Interval, value: Double) => {
      val hull = interval.hull(value)

      if (interval.isEmpty) {
        hull.isSingleton && hull.lowerBound == value
      } else {
        hull.contains(interval) && hull.contains(value)
      }
    }
  }

  property("hull(Interval)") = Prop.forAll {
    (firstInterval: Interval, secondInterval: Interval) => {
      val hull = firstInterval.hull(secondInterval)
      
      if (firstInterval.isEmpty) {
        hull == secondInterval
      } else if (secondInterval.isEmpty) {
        hull == firstInterval
      } else {
        hull.contains(firstInterval) && hull.contains(secondInterval)
      }
    }
  }

  property("intersection") = Prop.forAll(
    (firstInterval: Interval, secondInterval: Interval) => {
      val intersection = firstInterval.intersection(secondInterval)

      if (firstInterval.isEmpty || secondInterval.isEmpty) {
        intersection.isEmpty: Prop
      } else {
        Prop.forAll(Gen.chooseNum(firstInterval.lowerBound, firstInterval.upperBound)) {
          firstValue => intersection.contains(firstValue) == secondInterval.contains(firstValue)
        } &&
        Prop.forAll(Gen.chooseNum(secondInterval.lowerBound, secondInterval.upperBound)) {
          secondValue => intersection.contains(secondValue) == firstInterval.contains(secondValue)
        }
      }
    }
  )
}
