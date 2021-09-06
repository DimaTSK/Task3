package organization;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Покупатель
 */
public class Customer implements Runnable{
    /** Наименование покупателя */
    private final String name;

    /** Склад, с которого осуществляются покупки */
    private final Storage storage;

    /** Общее количество купленного товара */
    private int totalQty = 0;

    /** Количество покупок */
    private int numberOfPurchases = 0;

    /** Объект CyclicBarrier для задержки покупателей */
    private CyclicBarrier barrier;

    /**
     * Инициализирует экземпляр класса
     * @param name Наименование покупателя
     * @param storage Склад, с которого осуществляются покупки
     * @param barrier Объект CyclicBarrier для задержки покупателей
     */
    public Customer(String name, Storage storage, CyclicBarrier barrier) {
        this.name = name;
        this.storage = storage;
        this.barrier = barrier;
    }

    /**
     * Метод, который вызывается при запуске потока
     */
    public void run(){
        // Выполняем покупку
        buy();

        // Ожидаем, пока все покупатели достигнут барьера
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    /**
     * Выполняет однократную покупку со склада
     */
    public void buy() {
        // Случайным образом генерируем количество для покупки (от 1 до 10)
        int qty = (int) (Math.random() * 10 + 1);

        // Выполняем покупку сгенерированного количества товара.
        // Метод buy возвращает количество товара, которое
        // фактически удалось купить (на случай, если остаток
        // на складе меньше требуемого количества или равен нулю)
        int purchasedQty = storage.buy(qty);

        // Если удалось купить хоть какое-то количество товара,
        // добавляем его в статистику данного покупателя
        if (purchasedQty > 0){
            totalQty += purchasedQty;
            numberOfPurchases++;
        }
    }

    /**
     * Отображает на консоли результаты покупок данного покупателя
     */
    public void displayResults() {
        System.out.println(name + "\tКуплено товара: " + totalQty + "\tКол-во покупок: " + numberOfPurchases);
    }
}