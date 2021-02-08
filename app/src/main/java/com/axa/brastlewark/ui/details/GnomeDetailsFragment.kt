package com.axa.brastlewark.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.axa.brastlewark.databinding.FragmentGnomeDetailsBinding
import com.axa.brastlewark.model.Gnome
import com.axa.brastlewark.utils.Event
import com.axa.brastlewark.utils.autoCleared
import com.axa.brastlewark.utils.loadCircularImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GnomeDetailsFragment : Fragment() {

    companion object {
        const val BUNDLE_KEY_ID = "BUNDLE_KEY_ID"
    }

    private var binding: FragmentGnomeDetailsBinding by autoCleared()
    private val viewModel: GnomeDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGnomeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(BUNDLE_KEY_ID)?.let { viewModel.start(it) }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.gnome.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Event.Status.SUCCESS -> {
                    bindGnome(it.data)
                    binding.progressBar.visibility = View.GONE
                    binding.details.visibility = View.VISIBLE
                }

                Event.Status.ERROR ->
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

                Event.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.details.visibility = View.GONE
                }
            }
        })
    }

    private fun bindGnome(gnome: Gnome?) {
        gnome?.let {
            binding.name.text = it.name
            binding.hair.text = gnome.hair_color
            binding.weight.text = gnome.weight.toString()
            binding.height.text = gnome.height.toString()
            binding.age.text = gnome.age.toString()

            if(gnome.professions.isNotEmpty()) {
                binding.professionsLayout.visibility = View.VISIBLE
                binding.professions.text = listToString(gnome.professions)
            }

            if(gnome.friends.isNotEmpty()) {
                binding.friendsLayout.visibility = View.VISIBLE
                binding.friends.text = listToString(gnome.friends)
            }

            binding.image.loadCircularImage(gnome.thumbnail, binding.root)
        }
    }

    private fun listToString(list: List<String>): String {
        var text = ""
        list.forEach {
            text = "${it}\n${text}"
        }
        return text
    }
}