import enums.ActionLetter;
import enums.PaymentMethodWord;
import exceptions.ErrorException;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Random;
import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private String paymentMethod = PaymentMethodWord.NONE.getValue();

    private final Coins coins;
    private final BankCard bankCard;

    private static boolean isExit = false;
    UniversalArray<Product> allowProducts;

    private AppRunner() {
        Random rnd = new Random();
        bankCard = new BankCard(rnd.nextInt(1000 - 500) + 500);
        coins = new Coins(0);
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);
        allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        if (paymentMethod.equals(PaymentMethodWord.CARD.getValue())) {
            print("Баланс вашей карты: " + bankCard.getAmount());
        } else if (paymentMethod.equals(PaymentMethodWord.COINS.getValue())) {
            print("Монет на сумму: " + coins.getAmount());
        } else {
            print("\nВы не можете ничего купить - выберите метод оплаты");
        }
        chooseAction(allowProducts);
    }

    private void choosePaymentMethod() {
        print("Выберите метод оплаты");
        print("""
                1 - Банковская карта
                2 - Монеты
                """);
        String answer = fromConsole().trim();
        try {
            int answerToInt = Integer.parseInt(answer);
            if (answerToInt <= 0 || answerToInt > 2) {
                throw new ErrorException();
            }
            switch (answerToInt) {
                case 1:
                    setPaymentMethod(PaymentMethodWord.CARD.getValue());
                    break;
                case 2:
                    setPaymentMethod(PaymentMethodWord.COINS.getValue());
                    break;
                default:
                    print("404...");
            }
        } catch (NumberFormatException numberFormatException) {
            System.err.println("Вводите только цифры!");
            choosePaymentMethod();
        } catch (ErrorException e) {
            System.err.println("Такого способа оплаты нет!");
            choosePaymentMethod();
        }
    }

    private void topUpBalance() {
        if (paymentMethod.equals(PaymentMethodWord.COINS.getValue())) {
            print("Выберите монету для пополения баланса:\n");
            print("3 - 3 сома");
            print("5 - 5 сом");
            print("10 - 10 сом\n");
            int balance = coins.getAmount();
            String topUpValue = fromConsole().trim();
            switch (topUpValue) {
                case "3": {
                    coins.setAmount(balance + 3);
                    break;
                }
                case "5": {
                    coins.setAmount(balance + 5);
                    break;
                }
                case "10": {
                    coins.setAmount(balance + 10);
                    break;
                }
                default: {
                    System.err.println("Фальшивая монета - достаньте настоящую!");
                    topUpBalance();
                    break;
                }
            }
        } else if (paymentMethod.equals(PaymentMethodWord.CARD.getValue())) {
            System.err.println("Вы не можете пополнить баланс своей карты - получите ЗП!");
            startSimulation();
        }
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (paymentMethod.equals(PaymentMethodWord.CARD.getValue())) {
                if (bankCard.getAmount() >= products.get(i).getPrice()) {
                    allowProducts.add(products.get(i));
                }
            } else if (paymentMethod.equals(PaymentMethodWord.COINS.getValue())) {
                if (coins.getAmount() >= products.get(i).getPrice()) {
                    allowProducts.add(products.get(i));
                }
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print("\n a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти\n");
        String action = fromConsole().substring(0, 1);
        try {
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
                print("Завершение работы...");
            } else if ("a".equalsIgnoreCase(action)) {
                choosePaymentMethod();
                topUpBalance();
            }
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    if (paymentMethod.equals(PaymentMethodWord.CARD.getValue())) {
                        bankCard.setAmount(bankCard.getAmount() - products.get(i).getPrice());
                    } else if (paymentMethod.equals(PaymentMethodWord.COINS.getValue())) {
                        coins.setAmount(coins.getAmount() - products.get(i).getPrice());
                    }
                    print("Вы купили " + products.get(i).getName());
                    break;
                }
            }
        } catch (IllegalArgumentException ErrorException) {
            print("Недопустимая буква. Попробуйте еще раз.");
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

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
