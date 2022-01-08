package ru.spbstu.telematics;

import java.util.Random;

public class DFA {

    private Entry current;
    private boolean isFirst;
    private static final Random random = new Random();

    public void setUP() {
        isFirst = true;
        Entry A_ = new Entry("A'", false, true);
        Entry B_ = new Entry("B'", false, false);
        Entry A = new Entry("A", true,  false);
        Entry B = new Entry("B", false,  false);
        Entry C = new Entry("C", false,  false);
        A_.setMap(B_, C);
        B_.setMap(C, A);
        A.setMap(B, C);
        B.setMap(C, A);
        C.setMap(C, C);
        current = A_;
    }

    public DFA() {
        setUP();
    }

    public boolean isFinite() {
        return current.isFinite();
    }

    public DFA process(String input) {
        if ("".equals(input)) {
            System.out.println(current.getName());
            return this;
        }
        for (Character ch : input.toCharArray()) {
            if (isFirst) {
                System.out.print(current.getName());
                isFirst = false;
            }
            System.out.print(" -(" + ch + ")> ");
            current = current.getNext(ch);
            System.out.print(current.getName());
        }
        System.out.println();
        return this;
    }

    public String process(int length) {
        return recursive("", current, length);
    }

    public String recursive(String prev, Entry current, int length) {
        if (prev.length() == length) {
            if (current.isFinite()) {
                return prev;
            }
            else {
                return "";
            }
        }
        Character first = random.nextBoolean() ? 'a' : 'b';
        Character second = (first == 'a') ? 'b' : 'a';
        String res1 = recursive(prev + first, current.getNext(first), length);
        if (res1.length() == length) {
            return res1;
        }
        String res2 = recursive(prev + second, current.getNext(second), length);
        if (res2.length() == length) {
            return res2;
        }
        return prev;
    }

}
