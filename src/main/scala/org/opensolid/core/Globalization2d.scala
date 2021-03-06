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

final case class Globalization2d(frame: Frame2d) extends Transformation2d {
  override def apply(point: Point2d): Point2d =
    frame.originPoint + point.x * frame.xDirection + point.y * frame.yDirection

  override def apply(vector: Vector2d): Vector2d =
    vector.x * frame.xDirection + vector.y * frame.yDirection

  override def apply(direction: Direction2d): Direction2d =
    Direction2d(
      direction.x * frame.xDirection.x + direction.y * frame.yDirection.x,
      direction.x * frame.xDirection.y + direction.y * frame.yDirection.y
    )
}
