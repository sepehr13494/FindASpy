package ir.boardbazi.findaspy.places_activity

import ir.boardbazi.findaspy.R

class Place(var icon:Int,var name:String) {

    companion object{
        fun getPlaceCard(name:String): Int {
            var icon:Int = 0
            when (name){
                "فرودگاه"-> icon = R.drawable.airport
                "مسجد"-> icon = R.drawable.mosque
                "آتشنشانی"-> icon = R.drawable.fire_station
                "کلانتری"-> icon = R.drawable.police_station
                "رستوران"-> icon = R.drawable.restaurant
                "بیمارستان"-> icon = R.drawable.hospital
                "سیرک"-> icon = R.drawable.circus
                "کافه"-> icon = R.drawable.cafe
                "ساحل"-> icon = R.drawable.beach
                "هتل"-> icon = R.drawable.hotel
                "بانک"-> icon = R.drawable.bank
                "آرایشگاه"-> icon = R.drawable.barbery
                "سینما"-> icon = R.drawable.cinema
                "کشتی"-> icon = R.drawable.ship
                "قطار"-> icon = R.drawable.train
                "مدرسه"-> icon = R.drawable.school
                "دانشگاه"-> icon = R.drawable.univercity
                "شرکت"-> icon = R.drawable.work_place
                "کارخانه"-> icon = R.drawable.factory
                "جنگل"-> icon = R.drawable.forest
                "موزه"-> icon = R.drawable.museum
                "کاخ"-> icon = R.drawable.palace
                "خیابان"-> icon = R.drawable.street
                "پارک"-> icon = R.drawable.park
                "استخر"-> icon = R.drawable.swimming_pool
            }
            return icon
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Place){
            return other.name == this.name
        }else{
            return false
        }
    }

    override fun hashCode(): Int {
        return this.name.hashCode()
    }

}