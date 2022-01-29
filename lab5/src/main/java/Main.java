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
                        System.out.println(grammar.process(input));
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
                        String res;
                        try {
                            int i = Integer.parseInt(input);
                            if (i < 0) {
                                throw new IllegalArgumentException();
                            }
                            System.out.println(grammar.generate());
                        } catch (NumberFormatException ex) {
                            System.out.print("Неверный формат числа: " + input);
                        } catch (IllegalArgumentException ex) {
                            System.out.print("Значение меньше нуля: " + input);
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
