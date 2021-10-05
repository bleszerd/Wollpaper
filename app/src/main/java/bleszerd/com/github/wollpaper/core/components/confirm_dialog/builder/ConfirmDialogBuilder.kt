package bleszerd.com.github.wollpaper.core.components.confirm_dialog.builder

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import bleszerd.com.github.wollpaper.core.components.confirm_dialog.model.ConfirmDialogBuilderButtonState
import bleszerd.com.github.wollpaper.core.components.confirm_dialog.model.ConfirmDialogBuilderState
import bleszerd.com.github.wollpaper.core.components.confirm_dialog.ui.ConfirmDialog

class ConfirmDialogBuilder(
    private val context: Context
) {
    private lateinit var _title: String
    private lateinit var _text: String
    private lateinit var _positiveButton: ConfirmDialogBuilderButtonState
    private lateinit var _negativeButton: ConfirmDialogBuilderButtonState

    fun setTitle(title: String): ConfirmDialogBuilder {
        _title = title
        return this
    }

    fun setTitle(title: Int): ConfirmDialogBuilder {
        _title = context.getString(title)
        return this
    }

    fun setText(text: String): ConfirmDialogBuilder {
        _text = text
        return this
    }

    fun setText(text: Int): ConfirmDialogBuilder {
        _text = context.getString(text)
        return this
    }

    fun setPositiveButton(text: String, action: (ConfirmDialog) -> Unit): ConfirmDialogBuilder {
        _positiveButton = ConfirmDialogBuilderButtonState(
            text, action
        )
        return this
    }

    fun setPositiveButton(text: Int, action: (ConfirmDialog) -> Unit): ConfirmDialogBuilder {
        _positiveButton = ConfirmDialogBuilderButtonState(
            context.getString(text), action
        )
        return this
    }

    fun setNegativeButton(text: String, action: (ConfirmDialog) -> Unit): ConfirmDialogBuilder {
        _negativeButton = ConfirmDialogBuilderButtonState(
            text, action
        )
        return this
    }

    fun setNegativeButton(text: Int, action: (ConfirmDialog) -> Unit): ConfirmDialogBuilder {
        _negativeButton = ConfirmDialogBuilderButtonState(
            context.getString(text), action
        )
        return this
    }

    fun build(): ConfirmDialog {
        val dialogState = ConfirmDialogBuilderState(
            _title, _text, _positiveButton, _negativeButton
        )

        return ConfirmDialog(dialogState)
    }
}