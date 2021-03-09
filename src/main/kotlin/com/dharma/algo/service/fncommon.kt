package com.dharma.algo.service

import com.dhamma.ignitedata.service.HistoryIndicatorService
import java.util.*

//class fncommon {
//}
/*
if null then return today date
else between date
else single date
 */
fun fngetdatebetween(date: Optional<String>, historyIndicatorService: HistoryIndicatorService): Pair<String, String?> {
    // find single day
    if (!date.isPresent || date.get().isEmpty()) {
        println("-------DATE is EMPTY----${historyIndicatorService.today()}-------")
        return return Pair(historyIndicatorService.today().toString(), historyIndicatorService.today().toString())
    } else if (date.get().indexOf(',') > 0) {
        // find date between date
        var z = date.get().split(",")
        println("-------fngetdate-----${z[0]}---------${z[1]}---")
        return Pair(z[0], z[1])
    } else {
        return Pair(date.get(), null)
    }
}
