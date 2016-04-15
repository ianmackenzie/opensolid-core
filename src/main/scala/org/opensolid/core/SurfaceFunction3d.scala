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

trait SurfaceFunction3d extends Function1[Point2d, Point3d] {
  def apply(interval: Bounds2d): Bounds3d
}

object SurfaceFunction3d {
  def compile(expression: PointExpression3d[SurfaceParameter]): SurfaceFunction3d = {
    val compiler = new ExpressionCompiler(2)
    val (xIndex, yIndex, zIndex) = compiler.evaluate(expression)
    val arrayOperations = compiler.arrayOperations.toArray
    val arraySize = compiler.arraySize

    new SurfaceFunction3d {
      override def apply(parameterValue: Point2d): Point3d = {
        val array = Array.ofDim[Double](arraySize)
        array(0) = parameterValue.x
        array(1) = parameterValue.y
        for (operation <- arrayOperations) {
          operation.execute(array)
        }
        Point3d(array(xIndex), array(yIndex), array(zIndex))
      }

      override def apply(parameterBounds: Bounds2d): Bounds3d = {
        val array = Array.ofDim[Interval](arraySize)
        array(0) = parameterBounds.x
        array(1) = parameterBounds.y
        for (operation <- arrayOperations) {
          operation.execute(array)
        }
        Bounds3d(array(xIndex), array(yIndex), array(zIndex))
      }
    }
  }
}