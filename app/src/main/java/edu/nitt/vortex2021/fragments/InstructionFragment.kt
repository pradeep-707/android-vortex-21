package edu.nitt.vortex2021.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import edu.nitt.vortex2021.adapters.InstructionAdapter
import edu.nitt.vortex2021.databinding.FragmentInstructionBinding
import edu.nitt.vortex2021.helpers.*
import edu.nitt.vortex2021.model.Event


class InstructionFragment : Fragment() {

    private var binding by viewLifecycle<FragmentInstructionBinding>()

    private val args: InstructionFragmentArgs by navArgs()
    private lateinit var event: Event
    private lateinit var eventType: AppSupportedEvents

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstructionBinding.inflate(inflater, container, false)

        event = args.event
        eventType = getEventFromTitle(event.eventData.title)

        initGradientBackgroundAnimation(binding.root)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initStartPlayingButton()
    }

    private fun initStartPlayingButton() {
        binding.startPlayingButton.setOnClickListener {
            if (AppSupportedEvents.LINKED == eventType) {
                if (System.currentTimeMillis() < event.eventData.eventFrom.time) {
                    showToastMessage(
                        requireContext(),
                        "${event.eventData.title} event starts at ${event.eventData.eventFrom.getFormatted()}"
                    )
                } else if (System.currentTimeMillis() >= event.eventData.eventTo.time) {
                    showToastMessage(
                        requireContext(),
                        "${event.eventData.title} event ended at ${event.eventData.eventTo.getFormatted()}"
                    )
                } else {
                    findNavController().navigate(InstructionFragmentDirections.actionFragmentInstructionToFragmentLinked())
                }
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.eventData.link))
                requireContext().startActivity(intent)
            }
        }
    }

    private fun initViewPager() {
        val headings = listOf("Description", "Rules", "Format", "Resources")

        val data = args.event.eventData

        var eventDescription = data.description
        eventDescription += "<br><br><h3>Prizes</h3>"
        eventDescription += "<ul>"
        eventDescription += "<li>First Prize : ₹ ${data.prizeMoney[0]}</li>"
        eventDescription += "<li>Second Prize : ₹ ${data.prizeMoney[1]}</li>"
        eventDescription += "<li>Third Prize : ₹ ${data.prizeMoney[2]}</li>"
        eventDescription += "</ul>"

        val contents = listOf(
            eventDescription,
            data.rules,
            data.format,
            data.resources
        )

        binding.instuctionViewPager.apply {
            adapter = InstructionAdapter(contents)
            isUserInputEnabled = false
        }

        TabLayoutMediator(
            binding.instructionTabLayout,
            binding.instuctionViewPager
        ) { tab, position ->
            tab.text = headings[position]
        }.attach()
    }

}