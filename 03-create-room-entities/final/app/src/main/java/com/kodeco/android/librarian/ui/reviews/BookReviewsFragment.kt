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


package com.kodeco.android.librarian.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodeco.android.librarian.databinding.FragmentReviewsBinding
import com.kodeco.android.librarian.ui.addReview.AddBookReviewActivity
import com.kodeco.android.librarian.ui.viewmodel.ReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fetches and displays notes from the API.
 */
private const val REQUEST_CODE_ADD_REVIEW = 102

class BookReviewsFragment : Fragment() {

  private val adapter by lazy { BookReviewAdapter(onItemSelected = {},  )}
  private val reviewViewModel: ReviewViewModel by viewModel()

  private var _binding: FragmentReviewsBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentReviewsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initListeners()
    initUi()
  }

  private fun initUi() {
    with(binding) {
      reviewsRecyclerView.layoutManager = LinearLayoutManager(context)
      reviewsRecyclerView.adapter = adapter
    }
  }

  private fun initListeners() {
    with(binding) {
      pullToRefresh.isEnabled = false

      addBookReview.setOnClickListener {
        startActivityForResult(
          AddBookReviewActivity.getIntent(requireContext()), REQUEST_CODE_ADD_REVIEW
        )
      }
    }
  }










}