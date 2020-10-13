/*
 * Copyright (c) 2020 Razeware LLC
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

package com.raywenderlich.android.librarian.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.librarian.App
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.ui.addReview.AddBookReviewActivity
import com.raywenderlich.android.librarian.ui.bookReviewDetails.BookReviewDetailsActivity
import com.raywenderlich.android.librarian.utils.createAndShowDialog
import kotlinx.android.synthetic.main.fragment_reviews.*

/**
 * Fetches and displays notes from the API.
 */
private const val REQUEST_CODE_ADD_REVIEW = 102

class BookReviewsFragment : Fragment() {

  private val adapter by lazy { BookReviewAdapter(::onItemSelected, ::onItemLongTapped) }
  private val repository by lazy { App.repository }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_reviews, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initListeners()
    initUi()
    loadBookReviews()
  }

  private fun initUi() {
    reviewsRecyclerView.layoutManager = LinearLayoutManager(context)
    reviewsRecyclerView.adapter = adapter
  }

  private fun initListeners() {
    addBookReview.setOnClickListener {
      startActivityForResult(
          AddBookReviewActivity.getIntent(requireContext()), REQUEST_CODE_ADD_REVIEW
      )
    }

    pullToRefresh.setOnRefreshListener { loadBookReviews() }
  }

  private fun onItemSelected(item: BookReview) {
    startActivity(BookReviewDetailsActivity.getIntent(requireContext(), item))
  }

  private fun onItemLongTapped(item: BookReview) {
    createAndShowDialog(requireContext(),
        getString(R.string.delete_title),
        getString(R.string.delete_review_message, item.book.name),
        onPositiveAction = { removeReviewFromRepo(item) })
  }

  private fun removeReviewFromRepo(item: BookReview) {
    repository.removeReview(item.review)
    loadBookReviews()
  }

  private fun loadBookReviews() {
    adapter.setData(repository.getReviews())
    pullToRefresh.isRefreshing = false
  }
}