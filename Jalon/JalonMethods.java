import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class JalonMethods {

    static Scanner scanner = new Scanner(System.in);
    static ArrayList<String[]> consultations = new ArrayList<>(); // Liste des consultations

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Horaires cabinet
    static final LocalTime AM_START = LocalTime.of(8,0);
    static final LocalTime AM_END = LocalTime.of(12,0);
    static final LocalTime PM_START = LocalTime.of(14,0);
    static final LocalTime PM_END = LocalTime.of(17,0);

    // Informations des types de consultations en collection clé/valeur
    // Clé : code du type de consultation
    // Valeur : description du type de consultation avec son prix
    static final Map<String, String> types = new LinkedHashMap<>() {{
        put("BS", "Bilan de santé - 120 euros");
        put("CD", "Cardiologie - 200 euros");
        put("VC", "Vaccinations - sans frais");
        put("CM", "Certification médicale - 100 euros");
        put("GN", "Général - 70 euros");
        put("SM", "Suivi médical - 60 euros");
    }};

    // Prix des types de consultations
    // Clé : code du type de consultation
    // Valeur : prix de base du type de consultation
    static final Map<String, Double> basePrice = Map.of(
        "BS", 120d,
        "CD", 200d,
        "VC", 0d,
        "CM", 100d,
        "GN", 70d,
        "SM", 60d
    );
    
    // Durées des types de consultations
    // Clé : code du type de consultation
    // Valeur : durée en minutes du type de consultation
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
        System.out.print("\nNom : ");
        String surname = scanner.nextLine().trim();

        System.out.print("Prénom : ");
        String name = scanner.nextLine().trim(); 

        System.out.print("Âge : ");
        int age = verifyInput(1, 120); // Vérifie que l'âge est de min 1 au plus âgé

        // Type de consultation
        String codeType = chooseConsultType();

        // Date et heure de la consultation
        LocalDateTime dateTime = chooseConsultDateTime(codeType);
        
        // Génération du code de réference de la consultation
        String Initials = (name.charAt(0) +""+ surname.charAt(0)).toUpperCase(); // Initiales du patient
        String dateCode = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")); // Date et heure de la consultation
        String refCode = Initials + dateCode + codeType; // Code de référence de la consultation

        // Calcul du prix de la consultation avec éventuelles réductions
        double price = basePrice.get(codeType); // Prix de base du type de consultation
        if (age < 18 || age >= 60) {
            price *= 0.8; // Réduction de 80% pour les mineurs et les seniors par l'assurance maladie
        } else {
            price *= 0.6; // Réduction de 60% de base par l'assurance maladie
        }

        // Ajout de la consultation à la liste
        String[] consultation = {refCode, name, surname, String.valueOf(age), codeType, dateTime.format(formatter), String.format("%.2f", price)};
        consultations.add(consultation);

        System.out.println("\nConsultation ajoutée avec succès !");
        System.out.println("Code de référence : " + refCode);
        System.out.println("Prix à payer : " + String.format("%.2f", price) + " euros");

    
    }



    public static String chooseConsultType() {
        // Affiche les types de consultations disponibles
        // Retourne le code du type de consultation choisi
        System.out.println("Type de consultation :\n");
        List<String> codes = new ArrayList<>(types.keySet());
        for (int i = 0; i < codes.size(); i++) {
            System.out.println((i+1)+". "+types.get(codes.get(i)));
        }

        System.out.print("\nChoisissez un type de consultation : ");
        int choice = verifyInput(1, codes.size()); // Vérifie que le choix est dans la plage valide
        return codes.get(choice - 1); // Retourne le code du type de consultation choisi

    }



    public static boolean timeIsAvailable(LocalDateTime start, int durationMinutes) {
        // Vérifie si la date de la consultation est en semaine
        DayOfWeek day = start.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            System.out.println("Les consultations ne sont pas disponibles le week-end.");
            return false;
        }

        LocalTime timeStart = start.toLocalTime(); // Récupère l'heure de début de la consultation
        LocalTime timeEnd = timeStart.plusMinutes(durationMinutes); // Calcule l'heure de fin de la consultation

        boolean am = !timeStart.isBefore(AM_START) && !timeEnd.isAfter(AM_END); // Vérifie si la consultation est dans les horaires du matin
        boolean pm = !timeStart.isBefore(PM_START) && !timeEnd.isAfter(PM_END); // Vérifie si la consultation est dans les horaires de l'après-midi

        return am || pm; // Retourne true si la consultation est dans les horaires du cabinet
    }



    public static boolean cancelOverlap(LocalDateTime start, int durationMinutes) {
        // Vérifie si la date de la consultation n'est pas en conflit avec une autre consultation
        LocalDateTime end = start.plusMinutes(durationMinutes); // Calcule l'heure de fin de la consultation
        for (String[] consults : consultations) {
            LocalDateTime existingStart = LocalDateTime.parse(consults[5], formatter); // Récupère l'heure de début de la consultation existante
            LocalDateTime existingEnd = existingStart.plusMinutes(duration.get(consults[4])); // Calcule l'heure de fin de la consultation existante

            // Vérifie si les deux consultations se chevauchent
            boolean overlap = (!start.isAfter(existingEnd) || !end.isBefore(existingStart)); // start n'est pas après la fin de l'existante ou end n'est pas avant le début de l'existante
            if (overlap) {
                return true; // Retourne true si il y a un conflit
            }
        }
        return false; // Retourne false si il n'y a pas de conflit
    }



    public static void displaySortedConsultations() {
        // Affiche les consultations triées par date et heure
        if (consultations.isEmpty()) {
            System.out.println("Aucune consultation à afficher.");
            return;
        }

        // Trie les consultations par date et heure
        consultations.sort(Comparator.comparing(consult -> LocalDateTime.parse(consult[5], formatter)));

        System.out.println("\nConsultations à venir :");
        for (String[] consult : consultations) {
            displayConsultation(consult);
            System.out.println("---------------------------------");
        }       
    }



    public static void displayConsultation(String[] consult) {
        // Affiche les informations d'une consultation

        System.out.println("Code de référence : " + consult[0]);
        System.out.println("Nom : " + consult[2] + " " + consult[1]);
        System.out.println("Âge : " + consult[3]);
        System.out.println("Type de consultation : " + types.get(consult[4]));
        System.out.println("Date et heure : " + consult[5]);
        System.out.println("Prix : " + consult[6] + " euros");
    }



    public static void cancelConsultation() {
        String[] consult = searchByRefCode();
        if (consult != null) {
            consultations.remove(consult); // Supprime la consultation de la liste
            System.out.println("Consultation annulée avec succès !");
        } else {
            System.out.println("Aucune consultation trouvée avec ce code de référence.");
        }
        
    }


    public static int verifyInput(int min, int max) {
        // Vérifie si l'entrée de l'utilisateur est valide dans une plage donnée
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Veuillez entrer un nombre valide : ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide, veuillez entrer un nombre : ");
            }
        }
    }
}

