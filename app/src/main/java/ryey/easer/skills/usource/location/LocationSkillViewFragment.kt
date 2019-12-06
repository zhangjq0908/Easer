/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.skills.usource.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.ArraySet
import kotlinx.android.synthetic.main.skill_usource__location.*
import ryey.easer.R
import ryey.easer.commons.local_skill.InvalidDataInputException
import ryey.easer.commons.local_skill.ValidData
import ryey.easer.skills.SkillViewFragment
import ryey.easer.skills.usource.ScannerDialogFragment

class LocationSkillViewFragment : SkillViewFragment<LocationUSourceData>(), ScannerDialogFragment.OnListItemClickedListener<LocationCandidate> {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.skill_usource__location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageButton_pick.setOnClickListener {
            val dialogFrag = LocationScannerDialogFragment()
            dialogFrag.setTargetFragment(this, DIALOG_FRAGMENT)
            dialogFrag.show(fragmentManager!!, TAG_SCANNER)
        }
    }

    override fun _fill(@ValidData data: LocationUSourceData) {
        editText_location.setText(data.locations.joinToString("\n") {latLong ->
            latLong.toString()
        })
        text_radius.editText!!.setText(data.radius.toString())
        text_listen_min_time.editText!!.setText(data.listenMinTime.toString())
        text_threshold_age.editText!!.setText(data.thresholdAge.toString())
        text_threshold_accuracy.editText!!.setText(data.thresholdAccuracy.toString())
    }

    @ValidData
    @Throws(InvalidDataInputException::class)
    override fun getData(): LocationUSourceData {
        try {
            val locations = ArraySet<LatLong>(editText_location.text.split("\n")
                    .map { text -> LatLong.fromString(text) })
            val radius = text_radius.editText!!.text.toString().toInt()
            val listenMinTime = text_listen_min_time.editText!!.text.toString().toLong()
            val thresholdAge = text_threshold_age.editText!!.text.toString().toLong()
            val thresholdAccuracy = text_threshold_accuracy.editText!!.text.toString().toInt()
            return LocationUSourceData(locations, radius, listenMinTime, thresholdAge, thresholdAccuracy)
        } catch (e: Exception) {
            //TODO: More detailed
            throw InvalidDataInputException(e.toString())
        }
    }

    override fun onListItemClicked(data: LocationCandidate): Boolean {
        val latLong = LatLong(data.latitude, data.longitude)
        editText_location.setText(latLong.toString())
        return true
    }

    companion object {
        const val TAG_SCANNER = "scanner"
        const val DIALOG_FRAGMENT = 0
    }
}
