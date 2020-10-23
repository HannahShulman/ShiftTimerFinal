package com.shift.timer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.shift.timer.R
import com.shift.timer.animations.AnimationFinishedListener
import com.shift.timer.animations.AnimationUtils
import com.shift.timer.animations.Dismissible
import com.shift.timer.animations.RevealAnimationSetting
import com.shift.timer.databinding.DialogMoreViewBinding
import com.shift.timer.throttledClickListener
import com.shift.timer.viewBinding


class MoreViewDialog : DialogFragment(), Dismissible {

    val ARG_REVEAL_SETTINGS = "ARG_REVEAL_SETTINGS"

    private val binding: DialogMoreViewBinding by viewBinding(DialogMoreViewBinding::bind)

    override fun getTheme(): Int {
        return R.style.full_sheet_dialog
    }

    companion object {
        fun newInstance(setting: RevealAnimationSetting?): MoreViewDialog {
            return MoreViewDialog().apply {
                setting?.let {
                    arguments = bundleOf(ARG_REVEAL_SETTINGS to it)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_more_view, container, false)
        AnimationUtils.registerCircularRevealAnimation(context, view,
            requireArguments().getSerializable(ARG_REVEAL_SETTINGS) as RevealAnimationSetting?,
            getColor(requireContext(), R.color.cayanSelection), getColor(requireContext(), R.color.cayanSelection))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeBtn.throttledClickListener {
            dismiss(object : Dismissible.OnDismissedListener {
                override fun onDismissed() {
                    parentFragmentManager.beginTransaction().remove(this@MoreViewDialog).commitAllowingStateLoss();
                }

            })
        }
        binding.moreSuggestions.throttledClickListener { suggestImprovements() }
        binding.shareAppLink.throttledClickListener { shareThisApp() }
        binding.likeUs.throttledClickListener { likeUs() }
        binding.removeAds.throttledClickListener { removeAds() }
        binding.purchaseRecovery.throttledClickListener { purchaseRecovery() }
    }

    //functionality
    private fun suggestImprovements() {
        Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "maorduani@gmail.com", null)).apply {
            putExtra(Intent.EXTRA_SUBJECT, "Subject")
            putExtra(Intent.EXTRA_TEXT, "I'm an android app user, and I'd like to suggest")
        }.also { startActivity(Intent.createChooser(it, "Send suggestion...")) }
    }

    private fun shareThisApp() {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "I would like to share with you this app.")//add link to message
            type = "text/plain"
        }.also {
            Intent.createChooser(it, "Share via").also { intent ->
                startActivity(intent)
            }
        }
    }

    private fun likeUs() {

    }

    private fun removeAds() {

    }

    private fun purchaseRecovery() {

    }

    override fun dismiss(listener: Dismissible.OnDismissedListener?) {
        AnimationUtils.startCircularRevealExitAnimation(requireContext(), requireView(), requireArguments().getSerializable(ARG_REVEAL_SETTINGS) as RevealAnimationSetting,
            getColor(requireContext(), R.color.cayanSelection), getColor(requireContext(), R.color.cayanSelection), object : AnimationFinishedListener {
                override fun onAnimationFinished() {
                    listener?.onDismissed()
                }
            })
    }
}