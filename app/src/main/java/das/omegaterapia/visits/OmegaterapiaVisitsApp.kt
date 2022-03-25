package das.omegaterapia.visits

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class OmegaterapiaVisitsApp : Application() {

    lateinit var currentActivity: Activity

    override fun onCreate() {
        super.onCreate()

        /*------------------------------
        | Create Notification Channels |
        ------------------------------*/

        // Create the Authentication Notification Channel
        val name = getString(R.string.auth_channel_name)
        val descriptionText = getString(R.string.auth_channel_description)

        val mChannel = NotificationChannel("AUTH_CHANNEL", name, NotificationManager.IMPORTANCE_LOW)
        mChannel.description = descriptionText

        // Register the channel with the system
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
}

enum class NotificationID(val id: Int) {
    USER_CREATED(0)
}