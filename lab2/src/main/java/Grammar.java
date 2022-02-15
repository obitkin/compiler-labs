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

    List<String> alp() {
        List<String> alphabet = new ArrayList<>();
        List<String> NounA = Arrays.asList("cat", "dog", "boy", "girl", "magazine", "bird", "student", "teacher", "man", "tourist", "chief", "soldier");
        List<String> NounTHE = Arrays.asList("boys", "girls", "students", "books", "teachers", "tourists");
        List<String> Noun = Arrays.asList("lunch", "people", "water", "art", "love", "happiness", "advice", "information", "news", "sugar", "butter", "electricity", "gas", "power", "money");
        List<String> P = Arrays.asList("We", "They", "I", "You");
        List<String> Ps = Arrays.asList("He", "She", "It");
        List<String> H = Arrays.asList("have", "haven't");
        List<String> Hs = Arrays.asList("has", "hasn't");
        List<String> Been = Arrays.asList("been");
        List<String> Articles = Arrays.asList("A", "The", "the", "a");
        List<String> adj = Arrays.asList("nice","beautiful","small","big","black","white","typical","interesting","new","old","self-centred","dull","clever");
        List<String> verbs = Arrays.asList("writing" , "reading" , "playing" , "finding" , "studying" , "shopping" , "watching" , "listening" ,
                "traveling" , "preparing" , "cooking" , "speaking" , "spending" , "standing" , "talking" , "living");
        List<String> Place_in_athe = Arrays.asList("garden","kitchen","factory","bank","river","city","bed");
        List<String> Place_in = Arrays.asList("London","Russia");
        List<String> Place_at = Arrays.asList("home","work","school","university","college","war");
        List<String> Place_at_the = Arrays.asList("door","airport","desk","gym","doctor’s","bottom","window","table");
        List<String> Place_on = Arrays.asList("balcony","bus","train","plane","shelf","box");
        List<String> F = Arrays.asList("morning","yesterday","Monday","Saturday","holidays");
        List<String> U = Arrays.asList("hours","days","seconds","minutes");
        List<String> add = Arrays.asList("and","for", "since", "in", "at", "on","1","2","3","4","5","6","7","8","9","0", ",", ".");
        alphabet.addAll(NounA);
        alphabet.addAll(NounTHE);
        alphabet.addAll(Noun);
        alphabet.addAll(Ps);
        alphabet.addAll(P);
        alphabet.addAll(H);
        alphabet.addAll(Hs);
        alphabet.addAll(Articles);
        alphabet.addAll(adj);
        alphabet.addAll(verbs);
        alphabet.addAll(Place_in_athe);
        alphabet.addAll(Place_in);
        alphabet.addAll(Place_at);
        alphabet.addAll(Place_at_the);
        alphabet.addAll(Place_on);
        alphabet.addAll(F);
        alphabet.addAll(U);
        alphabet.addAll(add);
        alphabet.addAll(Been);
        return alphabet;
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

    public void checkAlphabet() {
        relation = new ArrayList<>();
        if (sentence.equals(Arrays.asList("[", "]"))) {
            throw new RuntimeException("Цепочка не принадлежит грамматике " + getAll());
        }
        for (int i = 0; i < sentence.size() - 1; i++) {
            Pair<String, String> pair = Pair.of(sentence.get(i), sentence.get(i + 1));
            Relation rel = relationsMap.get(pair);
            if (rel == null) {
                relation.add(Relation.Q);
                if (!alp().contains(sentence.get(i)) && !sentence.get(i).equals("[")) {
                    throw new RuntimeException("Цепочка не принадлежит грамматике " + getAll() + "\nТерминал не из алфавита: " + sentence.get(i));
                }
                if (!alp().contains(sentence.get(i + 1)) && !sentence.get(i).equals("]")) {
                    throw new RuntimeException("Цепочка не принадлежит грамматике " + getAll() + "\nТерминал не из алфавита: " + sentence.get(i + 1));
                }
                break;
            }
            relation.add(relationsMap.get(pair));
        }
        relation = null;
    }

    List<String> key;

    void step(int start, int end) {
        key = sentence.subList(start, end);
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
        checkAlphabet();
        updateRelation();
        while (!sentence.equals(Arrays.asList("[", "S", "]"))) {
            System.out.print(getAll() + " ");
            step();
            System.out.println(rules.get(key) + " <= " + key);
        }
        System.out.println(getAll());
        return true;
    }

    Random random = new Random();

    public String generate() {
        String res;
        if (random.nextInt(3) > 1) {
            res = NounGR() + " been " + Verb_ing() + ".";
        } else {
            res = NounGR() + " been " + Verb_ing() + " " + Additional() + ".";
        }
//        process(res);
        return res;
    }

    String NounGR() {
        List<String> NounA = Arrays.asList("cat", "dog", "boy", "girl", "magazine", "bird", "student", "teacher", "man", "tourist", "chief", "soldier");
        List<String> NounTHE = Arrays.asList("boys", "girls", "students", "books", "teachers", "tourists");
        List<String> Noun = Arrays.asList("lunch", "people", "water", "art", "love", "happiness", "advice", "information", "news", "sugar", "butter", "electricity", "gas", "power", "money");
        List<String> P = Arrays.asList("We", "They", "I", "You");
        List<String> Ps = Arrays.asList("He", "She", "It");
        List<String> H = Arrays.asList("have", "haven't");
        List<String> Hs = Arrays.asList("has", "hasn't");
        List<String> Articles = Arrays.asList("A", "The", "", "A", "The");

        String result = "";
        String art = randStrings(Articles);
        result += art;
        String adj = AdjGR();
        if (!adj.isEmpty()) {
            if (!result.isEmpty()) {
                result += " ";
            }
            result += adj;
        }
        switch (art) {
            case "A" : {
                result += " " + randStrings(NounA) + " " + randStrings(Hs);
                break;
            }
            case "The" : {
                result += " " + randStrings(NounTHE) + " " + randStrings(H);
                break;
            }
            case "" : {
                if (!result.isEmpty()) {
                    result += " ";
                }
                switch (random.nextInt(3)) {
                    case 0 : {
                        result += randStrings(Noun) + " " + randStrings(H);
                        break;
                    }
                    case 1 : {
                        result = randStrings(P) + " " + randStrings(H);
                        break;
                    }
                    case 2 : {
                        result = randStrings(Ps) + " " + randStrings(Hs);
                        break;
                    }
                }
                break;
            }
        }
        return result;
    }

    String AdjGR() {
        List<String> adj = Arrays.asList("nice","beautiful","small","big","black","white","typical","interesting","new","old","self-centred","dull","clever");
        String res = "";
        String delimeter = "";
        while (random.nextInt(3) <= 1) {
            res += delimeter + randStrings(adj);
            delimeter = " ";
        }
        return res;
    }

    String Verb_ing() {
        List<String> verbs = Arrays.asList("writing" , "reading" , "playing" , "finding" , "studying" , "shopping" , "watching" , "listening" ,
                "traveling" , "preparing" , "cooking" , "speaking" , "spending" , "standing" , "talking" , "living");
        if (random.nextInt(3) == 0) {
            return randStrings(verbs);
        }
        String res = randStrings(verbs);
        String del = ", ";
        while (random.nextInt(2) == 0) {
            res += del + randStrings(verbs);
        }
        return res + " and " + randStrings(verbs);
    }

    String Additional() {
        switch (random.nextInt(5)) {
            case 0 : {
                return Time();
            }
            case 1 : {
                return Place();
            }
            default:  {
                return Place() + " " + Time();
            }
        }
    }

    String Place() {
        List<String> Place_in_athe = Arrays.asList("garden","kitchen","factory","bank","river","city","bed");
        List<String> Place_in = Arrays.asList("London","Russia");
        List<String> Place_at = Arrays.asList("home","work","school","university","college","war");
        List<String> Place_at_the = Arrays.asList("door","airport","desk","gym","doctor’s","bottom","window","table");
        List<String> Place_on = Arrays.asList("balcony","bus","train","plane","shelf","box");
        String res = "";
        switch (random.nextInt(3)) {
            case 0 : {
                res += "in ";
                switch (random.nextInt(3)) {
                    case 0 : {
                        res += "the ";
                        res += randStrings(Place_in_athe);
                        break;
                    }
                    case 1 : {
                        res += "a ";
                        res += randStrings(Place_in_athe);
                        break;
                    }
                    case 2 : {
                        res += randStrings(Place_in);
                        break;
                    }
                }
                break;
            }
            case 1 : {
                res += "at ";
                if (random.nextInt(2) == 0) {
                    res += "the " + randStrings(Place_at_the);
                }
                else {
                    res += randStrings(Place_at);
                }
                break;
            }
            case 2 : {
                res += "on ";
                if (random.nextInt(2) == 0) {
                    res += "the " + randStrings(Place_on);
                }
                else {
                    res += "a " + randStrings(Place_on);
                }
                break;
            }
        }
        return res;
    }

    String Time() {
        List<String> F = Arrays.asList("morning","yesterday","Monday","Saturday","holidays");
        List<String> U = Arrays.asList("hours","days","seconds","minutes");
        if (random.nextInt(2) == 0) {
            return "since" + " " + randStrings(F);
        }
        else {
            return "for" + " " + random.nextInt(99) + " " + randStrings(U);
        }
    }


    private String randStrings(List<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

}
