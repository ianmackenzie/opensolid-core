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

import org.scalacheck.Gen
import org.scalatest._
import org.scalatest.matchers._
import org.scalatest.prop._

class Vector2dTestSuite extends TestSuite
  with DoubleGenerators
  with Vector2dGenerators
  with Axis2dGenerators
  with Plane3dGenerators
  with Vector2dMatchers {

  test("length") {
    forAll(anyVector2d, finiteDouble) {
      (vector: Vector2d, scale: Double) => {
        val scaledLength = vector.length * scale.abs
        val tolerance = 2 * eps(scaledLength)
        (vector * scale).length.should(beEqualTo(scaledLength, tolerance))
      }
    }
  }

  test("squaredLength") {
    forAll {
      (vector: Vector2d) => {
        val tolerance = 2 * eps(vector.squaredLength)
        vector.squaredLength.should(beEqualTo(vector.length * vector.length, tolerance))
      }
    }
  }

  test("projectedOnto(axis)") {
    forAll {
      (vector: Vector2d, axis: Axis2d) => {
        val projected = vector.projectedOnto(axis)
        val tolerance = 4 * eps(vector.length)
        projected.length.should(beEqualTo(vector.componentIn(axis.direction).abs, tolerance))
        projected.cross(axis.direction).should(beEqualTo(0.0, tolerance))
        val projectedTolerance = 8 * eps(projected.length)
        projected.projectedOnto(axis).should(beEqualTo(projected, projectedTolerance))
        (vector - projected).componentIn(axis.direction).should(beEqualTo(0.0, projectedTolerance))
      }
    }
  }

  test("placedOnto(plane)") {
    forAll {
      (vector: Vector2d, plane: Plane3d) => {
        val tolerance = 8 * eps(vector.length)
        val placed = vector.placedOnto(plane)
        placed.length.should(beEqualTo(vector.length, tolerance))
        val projected = placed.projectedInto(plane)
        projected.should(beEqualTo(vector, tolerance))
      }
    }
  }

  test("normalized") {
    forAll {
      (vector: Vector2d) => {
        whenever(vector != Vector2d.Zero) {
          val normalized = vector.normalized.get
          normalized.length.should(beEqualTo(1.0, 2 * eps(1.0)))
          val tolerance = 4 * eps(vector.length)
          vector.dot(normalized).should(beEqualTo(vector.length, tolerance))
          (normalized * vector.length).should(beEqualTo(vector, tolerance))
        }
      }
    }
  }

  test("direction") {
    forAll {
      (vector: Vector2d) => {
        whenever(vector != Vector2d.Zero) {
          vector.direction.get.vector.shouldBe(vector.normalized.get)
        }
      }
    }
  }

  test("dot(that)") {
    forAll {
      (firstVector: Vector2d, secondVector: Vector2d) => {
        val dotProduct = firstVector.dot(secondVector)
        val firstLength = firstVector.length
        val secondLength = secondVector.length
        val maxLength = firstLength.max(secondLength)
        val tolerance = maxLength * 2 * eps(maxLength)
        dotProduct.should(beLessThanOrEqualTo(firstLength * secondLength, tolerance))
        firstVector.dot(-secondVector).shouldBe(-dotProduct)
      }
    }
    forAll {
      (vector: Vector2d) => {
        whenever (vector != Vector2d.Zero) {
          val tolerance = 4 * eps(vector.length)
          vector.componentIn(vector.normalDirection.get).should(beEqualTo(0.0, tolerance))
          vector.componentIn(vector.direction.get).should(beEqualTo(vector.length, tolerance))
        }
      }
    }
  }
}
