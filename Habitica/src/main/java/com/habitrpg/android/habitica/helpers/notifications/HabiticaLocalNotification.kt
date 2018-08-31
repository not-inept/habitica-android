package com.habitrpg.android.habitica.helpers.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.annotation.CallSuper
import android.support.v4.app.NotificationCompat

import com.habitrpg.android.habitica.R
import com.habitrpg.android.habitica.ui.activities.MainActivity

/**
 * Created by keithholliday on 6/28/16.
 */
abstract class HabiticaLocalNotification(protected var context: Context, protected var identifier: String) {

    protected var data: Map<String, String>? = null
    protected var title: String? = null
    protected var message: String? = null

    protected var notificationBuilder = NotificationCompat.Builder(context, "default")

    @CallSuper
    open fun notifyLocally(title: String?, message: String?) {
        this.title = title
        this.message = message

        val path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        this.notificationBuilder = notificationBuilder
                .setSmallIcon(R.drawable.ic_gryphon_white)
                .setAutoCancel(true)
                .setSound(path)

        if (title != null) {
            notificationBuilder = notificationBuilder.setContentTitle(title)
        }
        if (message != null) {
            notificationBuilder = notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
        }

        this.setNotificationActions()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.notify(getNotificationID(), notificationBuilder.build())
    }

    fun setExtras(data: Map<String, String>) {
        this.data = data
    }

    protected open fun setNotificationActions()  {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("notificationIdentifier", identifier)
        configureMainIntent(intent)
        val pendingIntent = PendingIntent.getActivity(
                context,
                3000,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationBuilder.setContentIntent(pendingIntent)
    }

    protected open fun configureMainIntent(intent: Intent) {
    }

    protected open fun getNotificationID(): Int = 10
}
