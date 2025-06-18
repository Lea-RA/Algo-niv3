// Afficher le résultat d'une addition avec une fonction

import java.util.InputMismatchException;
import java.util.Scanner;

public class newExo1 {

    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {

        int resultat = additionner(0, 0);
        System.out.println("résultat : "+resultat+"\n");
        
    }

    static int additionner(int a, int b) {
        
        System.out.print("\nEntrez deux chiffres (séparés par un espace) : ");

        while (true) {
            try {
                a = sc.nextInt();
                b = sc.nextInt();
                return a + b;

            } catch (InputMismatchException e) {
                System.out.print("\nErreur. Veuillez entrer deux chiffres (séparés par un espace) : ");
                sc.nextLine();
            }
        }
    }

}
