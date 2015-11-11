package org.opensolid.core

import scala.math
import scala.util.Random

final case class Vector3d(x: Double, y: Double, z: Double) extends VectorTransformable3d[Vector3d] {
  def components: Array[Double] = Array(x, y , z)

  def apply(index: Int): Double = index match {
    case 0 => x
    case 1 => y
    case 2 => z
    case _ => throw new IndexOutOfBoundsException(s"Index $index is out of bounds for Vector3d")
  }

  def squaredLength: Double = x * x + y * y + z * z

  def length: Double = math.sqrt(squaredLength)

  def isZero(precision: Double): Boolean = squaredLength.isZero(precision * precision)

  override def transformedBy(transformation: Transformation3d): Vector3d = transformation(this)

  def normalized: Vector3d = {
    if (x != 0.0 || y != 0.0 || z != 0.0) {
      val length = this.length
      Vector3d(x / length, y / length, z / length)
    } else {
      Vector3d.Zero
    }
  }

  def direction: Direction3d = {
    if (x != 0.0 || y != 0.0 || z != 0.0) {
      val length = this.length
      Direction3d(x / length, y / length, z / length)
    } else {
      Direction3d.None
    }
  }

  def unary_- : Vector3d = Vector3d(-x, -y, -z)

  def +(that: Vector3d): Vector3d = Vector3d(x + that.x, y + that.y, z + that.z)

  def -(that: Vector3d): Vector3d = Vector3d(x - that.x, y - that.y, z - that.z)

  def *(value: Double): Vector3d = Vector3d(x * value, y * value, z * value)

  def /(value: Double): Vector3d = Vector3d(x / value, y / value, z / value)
}

object Vector3d {
  def fromComponents[T <% Double](components: Seq[T]): Vector3d = components match {
    case Seq(x, y, z) => Vector3d(x, y, z)
    case _ => throw new IllegalArgumentException("Vector3d requires 3 components")
  }

  def spherical(radius: Double, azimuth: Double, elevation: Double): Vector3d = {
    val cosElevation = math.cos(elevation)
    Vector3d(
      radius * cosElevation * math.cos(azimuth),
      radius * cosElevation * math.sin(azimuth),
      radius * math.sin(elevation))
  }

  val Zero: Vector3d = Vector3d(0.0, 0.0, 0.0)
}
