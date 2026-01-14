import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // ===== Задания 7–9. Классы, наследование, интерфейс =====
        Mage mage = new Mage(playerName, health, 50);
        mage.printInfo();
        mage.attack();
        mage.castSpell();
        mage.takeDamage(20);
        mage.heal(10);
        mage.printInfo();

        // ===== Задание 10. Enum =====
        System.out.print("Введите состояние игры (MENU, PLAYING, GAME_OVER): ");
        String stateInput = scanner.next();
        GameState state = GameState.valueOf(stateInput);

        switch (state) {
            case MENU:
                System.out.println("Главное меню");
                break;
            case PLAYING:
                System.out.println("Игра идет");
                break;
            case GAME_OVER:
                System.out.println("Игра окончена");
                break;
        }

        scanner.close();
    }

    // ===== Задание 1. Переменные =====
    static void task_1() {
        System.out.print("Введите имя игрока: ");

        // Пункт 1
        // Создайте строковую переменную playerName и запишите в нее с помощью scanner имя игрока через консоль
        // Подсказка: чтобы записать имя через консоль нужно использовать scanner.nextLine();

        String playerName = scanner.nextLine();

        // Пункт 2
        // Создайте целочисленные переменные health и score. Запишите любые значения в созданные переменные

        int health = 100;
        int score = 0;

        System.out.println("Имя игрока: " + playerName + "\nЗдоровье: " + health + "\nСчет: " + score);
    }

    // ===== Задание 2. if-else =====
    static void task_2() {
        System.out.print("Введите здоровье игрока: ");
        int health = scanner.nextInt();

        // Напишите конструкцию if - else if - else:
        // Если значение переменной health больше или равно 80, вывести в консоль: "Игрок в отличной форме"
        // Ичане если значение переменной health больше или равно 50 и меньше 80, вывести в консоль: "Игрок в норме"
        // Иначе вывести: "Игроку нужна мед-помощь"
        // Подсказка: Вывод в консоль осуществляется через System.out.println("Сообщение")

        if (health >= 80) {
            System.out.println("Игрок в отличной форме");
        } else if (health >= 50 && health < 80) {
            System.out.println("Игрок в норме");
        } else {
            System.out.println("Игроку нужна мед-помощь");
        }
    }

    // ===== Задание 3. switch-case =====
    static void task_3() {
        System.out.print("Введите номер уровня (1-3): ");
        int level = scanner.nextInt();

        // Напишите конструкцию switch-case, где проверяется значение переменной level:
        // Проверяемые значения: 1, 2, 3
        // При срабатывании одного из случаев должно вывестись уникальное сообщение в консоль (например, название уровня)
        // Если значение переменной level не соответсвует 1, 2 и 3, должно вывестить сообщение по умолчанию (default)
        // Подсказка: не забывайте про оператор break

        switch (level) {
            case 1:
                System.out.println("Swiftbroom Academy");
                break;
            case 2:
                System.out.println("Headman Manor");
                break;
            case 3:
                System.out.println("Museum of Human Art");
                break;
            default:
                System.out.println("Неизвестный уровень");
        }
    }

    // ===== Задание 4. Одномерный массив и цикл for =====
    static void task_4() {
        // Создайте одномерный массив mas для 5 дробных чисел (float)

        float[] mas = new float[5];

        mas[0] = 3.141592653f;
        mas[1] = 2.718281828f;
        mas[2] = 792458;
        mas[3] = 0;
        mas[4] = 6.62607015f;

        // Напишите цикл for, который "пройдется" по всем элементам массива mas и выведет их в консоль
        // Если какой-то элемент массива будет равен нулю необходимо пропусть его и не выводить в консоль
        // Подсказка: необходимо использовать оператор continue

        System.out.println("Введите 5 значений очков:");
        for (int i = 0; i < 5; i++) {
            if (mas[i] == 0) {
                continue;
            }
            System.out.println(mas[i]);
        }
    }

    // ===== Задание 5. Двумерный массив и вложенные циклы for =====
    static void task_5() {
        // Создайте двумерный массив целочисленных значений размером 3x3 (т.е. 3 массива по 3 элемента в каждом) и заполните значения, используя фигурные скобки { }
        int[][] field = {{1,2,3}, {4,5,6}, {7,8,9}};

        // С помощью вложенных циклов for выведите все элементы массива
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(field[i][j]);
            }
        }
    }

    // ===== Задание 6. Цикл while и операторы continue и break =====
    static void task_6() {
        int[] mas = {43, 54, -12, 34, -120, 38, 4387, 3, 0, -328, 32};
        int sum = 0;
        int i = 0;

        // Используя цикл while посчитайте сумму положительных чисел в массиве mas

        while (true) {
            // Если число отрицательное нужно перейти к следующей итерации цикла
            if (mas[i] < 0) {
                continue;
            }

            // Если число равно нулю завершить работу цикла
            if (mas[i] == 0) {
                break;
            }

            sum += mas[i];
            i += 1;
        }

        System.out.println("Сумма положительных чисел: " + sum);
    }

    // ===== Задание 7. Класс Player =====
    void task_7() {
        /* За пределами класса Main необходимо написать класс Player.
        У игрока должно быть:
            1) Публичное поле имени (name)
            2) Приватное поле текучего здоровья (health)
            3) Приватное и финальное поле максимально возможного здоровья (MAX_HEALTH)
            4) Конструктор по умолчанию, который принимает имя и текущее значение здоровья
                4.1) Если текущее значение здоровья передано больше чем MAX_HEALTH - записать в текущее здоровье MAX_HEALTH
                4.2) Если текущее значение здоровья передано меньше нуля - записать 0
            5) Публичный метод, который возвращает текущее значение здоровье
            6) Приватный метод, который устанавливает здоровье в MAX_HEALTH
        */
    }
}

class Player {
    // Публичное поле имени (name)
    private String name;

    // Приватное поле текучего здоровья (health)
    private int health;

    // Приватное и финальное поле максимально возможного здоровья (MAX_HEALTH)
    public final int MAX_HEALTH = 100;

    // Конструктор по умолчанию, который принимает имя и текущее значение здоровья
    public Player(String name, int health) {
        this.name = name;
        if (health > MAX_HEALTH) {
            this.health = MAX_HEALTH;
        } else if (health < 0) {
            this.health = 0;
        } else {
            this.health = health;
        }
    }

    // Публичный метод, который возвращает текущее значение здоровье
    public int get_health() {
        return health;
    }

    // Приватный метод, который устанавливает здоровье в MAX_HEALTH
    private void reset_health() {
        this.health = MAX_HEALTH;
    }
}

// ===== Задание 9. Интерфейс =====
interface Attackable {
    void attack();
}

// ===== Задание 8. Наследование + реализация интерфейса =====
class Mage extends Player implements Attackable {
    private int mana;

    public Mage(String name, int health, int mana) {
        super(name, health);
        this.mana = mana;
    }

    public void castSpell() {
        if (mana >= 10) {
            mana -= 10;
            System.out.println("Маг использовал заклинание. Мана: " + mana);
        } else {
            System.out.println("Недостаточно маны");
        }
    }

    @Override
    public void attack() {
        System.out.println("Маг атакует магией!");
    }
}

// ===== Задание 10. Enum =====
enum GameState {
    MENU,
    PLAYING,
    GAME_OVER
}