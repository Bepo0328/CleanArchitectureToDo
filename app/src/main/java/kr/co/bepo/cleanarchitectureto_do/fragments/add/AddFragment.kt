package kr.co.bepo.cleanarchitectureto_do.fragments.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kr.co.bepo.cleanarchitectureto_do.R
import kr.co.bepo.cleanarchitectureto_do.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAddBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    private fun initViews() = with(binding) {
        setHasOptionsMenu(true)
    }

}