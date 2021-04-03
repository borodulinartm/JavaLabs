import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    // Размер дисплея (квадратный размер вводим)
    private int displaySize;
    // Выводимое на экране изображение
    private JImageDisplay currImg;
    // Генератор фракталов
    private FractalGenerator fractalGenerator;
    // Область просмотра на экране
    private Rectangle2D.Double range;

    // Базовый конструктор, инициализирующий фракталы
    public FractalExplorer(int displaySize) {
        this.displaySize = displaySize;
        fractalGenerator = new Mandelbrot();
        range = new Rectangle2D.Double(0, 0, 0, 0);

        fractalGenerator.getInitialRange(range);
    }

    // Метод инициализирует GUI и прикручивает наши view-элементы
    public void createAndShowGUI() {
        // GUI компоненты
        JFrame frame = new JFrame("Fractals!!!");
        currImg = new JImageDisplay(displaySize, displaySize);
        JButton resetBtn = new JButton("Click me!!!");

        // Добавление событий кнопки и нажатия мышки
        ActionHandler actionHandler = new ActionHandler();
        MouseHandler mouseHandler = new MouseHandler();

        // Добавляем кнопку и изображение фрактала на текущий фрейм
        resetBtn.addActionListener(actionHandler);
        currImg.addMouseListener(mouseHandler);

        // Добавление компонентов
        frame.setLayout(new BorderLayout());
        frame.add(currImg, BorderLayout.CENTER);
        frame.add(resetBtn, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Не забываем добавить эти строчки, без них ничего работать не будет
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    // Метод рисует фрактал
    private void drawFractal() {
        for(int i = 0; i < displaySize; ++i) {
            for(int j = 0; j < displaySize; ++j) {
                double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, i);
                double yCoord = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, j);

                // Количество итераций
                int numberIterations = fractalGenerator.numIterations(xCoord, yCoord);

                if (numberIterations == -1) {
                    // Пиксель не в нашем множестве, поэтому цвет будет чёрным
                    currImg.drawPixel(i, j, 0);
                } else {
                    // Цвет фрактала будет напрямую зависеть от количества итераций
                    float hue = 0.7f + (float) numberIterations / 200f;

                    // Преобразуем цвет в RGB
                    int colorRGB = Color.HSBtoRGB(hue, 1f, 1f);

                    // Рисуем наши классные фракталы
                    currImg.drawPixel(i, j, colorRGB);
                }
            }
        }

        // Обновляем наш фрейм (без него ничего работать не будет)
        currImg.repaint();
    }

    // Внутренний класс, который занимается обработкой события нажатия кнопки
    public class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // При нажатии на кнопку масштабирование фракткала сбрасывается
            fractalGenerator.getInitialRange(range);

            // Заново рисуем фракталы
            drawFractal();
        }
    }

    // Событие нажатия мышки
    public class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, e.getY());

            // Зумим нашу картинку с фракталом (вот здесь комп начинает лагать, т.к работает в одном потоке)
            fractalGenerator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(950);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }
}
