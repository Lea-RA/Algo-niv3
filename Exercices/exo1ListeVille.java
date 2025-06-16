/* A l'aide de boucle, tableau, exception et fonction. Me faire un tableau contenant 5 noms de villes (Vous pouvez choisir vos propres villes), créer une méthode pour afficher les pays du tableau numéroté par ligne et autre une méthode pour choisir une villes selon 

la position de sa cellule du tableau et puis affichera un message de bienvenue dans la langue de la ville choisi.:

Bonjour, quelle ville voulez-vous visiter ?

0 - Paris 
1 - Londres
2 - Madrid
3 - Lisbonne
4 - Berlin

0
Bienvenue à Paris !

Voulez-vous visiter une autre ville ? (O/N)
O

1
Welcome to London !

Voulez-vous visiter une autre ville ? (O/N)
O

2
Bienvenido a Madrid !
Voulez-vous visiter une autre ville ? (O/N)
O


3
Bem-vindo a Lisboa !
Voulez-vous visiter une autre ville ? (O/N)
O

4
Willkommen in Berlin !
Voulez-vous visiter une autre ville ? (O/N)
O


5
Désolé, cette ville n'est pas sur la liste !
Voulez-vous visiter une autre ville ? (O/N)
O

Votre voyage est terminé :) !

*/

import java.util.Scanner;

public class exo1ListeVille {

    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {

        String[] cities = {"Paris", "Londres", "Madrid", "Lisbonne", "Berlin"};
        String[] greets = {"Bienvenue à Paris !", "Welcome to London !", "Bienvenido a Madrid !", "Bem-vindo a Lisboa !", "Willkommen in Berlin !"};

        System.out.println("\nBonjour, quelle ville voulez-vous visiter ?\n");

        printList(cities);

        while(true) {

            choose(cities, greets);
            boolean visit = questionYesNo("Voulez-vous visiter une autre ville ? (O/N)\n");

            if (!visit) {
                break;  
            }
        }

        System.out.println("\nVotre voyage est terminé !\n");    
    }

    // Méthode permettant de poser une question à choix fermé
    public static boolean questionYesNo(String question) { 
        
        while (true) {
            System.out.print(question);

            try {
                String response = sc.nextLine().trim().toLowerCase();

                if (response.equals("o")) {
                    return true;  
                } else if (response.equals("n")) {
                    return false;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                System.out.println("\nRépondez par O ou N.");
            }
        }         
    }

    // Méthode permettant d'afficher une liste avec un numéro correspondant
    public static void printList(String[] list) {

        for (int i=0; i < list.length; i++) {
            System.out.println((i+1)+" - "+list[i]);
        }
        System.out.print("\n");
    }

    // Méthode demandant un numéro affichant un message correspondant entre deux listes
    public static void choose(String[] city, String[] greet) {

        while(true) {
            try {
                int choose = Integer.parseInt(sc.nextLine());

                if (choose >= 1 && choose <= city.length) {
                    String greeting = greet[choose-1];
                    System.out.println(greeting);
                    break;
                } else {
                    System.out.println("\nCe numéro ne correspond à aucune ville, veuillez donner un autre chiffre :");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nEntrée invalide, veuillez donner un chiffre :");
            }
        }
    }
}