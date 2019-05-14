package com.algolia.instantsearch.demo.filter.segment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.android.selectable.SelectableRadioGroup
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.segment.FilterSegmentViewModel
import com.algolia.instantsearch.helper.filter.segment.connectFilterState
import com.algolia.instantsearch.helper.filter.segment.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_filter_segment.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterSegmentDemo : AppCompatActivity() {

    private val gender = Attribute("gender")
    private val colors
        get() = mapOf(
            gender.raw to ContextCompat.getColor(this, android.R.color.holo_orange_light)
        )

    private val index = client.initIndex(IndexName("mobile_demo_filter_segment"))
    private val groupID = FilterGroupID.And(gender)
    private val searcher = SearcherSingleIndex(index)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_segment)
        val viewModelGender = FilterSegmentViewModel(
            items = mapOf(
                R.id.male to Filter.Facet(gender, "male"),
                R.id.female to Filter.Facet(gender, "female")
            ),
            selected = R.id.male
        )
        val viewGender = SelectableRadioGroup(radioGroupGender)

        viewModelGender.connectFilterState(searcher.filterState, groupID)
        viewModelGender.connectView(viewGender)

        onChangeThenUpdateFiltersText(searcher.filterState, colors, filtersTextView)
        onClearAllThenClearFilters(searcher.filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher)

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}