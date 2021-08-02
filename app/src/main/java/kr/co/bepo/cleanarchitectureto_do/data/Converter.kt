package kr.co.bepo.cleanarchitectureto_do.data

import androidx.room.TypeConverter
import kr.co.bepo.cleanarchitectureto_do.data.models.Priority

class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }
}