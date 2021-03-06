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

final case class Translation2d(vector: Vector2d) extends Transformation2d {
  def this(x: Double, y: Double) =
    this(Vector2d(x, y))

  override def apply(point: Point2d): Point2d =
    point + vector

  override def apply(vector: Vector2d): Vector2d =
    vector

  override def apply(direction: Direction2d): Direction2d =
    direction
}

object Translation2d {
  def apply(x: Double, y: Double): Translation2d =
    Translation2d(Vector2d(x, y))

  def along(axis: Axis2d, distance: Double): Translation2d =
    Translation2d(axis.direction * distance)
}
