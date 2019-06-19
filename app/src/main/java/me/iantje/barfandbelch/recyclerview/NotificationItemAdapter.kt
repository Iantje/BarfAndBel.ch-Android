package me.iantje.barfandbelch.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.viewholder_notification_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.iantje.barfandbelch.R
import me.iantje.barfandbelch.localdbs.objects.NotificationScheduleItem
import me.iantje.barfandbelch.recyclerview.viewholder.NotificationItemViewholder
import me.iantje.barfandbelch.repository.WidgetRepository
import me.iantje.barfandbelch.viewmodel.ToolsViewModel

class NotificationItemAdapter(val toolsViewModel: ToolsViewModel, val liveData: LiveData<List<NotificationScheduleItem>>): RecyclerView.Adapter<NotificationItemViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationItemViewholder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.viewholder_notification_item, parent, false)

        return NotificationItemViewholder(view)
    }

    override fun getItemCount(): Int {
        val count = liveData.value?.count() ?: return 0

        return count
    }

    override fun onBindViewHolder(holder: NotificationItemViewholder, position: Int) {
        val items = liveData.value ?: return

        holder.fillItem(items[position])
        holder.itemView.notifItemSwitch.setOnClickListener {
            items[position].enabled = holder.itemView.notifItemSwitch.isChecked

            GlobalScope.launch {
                toolsViewModel.updateNotificationItem(items[position])
            }
        }
    }
}
