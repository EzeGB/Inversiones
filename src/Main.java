
import java.lang.Math;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Pesimista: ");
        double pesimista = scan.nextDouble();
        System.out.println("Probable: ");
        double probable = scan.nextDouble();
        System.out.println("Optimista: ");
        double optimista = scan.nextDouble();
        for (int i = 0; i<10; i++) {
            System.out.println("Se simulo: " + simularTriangular(pesimista,probable,optimista));
        }
    }
    public static double simularTriangular(double pesimista, double probable, double optimista){
        double relacion = (probable-pesimista)/(optimista-pesimista);
        double R = Math.random();
        if (R <= relacion){
            return (pesimista + Math.sqrt((optimista-pesimista)*(probable-pesimista)*R));
        } else {
            return (optimista - Math.sqrt((optimista-pesimista)*(optimista-probable)*(1-R)));
        }
    }

}
