import java.util.*;

public class fonctions {

    static Scanner sc = new Scanner(System.in);

    // méthode permettant de poser une question à choix fermé (booléen)
    // prends en compte les types de réponses oui/non classique français et anglais
    // la question se répète tant que les entrées sont fausses
    // utile pour fermer une boucle sous condition ou faire des choix A ou B
    // EX. boolean ciel = questionYesNo("Le ciel est-il bleu ? (oui/non)")
    public static boolean questionYesNo(String question) { 

        while (true) {
            
            System.out.print(question);
            List<String> positive = List.of("o", "oui", "y", "yes", "t", "true");
            List<String> negative = List.of("n", "non", "x", "no", "f", "false");

            try {
                String response = sc.nextLine().trim().toLowerCase();

                if (positive.contains(response)) {
                    return true;  
                } else if (negative.contains(response)) {
                    return false;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                System.out.println("\nRépondez par un des choix proposés.");
            }
        }         
    }
}
