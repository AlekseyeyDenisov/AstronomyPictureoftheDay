package ru.dw.astronomypictureoftheday

import android.app.Application
import androidx.room.Room
import ru.dw.astronomypictureoftheday.data.room.DBRoom
import ru.dw.astronomypictureoftheday.data.room.HelperRoom
import ru.dw.astronomypictureoftheday.utils.SharedPreferencesManager


class MyApp : Application() {

    companion object {
        private var appContext: MyApp? = null
        lateinit var pref: SharedPreferencesManager


        private var dbRoom: DBRoom? = null

        fun getDBRoom(): HelperRoom {
            if (dbRoom == null) {
                if (appContext != null) {
                    dbRoom = Room.databaseBuilder(appContext!!, DBRoom::class.java, "test").build()
                } else {
                    throw IllegalStateException("Пустой  appContext в APP")
                }
            }
            return HelperRoom(dbRoom!!.nasaDayDao())
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        pref = SharedPreferencesManager(this)


    }


}