package ru.spbstu.telematics;

import java.util.Random;

public class DFA {

    private Entry current;
    private boolean isFirst;
    private static final Random random = new Random();

    public void setUP() {
        isFirst = true;
        Entry s0 = new Entry("S0", false, true);
        Entry s1 = new Entry("S1", false, false);
        Entry s2 = new Entry("S2", true,  false);
        Entry s3 = new Entry("S3", true,  false);
        s0.setMap(s1, s0);
        s1.setMap(s1, s2);
        s2.setMap(s3, s1);
        s3.setMap(s3, s3);
        current = s0;
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
