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
 */package com.raywenderlich.android.librarian.repository

import com.raywenderlich.android.librarian.database.dao.BookDao
import com.raywenderlich.android.librarian.database.dao.GenreDao
import com.raywenderlich.android.librarian.database.dao.ReadingListDao
import com.raywenderlich.android.librarian.database.dao.ReviewDao
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks

class LibrarianRepositoryImpl(
    private val bookDao: BookDao,
    private val genreDao: GenreDao,
    private val readingListDao: ReadingListDao,
    private val reviewDao: ReviewDao
) : LibrarianRepository {

  override fun addBook(book: Book) = bookDao.addBook(book)

  override fun getBooks(): List<BookAndGenre> = bookDao.getBooks()

  override fun getBookById(bookId: String): Book = bookDao.getBookById(bookId)

  override fun removeBook(book: Book) = bookDao.removeBook(book)

  override fun getGenres(): List<Genre> = genreDao.getGenres()

  override fun getGenreById(genreId: String): Genre = genreDao.getGenreById(genreId)

  override fun addGenres(genres: List<Genre>) = genreDao.addGenres(genres)

  override fun addReview(review: Review) = reviewDao.addReview(review)

  override fun removeReview(review: Review) = reviewDao.removeReview(review)

  override fun getReviewById(reviewId: String): BookReview {
    val review = reviewDao.getReviewById(reviewId)

    return BookReview(review, bookDao.getBookById(review.bookId))
  }

  override fun getReviews(): List<BookReview> = reviewDao.getReviews().map {
    BookReview(it, bookDao.getBookById(it.bookId))
  }

  override fun updateReview(review: Review) = reviewDao.updateReview(review)

  override fun addReadingList(readingList: ReadingList) = readingListDao.addReadingList(readingList)

  override fun getReadingLists(): List<ReadingListsWithBooks> =
      readingListDao.getReadingLists().map {
        ReadingListsWithBooks(it.id, it.name, emptyList())
      }

  override fun removeReadingList(readingList: ReadingList) =
      readingListDao.removeReadingList(readingList)

  override fun getBooksByGenre(genreId: String): List<BookAndGenre> =
      genreDao.getBooksByGenre(genreId).let { booksByGenre ->
        val books = booksByGenre.books ?: return emptyList()

        return books.map { BookAndGenre(it, booksByGenre.genre) }
      }
}