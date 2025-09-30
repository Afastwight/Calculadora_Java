import java.util.Scanner;

public class calculadorav1 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        calculadorav1 ca = new calculadorav1();
        int opcion = 0; 
        boolean salir=true;
        double x, y;

        while (salir == true) {
        System.out.println("\n Bienvenido a tu calculadora: ");
        System.out.println("1.- Suma");
        System.out.println("2.- Resta");
        System.out.println("3.- Multiplicacion");
        System.out.println("4.- Division");
        System.out.println("5.- Salir");
        System.out.println("Opcion: ");
        opcion = sc.nextInt();

        System.out.println("");
        switch (opcion){
            case 1:
                System.out.println("Ingrese el primer numero: ");
                x = sc.nextDouble();
                System.out.println("Ingrese el segundo numero:");
                y = sc.nextDouble();
                System.out.println("");
                System.out.println("Tu respuesta es: " + ca.suma(x, y));
                break;
            case 2:
                System.out.println("Ingrese el primer numero: ");
                x = sc.nextDouble();
                System.out.println("Ingrese el segundo numero:");
                y = sc.nextDouble();
                System.out.println("");
                System.out.println("Tu respuesta es: " + ca.resta(x, y));
                break;
            case 3:
                System.out.println("Ingrese el primer numero: ");
                x = sc.nextDouble();
                System.out.println("Ingrese el segundo numero:");
                y = sc.nextDouble();
                System.out.println("");
                System.out.println( "Tu Respuesta es: " + ca.multiplicacion(x, y));
                break;
            case 4:
                System.out.println("Ingrese el primer numero: ");
                x = sc.nextDouble();
                System.out.println("Ingrese el segundo numero:");
                y = sc.nextDouble();
                System.out.println("");
                System.out.println("Tu Respuesta es: " + ca.division(x, y));
                break;
            default:
                System.out.println("Gracias por usar nuestra calculadora");
                System.out.println("");
                salir = false;
        }
    }

    }
    public double suma(double a, double b){
        return  a + b;
    }
    public double multiplicacion(double a, double b){
        return a * b;
    }
    public double division(double a, double b){
        return a / b;
    }
    public double resta(double a, double b){
        return a - b;
    }
}


