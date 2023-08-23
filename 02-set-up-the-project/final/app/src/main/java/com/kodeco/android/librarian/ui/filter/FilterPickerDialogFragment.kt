/*
* Copyright (c) 2023 Razeware LLC
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
* distribute, sublicense, create a derivative work, and/or sell copies of the
* Software in any work that is designed, intended, or marketed for pedagogical or
* instructional purposes related to programming, coding, application development,
* or information technology.  Permission for such use, copying, modification,
* merger, publication, distribution, sublicensing, creation of derivative works,
* or sale is expressly withheld.
*
* This project and source code may use libraries or frameworks that are
* released under various Open-Source licenses. Use of those libraries and
* frameworks are governed by their own individual licenses.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/


package com.kodeco.android.librarian.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.kodeco.android.librarian.R
import com.kodeco.android.librarian.databinding.DialogFilterBooksBinding
import com.kodeco.android.librarian.ui.viewmodel.BookViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterPickerDialogFragment(private val onFilterSelected: (Filter?) -> Unit)
  : DialogFragment() {
  private var _binding: DialogFilterBooksBinding? = null

  private val binding get() = _binding!!

  private val bookViewModel: BookViewModel by sharedViewModel()
  private var genres: List<Any> = emptyList()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

    _binding = DialogFilterBooksBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUi()

  }

  private fun initUi() {
    with(binding) {
      filterOptions.setOnCheckedChangeListener { _, checkedId ->
        updateOptions(checkedId)
      }

      selectFilter.setOnClickListener { filterBooks() }
    }
  }




  private fun initFilterAdapter(genreNames: List<String>) {
    binding.genrePicker.adapter = ArrayAdapter(
        requireContext(),
        android.R.layout.simple_spinner_dropdown_item,
        genreNames
    )
  }


  private fun filterBooks() {
    lifecycleScope.launch {
      with(binding) {
        val selectedGenre = ""// TODO: Get selected genre id


        val rating = ratingPicker.rating.toInt()

        if (selectedGenre == null && filterOptions.checkedRadioButtonId == R.id.byGenreFilter) {
          return@launch
        }

        if ((rating < 1 || rating > 5) && filterOptions.checkedRadioButtonId == R.id.byRatingFilter) {
          return@launch
        }

        val filter = when (filterOptions.checkedRadioButtonId) {
          R.id.byGenreFilter -> ByGenre(selectedGenre ?: "")
          R.id.byRatingFilter -> ByRating(ratingPicker.rating.toInt())
          else -> null
        }


        onFilterSelected(filter)
        dismissAllowingStateLoss()
      }
    }
  }

  private fun updateOptions(checkedId: Int) {

    with(binding) {
      if (checkedId == noFilter.id) {
        genrePicker.visibility = View.GONE
        ratingPicker.visibility = View.GONE
      }

      genrePicker.visibility = if (checkedId == byGenreFilter.id) View.VISIBLE else View.GONE
      ratingPicker.visibility = if (checkedId == byRatingFilter.id) View.VISIBLE else View.GONE
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NO_TITLE, R.style.FragmentDialogTheme)
  }

  override fun onStart() {
    super.onStart()
    dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}