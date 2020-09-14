package com.like.drive.motorfeed.ui.base.ext

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.like.drive.motorfeed.MotorFeedApplication
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData

fun MotorTypeData.setMotorType(): String {
    return if (modelCode == 0) {
        brandName
    } else {
        String.format(
            MotorFeedApplication.getContext().getString(R.string.motorType_format_text),
            brandName,
            modelName
        )
    }
}

//convert a data class to a map
fun <T> T.serializeToMap(): Map<String, Any> {
    return convert()
}

//convert a map to a data class
inline fun <reified T> Map<String, Any>.toDataClass(): T? {
    return convert()
}

//convert an object of type I to type O
inline fun <I, reified O> I.convert(): O {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<O>() {}.type)
}