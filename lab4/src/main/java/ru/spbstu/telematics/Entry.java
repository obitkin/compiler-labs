package ru.spbstu.telematics;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Entry {

    public String name;
    public boolean isFinite;
    public List<Edge> list = new ArrayList<>();

    public Entry(String name, boolean isFinite) {
        this.name = name;
        this.isFinite = isFinite;
    }

    public void add(Edge edge) {
        edge.from = this;
        list.add(edge);
    }

    public List<Edge> allowed(String signal, String stack) {
        return list.stream()
                .filter(pair -> pair.input.equals(Pair.of(signal, stack)))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name;
    }
}
