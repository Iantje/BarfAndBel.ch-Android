package me.iantje.barfandbelch.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_tools.*
import me.iantje.barfandbelch.R
import me.iantje.barfandbelch.localdbs.objects.NotificationFrequency
import me.iantje.barfandbelch.localdbs.objects.NotificationScheduleItem
import me.iantje.barfandbelch.localdbs.objects.NotificationType
import me.iantje.barfandbelch.recyclerview.NotificationItemAdapter
import me.iantje.barfandbelch.viewmodel.ToolsViewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class ToolsFragment : androidx.fragment.app.Fragment() {

    lateinit var toolsViewModel: ToolsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tools, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolsViewModel = ViewModelProviders.of(this).get(ToolsViewModel::class.java)

        notificationRecycler.adapter = NotificationItemAdapter(toolsViewModel, toolsViewModel.notificationItems)
        notificationRecycler.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)

        toolsViewModel.notificationItems.observe(this,
            Observer<List<NotificationScheduleItem>> {
                notificationRecycler.adapter?.notifyDataSetChanged()
            })
        toolsViewModel.refreshNotificationItems()

        addNotifItemBtn.setOnClickListener {
            toolsViewModel.addNotificationItem(
                NotificationScheduleItem(null, NotificationType.FullQuote,
                    NotificationFrequency.Month, "", true))
        }

        setItemTouchHelpers()
    }

    fun setItemTouchHelpers() {
        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = toolsViewModel.notificationItems.value?.get(viewHolder.adapterPosition) ?: return

                toolsViewModel.removeNotificationItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(notificationRecycler)
    }

}
