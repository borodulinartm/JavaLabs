import java.io.*;
import java.net.*;
import java.util.regex.*;

// Класс для многопоточной работы
public class CrawlerTask implements Runnable {
    // Регулярочки подъехали
    public static final String LINK_REGEX = "href\\s*=\\s*\"([^$^\"]*)\"";
    // На основе имеющиегося регулярного выражения делаем паттерн
	// который будет использоваться для обнаружения признаков ссылки
    public static final Pattern LINK_PATTERN = Pattern.compile(LINK_REGEX, Pattern.CASE_INSENSITIVE);
    // Время ожидания, когда сокет ждёт какой-либо информации от сервера
    public static int maxPatience = 5;
	// Массив (пул) адресов
    private URLPool pool; 

    // Констркируем объект
    public CrawlerTask(URLPool p) {
		pool = p;
	}

    // Метод делает запрос по указанному url-у
    public Socket sendRequest(URLDepthPair nextPair) throws UnknownHostException, SocketException, IOException {
		// Сокет для работы с http
		Socket socket = new Socket(nextPair.getHost(), 80);
		// Время-то дано в секундах
		socket.setSoTimeout(maxPatience * 1000);

		// Дескриптор, которй обеспечит нас данными из сокета
		OutputStream os = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(os, true);

		// Формируем http-запрос до нашего сервера
		writer.println("GET " + nextPair.getDocPath() + " HTTP/1.1");
		writer.println("Host: " + nextPair.getHost());
		writer.println("Connection: close");
		writer.println();

		// Возвращаем сокет
		return socket;
	}

    // Из конкретного сокета мы берём данные
	// находим среди них ссылки и добавляем в пул адресов
    public void processURL(URLDepthPair url) throws IOException {
	// Деоаем запрос на сайт
	Socket socket;
	try {
	    socket = sendRequest(url);
	}
	// Здесь простая обработка ошибок
	catch (UnknownHostException e) {
	    System.err.println("Host "+ url.getHost() + " couldn't be determined"); 
	    return;
	}

	catch (SocketException e) {
	    System.err.println("Error with socket connection: " + url.getURL() + 
			       " - " + e.getMessage());
	    return;
	}

	catch (IOException e) {
	    System.err.println("Couldn't retrieve page at " + url.getURL() +
			       " - " + e.getMessage());
	    return;
	}

	// Если всё окей, то просто формируем дескриптор
	// из которого будем извлекать данные
	InputStream input = socket.getInputStream();
	BufferedReader reader = new BufferedReader(new InputStreamReader(input));

	String line;
	// Пока строчки идут и идут
	while ((line = reader.readLine()) != null) {
		// Парсим ссылки в данной строке
	    Matcher LinkFinder = LINK_PATTERN.matcher(line);
	    // Пока есть ссыль, то обрабатываем его далле
	    while (LinkFinder.find()) {
			String newURL = LinkFinder.group(1);

			URL newSite;
			// Не все ссылки валидны, этим мы и занимаемся
			try {
				if (URLDepthPair.isAbsolute(newURL)) {
					newSite = new URL(newURL);
				} else {
					newSite = new URL(url.getURL(), newURL);
				}

				// Добавляем в пул адресов нашу ссылку с увеличенной глубиной
				pool.add(new URLDepthPair(newSite, url.getDepth() + 1));
			} catch (MalformedURLException ignored) {
			}
		}
	}
	// Дескрипторы закрываем
	reader.close();

	// Закрываем сокеты
	try {
	    socket.close();
	}
	catch (IOException e) {
	    System.err.println("Couldn't close connection to " + url.getHost() +
			       " - " + e.getMessage());
        }
    }
    
   // Берём первый урл из кучи
    public void run() {
		URLDepthPair nextPair;
		while (true) {
			nextPair = pool.get();
			try {
				processURL(nextPair);
			} catch (IOException e) {
				System.err.println("Error reading the page at " + nextPair.getURL() + " - " + e.getMessage());
			}
		}
	}
}
