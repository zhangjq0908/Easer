/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.core.ui.data.script

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ryey.easer.R
import ryey.easer.core.data.storage.ScriptDataStorage

class PickPredecessorsDialogFragment : DialogFragment() {
    private var listener: OnChosenListener? = null

    interface OnChosenListener {
        fun onPredecessorsChosen(chosenPredecessors: Collection<String>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OnChosenListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement OnChosenListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val candidates = ScriptDataStorage(it).list()
            arguments?.getStringArray(ARG_EXCLUDED)?.let { excludedPredecessors ->
                for (excluded in excludedPredecessors) {
                    candidates.remove(excluded)
                }
            }
            val checkedItems = BooleanArray(candidates.size)
            arguments?.getStringArray(ARG_CHOSEN)?.let { chosenPredecessors ->
                for (predecessor in chosenPredecessors) {
                    val index = candidates.indexOf(predecessor)
                    checkedItems[index] = true
                    selectedItems.add(index)
                }
            }
            val builder = AlertDialog.Builder(it)
            // Set the dialog title
            builder.setTitle(R.string.title_choose_predecessors)
                    .setMultiChoiceItems(candidates.toTypedArray(), checkedItems,
                            DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                                if (isChecked) {
                                    selectedItems.add(which)
                                } else if (selectedItems.contains(which)) {
                                    selectedItems.remove(Integer.valueOf(which))
                                }
                            })
                    .setPositiveButton(R.string.button_ok,
                            DialogInterface.OnClickListener { dialog, id ->
                                listener?.onPredecessorsChosen(selectedItems.map { index ->
                                    candidates[index]
                                })
                                dismiss()
                            })
                    .setNegativeButton(R.string.button_cancel,
                            DialogInterface.OnClickListener { dialog, id ->
                                dismiss()
                            })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    companion object {

        const val ARG_CHOSEN = "chosen"
        const val ARG_EXCLUDED = "excluded"

        fun createInstance(chosenPredecessors: Collection<String>, excludedPredecessors: Collection<String>): PickPredecessorsDialogFragment {
            val args = Bundle()
            args.putStringArray(ARG_CHOSEN, chosenPredecessors.toTypedArray())
            args.putStringArray(ARG_EXCLUDED, excludedPredecessors.toTypedArray())
            val fragment = PickPredecessorsDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}