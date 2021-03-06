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

package org.opensolid.core

import scala.math

import org.opensolid.core.DoubleMatchers._
import org.scalatest._
import org.scalatest.matchers._

trait Vector2dMatchers {
  def beEqualTo(expected: Vector2d, tolerance: Double): Matcher[Vector2d] =
    new ApproximatelyEqualMatcher[Vector2d](
      expected,
      tolerance,
      (firstVector: Vector2d, secondVector: Vector2d) =>
        (firstVector - secondVector).length match {
          case 0.0 =>
            None
          case difference: Double =>
            Some(difference)
        }
    )
}

object Vector2dMatchers extends Vector2dMatchers
