package bleszerd.com.github.wollpaper.core.components.confirm_dialog.model

data class ConfirmDialogBuilderState(
    val title: String,
    val text: String? = null,
    val positiveButton: ConfirmDialogBuilderButtonState,
    val negativeButton: ConfirmDialogBuilderButtonState,
)