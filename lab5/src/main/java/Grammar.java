import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Grammar {

    List<String> net = Arrays.asList("S'", "S", "A", "B", "C", "D");
    List<String> sem = Arrays.asList("y1", "y2", "y3", "y4");

    Pair<String, List<String>> r1 = Pair.of("S'", List.of("$", "S"));
    Pair<String, List<String>> r2 = Pair.of("S", List.of("A", "B"));
    Pair<String, List<String>> r3 = Pair.of("A", List.of("A", "B", "+"));
    Pair<String, List<String>> r4 = Pair.of("A", List.of());
    Pair<String, List<String>> r5 = Pair.of("B", List.of("C", "D"));
    Pair<String, List<String>> r6 = Pair.of("C", List.of("C", "D", "*"));
    Pair<String, List<String>> r7 = Pair.of("C", List.of());
    Pair<String, List<String>> r8 = Pair.of("D", List.of(")", "S", "("));
    Pair<String, List<String>> r9 = Pair.of("D", List.of("a"));

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
        List<String> semantic = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        stack.push("S'");
        input += "$";
        while (!input.isEmpty()) {
            String st = stack.peek();
            if (sem.contains(st)) {
                semantic.add(stack.pop());
            } else {
                String ch = String.valueOf(input.charAt(0));
                Pair<String, List<String>> rule = table.get(Pair.of(st, ch));
                if (rule != null) {
                    handle(stack, rule);
                } else if (st.equals(ch)) {
                    stack.pop();
                    input = input.substring(1);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public String generate() {
        return "TO DO";
    }

}
