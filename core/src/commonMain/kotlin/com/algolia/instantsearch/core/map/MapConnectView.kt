package com.algolia.instantsearch.core.map

import com.algolia.instantsearch.core.item.connectView


public fun <K, V> MapViewModel<K, V>.connectView(view: MapView<K, V>) {
    connectView(view) { it }
    view.onClick  = { remove(it) }
}