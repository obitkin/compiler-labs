package ru.spbstu.telematics;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

public class Context {

    public Queue<String> signals;
    public Stack<String> stack;
    public Entry current;

    public boolean isFinite() {
        return current.isFinite &&
                signals.isEmpty() &&
                stack.size() == 1 &&
                stack.peek().equals("⟂");
    }

    public boolean isFinite2() {
        return current.isFinite &&
                stack.size() == 1 &&
                stack.peek().equals("⟂");
    }

    public Context(Entry entry, String input) {
        if (input.isEmpty()) {
            signals = new LinkedList<>();
        } else {
            signals = Arrays.stream(input.split("")).collect(Collectors.toCollection(LinkedList::new));
        }
        stack = new Stack<>();
        stack.add("⟂");
        current = entry;
    }

    public Context(Entry entry, Stack<String> stack) {
        this.stack = stack;
        this.current = entry;
    }

    private Context(Entry entry, Queue<String> signals, Stack<String> stack) {
        this.current = entry;
        this.signals = signals;
        this.stack = stack;
    }

    public List<Pair<Context, Edge>> next() {
        List<Pair<Context, Edge>> result = new ArrayList<>();
        String stackNext = stack.peek();
        current.allowed("ε", stackNext).forEach(edge -> {
            Queue<String> signalsNew = new LinkedList<>(signals);
            Stack<String> stackNew = (Stack<String>) stack.clone();
            stackNew.pop();
            List<String> inputStack = new ArrayList<>(edge.output.getValue());
            Collections.reverse(inputStack);
            inputStack.forEach(stackNew::push);
            result.add(Pair.of(new Context(edge.output.getKey(), signalsNew, stackNew), edge));
        });
        current.allowed(signals.peek(), stackNext).forEach(edge -> {
            Queue<String> signalsNew = new LinkedList<>(signals);
            Stack<String> stackNew = (Stack<String>) stack.clone();
            signalsNew.poll();
            stackNew.pop();
            List<String> inputStack = new ArrayList<>(edge.output.getValue());
            Collections.reverse(inputStack);
            inputStack.forEach(stackNew::push);
            result.add(Pair.of(new Context(edge.output.getKey(), signalsNew, stackNew), edge));
        });
        return result;
    }

    public List<Pair<Context, Edge>> nextRandom() {
        List<String> signalsAll = current.list.stream()
                .map(edge -> edge.input.getKey())
                .distinct()
                .collect(Collectors.toList());
        List<Pair<Context, Edge>> result = new ArrayList<>();
        String stackNext = stack.peek();
        signalsAll.forEach(signal -> {
            current.allowed(signal, stackNext).forEach(edge -> {
                Stack<String> stackNew = (Stack<String>) stack.clone();
                stackNew.pop();
                List<String> inputStack = new ArrayList<>(edge.output.getValue());
                Collections.reverse(inputStack);
                inputStack.forEach(stackNew::push);
                result.add(Pair.of(new Context(edge.output.getKey(), stackNew), edge));
            });
        });
        return result;
    }

    @Override
    public String toString() {
        List<String> reversedStack = new ArrayList<>(stack);
        Collections.reverse(reversedStack);
        return String.format("Состояние = %3s Цепочка = %s Стек = %s", current.name, signals, reversedStack);
    }
}
