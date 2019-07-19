package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.connection.Connection


public class NumberRangeConnectionView<T>(
    public val viewModel: NumberRangeViewModel<T>,
    public val view: NumberRangeView<T>
) : Connection where T : Number, T : Comparable<T> {

    override var isConnected: Boolean = false

    private val updateBounds: (Range<T>?) -> Unit = { bounds ->
        view.setBounds(bounds)
    }
    private val updateRange: (Range<T>?) -> Unit = { range ->
        view.setRange(range)
    }

    override fun connect() {
        super.connect()
        viewModel.bounds.subscribePast(updateBounds)
        viewModel.range.subscribePast(updateRange)
        view.onRangeChanged = (viewModel.eventRange::send)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.bounds.unsubscribe(updateBounds)
        viewModel.bounds.unsubscribe(updateRange)
        view.onRangeChanged = null
    }
}