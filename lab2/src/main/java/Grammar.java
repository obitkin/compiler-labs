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
                throw new RuntimeException("Incorrect string " + getAll());
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
        updateRelation();
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
            throw new RuntimeException("Incorrect string " + getAll());
        }
    }

    public boolean process(String input) {
        sentence = parse(input);
        sentence.add(0, "[");
        sentence.add("]");
        updateRelation();
        while (sentence.size() > 1) {
            System.out.println(getAll());
            step();
        }
        return true;
    }

    boolean NounGR(String inputNoun) {
        List<String> NounA = Arrays.asList("cat", "dog", "boy", "girl", "book", "bird", "student", "teacher", "man", "tourist", "chief");
        List<String> NounTHE = Arrays.asList("boys", "girls", "students", "books", "teachers", "tourists");
        List<String> Noun = Arrays.asList("lunch", "people", "water", "art", "love", "happiness", "advice", "information", "news", "sugar",
                "butter", "electricity", "gas", "power", "money");
        List<String> P = Arrays.asList("We", "They", "I", "You");
        List<String> Ps = Arrays.asList("He", "She", "It");
        List<String> H = Arrays.asList("have", "haven't");
        List<String> Hs = Arrays.asList("has", "hasn't");
        List<String> nouns = Arrays.stream(inputNoun.split(" ")).collect(Collectors.toList());
        if (nouns.size() < 2) {
            return false;
        }
        String first = nouns.get(0);
        if (first.equals("A")) {
            nouns.remove(0);
            nouns = AdjGR(nouns);
            return nouns.size() == 2 && NounA.contains(nouns.get(0)) && Hs.contains(nouns.get(1));
        } else if (first.equals("The")) {
            nouns.remove(0);
            nouns = AdjGR(nouns);
            if (nouns.size() == 2) {
                return NounTHE.contains(nouns.get(0)) && H.contains(nouns.get(1)) ||
                        NounA.contains(nouns.get(0)) && Hs.contains(nouns.get(1));
            }
            return false;
        } else if (P.contains(first)) {
            nouns.remove(0);
            return nouns.size() == 1 && H.contains(nouns.get(0));
        } else if (Ps.contains(first)) {
            nouns.remove(0);
            return nouns.size() == 1 && Hs.contains(nouns.get(0));
        } else if (nouns.size() == 2) {
            return Noun.contains(nouns.get(0)) && H.contains(nouns.get(1));
        }
        return false;
    }

    List<String> AdjGR(List<String> adj) {
        List<String> word = Arrays.asList("nice","beautiful","small","big","black","white","typical","interesting","new","old","self-centred","dull","clever");
        while (!adj.isEmpty() && word.contains(adj.get(0))) {
            adj.remove(0);
        }
        return adj;
    }

    boolean Verb(String verb) {
        List<String> words = Arrays.asList("writing" , "reading" , "playing" , "finding" , "studying" , "shopping" , "watching" , "listening" ,
                "traveling" , "preparing" , "cooking" , "speaking" , "spending" , "standing" , "talking" , "living");
        String newVerb = verb;
        String starts = starts(verb, words);
        if (starts == null) {
            return false;
        }
        while (starts != null) {
            newVerb = newVerb.replaceFirst(starts, "");
            if (newVerb.startsWith(", ")) {
                newVerb = newVerb.replaceFirst(", ", "");
            } else if (newVerb.startsWith(" and ")) {
                newVerb = newVerb.replaceFirst(" and ", "");
                starts = starts(verb, words);
                if (starts != null) {
                    newVerb = newVerb.replaceFirst(starts, "");
                }
            } else if (newVerb.startsWith(" ")) {
                newVerb = newVerb.replaceFirst(" ", "");
            } else {
                break;
            }
            starts = starts(newVerb, words);
        }
        return Additional(newVerb);
    }

    String starts(String str, List<String> list) {
        for (String l : list) {
            if (str.startsWith(l)) {
                return l;
            }
        }
        return null;
    }

    boolean Additional(String add) {
        List<String> IN = Arrays.asList("the garden", "London", "the kitchen", "a factory", "a bank", "the sea", "the river",
                "a city", "the world", "bed"
        );
        List<String> AT = Arrays.asList(
                "the bus stop", "the door", "desk", "home", "work", "school", "university", "college", "the airport"
        );
        List<String> ON = Arrays.asList(
                "a balcony", "the floor", "a bus", "a train", "a plane"
        );
        if (add.isEmpty()) {
            return true;
        }
        String after = Place(add);
        if (after == null) {
            return false;
        }
        return after.isEmpty();
    }

    String Place(String place) {
        List<String> IN = Arrays.asList("the garden", "London", "the kitchen", "a factory", "a bank", "the sea", "the river",
                "a city", "the world", "bed"
        );
        List<String> AT = Arrays.asList(
                "the bus stop", "the door", "desk", "home", "work", "school", "university", "college", "the airport"
        );
        List<String> ON = Arrays.asList(
                "a balcony", "the floor", "a bus", "a train", "a plane"
        );
        List<String> st = Arrays.asList(
                "in", "at", "on"
        );
        String starts = starts(place, st);
        String newStr;
        if (starts != null) {
            newStr = place.replaceFirst(starts, "");
            if (newStr.startsWith(" ")) {
                newStr = newStr.replaceFirst(" ", "");
            } else {
                return null;
            }
            if (starts.equals("in")) {
                starts = starts(newStr, IN);
                if (starts == null) {
                    return null;
                }
                newStr = place.replaceFirst(starts, "");
            } else if (starts.equals("at")) {
                starts = starts(newStr, AT);
                if (starts == null) {
                    return null;
                }
                newStr = place.replaceFirst(starts, "");
            } else if (starts.equals("on")) {
                starts = starts(newStr, ON);
                if (starts == null) {
                    return null;
                }
                newStr = place.replaceFirst(starts, "");
            }
            return newStr;
        } else {
            return place;
        }
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
