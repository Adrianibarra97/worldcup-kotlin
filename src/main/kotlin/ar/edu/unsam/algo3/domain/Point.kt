package ar.edu.unsam.algo3.domain

class Point(val x: Double, val y: Double) {

    fun distance(anotherPoint: Point): Double =
        Math.hypot(this.x - anotherPoint.x, this.y - anotherPoint.y)
}