import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;


public class Grammar {

    Map<List<String>, String> rules;
    Map<Pair<String, String>, Relation> relationsMap;

    public Grammar(Map<Pair<String, String>, Relation> relationsMap, Map<List<String>, String> rules) {
        this.relationsMap = relationsMap;
        this.rules = rules;
    }

    List<String> sentence;
    List<Relation> relation;

    List<String> parse(String sentence) {
        List<String> res = Arrays.stream(sentence.split(" "))
                .flatMap(str -> Arrays.stream(str.replaceAll(",", "ε,ε").split("ε")))
                .flatMap(str -> Arrays.stream(str.replaceAll("\\.", "ε.").split("ε")))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toList());
        List<String> result = new ArrayList<>();
        res.forEach(str -> {
            if (str.matches("^\\d+$")) {
                result.addAll(Arrays.asList(str.split("")));
            } else {
                result.add(str);
            }
        });
        return result;
    }

    public String getAll() {
        List<String> all = new ArrayList<>();
        int indexSentence = 0;
        int indexRelation = 0;
        while (indexSentence < sentence.size() || indexRelation < relation.size()) {
            if (indexSentence < sentence.size()) {
                all.add(sentence.get(indexSentence++));
            }
            if (indexRelation < relation.size()) {
                all.add(relation.get(indexRelation++).toString());
            }
        }
        String result = "";
        for (String str : all) {
            result += str;
            result += " ";
        }
        return result;
    }

    public void updateRelation() {
        relation = new ArrayList<>();
        for (int i = 0; i < sentence.size() - 1; i++) {
            Pair<String, String> pair = Pair.of(sentence.get(i), sentence.get(i + 1));
            Relation rel = relationsMap.get(pair);
            if (rel == null) {
                if (sentence.equals(Arrays.asList("[", "S", "]"))) {
                    return;
                }
                relation.add(Relation.Q);
                throw new RuntimeException("Цепочка не принадлежит грамматике " + getAll());
            }
            relation.add(relationsMap.get(pair));
        }
    }

    void step(int start, int end) {
        List<String> key = sentence.subList(start, end);
        List<String> sentenceNew = new ArrayList<>();
        for (int i = 0; i < sentence.size(); i++) {
            if (i < start || i >= end) {
                sentenceNew.add(sentence.get(i));
            }
        }
        sentence = sentenceNew;
        sentence.add(start, rules.get(key));
        updateRelation();
    }

    void step() {
        int indexStart = -1;
        int indexEnd = -1;
        for (int i = 0; i < relation.size(); i++) {
            if (relation.get(i).equals(Relation.MORE)) {
                indexEnd = i;
                break;
            }
        }
        for (int i = indexEnd; i >= 0; i--) {
            if (relation.get(i).equals(Relation.LESS)) {
                indexStart = i;
                break;
            }
        }
        if (indexStart < indexEnd && indexStart != -1) {
            step(indexStart + 1, indexEnd + 1);
        } else {
            if (sentence.equals(Arrays.asList("[", "S", "]"))) {
                return;
            }
            throw new RuntimeException("Цепочка не принадлежит грамматике " + getAll());
        }
    }

    public boolean process(String input) {
        sentence = parse(input);
        sentence.add(0, "[");
        sentence.add("]");
        updateRelation();
        while (!sentence.equals(Arrays.asList("[", "S", "]"))) {
            System.out.println(getAll());
            step();
        }
        System.out.println(getAll());
        return true;
    }

    public String generate() {
        Random random = new Random();
        String output;
        do {
            int size = random.nextInt(25) + random.nextInt(25);
            output = "";
            for (int i = 0; i < size; i++) {
                if (output.isEmpty()) {
                    output += randStrings(List.of("(", "a"));
                }
                String last = String.valueOf(output.charAt(output.length() - 1));
                switch (last) {
                    case "(" : {
                        output += randStrings(List.of("(", "a"));
                        break;
                    }
                    case "a" : {
                        output += randStrings(List.of("+", "*", ")"));
                        break;
                    }
                    case ")" : {
                        output += randStrings(List.of("+", ")", "*"));
                        break;
                    }
                    case "*" :
                    case "+" : {
                        output += randStrings(List.of("a", "("));
                        break;
                    }
                    default: {
                        throw new RuntimeException("Unexpected symbol");
                    }
                }
            }
            output += "$";
        } while (!process(output));
        return output;
    }

    private String randStrings(List<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

}
