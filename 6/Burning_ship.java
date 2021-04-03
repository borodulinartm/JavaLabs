import java.awt.geom.Rectangle2D;

public class Burning_ship extends FractalGenerator {
    // Максимальное количество итераций
    public static final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        // Указанная область для рисования фрактала
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }

    @Override
    public int numIterations(double x, double y) {
        // Квадрат c
        double magSq;
        // Действительная часть z_i
        double re = x;
        // Мнимая часть z_i
        double im = y;
        // Действительная часть z_(i+1)
        double nextRe;
        // Мнимая часть _(i+1)
        double nextIm;
        // Итератор
        int i = 0;
        while (i < MAX_ITERATIONS) {
            i += 1;
            nextRe = x + Math.abs(re) * Math.abs(re)
                    - Math.abs(im) * Math.abs(im);
            nextIm = y + 2 * Math.abs(re) * Math.abs(im);
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
