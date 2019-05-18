package ir.boardbazi.findaspy

import ir.boardbazi.findaspy.places_activity.Place

class Roles{

    lateinit var place:Place
    lateinit var players:ArrayList<String>

    constructor(){}

    constructor(place:Place,players:ArrayList<String>){
        this.place = place
        this.players = players
    }

}
