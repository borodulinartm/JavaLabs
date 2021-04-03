import java.util.*;

// Храним пул адресов
public class URLPool {
	// Максимальная глубина
    private int maxDepth;
    // Количество потоков, ожидающих своей очереди
    private int waitCount = 0;
    // Непосещённые ссылки
    private LinkedList<URLDepthPair> pendingURLs;
	// Уникальные ссылки, которые были просмотрены
    private LinkedList<URLDepthPair> processedURLs;
    // Уже просмотренные ссылки
    private HashSet<String> seenURLs;

    // Формируем ссылкм с заданной глубиной
    public URLPool(int max) {
		pendingURLs = new LinkedList<URLDepthPair>();
		processedURLs = new LinkedList<URLDepthPair>();
		seenURLs = new HashSet<String>();

		maxDepth = max;
	}

	// Метод будет работать в многопотоке, поэтому
	// юзаем synchronized
    public synchronized int getWaitCount() {
		return waitCount;
	}

	// Добавляем новую пару в массив
    public synchronized void add(URLDepthPair nextPair) {
		String newURL = nextPair.getURL().toString();

		// Отрезает последний слеш, чтобы строки выглядели единообразно
		String trimURL = (newURL.endsWith("/")) ? newURL.substring(0, newURL.length() - 1) : newURL;

		// Если ссылка уже была исследована
		if (seenURLs.contains(trimURL)) {
			return;
		}

		// Ссылка просмотрена, поэтому отправляется в множество
		// просмотренных ссылок
		seenURLs.add(trimURL);

		// Если глубина просмотра подходит
		if (nextPair.getDepth() < maxDepth) {
			// Добавляем в массив ожидающих обработок ссылок
			pendingURLs.add(nextPair);
			notify();
		}

		// параллельно добавляем в список, находящихся в процессе
		processedURLs.add(nextPair);
	}

	// Извлекаем из общего пула ссылку
    public synchronized URLDepthPair get() {
		// Если куча не пуста
		while (pendingURLs.size() == 0) {
			// Количество ожидающих на ссыль
			waitCount++;
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Ignoring unexpected InterruptedException - " +  e.getMessage());
			}
			// Ссылку извлекли, поэтому на одного меньше
			waitCount--;
		}

		// Удаляем первый элемент из пула
		return pendingURLs.removeFirst();
	}

    // Печатаем все уникальные урлы
    public synchronized void printURLs() {
		System.out.println("\nUnique URLs Found: " + processedURLs.size());
		while (!processedURLs.isEmpty()) {
			System.out.println(processedURLs.removeFirst());
		}
	}
}