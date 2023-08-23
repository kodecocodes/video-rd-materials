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


package com.kodeco.android.librarian.ui.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodeco.android.librarian.R
import com.kodeco.android.librarian.databinding.FragmentBooksBinding
import com.kodeco.android.librarian.ui.addBook.AddBookActivity
import com.kodeco.android.librarian.ui.filter.Filter
import com.kodeco.android.librarian.ui.filter.FilterPickerDialogFragment
import com.kodeco.android.librarian.ui.viewmodel.BookViewModel
import com.kodeco.android.librarian.utils.createAndShowDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val REQUEST_CODE_ADD_BOOK = 101

class BooksFragment : Fragment() {

  private val adapter by lazy { BookAdapter(::onItemLongTapped) }
 private val  bookViewModel: BookViewModel by viewModel()
  private var filter: Filter? = null

  private var _binding: FragmentBooksBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    _binding = FragmentBooksBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUi()
  }

  private fun initUi() {
    with (binding) {
      pullToRefresh.setOnRefreshListener {
    // TODO: Load Books
      }

      booksRecyclerView.adapter = adapter
      booksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
      addBook.setOnClickListener {
        startActivityForResult(AddBookActivity.getIntent(requireContext()), REQUEST_CODE_ADD_BOOK)
      }

      filterBooks.setOnClickListener {
        val dialog = FilterPickerDialogFragment { filter ->
          this@BooksFragment.filter = filter

        }

        dialog.show(requireFragmentManager(), null)
      }
    }
  }







  private fun onItemLongTapped(book: Any) {
    createAndShowDialog(requireContext(),
        getString(R.string.delete_title),
        getString(R.string.delete_message, ""),
        onPositiveAction = {
          // TODO: Remove Book
        }
    )
  }


}