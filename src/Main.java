
import java.lang.Math;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Ingrese estimaciones AF");
        double[] estimacionesAF = recolectarEstimaciones(scan);
        System.out.println("Ingrese estimaciones AC");
        double[] estimacionesAC = recolectarEstimaciones(scan);
        System.out.println("Ingrese estimaciones FAI");
        double[] estimacionesFAI = recolectarEstimaciones(scan);

        System.out.println("Se simulo para AF:" + simularTriangular(estimacionesAF));
        System.out.println("Se simulo para AC:" + simularTriangular(estimacionesAC));

        for (int i = 0; i<5; i++) {
            System.out.println("Se simulo para FAI en año " + (i+1) + ": " +simularTriangular(estimacionesFAI));
        }
    }
    public static double simularTriangular(double[] estimaciones){
        double a = estimaciones[0];
        double b = estimaciones[1];
        double c = estimaciones[2];

        double relacion = (b - a)/(c - a);
        double R = Math.random();
        if (R <= relacion){
            return (a + Math.sqrt((c - a)*(b - a)*R));
        } else {
            return (c - Math.sqrt((c - a)*(c - b)*(1-R)));
        }
    }

    public static double[] recolectarEstimaciones(Scanner scan){
        double [] datos = new double[3];
        System.out.println("Pesimista: ");
        datos[0] = scan.nextDouble();
        System.out.println("Probable: ");
        datos[1]  = scan.nextDouble();
        System.out.println("Optimista: ");
        datos[2]  = scan.nextDouble();
        return datos;
    }

}
