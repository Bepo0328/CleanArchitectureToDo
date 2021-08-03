package kr.co.bepo.cleanarchitectureto_do.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kr.co.bepo.cleanarchitectureto_do.R
import kr.co.bepo.cleanarchitectureto_do.data.models.ToDoData
import kr.co.bepo.cleanarchitectureto_do.data.viewmodel.ToDoViewModel
import kr.co.bepo.cleanarchitectureto_do.databinding.FragmentListBinding
import kr.co.bepo.cleanarchitectureto_do.fragments.SharedViewModel
import kr.co.bepo.cleanarchitectureto_do.fragments.list.adapter.ListAdapter
import kr.co.bepo.cleanarchitectureto_do.util.*

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val todoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentListBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        setupRecyclerview()

        todoViewModel.getAllData.observe(viewLifecycleOwner) { data ->
            sharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        }

        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner) {
            showEmptyDatabaseViews(it)
        }

        floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        setHasOptionsMenu(true)

        hideKeyboard(requireActivity())
    }

    private fun setupRecyclerview() = with(binding) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deleteItem = adapter.dataList[viewHolder.adapterPosition]
                todoViewModel.deleteItem(deleteItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                restoreDeletedData(viewHolder.itemView, deleteItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deleteItem: ToDoData) {
        Snackbar.make(view, "Deleted '${deleteItem.title}'", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                todoViewModel.insertData(deleteItem)
            }
            .setActionTextColor(view.color(R.color.holo_orange_light))
            .show()
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) = with(binding) {
        if (emptyDatabase) {
            noDataImageView.toVisible()
            noDataTextView.toVisible()
        } else {
            noDataImageView.toInVisible()
            noDataTextView.toInVisible()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> sortByHighPriority()
            R.id.menu_priority_low -> sortByLowPriority()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) =
        todoViewModel.searchDatabase("%$query%").observeOnce(viewLifecycleOwner) { list ->
            list?.let {
                adapter.setData(it)
            }
        }

    private fun confirmRemoval() {
        AlertDialog.Builder(requireContext())
            .setPositiveButton("Yes") { _, _ ->
                todoViewModel.deleteAll()
                Toast.makeText(
                    requireContext(),
                    "Successfully Removed Everything!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("No") { _, _ -> }
            .setTitle("Delete Everything?")
            .setMessage("Are you sure you want to remove Everything?")
            .create()
            .show()
    }

    private fun sortByHighPriority() = todoViewModel.sortByHighPriority.observe(this) {
        adapter.setData(it)
    }

    private fun sortByLowPriority() = todoViewModel.sortByLowPriority.observe(this) {
        adapter.setData(it)
    }
}