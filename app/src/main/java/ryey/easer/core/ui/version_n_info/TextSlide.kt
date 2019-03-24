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

package ryey.easer.core.ui.version_n_info

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ryey.easer.R


class TextSlide : Fragment() {
    private var stringId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                     savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome_1, container, false)
        val tvDescription = view.findViewById<TextView>(R.id.description)
        tvDescription.setText(stringId)
        tvDescription.movementMethod = LinkMovementMethod.getInstance()
        return view
    }

    companion object {

        private const val ARG_STRING_RES_ID = "STRING_RES_ID"

        fun newInstance(stringId: Int): TextSlide {
            val slide = TextSlide()

            val args = Bundle()
            args.putInt(ARG_STRING_RES_ID, stringId)
            slide.arguments = args

            return slide
        }
    }
}