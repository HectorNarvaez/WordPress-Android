package org.wordpress.android.ui.bloggingreminders

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Type
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Type.TITLE

class BloggingRemindersAdapter : Adapter<BloggingRemindersViewHolder<*>>() {
    private var items: List<BloggingRemindersItem> = listOf()

    fun update(newItems: List<BloggingRemindersItem>) {
        val diffResult = DiffUtil.calculateDiff(
                BloggingRemindersDiffCallback(
                        items,
                        newItems
                )
        )
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BloggingRemindersViewHolder<*>, position: Int) {
        val item = items[position]
        // Currently we have only one ViewHolder type
        TODO("Add view holders")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloggingRemindersViewHolder<*> {
        return when (Type.values()[viewType]) {
            TITLE -> TODO()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type.ordinal
    }
}
