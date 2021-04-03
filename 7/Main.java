public class Main {
    public static void main(String[] args) {
        String url = args[0].toString();
        int depth = Integer.parseInt(args[1].toString());

        Crawler cr = new Crawler(url, depth);
        cr.Parse();

        // Из особенностей алгоритма
        var val = cr.getVisited_sites();
        for(int i = 0; i < val.size(); ++i) {
            System.out.println(val.get(i).toString());
        }
        val = cr.getNot_visited_sites();
        for(int i = 0; i < val.size(); ++i) {
            System.out.println(val.get(i).toString());
        }
    }
}

/*
Список адекватных сайтов:
http://www.cleverstudents.ru/
http://old.exponenta.ru/EDUCAT/class/courses/tfkp/theme8/theory.asp
 */