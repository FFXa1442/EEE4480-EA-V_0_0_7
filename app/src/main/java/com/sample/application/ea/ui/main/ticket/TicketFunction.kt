package com.sample.application.ea.ui.main.ticket

import android.content.Intent
import android.net.Uri
import android.view.View
import com.sample.application.ea.R

val TicketFragment.onClickListener: View.OnClickListener
    get() = View.OnClickListener { v ->
        startActivity(Intent(Intent.ACTION_VIEW, table[v.id]!!))
    }

private val table by lazy {
    mutableMapOf<Int, Uri>().apply {
        put(
            key = R.id.japan_button,
            value = Uri.parse(
                "https://hk.trip.com/flights/cheap-flights-to-japan-78?" +
                        "allianceid=14882&" +
                        "sid=4137882&" +
                        "ppcid=ckid-3042109189_adid-608222240614_akid-kwd-299647035599_adgid-142074030607&" +
                        "utm_source=google&" +
                        "utm_medium=cpc&" +
                        "utm_campaign=17652160069&" +
                        "gad_source=1&" +
                        "gclid=Cj0KCQiA6Ou5BhCrARIsAPoTxrByJ1WtmxOoDQmLbuxov5RNQvIUiaJElI7U-DYyoOmTz0XCd_-2jB4aArLHEALw_wcB&" +
                        "gclsrc=aw.ds"
            )
        )
        put(
            key = R.id.south_korea_button,
            value = Uri.parse(
                "https://hk.trip.com/flights/cheap-flights-to-south-korea-42?" +
                        "allianceid=14882&" +
                        "sid=4137882&" +
                        "ppcid=ckid-3045244165_adid-608222332303_akid-kwd-300340561411_adgid-142074036687&" +
                        "utm_source=google&" +
                        "utm_medium=cpc&" +
                        "utm_campaign=17652160069&" +
                        "gad_source=1&" +
                        "gclid=Cj0KCQiA6Ou5BhCrARIsAPoTxrBbxcmohIFrWg3yALUk44_nXj7Np6G4JtbjbcaBNgxAOnLlcMWm5pEaAvBREALw_wcB&" +
                        "gclsrc=aw.ds"
            )
        )
        put(
            key = R.id.taiwan_button,
            value = Uri.parse(
                "https://hk.trip.com/flights/cheap-flights-to-china-1-53?allianceid=14882&" +
                        "sid=4137882&" +
                        "ppcid=ckid-3044685061_adid-608287391942_akid-kwd-297240553322_adgid-142074035487&" +
                        "utm_source=google&" +
                        "utm_medium=cpc&" +
                        "utm_campaign=17652160069&" +
                        "gad_source=1&" +
                        "gclid=Cj0KCQiA6Ou5BhCrARIsAPoTxrC9KIxI6iptd_3PyxsGNyrZQPNYY9VcG6yOUlA_zWwhGcUD8AmQB5kaAg-6EALw_wcB&" +
                        "gclsrc=aw.ds"
            )
        )
        put(
            key = R.id.britain_button,
            value = Uri.parse(
                "https://hk.trip.com/flights/cheap-flights-to-united-kingdom-109?allianceid=14882" +
                        "&sid=4137882" +
                        "&ppcid=ckid-3042458629_adid-611269696455_akid-kwd-6488096366_adgid-142074023207" +
                        "&utm_source=google" +
                        "&utm_medium=cpc" +
                        "&utm_campaign=17652160069" +
                        "&gad_source=1" +
                        "&gclid=Cj0KCQiA6Ou5BhCrARIsAPoTxrCczsfTBB8PD-Irxvn-4oXa6GxqN_dtd6Ygk_vMSm4T1_5PO2YdY6QaAqKTEALw_wcB" +
                        "&gclsrc=aw.ds"
            )
        )
        put(
            key = R.id.china_button,
            value = Uri.parse(
                "https://flights.ctrip.com/online/channel/domestic?allianceid=4899&" +
                        "sid=156000&" +
                        "utm_medium=google&" +
                        "utm_campaign=ty&" +
                        "utm_source=googleppc&" +
                        "gad_source=1&" +
                        "gclid=Cj0KCQiA6Ou5BhCrARIsAPoTxrBReQFBrBbpfv0HYzX8PrNLe-UiLfz-cYUPf12e1FP0AwFCyaEU_WkaAoIoEALw_wcB&" +
                        "gclsrc=aw.ds&" +
                        "keywordid=1997593621-72767959376"
            )
        )
        put(
            key = R.id.singapore_button,
            value = Uri.parse(
                "https://hk.trip.com/flights/to-singapore/airfares-sin?allianceid=14882&" +
                        "sid=4137876&" +
                        "locale=zh_hk&" +
                        "curr=hkd&" +
                        "ppcid=ckid-43762482514_adid-696008865483_akid-kwd-320509437274_adgid-164462771687&" +
                        "utm_source=google&" +
                        "utm_medium=cpc&" +
                        "utm_campaign=21159951018&" +
                        "gad_source=1&" +
                        "gclid=Cj0KCQiA6Ou5BhCrARIsAPoTxrA0Q5sQJKbuUTvScoh3vcL0ovHCabVw2CbYmM_Dmq1EQySrVs9TlqMaAkpPEALw_wcB"
            )
        )
        put(
            key = R.id.malaysia_button,
            value = Uri.parse(
                "https://hk.trip.com/flights/cheap-flights-to-malaysia-2?allianceid=14882&" +
                        "sid=4137882&" +
                        "ppcid=ckid-3044006149_adid-648961361211_akid-kwd-300340561651_adgid-142074026287&" +
                        "utm_source=google&" +
                        "utm_medium=cpc&" +
                        "utm_campaign=17652160069&" +
                        "gad_source=1&" +
                        "gclid=Cj0KCQiA6Ou5BhCrARIsAPoTxrB0Cdbn9S47WtdrSOrtPOOAd_ZwJ78hAsQwzan6hMUpBpIpnnFlmi0aArz6EALw_wcB&" +
                        "gclsrc=aw.ds"
            )
        )
        put(
            key = R.id.united_state_button,
            value = Uri.parse(
                "https://hk.trip.com/flights/cheap-flights-to-united-states-66?allianceid=14882&" +
                        "sid=4137882&" +
                        "ppcid=ckid-3044884741_adid-608222313784_akid-kwd-300340560211_adgid-142074036127&" +
                        "utm_source=google&" +
                        "utm_medium=cpc&" +
                        "utm_campaign=17652160069&" +
                        "gad_source=1&" +
                        "gclid=Cj0KCQiAi_G5BhDXARIsAN5SX7rZNI-NBBJp3jNuIR4gF-yCCYwkhrVZnx8x6L0ruQJCVADl7Z7an9AaAg1NEALw_wcB&" +
                        "gclsrc=aw.ds"
            )
        )
    }
}