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

package com.raywenderlich.android.librarian.ui.bookReviewDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.raywenderlich.android.librarian.App
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.ReadingEntry
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.ui.bookReviewDetails.readingEntries.AddReadingEntryDialogFragment
import com.raywenderlich.android.librarian.ui.bookReviewDetails.readingEntries.ReadingEntryAdapter
import com.raywenderlich.android.librarian.utils.createAndShowDialog
import com.raywenderlich.android.librarian.utils.formatDateToText
import com.raywenderlich.android.librarian.utils.toast
import kotlinx.android.synthetic.main.activity_book_review_details.*
import kotlinx.coroutines.launch

class BookReviewDetailsActivity : AppCompatActivity() {

  private var bookReview: BookReview? = null
  private val adapter by lazy { ReadingEntryAdapter(::onItemLongTapped) }
  private val repository by lazy { App.repository }

  companion object {
    private const val KEY_BOOK_REVIEW = "book_review"

    fun getIntent(context: Context, review: BookReview): Intent {
      val intent = Intent(context, BookReviewDetailsActivity::class.java)

      intent.putExtra(KEY_BOOK_REVIEW, review)
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_book_review_details)
    initUi()
  }

  private fun initUi() {
    readingEntriesRecyclerView.layoutManager = LinearLayoutManager(this)
    readingEntriesRecyclerView.adapter = adapter
    addReadingEntry.setOnClickListener {
      val dialog = AddReadingEntryDialogFragment { readingEntry ->
        addNewEntry(readingEntry)
      }
      dialog.show(supportFragmentManager, null)
    }

    loadData()
  }

  private fun loadData() {
    val review: BookReview = intent?.getParcelableExtra(KEY_BOOK_REVIEW) ?: return
    val reviewId = review.review.id

    displayData(reviewId)
  }

  private fun displayData(reviewId: String) = lifecycleScope.launch {
    refreshData(reviewId)
    val data = bookReview ?: return@launch
    val genre = repository.getGenreById(data.book.genreId)

    Glide.with(this@BookReviewDetailsActivity).load(data.review.imageUrl).into(bookImage)
    reviewTitle.text = data.book.name
    reviewRating.rating = data.review.rating.toFloat()
    reviewDescription.text = data.review.notes
    lastUpdated.text = formatDateToText(data.review.lastUpdatedDate)
    bookGenre.text = genre.name

//    adapter.setData(data.review.entries)
  }

  private suspend fun refreshData(id: String) {
    val storedReview = repository.getReviewById(id)

    bookReview = storedReview
  }

  private fun onItemLongTapped(readingEntry: ReadingEntry) {
    createAndShowDialog(
        this,
        getString(R.string.delete_title),
        getString(R.string.delete_entry_message),
        onPositiveAction = {
          removeReadingEntry(readingEntry)
        }
    )
  }

  private fun addNewEntry(readingEntry: ReadingEntry) {
    val data = bookReview?.review ?: return

//    val updatedReview = data.copy(entries = data.entries + readingEntry,
//        lastUpdatedDate = Date())

    // TODO update review
    toast("Entry added!")

    displayData(data.id)
  }

  private fun removeReadingEntry(readingEntry: ReadingEntry) {
    val data = bookReview ?: return
    val currentReview = data.review

//    val newReview = currentReview.copy(
//        entries = currentReview.entries - readingEntry,
//        lastUpdatedDate = Date()
//    )
    // TODO update review

    loadData()
  }
}