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

package com.raywenderlich.android.librarian.ui.addReview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.raywenderlich.android.librarian.App
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Review
import kotlinx.android.synthetic.main.activity_add_book_review.*
import kotlinx.coroutines.launch
import java.util.*

class AddBookReviewActivity : AppCompatActivity() {

  private val repository by lazy { App.repository }

  companion object {
    fun getIntent(context: Context) = Intent(context, AddBookReviewActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_book_review)
    initUi()
  }

  private fun initUi() {
    lifecycleScope.launch {
      bookOption.adapter = ArrayAdapter(
          this@AddBookReviewActivity,
          android.R.layout.simple_spinner_dropdown_item,
          repository.getBooks().map { it.book.name }
      )
    }

    addReview.setOnClickListener { addBookReview() }
  }

  private fun addBookReview() = lifecycleScope.launch {
    val rating = reviewRating.rating.toInt()
    val bookId = repository.getBooks()
        .firstOrNull { it.book.name == bookOption.selectedItem }?.book?.id

    val imageUrl = bookImageUrl.text.toString()
    val notes = reviewNotes.text.toString()

    if (bookId != null && imageUrl.isNotBlank() && notes.isNotBlank()) {
      val bookReview = Review(
          bookId = bookId,
          rating = rating,
          notes = notes,
          imageUrl = imageUrl,
          lastUpdatedDate = Date(),
          entries = emptyList()
      )

      repository.addReview(bookReview)
      setResult(Activity.RESULT_OK)
      finish()
    }
  }
}