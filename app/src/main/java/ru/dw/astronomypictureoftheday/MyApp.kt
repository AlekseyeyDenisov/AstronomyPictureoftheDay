package ru.dw.astronomypictureoftheday

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import ru.dw.astronomypictureoftheday.data.room.DBRoom
import ru.dw.astronomypictureoftheday.data.room.HelperRoom



class MyApp : Application() {

    companion object {
        private var appContext: MyApp? = null
        val isConnectivity: MutableLiveData<Boolean> = MutableLiveData()


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

    }


}