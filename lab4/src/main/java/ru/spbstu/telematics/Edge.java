package ru.spbstu.telematics;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Edge {

    public Entry from;
    public Pair<String, String> input;       //signal stack
    public Pair<Entry, List<String>> output; //next stack

    public Edge(String signal, String stack, Entry next, List<String> stackOut) {
        this.input = Pair.of(signal, stack);
        this.output = Pair.of(next, stackOut);
    }

    @Override
    public String toString() {
        return "δ = ((" + from.name + "," + input.getKey() + "," + input.getValue() + "), " + output + ")";
    }
}
