package bleszerd.com.github.wollpaper.core.components.confirm_dialog.model

import bleszerd.com.github.wollpaper.core.components.confirm_dialog.ui.ConfirmDialog

data class ConfirmDialogBuilderButtonState(
    val text: String? = null,
    val action: (ConfirmDialog) -> Unit,
)