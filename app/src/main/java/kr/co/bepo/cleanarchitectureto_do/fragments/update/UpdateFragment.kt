package kr.co.bepo.cleanarchitectureto_do.fragments.update

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import kr.co.bepo.cleanarchitectureto_do.R
import kr.co.bepo.cleanarchitectureto_do.data.models.Priority
import kr.co.bepo.cleanarchitectureto_do.databinding.FragmentUpdateBinding
import kr.co.bepo.cleanarchitectureto_do.fragments.SharedViewModel

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateFragmentArgs by navArgs()
    private val sharedViewModel: SharedViewModel by viewModels()

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    private fun initViews() = with(binding) {
        setHasOptionsMenu(true)

        currentTitleEditText.setText(args.currentItem.title)
        currentDescriptionEditText.setText(args.currentItem.description)
        currentPrioritiesSpinner.setSelection(parsePriority(args.currentItem.priority))
        currentPrioritiesSpinner.onItemSelectedListener = sharedViewModel.listener
    }

    private fun parsePriority(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

}