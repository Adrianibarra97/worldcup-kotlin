package ar.edu.unsam.algo3.domain

abstract class Coleccionable {
    var id: Int = -1
    
    abstract fun search(value: String): Boolean
}