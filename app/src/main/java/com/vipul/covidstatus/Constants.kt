package com.vipul.covidstatus

import android.net.Uri

class Constants {

    companion object {

        /*  use for logs  */
        const val TAG = "TAG_COVID_STATUS"

        private const val HTTPS = "https"
        private const val BASE_URL = "covid19india.org"
        private const val BASE_API_URL = "api.covid19india.org"
        private const val DATA_PATH = "data.json"

        private fun getUriBuilder(): Uri.Builder {
            return Uri.Builder()
                    .scheme(HTTPS)
        }

        /*  use for fetching data from api  */
        fun getAPIDataUrl(): String {
            return getUriBuilder()
                    .authority(BASE_API_URL)
                    .appendPath(DATA_PATH)
                    .build().toString()
        }

        /*  official covid19 india org url  */
        fun getBaseUrl(): String {
            return getUriBuilder()
                    .authority(BASE_URL)
                    .build().toString()
        }
    }
}
