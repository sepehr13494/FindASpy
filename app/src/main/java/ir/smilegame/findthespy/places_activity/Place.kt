package ir.smilegame.findthespy.places_activity

import ir.smilegame.findthespy.R

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
                "کوه دماوند"-> icon = R.drawable.damavand
                "سی و سه پل"-> icon = R.drawable.siosepol
                "برج میلاد"-> icon = R.drawable.borjmilad
                "دریاچه ارومیه"-> icon = R.drawable.orumie
                "دریای خزر"-> icon = R.drawable.khazar
                "فشافویه"-> icon = R.drawable.fashafuye
                "رود کارون"-> icon = R.drawable.rudkarun
                "کویر مصر"-> icon = R.drawable.kavirmesr
                "ارگ بم"-> icon = R.drawable.argbam
                "میدان آزادی"-> icon = R.drawable.azadi
                "غار علیصدر"-> icon = R.drawable.gharalisadr
                "تخت جمشید"-> icon = R.drawable.takhtjamshid
                "جاده چالوس"-> icon = R.drawable.chalus
                "نقش جهان"-> icon = R.drawable.naghshjahan
                "آرامگاه حافظ"-> icon = R.drawable.hafezie
                "آرامگاه کوروش"-> icon = R.drawable.ghabrkurosh
                "حمام فین"-> icon = R.drawable.hamamfin
                "تنگه واشی"-> icon = R.drawable.tangevashi
                "نارین قلعه"-> icon = R.drawable.naringhale
                "بازار تهران"-> icon = R.drawable.tehranbazar
                "تنکابن"-> icon = R.drawable.tonekabon
                "جنگل ابر"-> icon = R.drawable.abrjangal
                "آتشکده یزد"-> icon = R.drawable.atashkade
                "منار جنبان"-> icon = R.drawable.menarjonban
                "کویر مرنجاب"-> icon = R.drawable.maranjab
                "ونیز"-> icon = R.drawable.venice
                "میدان تایمز نیویورک"-> icon = R.drawable.timesquare
                "رشته کوه آلپ"-> icon = R.drawable.thealps
                "تاج محل"-> icon = R.drawable.tajmahal
                "استون هنج"-> icon = R.drawable.stonehenj
                "مجستمه آزادی"-> icon = R.drawable.statueofliberty
                "ریو د جنیرو"-> icon = R.drawable.riodejanero
                "برج پیتزا"-> icon = R.drawable.pizzatower
                "خانه اپرا دیزنی"-> icon = R.drawable.operahouse
                "کلیسا نوتردام"-> icon = R.drawable.notredamechurch
                "آبشار نیاگارا"-> icon = R.drawable.niagara
                "موزه لوور پاریس"-> icon = R.drawable.louvermusium
                "لاس وگاس"-> icon = R.drawable.lasvegas
                "برج ایفل"-> icon = R.drawable.ifeltower
                "اهرام ثلاثه"-> icon = R.drawable.greatpyramid
                "دره fjaorargljufurcanyon"-> icon = R.drawable.fjaorargljufurcanyon
                "ساختمان امپایر استیت"-> icon = R.drawable.empirestatebuilding
                "کلوسئوم"-> icon = R.drawable.colosseum
                "دیوار چین"-> icon = R.drawable.chinawall
                "کعبه"-> icon = R.drawable.kabe
                "ایبیزا"-> icon = R.drawable.ibiza
                "کاخ سفید"-> icon = R.drawable.whitehouse
                "پارک مرکزی"-> icon = R.drawable.centralpark
                "پارک والت دیزنی"-> icon = R.drawable.waltdisneypark
                "بودای بزرگ"-> icon = R.drawable.bigbuda
                "ابوالهول"-> icon = R.drawable.abolhole
                "بیگ بن"-> icon = R.drawable.bigben
                "مثلث برمودا"-> icon = R.drawable.bermudar
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