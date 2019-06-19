package me.iantje.barfandbelch.recyclerview.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.viewholder_notification_item.view.*
import me.iantje.barfandbelch.localdbs.objects.NotificationScheduleItem

class NotificationItemViewholder(view: View): RecyclerView.ViewHolder(view) {

    fun fillItem(notification: NotificationScheduleItem) {
        itemView.notifItemType.text = notification.frequency.toString()
        itemView.notifItemDescription.text = "Every 15th at 15:00"
        itemView.notifItemSwitch.isChecked = notification.enabled
    }
}