import java.awt.geom.Rectangle2D;

// Рисует фрактал "Треугольник"
public class Tricon extends FractalGenerator {
    // Максимальное количество итераций
    public static final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        // Указанная область для рисования фрактала
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    @Override
    public int numIterations(double x, double y) {
        // Квадрат величины c
        double magSq;
        // Действительная часть
        double re = x;
        // z_i - мнимая часть
        double im = y;
        // Действительная часть z_(i+1)
        double nextRe;
        // Мнимая часть z_(i+1)
        double nextIm;
        // Итератор
        int i = 0;

        while (i < MAX_ITERATIONS) {
            ++i;
            nextRe = x + re * re - im * im;
            nextIm = y - 2 * re * im;
            re = nextRe;
            im = nextIm;
            magSq = re * re + im * im;
            if (magSq > 4) {
                return i;
            }
        }
        return -1;
    }
}
