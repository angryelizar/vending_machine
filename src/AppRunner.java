import enums.ActionLetter;
import exceptions.ErrorException;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(20);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        app.choosePaymentMethod();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);
        print("Монет на сумму: " + coinAcceptor.getAmount());
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);
    }

    private void choosePaymentMethod(){
        print("Выберите метод оплаты");
        print("""
                1 - Банковская карта
                2 - Монеты
                """);
        String answer = fromConsole().trim();
        try {
            int answerToInt = Integer.parseInt(answer);
            if (answerToInt <= 0 || answerToInt > 2){
                throw new ErrorException();
            }
        } catch (NumberFormatException numberFormatException){
            numberFormatException.getMessage();
            System.err.println("Вводите только цифры!");
            choosePaymentMethod();
        } catch (ErrorException e) {
            System.err.println("Такого способа оплаты нет!");
            choosePaymentMethod();
        }

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        if (allowProducts.size() == 0) {
            print("""
                                
                    У вас недостаточно денег
                    """);
            isExit = true;
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        try {
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
                print("Завершение работы");
            } else if ("a".equalsIgnoreCase(action)) {
                print("Пополняем баланс");
            }
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попрбуйте еще раз.");
            chooseAction(products);
        }
    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
