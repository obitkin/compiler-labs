package ru.spbstu.telematics;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DFA dfa = new DFA();
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
                        try {
                            System.out.println("\n\nВведите строку или '-1' для выхода");
                            String input = scanner.nextLine();
                            if (input.equals("-1")) {
                                break;
                            }
                            dfa.setUP();
                            dfa.process(input);
                            System.out.println((dfa.isFinite() ? "да" : "нет"));
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                }
                case 2: {
                    while (true) {
                        System.out.println("\n\nВведите размер или '-1' для выхода");
                        String input = scanner.nextLine();
                        if (input.equals("-1")) {
                            break;
                        }
                        dfa.setUP();
                        String res;
                        try {
                            int i = Integer.parseInt(input);
                            if (i < 0) {
                                throw new NumberFormatException("For input string: \"" + i + "\"");
                            }
                            res = dfa.process(i);
                            if (!res.equals("")) {
                                System.out.print(res);
                            } else {
                                System.out.print("Нет последовательности заданной длины: " + i);
                            }
                        } catch (NumberFormatException ex) {
                            System.out.print("Неверный формат: " + ex.getMessage());
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
