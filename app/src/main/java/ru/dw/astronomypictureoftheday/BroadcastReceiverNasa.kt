package ru.dw.astronomypictureoftheday

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast


class BroadcastReceiverNasa : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            "android.net.conn.CONNECTIVITY_CHANGE" -> {
                val isConnectivity =
                    intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
                MyApp.pref.setIsInternet(!isConnectivity)
                Toast.makeText(context, "isConnectivity ${!isConnectivity}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
