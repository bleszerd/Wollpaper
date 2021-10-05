package bleszerd.com.github.wollpaper.core.components.confirm_dialog.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import bleszerd.com.github.wollpaper.core.components.confirm_dialog.model.ConfirmDialogBuilderState
import bleszerd.com.github.wollpaper.databinding.DialogConfirmBinding

class ConfirmDialog(
    private val builderState: ConfirmDialogBuilderState
): DialogFragment() {

    private lateinit var textTitle: TextView
    private lateinit var textText: TextView
    private lateinit var buttonPositiveButton: Button
    private lateinit var buttonNegativeButton: Button
    private lateinit var binding: DialogConfirmBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogConfirmBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateViewReferences()
        populateViewData()
        setupViewListeners()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
    }

    private fun populateViewReferences() {
        textTitle = binding.textDialogConfirmTitle
        textText = binding.textDialogConfirmText
        buttonPositiveButton = binding.buttonDialogConfirmPositive
        buttonNegativeButton = binding.buttonDialogConfirmNegative
    }

    private fun populateViewData() {
        builderState.apply {
            textTitle.text = title
            textText.text = text
            buttonPositiveButton.text = positiveButton.text
            buttonNegativeButton.text = negativeButton.text
        }
    }

    private fun setupViewListeners() {
        buttonPositiveButton.setOnClickListener {
            builderState.positiveButton.action(this)
            dismiss()
        }

        buttonNegativeButton.setOnClickListener {
            builderState.negativeButton.action(this)
            dismiss()
        }
    }
}