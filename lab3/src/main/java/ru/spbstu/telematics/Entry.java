package ru.spbstu.telematics;

import java.util.Map;
import java.util.TreeMap;

class Entry {

    private String name;

    private boolean isFinite;
    private boolean isInitial;

    private Map<Character, Entry> map;

    public Entry(String name, boolean isFinite, boolean isInitial) {
        this.name = name;
        this.isFinite = isFinite;
        this.isInitial = isInitial;
    }

    public void setMap(Entry entryA, Entry entryB) {
        this.map = new TreeMap<>();
        map.put('a', entryA);
        map.put('b', entryB);
    }

    public Entry getNext(Character input) {
        Entry next = map.get(input);
        if (next == null) {
            throw new IllegalArgumentException("\nЗначение '" + input + "' не из алфавита");
        }
        return next;
    }

    public boolean isFinite() {
        return isFinite;
    }

    public String getName() {
        return name;
    }
}