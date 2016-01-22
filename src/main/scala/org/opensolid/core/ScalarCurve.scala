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

abstract class ScalarCurve {
  import ScalarCurve._

  def expression: Expression

  def domain: Interval
}

object ScalarCurve {
  type Expression = Expression1d[Expression1d.Parameter1d]

  case class Constant(value: Double) extends ScalarCurve {
    def expression: Expression = Expression1d.Constant(value)

    def domain: Interval = Interval.Whole
  }

  case class Generic(expression: Expression, domain: Interval) extends ScalarCurve

  def apply(expression: Expression, domain: Interval): ScalarCurve = Generic(expression, domain)
}
