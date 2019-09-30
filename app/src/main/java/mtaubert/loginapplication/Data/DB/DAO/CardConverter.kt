package mtaubert.loginapplication.Data.DB.DAO

import androidx.room.TypeConverter
import mtaubert.loginapplication.Data.Remote.Model.Legalities

class CardConverter {

    @TypeConverter
    fun legalitiesToArrayList(legalities: Legalities?): ArrayList<String>? {
        if(legalities != null) {
            var list = ArrayList<String>()
            list.add(legalities.standard)
            list.add(legalities.future)
            list.add(legalities.modern)
            list.add(legalities.legacy)
            list.add(legalities.pauper)
            list.add(legalities.vintage)
            list.add(legalities.penny)
            list.add(legalities.commander)
            list.add(legalities.brawl)
            list.add(legalities.duel)
            list.add(legalities.oldschool)

            return list
        }
        return null
    }

    @TypeConverter
    fun fromArrayListToLegalities(list: ArrayList<String>?): Legalities? {
        if(list != null) {
            var legalities = Legalities(
                list[0],
                list[1],
                list[2],
                list[3],
                list[4],
                list[5],
                list[6],
                list[7],
                list[8],
                list[9],
                list[10]
            )

            return legalities
        }
        return null
    }
}