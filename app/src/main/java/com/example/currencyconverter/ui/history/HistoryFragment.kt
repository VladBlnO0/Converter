package com.example.currencyconverter.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.databinding.FragmentHistBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistBinding? = null
    private lateinit var dbViewModel: HistoryDbViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(HistoryDbViewModel::class.java)

        val adapter = HistoryAdapter(emptyList())
        binding.historyRecyclerView.adapter = adapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.onDeleteClicked = { item ->
            dbViewModel.delete(item)
        }

        dbViewModel.allHistory.observe(viewLifecycleOwner) { historyList ->
            adapter.items = historyList
            adapter.notifyDataSetChanged()
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}