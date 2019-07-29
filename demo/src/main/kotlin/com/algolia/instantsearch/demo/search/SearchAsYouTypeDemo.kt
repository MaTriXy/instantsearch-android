package com.algolia.instantsearch.demo.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.demo.list.movie.MovieAdapter
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.SearchBoxWidget
import com.algolia.instantsearch.helper.searchbox.SearchMode
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectListAdapter
import com.algolia.search.helper.deserialize
import kotlinx.android.synthetic.main.demo_search.*
import kotlinx.android.synthetic.main.include_search.*


class SearchAsYouTypeDemo : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val widgetSearchBox = SearchBoxWidget(searcher, searchMode = SearchMode.AsYouType)
    private val connection = ConnectionHandler(widgetSearchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_search)

        val adapter = MovieAdapter()
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection.apply {
            +widgetSearchBox.connectView(searchBoxView)
            +searcher.connectListAdapter(adapter) { hits -> hits.deserialize(Movie.serializer()) }
        }

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureRecyclerView(list, adapter)
        configureSearchView(searchView, getString(R.string.search_movies))

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}
