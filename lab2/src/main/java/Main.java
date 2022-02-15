import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    static Path pathGrammar = Path.of("src/main/resources/Grammar.txt");
    static Path pathRelation = Path.of("src/main/resources/Relation.txt");

    @SneakyThrows
    static Map<List<String>, String> readRules() {
        List<String> lines = Files.lines(pathGrammar).collect(Collectors.toList());
        List<Pair<List<String>, String>> res = new ArrayList<>();
        for (String line : lines) {
            List<String> list = Arrays.stream(line.split("->")).collect(Collectors.toList());
            if (list.size() != 2) {
                throw new IOException();
            }
            String left = list.get(0).trim();
            List<String> right = Arrays.stream(list.get(1).split("\\|")).map(String::trim).collect(Collectors.toList());
            for (String rightStr : right) {
                res.add(Pair.of(Arrays.stream(rightStr.split(" ")).filter(str -> !str.isEmpty()).collect(Collectors.toList()), left));
            }
        }
        Map<List<String>, String> map = res.stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        if (map.size() != res.size()) {
            throw new IOException();
        }
        return map;
    }

    @SneakyThrows
    static Map<Pair<String, String>, Relation> readRelations() {
        List<String> lines = Files.lines(pathRelation).collect(Collectors.toList());
        List<Pair<Pair<String, String>, Relation>> res = new ArrayList<>();
        for (String line : lines) {
            Relation relation;
            if (line.contains(Relation.MORE.toString())) {
                relation = Relation.MORE;
            } else if (line.contains(Relation.EQUAL.toString())) {
                relation = Relation.EQUAL;
            } else if (line.contains(Relation.LESS.toString())) {
                relation = Relation.LESS;
            } else {
                throw new IOException();
            }
            List<String> list = Arrays.stream(line.split(relation.toString())).collect(Collectors.toList());
            if (list.size() != 2) {
                throw new IOException();
            }
            List<String> left = Arrays.stream(list.get(0).split("\\|")).map(String::trim).collect(Collectors.toList());
            List<String> right = Arrays.stream(list.get(1).split("\\|")).map(String::trim).collect(Collectors.toList());
            for (String leftStr : left) {
                for (String rightStr : right) {
                    res.add(Pair.of(Pair.of(leftStr, rightStr), relation));
                }
            }
        }
        Map<Pair<String, String>, Relation> map = res.stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        if (map.size() != res.size()) {
            throw new IOException();
        }
        return map;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grammar grammar = new Grammar(readRelations(), readRules());
        exit:
        while (true) {
            System.out.println("\nВыберите опцию:");
            System.out.println("1. Распознавание");
            System.out.println("2. Генерация");
            System.out.println("3. Выход");
            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1: {
                    while (true) {
                        System.out.println("\n\nВведите строку или '-1' для выхода");
                        String input = scanner.nextLine();
                        if (input.equals("-1")) {
                            break;
                        }
                        try {
                            System.out.println("Цепочка" + (grammar.process(input) ? " " : " не ") + "принадлежит грамматике");
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }

                    }
                    break;
                }
                case 2: {
                    while (true) {
                        System.out.println("\n\nНажмите 'Enter' для генерации или Введите '-1' для выхода");
                        String input = scanner.nextLine();
                        if (input.equals("-1")) {
                            break;
                        }
                        if (input.isEmpty()) {
                            System.out.println(grammar.generate());
                        } else {
                            System.out.println("Ошибка ввода: Ожидалась пустая строка или '-1'");
                        }
                    }
                    break;
                }
                case 3: {
                    break exit;
                }
                default: {
                    System.out.println("Неверная опция");
                }
            }
        }
    }

}
