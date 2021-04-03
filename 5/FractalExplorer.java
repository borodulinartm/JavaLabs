import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FractalExplorer {
    // Размер нашего окна (квадартаное)
    private int displaySize;
    // Выводимое на экране изображение
    private JImageDisplay currImg;
    // Генератор фракталов (опроеделённого типа)
    private FractalGenerator fractalGenerator;
    // Область просмотра на экране
    private Rectangle2D.Double fractalRange;
    // GUI-омпоненты. Это наше окно
    JFrame frame;
    // Кнопка сброса масштабирования
    JButton resetBtn;
    // Кнопка сохранения изображения
    JButton saveBtn;
    // Лейбл рядом с комбобоксом
    JLabel title;
    // Выпадающий список наших фракталов
    JComboBox<FractalGenerator> fractalCBox;
    // Панель, куда будут крепиться комбобокс
    JPanel panel;
    // Панель, куда будут крепиться конопки
    JPanel buttonPanel;

    // Конструктор, инициализирующий по умолчанию фрактал Мандельброта
    public FractalExplorer(int displaySize) {
        this.displaySize = displaySize;
        this.fractalGenerator = new Mandelbrot();
        this.fractalRange = new Rectangle2D.Double(0, 0, 0, 0);
        fractalGenerator.getInitialRange(this.fractalRange);
    }

    // Отображение View-элементов на экране
    public void createAndShowGUI() {
        // Инициализация основных GUI-компонентов
        frame = new JFrame("Fractal Explorers!");
        currImg = new JImageDisplay(displaySize, displaySize);

        resetBtn = new JButton("Reset Display");
        resetBtn.setActionCommand("reset");

        saveBtn = new JButton("Save Image");
        saveBtn.setActionCommand("save");

        title = new JLabel("Fractal: ");

        // Инициализация выпадающего списка
        fractalCBox	= new JComboBox<FractalGenerator>();
        fractalCBox.addItem(new Mandelbrot());
        fractalCBox.addItem(new Burning_ship());
        fractalCBox.addItem(new Tricon());

        panel = new JPanel();
        panel.add(title);
        panel.add(fractalCBox);

        buttonPanel = new JPanel();
        buttonPanel.add(saveBtn);
        buttonPanel.add(resetBtn);


        // Добавление обработчиков событий
        ActionHandler buttonHandler = new ActionHandler();
        MouseHandler mouseHandler = new MouseHandler();
        ComboBoxEvent comboBoxHandler = new ComboBoxEvent();

        // Добавляем обработчики событий к view-лементам
        resetBtn.addActionListener(buttonHandler);
        saveBtn.addActionListener(buttonHandler);
        currImg.addMouseListener(mouseHandler);
        fractalCBox.addActionListener(comboBoxHandler);

        // Позиционируем все наши элементы на экране
        frame.setLayout(new java.awt.BorderLayout());
        frame.add(currImg, java.awt.BorderLayout.CENTER);
        frame.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        frame.add(panel, java.awt.BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Отображение View-элементов
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    // Отображение наших фракталов
    private void drawFractal() {
        for (int i = 0; i < displaySize; i++) {
            for (int j = 0; j < displaySize; j++) {
                double xCoord = FractalGenerator.getCoord(fractalRange.x,fractalRange.x + fractalRange.width, displaySize, i);
                double yCoord = FractalGenerator.getCoord(fractalRange.y,fractalRange.y + fractalRange.width, displaySize, j);
                double countIterations = fractalGenerator.numIterations(xCoord, yCoord);

                if (countIterations == -1) {
                    // Пиксель не в нашем множестве, поэтому рисуем чёрный цвет
                    currImg.drawPixel(i, j, 0);
                }
                else {
                    // Преобразуем из hsb в rgb
                    float hue = 0.7f + (float) countIterations / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    currImg.drawPixel(i, j, rgbColor);
                }
            }
        }
        // Не забываем обновить окно
        currImg.repaint();
    }

    // Обработчик собыйтий для нажатий кнопок
    public class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Если была нажата кнопка reset
            if (e.getActionCommand().equals("reset")) {
                // брасываем масштаб
                fractalGenerator.getInitialRange(fractalRange);
                // Перерисовываем
                drawFractal();
            }
            // Если была нажата кнопка save image
            else {
                // Окно с предложением сохранить файл
                JFileChooser fileChooser = new JFileChooser();
                // Фильтр
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                fileChooser.setFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int res = fileChooser.showSaveDialog(currImg);

                if (res == JFileChooser.APPROVE_OPTION) {
                    try {
                        // Сохраняем изображение
                        javax.imageio.ImageIO.write(currImg.getBfImage(), "png", fileChooser.getSelectedFile());
                    } catch (NullPointerException | IOException e1) {
                        // Произошла ошибка
                        javax.swing.JOptionPane.showMessageDialog(currImg, e1.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    // Обработчки нажатия комбобокса
    public class ComboBoxEvent implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Оператор преобразования совместим, т.к fractalGenerator - базовый класс
            fractalGenerator = (FractalGenerator) fractalCBox.getSelectedItem();
            fractalGenerator.getInitialRange(fractalRange);
            // Рисуем фрактал
            drawFractal();
        }
    }
    // Обработчик нажатия мыши
    public class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Получаем новые координаты нажатия мыши
            double xCoord = FractalGenerator.getCoord(fractalRange.x,fractalRange.x + fractalRange.width, displaySize, e.getX());
            double yCoord = FractalGenerator.getCoord(fractalRange.y,fractalRange.y + fractalRange.width, displaySize, e.getY());
            // Зумим
            fractalGenerator.recenterAndZoomRange(fractalRange, xCoord, yCoord, 0.5);
            // Рисуем фрактал
            drawFractal();
        }
    }

    // Запуск приложения
    public static void main(String[] args) {
        FractalExplorer fracExp = new FractalExplorer(800);
        fracExp.createAndShowGUI();
        fracExp.drawFractal();
    }
}