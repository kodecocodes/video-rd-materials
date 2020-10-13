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

package com.raywenderlich.android.librarian.ui.addBook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.raywenderlich.android.librarian.App
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.utils.toast
import kotlinx.android.synthetic.main.activity_add_book.*
import kotlinx.coroutines.launch

class AddBookActivity : AppCompatActivity() {

  private val repository by lazy { App.repository }

  companion object {
    fun getIntent(context: Context): Intent = Intent(context, AddBookActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_book)
    initUi()
  }

  private fun initUi() {
    addBook.setOnClickListener { createBook() }

    lifecycleScope.launch {
      genrePicker.adapter = ArrayAdapter(
          this@AddBookActivity,
          android.R.layout.simple_spinner_dropdown_item,
          repository.getGenres().map { it.name }
      )
    }
  }

  private fun createBook() = lifecycleScope.launch {
    val title = bookTitle.text.toString()
    val description = bookDescription.text.toString()
    val genreId = repository.getGenres().firstOrNull { it.name == genrePicker.selectedItem }?.id

    if (title.isNotBlank() && description.isNotBlank() && !genreId.isNullOrBlank()) {
      val book = Book(
          name = title,
          description = description,
          genreId = genreId
      )

      repository.addBook(book)
      toast("Book added! :]")
      setResult(Activity.RESULT_OK)
      finish()
    }
  }
}