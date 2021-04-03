import java.util.Scanner;

public class Main {
    public static double computeArea(Point3D first_point, Point3D second_point, Point3D third_point) {
        double x = first_point.distanceTo(second_point);
        double y = second_point.distanceTo(third_point);
        double z = third_point.distanceTo(first_point);

        double polu_perimeter = (x + y + z) / 2;
        return Math.sqrt(polu_perimeter * (polu_perimeter - x) * (polu_perimeter - y) * (polu_perimeter - z));
    }

    public static void main(String[] args) {
        Point3D point_1 = new Point3D(7.85, 3.52, 4.96);
        Point3D point_2 = new Point3D(5., 7.47, 8.);
        Point3D point_3 = new Point3D(10.34, 8.96, -9.41);

        if (point_1.equals(point_2) || point_1.equals(point_3) || point_3.equals(point_2)) {
            System.out.println("Площадь не вычислима");
        } else {
            System.out.println(computeArea(point_1, point_2, point_3));
        }
    }
}
