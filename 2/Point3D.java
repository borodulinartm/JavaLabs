public class Point3D {
    private double xCoord, yCoord, zCoord;

    // Конструктор с параметрами
    Point3D(double x, double y, double z) {
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }

    // Конструктор по умолчанию
    Point3D() {
        this(0., 0., 0.);
    }

    // Геттеры
    public double getxCoord() {
        return xCoord;
    }

    public double getyCoord() {
        return yCoord;
    }

    public double getzCoord() {
        return zCoord;
    }

    // Сеттеры для приватных полей класса
    public void setxCoord(double xCoord) {
        this.xCoord = xCoord;
    }

    public void setyCoord(double yCoord) {
        this.yCoord = yCoord;
    }

    public void setzCoord(double zCoord) {
        this.zCoord = zCoord;
    }

    // Проверка на сравнение строк
    public boolean equals(Point3D other) {
        return xCoord == other.xCoord && yCoord == other.yCoord && zCoord == other.zCoord;
    }

    // Вычисление расстояния между строками
    public double distanceTo(Point3D other) {
       return Math.sqrt(Math.pow((xCoord - other.xCoord),2) + Math.pow((yCoord - other.yCoord), 2) +
                Math.pow((zCoord - other.zCoord), 2));
    }
}
