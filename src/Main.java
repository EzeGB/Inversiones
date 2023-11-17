
import java.lang.Math;
public class Main {
    public static void main(String[] args) {
        System.out.println(simularTriangular(-100000, -70000,-60000));
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
