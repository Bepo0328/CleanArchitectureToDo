package kr.co.bepo.cleanarchitectureto_do.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.bepo.cleanarchitectureto_do.R
import kr.co.bepo.cleanarchitectureto_do.data.models.Priority
import kr.co.bepo.cleanarchitectureto_do.data.models.ToDoData
import kr.co.bepo.cleanarchitectureto_do.databinding.RowLayoutBinding
import kr.co.bepo.cleanarchitectureto_do.fragments.list.ListFragmentDirections

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    var dataList: List<ToDoData> = listOf()

    inner class ListViewHolder(
        private val binding: RowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ToDoData) = with(binding) {
            titleTextView.text = data.title
            descriptionTextView.text = data.description

            rowBackground.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(data)
                root.findNavController().navigate(action)
            }

            when (data.priority) {
                Priority.HIGH -> priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.red
                    )
                )
                Priority.MEDIUM -> priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.yellow
                    )
                )
                Priority.LOW -> priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.green
                    )
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int =
        dataList.size

    fun setData(toDoList: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(dataList, toDoList)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoList
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}