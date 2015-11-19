////////////////////////////////////////////////////////////////////////////////
//                                                                            //
//  OpenSolid is a generic library for the representation and manipulation    //
//  of geometric objects such as points, curves, surfaces, and volumes.       //
//                                                                            //
//  Copyright 2007-2015 by Ian Mackenzie                                      //
//  ian.e.mackenzie@gmail.com                                                 //
//                                                                            //
//  This Source Code Form is subject to the terms of the Mozilla Public       //
//  License, v. 2.0. If a copy of the MPL was not distributed with this file, //
//  you can obtain one at http://mozilla.org/MPL/2.0/.                        //
//                                                                            //
////////////////////////////////////////////////////////////////////////////////

package org.opensolid

import scala.math

package object core {
  implicit class Scalar(val value: Double) extends AnyVal {
    def isZero(tolerance: Double): Boolean = value >= -tolerance && value <= tolerance

    def isNotZero(tolerance: Double): Boolean = value < -tolerance || value > tolerance

    def meters = value

    def inMeters = value

    def centimeters = value * 1e-2

    def inCentimeters = value * 1e2

    def millimeters = value * 1e-3

    def inMillimeters = value * 1e3

    def microns = value * 1e-6

    def inMicrons = value * 1e6

    def inches = value * 0.0254

    def inInches = value / 0.0254

    def feet = value * 0.3048

    def inFeet = value / 0.3048

    def thou = value * 0.0000254

    def inThou = value / 0.0000254

    def radians = value

    def inRadians = value

    def degrees = math.toRadians(value)

    def inDegrees = math.toDegrees(value)

    def *(sign: Sign): Double = value * sign.value

    def +(interval: Interval): Interval = {
      Interval(value + interval.lowerBound, value + interval.upperBound)
    }

    def -(interval: Interval): Interval = {
      Interval(value - interval.upperBound, value - interval.lowerBound)
    }

    def *(interval: Interval): Interval = interval * value

    def /(interval: Interval): Interval = {
      if (interval.isEmpty) {
        Interval.Empty
      } else if (interval.lowerBound > 0.0) {
        if (value >= 0.0) {
          Interval(value / interval.upperBound, value / interval.lowerBound)
        } else {
          Interval(value / interval.lowerBound, value / interval.upperBound)
        }
      } else if (interval.upperBound < 0.0) {
        if (value >= 0.0) {
          Interval(value / interval.upperBound, value / interval.lowerBound)
        } else {
          Interval(value / interval.lowerBound, value / interval.upperBound)
        }
      } else if (value == 0.0) {
        Interval(0.0)
      } else {
        Interval.Whole
      }
    }

    def *(vector: Vector2d): Vector2d = vector * value

    def *(vector: Vector3d): Vector3d = vector * value

    def *(direction: Direction2d): Vector2d = direction * value

    def *(direction: Direction3d): Vector3d = direction * value
  }
}
