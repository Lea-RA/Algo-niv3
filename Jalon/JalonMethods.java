import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class JalonMethods {

    static Scanner scanner = new Scanner(System.in);
    static ArrayList<String[]> consultations = new ArrayList<>();

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Horaires cabinet 
    static final LocalTime AM_START = LocalTime.of(8,0);
    static final LocalTime AM_END = LocalTime.of(12,0);
    static final LocalTime PM_START = LocalTime.of(14,0);
    static final LocalTime PM_END = LocalTime.of(17,0);

    // Informations des types de consultations en collection clé/valeur
    static final Map<String, String> types = new LinkedHashMap<>() {{
        put("BS", "Bilan de santé - 120 euros");
        put("CD", "Cardiologie - 200 euros");
        put("VC", "Vaccinations - sans frais");
        put("CM", "Certification médicale - 100 euros");
        put("GN", "Général - 70 euros");
        put("SM", "Suivi médical - 60 euros");
    }};
    // Prix des types de consultations
    static final Map<String, Double> basePrice = Map.of(
        "BS", 120d,
        "CD", 200d,
        "VC", 0d,
        "CM", 100d,
        "GN", 70d,
        "SM", 60d
    );
    // Durées des types de consultations
    static final Map<String, Integer> duration = Map.of(
        "BS", 150,
        "CD", 60,
        "VC", 15,
        "CM", 10,
        "GN", 45,
        "SM", 30
    );


    public static void displayMenu() {
        System.out.println("\n===== CONSULTATIONS MEDICALES =====");
        System.out.println("(1). Ajouter une consultation");
        System.out.println("(2). Modifier une consultation");
        System.out.println("(3). Annuler une consultation");
        System.out.println("(4). Afficher les consultations à venir");
        System.out.println("(0). Quitter");
    }



    public static void addConsultation() {
        // Récolte des informations de création de consultation
        // Nom, Prénom, Âge
        System.out.println("\nNom : ");
        String surname = scanner.nextLine().trim();

        System.out.println("Prénom : ");
        String name = scanner.nextLine().trim();

        System.out.println("Âge : ");
        int age = Integer.parseInt(scanner.nextLine().trim());

        // Type de consultations
    }



    public static void chooseConsultType() {
        // Demande le type de consultations 
        System.out.println("Type de consultation : ");
        List<String> codes = new ArrayList<>(types.keySet());
        for (int i = 0; i < codes.size(); i++) {
            System.out.println((i+1)+". "+types.get(codes.get(i)));
        }
    }

}
