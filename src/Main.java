
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
        ArrayList<Double> Inf = new ArrayList<>();
        Inf.add(simularTriangular(recolectarEstimaciones(scan, "Inf1")));
        Inf.add(simularTriangular(recolectarEstimaciones(scan, "Inf2")));
        Inf.add(simularTriangular(recolectarEstimaciones(scan, "Inf3")));
        Inf.add(simularTriangular(recolectarEstimaciones(scan, "Inf4")));
        Inf.add(simularTriangular(recolectarEstimaciones(scan, "Inf5")));
        double TasaImpuestos = 0.5;
        double [] FDI  = new double[5];

        for (int i = 0; i<5; i++){
            FDI[i]=calcularFDI((i+1),FAI[i],Inf,TasaImpuestos,AF,AC);
        }
        double VR = (AC + (0.2*AF)*(1-TasaImpuestos))*(-1);
        System.out.println("VPN con TIR=-3.1: " +calcularVPN(FDI,AF,AC,VR,-0.031));
        System.out.println("VPN con TIR=19.91: " + calcularVPN(FDI,AF,AC,VR,.1991));
        System.out.println("VPN con TIR=7.26: "+calcularVPN(FDI,AF,AC,VR,.0726));

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

        if (variable.equals("Inf1")||variable.equals("Inf2")||variable.equals("Inf3")||variable.equals("Inf4")||variable.equals("Inf5")){
            double intercambio = datos[0];
            datos[0]=datos[2];
            datos[2]=intercambio;
        }
        return datos;
    }


    public static double calcularFDI(int periodo, double FAI, ArrayList<Double>Inf, double T, double AF, double AC){
        double productoriaGral = calcularProductoriaInflacion(1,periodo,Inf);
        double productoriaAC = 0;
        if (periodo>1){
            productoriaAC = calcularProductoriaInflacion(2, periodo, Inf);
        }
        return (((FAI)*(productoriaGral)*(1-T))+((0.2*-AF)*(T))-((-AC*Inf.get((periodo-1)))*(productoriaAC)))/(productoriaGral);
    }

    public static double calcularProductoriaInflacion(int periodoInicio, int cantidadPeriodos, ArrayList<Double>Inf){
        double producto=1;
        for (int i=(periodoInicio-1); i<cantidadPeriodos; i++){
            producto=producto*(1+Inf.get(i));
        }
        return producto;
    }

    public static double calcularVPN(double []FDI, double AF, double AC, double VR, double TIR){
        double sumatoria = 0;
        for (int i = 0; i<5; i++){
            sumatoria = sumatoria + (FDI[i]/ Math.pow((1+TIR),(i+1)));
        }
        sumatoria = sumatoria + VR/ Math.pow((1+TIR),5);
        return sumatoria + AC + AF;
    }

}
