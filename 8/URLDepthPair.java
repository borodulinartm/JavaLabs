import java.net.*;
import java.util.regex.*;

// Структура, хранящая прау URL-depth
public class URLDepthPair {
    // Регулярочки
    public static final String URL_REGEX = "(https?:\\/\\/)((\\w+\\.)+\\.(\\w)+[~:\\S\\/]*)";
    // На основе регулярок делаем паттерн
    public static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX,  Pattern.CASE_INSENSITIVE);
    // Поля класса: ссылка
    private URL URL;
    // Глубина
    private int depth;

    // Конструктор
    public URLDepthPair(URL url, int d) throws MalformedURLException {
        // Здесь происходит проверка на то, что это корректный урд
        URL = new URL(url.toString());
        // Если в строке 18 произойдёт ошибка, то до этого места уже не дойдёт
        depth = d;
    }

    // Для красивого вывода на экран
    @Override public String toString() {
        return "URL: " + URL.toString() + ", Depth: " + depth;
    }

    // Геттер урла
    public URL getURL() {
        return URL;
    }
    
    // Геттер для глубины
    public int getDepth() {
        return depth;
    }

    // Вернёт хост
    public String getHost() {
        return URL.getHost();
    }
    
    // Возвращает адрес документа на серваке
    public String getDocPath() {
        return URL.getPath();
    }
    
    // Проверка, а действительно ли строка есть урл
    public static boolean isAbsolute(String url) {
	Matcher URLChecker = URL_PATTERN.matcher(url);
        return URLChecker.find();
    }
}
