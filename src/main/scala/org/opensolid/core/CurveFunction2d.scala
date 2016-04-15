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

trait CurveFunction2d extends Function1[Double, Point2d] {
  def apply(interval: Interval): Bounds2d
}

object CurveFunction2d {
  def compile(expression: PointExpression2d[CurveParameter]): CurveFunction2d = {
    val compiler = new ExpressionCompiler(1)
    val (xIndex, yIndex) = compiler.evaluate(expression)
    val arrayOperations = compiler.arrayOperations.toArray
    val arraySize = compiler.arraySize

    new CurveFunction2d {
      override def apply(parameterValue: Double): Point2d = {
        val array = Array.ofDim[Double](arraySize)
        array(0) = parameterValue
        for (operation <- arrayOperations) {
          operation.execute(array)
        }
        Point2d(array(xIndex), array(yIndex))
      }

      override def apply(parameterBounds: Interval): Bounds2d = {
        val array = Array.ofDim[Interval](arraySize)
        array(0) = parameterBounds
        for (operation <- arrayOperations) {
          operation.execute(array)
        }
        Bounds2d(array(xIndex), array(yIndex))
      }
    }
  }
}
