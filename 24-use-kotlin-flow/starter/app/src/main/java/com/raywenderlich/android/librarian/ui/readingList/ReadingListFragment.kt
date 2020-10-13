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

package com.raywenderlich.android.librarian.ui.readingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.librarian.App
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.ui.readingList.dialog.AddReadingListDialogFragment
import com.raywenderlich.android.librarian.ui.readingListDetails.ReadingListDetailsActivity
import com.raywenderlich.android.librarian.utils.createAndShowDialog
import com.raywenderlich.android.librarian.utils.toast
import kotlinx.android.synthetic.main.fragment_reading_list.*
import kotlinx.coroutines.launch

class ReadingListFragment : Fragment() {

  private val adapter by lazy { ReadingListAdapter(::onItemSelected, ::onItemLongTapped) }
  private val repository by lazy { App.repository }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_reading_list, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initListeners()
    initUi()
    loadReadingLists()
  }

  private fun initUi() {
    readingListRecyclerView.layoutManager = LinearLayoutManager(context)
    readingListRecyclerView.adapter = adapter
  }

  private fun loadReadingLists() = lifecycleScope.launch {
    adapter.setData(repository.getReadingLists())
    pullToRefresh.isRefreshing = false
  }

  private fun initListeners() {
    addReadingList.setOnClickListener {
      showAddReadingListDialog()
    }

    pullToRefresh.setOnRefreshListener { loadReadingLists() }
  }

  private fun showAddReadingListDialog() {
    val fragmentManager = fragmentManager ?: return

    val dialog = AddReadingListDialogFragment {
      activity?.toast("List created!")
      loadReadingLists()
    }

    dialog.show(fragmentManager, null)
  }

  private fun onItemLongTapped(readingList: ReadingListsWithBooks) {
    createAndShowDialog(requireContext(),
        getString(R.string.delete_title),
        getString(R.string.delete_message, readingList.name),
        onPositiveAction = { removeReadingList(readingList) }
    )
  }

  private fun removeReadingList(readingList: ReadingListsWithBooks) {
    lifecycleScope.launch {
      repository.removeReadingList(ReadingList(readingList.id, readingList.name))
      loadReadingLists()
    }
  }

  private fun onItemSelected(readingList: ReadingListsWithBooks) {
    startActivity(ReadingListDetailsActivity.getIntent(requireContext(), readingList))
  }
}