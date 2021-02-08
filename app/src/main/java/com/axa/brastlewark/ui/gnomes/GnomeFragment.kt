package com.axa.brastlewark.ui.gnomes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.axa.brastlewark.R
import com.axa.brastlewark.databinding.FragmentGnomesBinding
import com.axa.brastlewark.ui.details.GnomeDetailsFragment.Companion.BUNDLE_KEY_ID
import com.axa.brastlewark.utils.Event
import com.axa.brastlewark.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class GnomeFragment: Fragment() {

    companion object {
        const val QUERY_STRING_ID = "QUERY_STRING_ID"
    }

    private var binding: FragmentGnomesBinding by autoCleared()
    private val viewModel: GnomeViewModel by viewModels()
    private lateinit var adapter: GnomeAdapter

    private var lastSearchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGnomesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupSearch()
        restoreInstance(savedInstanceState)
    }

    private fun setupRecyclerView() {
        adapter = GnomeAdapter(::onItemClickListener)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.getGnomes().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Event.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    if (!it.data.isNullOrEmpty())
                        if (lastSearchQuery.isEmpty())
                            adapter.setData(it.data)
                        else
                            filterList(lastSearchQuery)
                }
                Event.Status.ERROR ->
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                Event.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (lastSearchQuery.isNotEmpty()) {
            outState.putString(QUERY_STRING_ID, lastSearchQuery)
        }
        super.onSaveInstanceState(outState)
    }

    private fun restoreInstance(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val query = savedInstanceState.getString(QUERY_STRING_ID)
            if (query != null) {
                setLastSearchQuery(query)
            }
        }
    }

    private fun setLastSearchQuery(query: String) {
        lastSearchQuery = query
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(queryString: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(queryString: String): Boolean {
                filterList(queryString)
                setLastSearchQuery(queryString)
                return true
            }

        })
    }

    private fun filterList(query: String){
        viewModel.getGnomes().value?.data?.filter {
            it.name.toLowerCase(Locale.ROOT)
                .contains(query.toLowerCase(Locale.ROOT))
        }?.let {
            adapter.setData(
                it
            )
        }
    }

    // Show gnome detail view
    private fun onItemClickListener(gnomeId: Int) {
        findNavController().navigate(
            R.id.action_gnomesFragment_to_gnomeDetailFragment,
            bundleOf(BUNDLE_KEY_ID to gnomeId)
        )
    }

}