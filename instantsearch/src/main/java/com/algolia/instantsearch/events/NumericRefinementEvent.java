package com.algolia.instantsearch.events;

import android.support.annotation.NonNull;

import com.algolia.instantsearch.model.NumericRefinement;

public class NumericRefinementEvent extends RefinementEvent {

    /** A description of the refinement: {@link NumericRefinement#attribute attribute}
     * is refined with {@link NumericRefinement#value value}
     * using {@link NumericRefinement#operator operator}. */
    public @NonNull final NumericRefinement refinement;

    public NumericRefinementEvent(final @NonNull Operation operation, final @NonNull NumericRefinement refinement) {
        super(operation, refinement.attribute);
        this.refinement = refinement;
    }

    @Override public String toString() {
        return "NumericRefinementEvent{" +
                "operation=" + operation +
                ", attribute='" + attribute + '\'' +
                ", refinement=" + refinement +
                '}';
    }
}