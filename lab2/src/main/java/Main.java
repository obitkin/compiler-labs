import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grammar grammar = new Grammar();
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
                        } catch (IllegalArgumentException ex) {
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
