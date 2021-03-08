package com.dharma.algo.service

import java.time.LocalDate
import java.util.*

//class fncommon {
//}
/*
if null then return today date
else between date
else single date
 */
fun fngetdate(date: Optional<String>): Pair<String, String?> {
    if (!date.isPresent) {
        return return Pair(LocalDate.now().toString(), null)
    } else if (date.get().indexOf(',') > 0) {
        var z = date.get().split(",")
        println("-------fngetdate-----${z[0]}---------${z[1]}---")
        return Pair(z[0], z[1])
    } else {
        return Pair(date.get(), null)
    }
}
