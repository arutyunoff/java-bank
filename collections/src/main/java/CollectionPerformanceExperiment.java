import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;

/**
 * Сравнение ArrayList и LinkedList на одинаковых операциях.
 */
public class CollectionPerformanceExperiment {
    private static final int N = 10_000;
    private static final int WARMUP = 3;
    private static final int RUNS = 7;

    private final Integer[] data = new Integer[N];
    private long checksum;

    private CollectionPerformanceExperiment() {
        for (int i = 0; i < N; i++) {
            data[i] = i;
        }
    }

    /**
     * Запускает эксперимент и печатает таблицу результатов.
     *
     * @param args не используются
     */
    public static void main(String[] args) {
        CollectionPerformanceExperiment experiment = new CollectionPerformanceExperiment();
        experiment.run();
    }

    private void run() {
        System.out.printf("%-28s %-10s %-16s %-16s%n", "Операция", "Элементов", "ArrayList, мкс", "LinkedList, мкс");

        printRow("Добавление в конец", false, list -> {
            for (Integer x : data) {
                list.add(x);
            }
            return list.size();
        });

        printRow("Добавление в начало", false, list -> {
            for (Integer x : data) {
                list.add(0, x);
            }
            return list.size();
        });

        printRow("Вставка в середину", false, list -> {
            for (Integer x : data) {
                list.add(list.size() / 2, x);
            }
            return list.size();
        });

        printRow("Доступ по индексу", true, list -> {
            long sum = 0;
            for (int i = 0; i < list.size(); i++) {
                sum += list.get(i);
            }
            return sum;
        });

        printRow("Удаление из начала", true, list -> {
            long sum = 0;
            while (!list.isEmpty()) {
                sum += list.remove(0);
            }
            return sum;
        });

        printRow("Удаление из конца", true, list -> {
            long sum = 0;
            while (!list.isEmpty()) {
                sum += list.remove(list.size() - 1);
            }
            return sum;
        });

        System.out.println("Контрольная сумма: " + checksum);
    }

    private void printRow(String name, boolean filled, ToLongFunction<List<Integer>> operation) {
        long arrayTime = measure(ArrayList::new, filled, operation);
        long linkedTime = measure(LinkedList::new, filled, operation);
        System.out.printf("%-28s %-10d %-16d %-16d%n", name, N, arrayTime / 1000, linkedTime / 1000);
    }

    private long measure(Supplier<List<Integer>> factory, boolean filled, ToLongFunction<List<Integer>> operation) {
        for (int i = 0; i < WARMUP; i++) {
            List<Integer> list = prepare(factory, filled);
            checksum += operation.applyAsLong(list);
        }

        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            List<Integer> list = prepare(factory, filled);
            long start = System.nanoTime();
            checksum += operation.applyAsLong(list);
            times[i] = System.nanoTime() - start;
        }

        Arrays.sort(times);
        return times[RUNS / 2];
    }

    private List<Integer> prepare(Supplier<List<Integer>> factory, boolean filled) {
        List<Integer> list = factory.get();
        if (filled) {
            for (Integer x : data) {
                list.add(x);
            }
        }
        return list;
    }
}
