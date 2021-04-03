import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    // Максимальное количество итераций
    public static final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        // Указанная область для рисования фрактала
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    /*
    Функция, расчитывающая необходимое количесвто итераций
    для рисования фрактала
    z_n = (z_{n-1})^2 + c
    */
    public int numIterations(double a, double b) {
        // Квадрат величины c
        double magSq;
        // Действительная часть
        double re = a;
        // z_i - мнимая часть
        double im = b;
        // Действительная часть z_(i+1)
        double nextRe;
        // Мнимая часть z_(i+1)
        double nextIm;
        // Итератор
        int i = 0;

        while (i < MAX_ITERATIONS) {
            i += 1;
            nextRe = a + re * re - im * im;
            nextIm = b + 2 * re * im;
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
