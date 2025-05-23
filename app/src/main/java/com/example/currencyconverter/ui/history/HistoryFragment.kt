package com.example.currencyconverter.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.databinding.FragmentHistBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class.java]

        viewModel.historyList.observe(viewLifecycleOwner) { historyList ->
            val adapter = HistoryAdapter(historyList)

            adapter.onReturnClicked = { item ->
                viewModel.selectItem(item)
                findNavController().navigateUp()
            }
            adapter.onDeleteClicked = { item ->
                viewModel.deleteItem(item)
            }

            binding.historyRecyclerView.adapter = adapter
            binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}