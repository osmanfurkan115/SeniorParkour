package me.heymrau.seniorparkour.menu.page;

import com.google.common.base.Preconditions;

import java.util.List;

public class Page<T> {

    private final List<T> data;
    private final int pageNumber;
    private final int pageSize;

    public Page(List<T> data, int pageNumber, int pageSize) {
        Preconditions.checkArgument(pageNumber > 0, "pageNumber must be > 0");
        Preconditions.checkArgument(pageSize > 0, "pageSize must be > 0");

        this.data = data.stream().distinct().toList();
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public Page(List<T> data, int pageSize) {
        this(data, 1, pageSize);
    }

    public Page(List<T> data) {
        this(data, 1);
    }

    public static <T> Page<T> of(List<T> data) {
        return new Page<>(data);
    }

    public static <T> Page<T> of(List<T> data,  int pageSize) {
        return new Page<>(data, pageSize);
    }

    public Page<T> previous() {
        if (pageNumber == 1) return this;

        return page(pageNumber - 1);
    }

    public Page<T> next() {
        return page(pageNumber + 1);
    }

    public Page<T> page(int pageNumber) {
        return new Page<>(this.data, pageNumber, this.pageSize);
    }

    public List<T> getContent() {
        int startIndex = (pageNumber - 1) * pageSize;
        int lastIndex = pageNumber * pageSize;

        if (startIndex >= data.size()) return List.of();

        return data.subList(startIndex, Math.min(lastIndex, data.size()));
    }

    public int position(T item) {
        return data.indexOf(item) + 1;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
