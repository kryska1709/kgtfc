import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    // Метод для поиска максимального количества подряд идущих пар AB или CB
    public static int findMaxPairs(String data) {
        int maxCount = 0;
        int currentCount = 0;

        for (int i = 0; i < data.length() - 1; i++) {
            String pair = data.substring(i, i + 2);
            if (pair.equals("AB") || pair.equals("CB")) {
                currentCount++;
                maxCount = Math.max(maxCount, currentCount);
                i++; // Пропускаем следующий символ
            } else {
                currentCount = 0; // Сбрасываем счетчик
            }
        }

        return maxCount;
    }

    public static void main(String[] args) {
        String filePath = "fl.txt";

        // Используем try-with-resources для автоматического управления ресурсами
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             ExecutorService executor = Executors.newFixedThreadPool(5)) {

            List<Future<Integer>> futures = new ArrayList<>();
            String line;

            // Чтение файла построчно
            while ((line = reader.readLine()) != null) {
                String finalLine = line;
                futures.add(executor.submit(() -> findMaxPairs(finalLine)));
            }

            // Вычисление максимального значения
            int max = futures.stream()
                    .mapToInt(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            return 0; // В случае ошибки возвращаем 0
                        }
                    }).max().orElse(0);

            System.out.println("Максимальное количество подряд идущих пар AB или CB: " + max);

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}
