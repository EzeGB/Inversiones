
import java.lang.Math;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        double AF = simularTriangular(recolectarEstimaciones(scan, "AF"));
        double AC = simularTriangular(recolectarEstimaciones(scan, "AC"));
        double[] estimacionesFAI = recolectarEstimaciones(scan, "FAI");
        double [] FAI = new double[5];
            for (int i = 0; i<5; i++) {
                FAI[i]=simularTriangular(estimacionesFAI);
            }
        ArrayList<Double> Inf = new ArrayList<Double>();
        Inf.add(simularTriangular(recolectarEstimaciones(scan, "Inf1")));
        Inf.add(simularTriangular(recolectarEstimaciones(scan, "Inf2")));
        Inf.add(simularTriangular(recolectarEstimaciones(scan, "Inf3")));
        Inf.add(simularTriangular(recolectarEstimaciones(scan, "Inf4")));
        Inf.add(simularTriangular(recolectarEstimaciones(scan, "Inf5")));
        double TasImpuestos = 0.5;
        double FDI [] = new double[5];
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

    public static double[] recolectarEstimaciones(Scanner scan, String variable){
        double [] datos = new double[3];
        System.out.println("Ingrese estimaciones " + variable);
        System.out.println("Pesimista: ");
        datos[0] = scan.nextDouble();
        System.out.println("Probable: ");
        datos[1]  = scan.nextDouble();
        System.out.println("Optimista: ");
        datos[2]  = scan.nextDouble();
        return datos;
    }

    public static double calcularFDI(double FAI, ArrayList<Double>Inf, double T, double AF, double AC){
        return 1;
    }

}
