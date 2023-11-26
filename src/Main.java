
import java.lang.Math;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Numero de iteraciones?:");
        int numeroIteraciones = scan.nextInt();

        double [] estAF = recolectarEstimaciones(scan, "AF");
        double [] estAC = recolectarEstimaciones(scan, "AC");
        double [] estFAI = recolectarEstimaciones(scan, "FAI");
        double [] estInf1 = recolectarEstimaciones(scan, "Inf1");
        double [] estInf2 = recolectarEstimaciones(scan, "Inf2");
        double [] estInf3 = recolectarEstimaciones(scan, "Inf3");
        double [] estInf4 = recolectarEstimaciones(scan, "Inf4");
        double [] estInf5 = recolectarEstimaciones(scan, "Inf5");
        double TasaImpuestos = 0.5;
        double TIRminima=calcularTIRextrema(estAF[0],estAC[0],estFAI[0],estInf1[2],estInf2[2],estInf3[2],estInf4[2],estInf5[2],TasaImpuestos);
        double TIRmaxima=calcularTIRextrema(estAF[2],estAC[2],estFAI[2],estInf1[0],estInf2[0],estInf3[0],estInf4[0],estInf5[0],TasaImpuestos);
        System.out.println("TIR minima: "+ TIRminima);
        System.out.println("TIR maxima: "+ TIRmaxima);
        double [] reporteTIR = new double[numeroIteraciones];
        int indiceReporte = 0;

        while (numeroIteraciones>0){
            reporteTIR[indiceReporte] = iterar(estAF,estAC,estFAI,estInf1,estInf2,estInf3,estInf4,estInf5,
                                        TasaImpuestos,TIRminima,TIRmaxima);
            indiceReporte++;
            numeroIteraciones--;
        }

    }
    public static double iterar(double [] estAF, double [] estAC, double [] estFAI, double [] estInf1,
                                double [] estInf2, double [] estInf3, double [] estInf4, double [] estInf5,
                                double TasaImpuestos ,double TIRminima, double TIRmaxima){
        double AF = simularTriangular(estAF);
        double AC = simularTriangular(estAC);
        double [] FAI = new double[5];
        for (int i = 0; i<5; i++) {
            FAI[i]=simularTriangular(estFAI);
        }
        ArrayList<Double> Inf = new ArrayList<>();
        Inf.add(simularTriangular(estInf1));
        Inf.add(simularTriangular(estInf2));
        Inf.add(simularTriangular(estInf3));
        Inf.add(simularTriangular(estInf4));
        Inf.add(simularTriangular(estInf5));
        double [] FDI  = new double[5];

        for (int i = 0; i<5; i++){
            FDI[i]=calcularFDI((i+1),FAI[i],Inf,TasaImpuestos,AF,AC);
        }
        double VR = (AC + (0.2*AF)*(1-TasaImpuestos))*(-1);
        double [] intervalosTIR = calcularIntervalosTIR(TIRminima,TIRmaxima,100);
        double TIR0 = 0;

        int Intervaloinferior = 0;
        while (calcularVPN(FDI,AF,AC,VR,intervalosTIR[Intervaloinferior])>0){
            TIR0=interpolarLinealA0(intervalosTIR[Intervaloinferior],intervalosTIR[Intervaloinferior+1],
                    calcularVPN(FDI,AF,AC,VR,intervalosTIR[Intervaloinferior]), calcularVPN(FDI,AF,AC,VR,intervalosTIR[Intervaloinferior+1]));
            Intervaloinferior++;
        }

        System.out.println("La TIR interpolada es: " + TIR0 + " con una VPN de: " + calcularVPN(FDI,AF,AC,VR,TIR0));
        return TIR0;
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

    public static double interpolarLinealA0 (double TIRinferior, double TIRsuperior, double VPNinferior, double VPNsuperior){
        double TIR0 = (((-VPNinferior)*((TIRsuperior-TIRinferior)/(VPNsuperior-VPNinferior))) + TIRinferior);
        return TIR0;
    }

    public static double [] calcularIntervalosTIR (double TIRminima, double TIRmaxima, int numeroIntervalos){
        double [] intervalos=new double[numeroIntervalos+1];
        double intervalo = (TIRmaxima-TIRminima)/numeroIntervalos;
        for (int i=0; i<(numeroIntervalos+1);i++){
            intervalos[i]=TIRminima+i*intervalo;
        }
        return intervalos;
    }

    public static double calcularTIRextrema (double AF, double AC, double FAI, double Inf1, double Inf2, double Inf3,
                                             double Inf4, double Inf5, double TasaImpuestos){
        double TIRextrema = 0;
        ArrayList<Double> Inf = new ArrayList<>();
        Inf.add(Inf1);
        Inf.add(Inf2);
        Inf.add(Inf3);
        Inf.add(Inf4);
        Inf.add(Inf5);
        double [] FDI  = new double[5];
        for (int i = 0; i<5; i++){
            FDI[i]=calcularFDI((i+1),FAI,Inf,TasaImpuestos,AF,AC);
        }
        double VR = (AC + (0.2*AF)*(1-TasaImpuestos))*(-1);

        if (calcularVPN(FDI,AF,AC,VR,TIRextrema)>0){
            for (int i=0; calcularVPN(FDI,AF,AC,VR,TIRextrema)>0; i++){
                TIRextrema=TIRextrema+0.0001;
            }
        } else if (calcularVPN(FDI,AF,AC,VR,TIRextrema)<0) {
            for (int i=0; calcularVPN(FDI,AF,AC,VR,TIRextrema)<0; i++){
                TIRextrema=TIRextrema-0.0001;
            }
        }
        return TIRextrema;
    }

}
