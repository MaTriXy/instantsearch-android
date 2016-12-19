/*
 * Initially made by Android-WTF: android-wtf.com/2013/06/how-to-easily-traverse-any-view-hierarchy-in-android/
 * License: "I haven’t applied a licence to it yet. But you are free to use it for any kind of project,
 * open source or commercial. A link to this page in source code would be great, though."
 */
package com.algolia.instantsearch.ui.utils;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * Helper to traverse a view hierarchy.
 */
public class LayoutTraverser {
    private final Processor processor;

    private LayoutTraverser(Processor processor) {
        this.processor = processor;
    }

    @NonNull public static LayoutTraverser build(Processor processor) {
        return new LayoutTraverser(processor);
    }

    public void traverse(@NonNull ViewGroup root) {
        final int childCount = root.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            final View child = root.getChildAt(i);
            processor.process(child);

            if (child instanceof ViewGroup) {
                traverse((ViewGroup) child);
            }
        }
    }

    public interface Processor {
        void process(View view);
    }
}