package ir.boardbazi.findaspy

import ir.boardbazi.findaspy.places_activity.Place

class GameOption {

    var persons: Int = 0
    var spys: Int = 0
    var time: Int = 0
    lateinit var places: ArrayList<Place>

    constructor(){}

    constructor(persons: Int, spys: Int,time: Int, places: ArrayList<Place>){
        this.persons = persons
        this.spys = spys
        this.time = time
        this.places = places
    }
}