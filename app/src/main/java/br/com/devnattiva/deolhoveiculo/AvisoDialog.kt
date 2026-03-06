package br.com.devnattiva.deolhoveiculo

import android.content.Context
import android.content.DialogInterface
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AvisoDialog() {
    fun createDialog(
        context: Context,
        title: String,
        messageText: String,
        textPrimaryButton: String = "Sim",
        textSecundaryButton: String = "Não",
        textThirdButton: String = "Cancelar",
        primaryButtonAction: (dialog: DialogInterface) -> Unit,
        secundaryButtonAction: (dialog: DialogInterface) -> Unit,
        thirdButtonAction: (dialog: DialogInterface) -> Unit,
    ) {
        val dialogBuilder = MaterialAlertDialogBuilder(context, R.style.ThemeCustomDialog)
            .setBackground(android.R.color.transparent.toDrawable())
            .setTitle(title)
            .setMessage(messageText)
            .setPositiveButton(textPrimaryButton) { dialog, _ ->
                primaryButtonAction.invoke(dialog)
            }
            .setNegativeButton(textSecundaryButton) { dialog, _ ->
                secundaryButtonAction.invoke(dialog)
            }
            .setNeutralButton(textThirdButton) { dialog, _ ->
                thirdButtonAction.invoke(dialog)
            }
            .setCancelable(false)
            .create()

        dialogBuilder.show()
        dialogBuilder.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}