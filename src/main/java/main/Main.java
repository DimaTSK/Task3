package main;

import organization.Customer;
import organization.Storage;

import java.util.concurrent.CyclicBarrier;
public class Main {

    /** Количество покупателей */
    private static int countOfCustomers;

    public static void main(String[] args) {
        System.out.println("Система \"Склад - Покупатели\"");

        // Инициализируем склад
        Storage storage = new Storage(1000);

        try {
            // Считываем количество покупателей из первого аргумента,
            // переданного в функцию main, и выводим его на консоль
            countOfCustomers = Integer.parseInt(args[0]);
            System.out.println("Количество покупателей: " + countOfCustomers);

            // Создаём объект barrier для задержки покупателей. Первый параметр (количество потоков)
            // равен общему количеству покупателей. В качестве второго параметра (действие) передаётся сам склад.
            // Когда все покупатели сделают по одной покупке и достигнут этого барьера, на складе будет выполнен
            // метод run(). В этом методе выполняется проверка, остались ли ещё товары на складе. Если остались,
            // процесс покупок будет запущен снова, а если нет, то покупатели завершат свою работу
            // и выведут информацию о покупках в консоль.
            CyclicBarrier barrier = new CyclicBarrier(countOfCustomers, storage);

            // Создаём покупателей и добавляем их в список покупателей склада
            for (int i = 0; i < countOfCustomers; i++) {

                // Создаём покупателя, назначаем ему имя, передаём ссылку на объект склада,
                // а также объект barrier. После инициализации добавляем его в список покупателей склада.
                Customer customer = new Customer("Покупатель " + (i + 1), storage, barrier);
                storage.customers.add(customer);
            }

            // Запускаем процесс покупки на складе
            storage.startBuying();
        } catch (Exception ex){
            System.out.println("Не удалось определить количество покупателей");
        }
    }
}