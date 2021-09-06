package organization;

import java.util.ArrayList;

public class Storage implements Runnable{
    /** Количество товара на складе */
    private int qty;

    /** Список покупателей */
    public ArrayList<Customer> customers;

    /**
     * Инициализирует экземпляр класса
     * @param count Начальное количество товара на складе
     */
    public Storage(int count) {
        this.qty = count;
        customers = new ArrayList();
    }

    /**
     * Метод, который вызывается при запуске потока
     * (в данном случае - при достижении барьера всеми покупателями)
     */
    @Override
    public void run() {
        // Если склад пустой, выводим информацию о покупках каждого покупателя
        if (isEmpty()){
            for (Customer customer : customers){
                customer.displayResults();
            }
        }

        // Иначе - снова запускаем процесс покупок.
        else {
            startBuying();
        }
    }

    /**
     * Запускает процесс покупок для всех покупателей
     */
    public void startBuying(){
        // Каждый покупатель начинает параллельно выполнять покупки
        for (Customer customer : customers) {
            // Получаем из массива покупателя,
            // создаём на его основании поток
            // и запускаем этот поток
            Thread thread = new Thread(customer);
            thread.start();
        }
    }

    /**
     * Выполняет покупку со склада
     * @param quantityToBuy Количество товара, которое необходимо купить
     * @return Количество товара, которое фактически удалось купить
     */
    public synchronized int buy(int quantityToBuy){
        // Количество товара, которое фактически удалось купить
        int result;

        // Если остаток на складе больше или равен требуемому
        // количеству, покупаем это количество, иначе -
        // покупаем весь остаток, оставляя количество на
        // складе равным нулю
        if (qty >= quantityToBuy) {
            qty -= quantityToBuy;
            result = quantityToBuy;
        } else {
            result = qty;
            qty = 0;
        }
        return result;
    }

    /**
     * Проверяет, пустой склад или не пустой
     * @return Истина, если остаток на складе равен нулю, в противном случае - ложь
     */
    private boolean isEmpty(){
        return qty == 0;
    }
}