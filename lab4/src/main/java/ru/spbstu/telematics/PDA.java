package ru.spbstu.telematics;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class PDA {

    public Entry startState;

    public PDA() {
        Entry q_0 = new Entry("q_0", false);
        Entry q = new Entry("q", false);
        Entry q_f = new Entry("q_f", true);
        q_0.add(new Edge("ε", "⟂", q, List.of("S'", "⟂")));
        q.add(new Edge("ε", "⟂", q_f, List.of("⟂")));
        q.add(new Edge("a", "a", q, List.of()));
        q.add(new Edge("b", "b", q, List.of()));
        q.add(new Edge("ε", "S'", q, List.of()));
        q.add(new Edge("ε", "S'", q, List.of("S")));
        q.add(new Edge("ε", "S", q, List.of("B")));
        q.add(new Edge("ε", "S", q, List.of("B", "b")));
        q.add(new Edge("ε", "B", q, List.of("a", "B")));
        q.add(new Edge("ε", "B", q, List.of("b", "B")));
        q.add(new Edge("ε", "B", q, List.of("a")));
        q.add(new Edge("ε", "B", q, List.of("b")));
        startState = q_0;
    }

    public String process(String input) {
        String result = recursive(new Context(startState, input));
        if (result == null) {
            result = "Цепочка НЕ принадлежит грамматике";
        }
        return result;
    }

    public String recursive(Context context) {
        if (context.isFinite()) {
            return context + "\n" + "Цепочка принадлежит грамматике";
        }
        List<Pair<Context, Edge>> next = context.next();
        for (Pair<Context, Edge> pair : next) {
            String result = recursive(pair.getKey());
            if (result != null) {
                return context + " " + pair.getValue() + "\n" + result;
            }
        }
        return null;
    }

    public void generate(int size) {

    }
}
