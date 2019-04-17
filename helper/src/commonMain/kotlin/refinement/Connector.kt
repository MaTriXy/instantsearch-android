package refinement

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import filter.FilterState
import refinement.RefinementMode.And
import refinement.RefinementMode.Or
import search.GroupID
import search.SearcherSingleIndex


public fun RefinementFacetsViewModel.connect(
    attribute: Attribute,
    searcher: SearcherSingleIndex,
    mode: RefinementMode = And,
    groupName: String = attribute.raw
) {
    val groupID = when (mode) {
        And -> GroupID.And(groupName)
        Or -> GroupID.Or(groupName)
    }
    val filterStateListener: (FilterState) -> Unit = { state ->
        selections = state.getFacets(groupID).orEmpty().map { it.value.raw as String }
    }

    filterStateListener(searcher.filterState)
    searcher.filterState.listeners += filterStateListener
    searcher.responseListeners += { response ->
        response.facets[attribute]?.let { refinements = it }
    }
    selectedListeners += { newSelections ->
        val currentFilters = selections.map { Filter.Facet(attribute, it) }.toSet()
        val newFilters = newSelections.map { Filter.Facet(attribute, it) }.toSet()

        searcher.filterState.notify {
            remove(groupID, currentFilters)
            add(groupID, newFilters)
        }
        searcher.search()
    }
}

fun RefinementFacetsViewModel.connect(presenter: RefinementFacetsPresenter) {
    refinementsListeners += { refinements ->
        presenter.refinements = refinements.map { it to selections.contains(it.value) }
    }
    selectionsListeners += { selections ->
        presenter.refinements = refinements.map { it to selections.contains(it.value) }
    }
}

fun RefinementFacetsViewModel.connect(view: RefinementFacetsView) {
    view.onClickRefinement { select(it.value) }
}

fun RefinementFacetsPresenter.connect(view: RefinementFacetsView) {
    listeners += { view.setRefinements(it) }
}