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

package com.raywenderlich.android.librarian.ui.readingListDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.ui.bookPicker.BookPickerDialogFragment
import com.raywenderlich.android.librarian.ui.books.BookAdapter
import com.raywenderlich.android.librarian.utils.createAndShowDialog
import com.raywenderlich.android.librarian.utils.gone
import com.raywenderlich.android.librarian.utils.visible
import kotlinx.android.synthetic.main.activity_reading_list_details.*

class ReadingListDetailsActivity : AppCompatActivity() {

  private val adapter by lazy { BookAdapter(::onItemLongTapped) }
  private var readingList: ReadingListsWithBooks? = null

  companion object {
    private const val KEY_BOOK_REVIEW = "book_review"

    fun getIntent(context: Context, readingList: ReadingListsWithBooks): Intent {
      val intent = Intent(context, ReadingListDetailsActivity::class.java)

      intent.putExtra(KEY_BOOK_REVIEW, readingList)
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_reading_list_details)
    initUi()
  }

  private fun initUi() {
    readingList = intent.getParcelableExtra(KEY_BOOK_REVIEW)

    val data = readingList

    if (data == null) {
      finish()
      return
    }
    addBookToList.setOnClickListener { showBookPickerDialog() }
    pullToRefresh.setOnRefreshListener { refreshList() }

    toolbar.title = data.name

    if (data.books.isEmpty()) {
      noBooksView.visible()
      booksRecyclerView.gone()
    } else {
      noBooksView.gone()
      booksRecyclerView.visible()
    }

    booksRecyclerView.layoutManager = LinearLayoutManager(this)
    booksRecyclerView.adapter = adapter
    adapter.setData(data.books)
  }

  private fun refreshList() {
    val data = readingList

    if (data == null) {
      pullToRefresh.isRefreshing = false
      return
    }

    val refreshedList = readingList // TODO load from DB
    readingList = refreshedList

    adapter.setData(refreshedList?.books ?: emptyList())
    pullToRefresh.isRefreshing = false
  }

  private fun showBookPickerDialog() {
    val dialog = BookPickerDialogFragment { bookId ->
      addBookToReadingList(bookId)
    }

    dialog.show(supportFragmentManager, null)
  }

  private fun addBookToReadingList(bookId: String) {
    val data = readingList

    if (data != null) {
      val bookIds = (data.books.map { it.book.id } + bookId).distinct()

      val newReadingList = ReadingList(
          data.id,
          data.name,
          bookIds
      )
      // TODO update reading list

      refreshList()
    }
  }

  private fun removeBookFromReadingList(bookId: String) {
    val data = readingList

    if (data != null) {
      val bookIds = data.books.map { it.book.id } - bookId

      val newReadingList = ReadingList(
          data.id,
          data.name,
          bookIds
      )

      // TODO update reading list

      refreshList()
    }
  }

  private fun onItemLongTapped(book: Book) {
    createAndShowDialog(this,
        getString(R.string.delete_title),
        getString(R.string.delete_message, book.name),
        onPositiveAction = {
          removeBookFromReadingList(book.id)
        }
    )
  }
}