import java.net.*;

// Данная программа делает тоже самое, что и в 7 лабе,
// только работает в многопотоке
public class Crawler {
	// Пул наш url-depth адресов
    private URLPool pool;
    // Количество потоков (поступает в качестве аргкментов командной строки)
    public int numThreads = 4;

    // Констрируем объект
    public Crawler(String root, int max) throws MalformedURLException {
    	// Инициализация. В качестве параметра идёт максимальная глубина
		pool = new URLPool(max);

		// root - это корневой урл, от которого начинается наша работа
		URL rootURL = new URL(root);
		// Добавляем его в пул адресов
		pool.add(new URLDepthPair(rootURL, 0));
	}

	// Метод-генератор потоков
    public void crawl() {
		for (int i = 0; i < numThreads; i++) {
			// Инициализируем поток
			CrawlerTask crawler = new CrawlerTask(pool);
			Thread thread = new Thread(crawler);
			// Стартуем
			thread.start();
		}

		// Если все потоки безработны, то метод закончил свою работу
		// поскольку все урлы были обработаны
		while (pool.getWaitCount() != numThreads) {
			try {
				// Заглушаем потоки
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("Ignoring unexpected InterruptedException - " + e.getMessage());
			}
		}

		pool.printURLs();
	}

    // Именно отсюда зарождается жизнь
    public static void main(String[] args) {
		// Обработчик командной строки (проверка на число аргументов)
		if (args.length < 2 || args.length > 5) {
			System.err.println("Usage: java Crawler <URL> <depth> " +
					"<patience> -t <threads>");
			System.exit(1);
		}

		// Вызываем класс crawl
		try {
			Crawler crawler = new Crawler(args[0], Integer.parseInt(args[1]));
			switch (args.length) {
				case 3:
					CrawlerTask.maxPatience = Integer.parseInt(args[2]);
					break;
				case 4:
					crawler.numThreads = Integer.parseInt(args[3]);
					break;
				case 5:
					CrawlerTask.maxPatience = Integer.parseInt(args[2]);
					crawler.numThreads = Integer.parseInt(args[4]);
					break;
			}

			// Создаём потоки
			crawler.crawl();
		} catch (MalformedURLException e) {
			// Если урл не валиден, то ошибочка
			System.err.println("Error: The URL " + args[0] + " is not valid");
			System.exit(1);
		}
		System.exit(0);
	}
}
