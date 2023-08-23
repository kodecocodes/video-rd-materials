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


package com.kodeco.android.librarian.ui.bookReviewDetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodeco.android.librarian.databinding.ActivityBookReviewDetailsBinding
import com.kodeco.android.librarian.ui.bookReviewDetails.readingEntries.AddReadingEntryDialogFragment
import com.kodeco.android.librarian.ui.bookReviewDetails.readingEntries.ReadingEntryAdapter
import com.kodeco.android.librarian.ui.viewmodel.BookViewModel
import com.kodeco.android.librarian.ui.viewmodel.ReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookReviewDetailsActivity : AppCompatActivity() {

  private val bookViewModel: BookViewModel by viewModel()
  private val reviewViewModel: ReviewViewModel by viewModel()
  private val adapter by lazy { ReadingEntryAdapter() }


  private lateinit var bookReviewBinding: ActivityBookReviewDetailsBinding


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bookReviewBinding = ActivityBookReviewDetailsBinding.inflate(layoutInflater)
    setContentView(bookReviewBinding.root)
    initUi()
  }

  private fun initUi() {
    with(bookReviewBinding) {
      readingEntriesRecyclerView.layoutManager = LinearLayoutManager(this@BookReviewDetailsActivity)
      readingEntriesRecyclerView.adapter = adapter
      addReadingEntry.setOnClickListener {
        val dialog = AddReadingEntryDialogFragment { readingEntry ->
          // Todo: Add new reading entry
        }
        dialog.show(supportFragmentManager, null)
      }

    }


  }
}

