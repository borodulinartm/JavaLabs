import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

// Класс-виджет, используемый для рисования фракталов
public class JImageDisplay extends JComponent {
    // Это изображение, которым мы управляем
    private BufferedImage bfImage;

    // width * height нашего окна
    JImageDisplay(int width, int height) {
        // Код из методички. Ничего сложного ;)
        bfImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Dimension dm = new Dimension(width, height);
        super.setPreferredSize(dm);
    }

    // Рисуем фракталы
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bfImage, 0, 0, bfImage.getWidth(), bfImage.getHeight(), null);
    }

    // Данный метод устанавливает чрёрный цвет для всех пикселов
    public void clearImage() {
        for(int i = 0; i < bfImage.getWidth(); ++i) {
            for(int j = 0; j < bfImage.getHeight(); ++j) {
                bfImage.setRGB(i, j, 0);
            }
        }
    }

    // Метод задаёт указанный цвет пиксела
    public void drawPixel(int x, int y, int colorRGB) {
        bfImage.setRGB(x, y, colorRGB);
    }

    public BufferedImage getBfImage() {
        return bfImage;
    }
}
