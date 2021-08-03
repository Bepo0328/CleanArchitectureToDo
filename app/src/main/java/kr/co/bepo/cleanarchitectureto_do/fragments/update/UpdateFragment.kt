package kr.co.bepo.cleanarchitectureto_do.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kr.co.bepo.cleanarchitectureto_do.R
import kr.co.bepo.cleanarchitectureto_do.data.models.ToDoData
import kr.co.bepo.cleanarchitectureto_do.data.viewmodel.ToDoViewModel
import kr.co.bepo.cleanarchitectureto_do.databinding.FragmentUpdateBinding
import kr.co.bepo.cleanarchitectureto_do.fragments.SharedViewModel

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateFragmentArgs by navArgs()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val toDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentUpdateBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() = with(binding) {
        setHasOptionsMenu(true)

        currentTitleEditText.setText(args.currentItem.title)
        currentDescriptionEditText.setText(args.currentItem.description)
        currentPrioritiesSpinner.setSelection(sharedViewModel.parsePriorityToInt(args.currentItem.priority))
        currentPrioritiesSpinner.onItemSelectedListener = sharedViewModel.listener
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() = with(binding) {
        val title = currentTitleEditText.text.toString()
        val description = currentDescriptionEditText.text.toString()
        val getPriority = currentPrioritiesSpinner.selectedItem.toString()

        val validation = sharedViewModel.verifyDataFromUser(title, description)
        if (validation) {
            val updateItem = ToDoData(
                id = args.currentItem.id,
                title = title,
                priority = sharedViewModel.parsePriority(getPriority),
                description = description
            )
            toDoViewModel.updateData(updateItem)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun confirmItemRemoval() {
        AlertDialog.Builder(requireContext())
            .setPositiveButton("Yes") { _, _ ->
                toDoViewModel.deleteItem(args.currentItem)
                Toast.makeText(
                    requireContext(),
                    "Successfully Removed: ${args.currentItem.title}",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            .setNegativeButton("No") { _, _ -> }
            .setTitle("Delete ${args.currentItem.title}?")
            .setMessage("Are you sure you want to remove '${args.currentItem.title}'?")
            .create()
            .show()
    }

}