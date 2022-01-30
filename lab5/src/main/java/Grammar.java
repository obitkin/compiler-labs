import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Grammar {

    List<String> alp = Arrays.asList("a", "+", "*", "(", ")", "$");
    String start = "operator fun String.times(other: String): String {\n" +
            "    return other + this\n" +
            "}\nfun main(args: Array<String>) {\n\tvar i = 0\n\tprintln(";
    String mul = "*|";
    String plus = "+|";
    String open = "(|";
    String close = ")|";
    String end = ")\n}";
    String arg = "args[i++]";
    List<String> sem = Arrays.asList(start, mul, plus, open, close, end, arg);

    Pair<String, List<String>> r1 = Pair.of("S'", List.of("$", end, "S", start));
    Pair<String, List<String>> r2 = Pair.of("S", List.of("A", "B"));
    Pair<String, List<String>> r3 = Pair.of("A", List.of("A", "B", plus, "+"));
    Pair<String, List<String>> r4 = Pair.of("A", List.of());
    Pair<String, List<String>> r5 = Pair.of("B", List.of("C", "D"));
    Pair<String, List<String>> r6 = Pair.of("C", List.of("C", "D", mul, "*"));
    Pair<String, List<String>> r7 = Pair.of("C", List.of());
    Pair<String, List<String>> r8 = Pair.of("D", List.of(close, ")", "S", open, "("));
    Pair<String, List<String>> r9 = Pair.of("D", List.of(arg, "a"));

    HashMap<Pair<String, String>, Pair<String, List<String>>> table = new HashMap<>();

    public Grammar() {
        table.put(Pair.of("S'", "("), r1);
        table.put(Pair.of("S'", "a"), r1);
        table.put(Pair.of("S", "("), r2);
        table.put(Pair.of("S", "a"), r2);
        table.put(Pair.of("A", ")"), r4);
        table.put(Pair.of("A", "+"), r3);
        table.put(Pair.of("A", "$"), r4);
        table.put(Pair.of("B", "("), r5);
        table.put(Pair.of("B", "a"), r5);
        table.put(Pair.of("C", ")"), r7);
        table.put(Pair.of("C", "+"), r7);
        table.put(Pair.of("C", "*"), r6);
        table.put(Pair.of("C", "$"), r7);
        table.put(Pair.of("D", "("), r8);
        table.put(Pair.of("D", "a"), r9);
    }

    public void handle(Stack<String> stack, Pair<String, List<String>> rule) {
        stack.pop();
        stack.addAll(rule.getSecond());
    }

    public boolean process(String input) {
        String check = input;
        for (String alph : alp) {
            check = check.replace(alph, "");
        }
        if (!check.isEmpty()) {
            throw new IllegalArgumentException("Символ не из алфавита: '" + check.charAt(0) + "'");
        }
        List<String> semantic = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        stack.push("S'");
        while (!input.isEmpty()) {
            String st;
            if (stack.isEmpty()) {
                st = null;
            } else {
                st = stack.peek();
            }
            if (sem.contains(st)) {
                semantic.add(stack.pop());
            } else {
                String ch = String.valueOf(input.charAt(0));
                Pair<String, List<String>> rule = table.get(Pair.of(st, ch));
                if (rule != null) {
                    handle(stack, rule);
                } else if (st != null && st.equals(ch)) {
                    stack.pop();
                    input = input.substring(1);
                } else {
                    return false;
                }
            }
        }
        if (!stack.isEmpty()) {
            return false;
        }
        System.out.println("Результат семантических действий:");
        System.out.println(semantic.stream().reduce(String::concat).orElse("Error").replace("|", ""));
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
