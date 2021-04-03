import java.io.*;

import java.net.URL;
import java.net.UnknownHostException;

import java.util.LinkedList;

public class Crawler {
    // Наш url (поступает из конструктора)
    private String url;
    // Текущая строчка из html-кода
    private String line;
    // Глубина, заданная пользователем и текущая глубина
    private int depth, currentDepth;

    // Массив необработанных ссылок
    private LinkedList<URLDepthPair> not_visited_sites;
    // Массив обработанных ссылок
    private LinkedList<URLDepthPair> visited_sites;
    // Сокет, по которому мы будем подключаться к сайту
    private URL url_socket;
    // Строковый дескриптор
    private BufferedReader bufferedReader;

    // Константы
    // Префикс названия протокола
    private static final String URL_PREFIX = "http://";
    private static final String URL_PREFIX_S = "https://";
    private static final String A_PREFIX = "<a";
    private static final String HREF_ATTR = "href=";

    // Конструктор
    public Crawler(String url, int depth) {
        this.url = url;
        this.depth = depth;
        currentDepth = 1;

        not_visited_sites = new LinkedList<URLDepthPair>();
        visited_sites = new LinkedList<URLDepthPair>();
    }

    public void Parse() {
        while (currentDepth < depth + 1) {
            try {
                // Инициализация сокета
                url_socket = new URL(url);
                bufferedReader = new BufferedReader(
                        new InputStreamReader(url_socket.openStream())
                );

                String link;
                // Цикл выполняется, пока строка не пустая
                while ((line = bufferedReader.readLine()) != null) {
                    // Пасим ссылку из сторки
                    link = GetUrl(line);

                    // ссылка может и не быть
                    if (link != null) {
                        // Если есть, добавляем в список необработанных ссылок
                        not_visited_sites.add(new URLDepthPair(link, currentDepth));
                    }
                }
                // Закрываем сокет
                bufferedReader.close();
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + url);
            } catch (IOException e) {
                System.err.println("Web page is not found or access is denied " + url);
            }

            // Обрабатываем следующую ссылку из списка необработанных
            // И так depth раз
            visited_sites.add(
                    not_visited_sites.get(0)
            );

            // Меняем для них url и current_depth
            this.url = not_visited_sites.get(0).getUrl();
            currentDepth = not_visited_sites.get(0).getDepth() + 1;

            // А из списка необработанных убираем данную ссыль
            not_visited_sites.removeFirst();
        }
    }

    // Парсим ссылку из конкретной строки
    private String GetUrl(String input_string) {
        // Индекс начала ссылки
        int index = 0;
        // Если есть тег a и есть href
        if (input_string.contains(A_PREFIX) && input_string.contains(HREF_ATTR)) {
            // В теге может быть не ссылка, а абсолютный адрес. Их не учитываем
            if (input_string.contains(URL_PREFIX)) {
                index = input_string.indexOf(URL_PREFIX);
            } else if (input_string.contains(URL_PREFIX_S)) {
                index = input_string.indexOf(URL_PREFIX_S);
            } else {
                return null;
            }

            // Формируем новую строку
            StringBuilder output = new StringBuilder();

            // Проходим циклом до тех пор, пока не встретились символы
            for (int i = index; input_string.charAt(i) != '\'' && input_string.charAt(i) != '\"'; ++i) {
                output.append(input_string.charAt(i));
            }

            // Возвращаем в формате string
            return output.toString();
        }

        return null;
    }

    // Геттеры
    public LinkedList<URLDepthPair> getNot_visited_sites() {
        return not_visited_sites;
    }

    public LinkedList<URLDepthPair> getVisited_sites() {
        return visited_sites;
    }
}
