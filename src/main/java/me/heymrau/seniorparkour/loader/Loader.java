package me.heymrau.seniorparkour.loader;

import java.util.List;

public interface Loader<S, T> {
    T loadOne(S source);

    default List<T> loadAll(List<S> sources) {
        return sources.stream()
                .map(this::loadOne)
                .toList();
    }
}
