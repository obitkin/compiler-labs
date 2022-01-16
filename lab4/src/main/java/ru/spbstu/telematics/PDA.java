package ru.spbstu.telematics;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

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
        String replaced = input.replace("a", "").replace("b", "");
        if (!replaced.isEmpty()) {
            return "Ошибка: символ не из алфавита: '" + replaced.charAt(0) + "'";
        }
        StringBuilder textResult = new StringBuilder();
        boolean result = recursive(new Context(startState, input), textResult);
        if (!result) {
            textResult = new StringBuilder("Цепочка НЕ принадлежит грамматике");
        }
        return textResult.toString();
    }

    public boolean recursive(Context context, StringBuilder res) {
        if (context.isFinite()) {
            res.append(context).append("\n").append("Цепочка принадлежит грамматике");
            return true;
        }
        List<Pair<Context, Edge>> next = context.next();
        for (Pair<Context, Edge> pair : next) {
            boolean result = recursive(pair.getKey(), res);
            if (result) {
                res.insert(0, context + " " + pair.getValue() + "\n");
                return true;
            }
        }
        return false;
    }

    public String generate(int size) {
        String res = recursiveGenerate(new Context(startState, ""),"", size);
        if (res == null) {
            return "Нет последовательности заданной длины: " + size;
        }
        return res;
    }

    public String recursiveGenerate(Context context, String str, int size) {
        if (context.isFinite() || str.length() >= size) {
            if (str.length() == size) {
                return str;
            } else {
                return null;
            }
        }
        List<Pair<Context, Edge>> next = context.nextRandom();
        Collections.shuffle(next);
        for (Pair<Context, Edge> pair : next) {
            String strNew = str;
            if (!pair.getValue().input.getKey().equals("ε")) {
                strNew += pair.getValue().input.getKey();
            }
            String result = recursiveGenerate(pair.getKey(), strNew, size);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
