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
        put("BS", "Bilan de santé | 120 euros | 2h30");
        put("CD", "Cardiologie | 200 euros | 1h");
        put("VC", "Vaccinations | sans frais | 15min");
        put("CM", "Certification médicale | 100 euros | 10min");
        put("GN", "Général | 70 euros | 45min");
        put("SM", "Suivi médical | 60 euros | 30min");
    }};

    // Prix des types de consultations
    // Clé : code du type de consultation
    // Valeur : prix de base du type de consultation
    static final Map<String, Double> basePrices = Map.of(
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
    static final Map<String, Integer> durations = Map.of(
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
        double price = basePrices.get(codeType); // Prix de base du type de consultation
        if (price > 0) {
            if (age < 18 || age >= 60) {
                price *= 0.8; // Réduction de 80% pour les mineurs et les seniors par l'assurance maladie
            } else {
                price *= 0.6; // Réduction de 60% de base par l'assurance maladie
            }
        }

        // Ajout de la consultation à la liste
        String[] consultation = {refCode, name, surname, String.valueOf(age), codeType, dateTime.format(formatter), String.valueOf(price)};
        consultations.add(consultation);

        System.out.println("\nConsultation ajoutée avec succès.");
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



    public static LocalDateTime chooseConsultDateTime(String codeType) {
        
        LocalDateTime dateTime = null;
        boolean valid = false;
        int duration = durations.get(codeType); // Récupère la durée du type de consultation choisi

        while (!valid) {
            try {
                System.out.print("Date et heure de la consultation (jj/mm/aaaa hh:mm) : ");
                String input = scanner.nextLine().trim();
                dateTime = LocalDateTime.parse(input, formatter); // Parse la date et l'heure de la consultation

                if (dateTime.isBefore(LocalDateTime.now())) {
                    System.out.println("La date et l'heure de la consultation ne peuvent être antérieures à aujourd'hui.");
                } else if (!timeIsAvailable(dateTime, duration)) {
                    System.out.println("La consultation doit être dans les horaires du cabinet (8h-12h et 14h-17h en semaine).");
                } else if (cancelOverlap(dateTime, duration)) {
                    System.out.println("La consultation se chevauche avec une autre consultation existante.");
                } else {
                    valid = true; // La date et l'heure sont valides
                }
            } catch (Exception e) { // Gère les exceptions de parsing
                System.out.println("Format de date invalide. Veuillez utiliser le format jj/mm/aaaa hh:mm.");
            }
        }
        return dateTime; // Retourne la date et l'heure de la consultation
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

        return am || pm; // Retourne true si la consultation est dans les horaires du cabinet soit le matin, soit l'après-midi
    }



    public static boolean cancelOverlap(LocalDateTime start, int durationMinutes) {
        // Vérifie si la date de la consultation n'est pas en conflit avec une autre consultation
        LocalDateTime end = start.plusMinutes(durationMinutes); // Calcule l'heure de fin de la consultation
        for (String[] consults : consultations) {
            LocalDateTime existingStart = LocalDateTime.parse(consults[5], formatter); // Récupère l'heure de début de la consultation existante
            int duration = durations.get(consults[4]); // Récupère la durée de la consultation existante
            LocalDateTime existingEnd = existingStart.plusMinutes(duration); // Calcule l'heure de fin de la consultation existante

            // Vérifie si les deux consultations se chevauchent
            boolean overlap = (start.isBefore(existingEnd) && end.isAfter(existingStart));
            if (overlap) {
                return true; // Retourne true si il y a un conflit
            }
        }
        return false; // Retourne false si il n'y a pas de conflit
    }



    public static void displaySortedConsultations() {
        // Affiche les consultations triées par date et heure
        if (consultations.isEmpty()) {
            System.out.println("\nAucune consultation à afficher.");
            return;
        }

        // Trie les consultations par date et heure
        consultations.sort(Comparator.comparing(consult -> LocalDateTime.parse(consult[5], formatter)));

        System.out.println("\nConsultations à venir :\n");
        for (String[] consult : consultations) {
            displayConsultation(consult);
            System.out.println("---------------------------------");
        }       
    }



    public static void displayConsultation(String[] consult) {
        // Affiche les informations d'une consultation

        String codeType = consult[4]; 
        int age = Integer.parseInt(consult[3]); 
        double basePrice = basePrices.get(codeType);
        double price = Double.parseDouble(consult[6]);
        double discount = basePrice - price;

        System.out.println("Code de référence : " + consult[0]);
        System.out.println("Nom : " + consult[1] + " " + consult[2]);
        System.out.println("Âge : " + age + " ans");
        System.out.println("Type de consultation : " + types.get(codeType));
        System.out.println("Date et heure : " + consult[5]);

        if (basePrice > 0 && discount > 0) {
            System.out.println("Remboursée par l'assurance maladie : " + String.format("%.2f", discount) + " euros");
        }

        System.out.println("Prix à payer : " + String.format("%.2f", price) + " euros");
    }



    public static void cancelConsultation() {
        // Annule une consultation en recherchant par code de référence
        String[] consult = searchByRefCode();
        if (consult != null) {
            consultations.remove(consult); // Supprime la consultation de la liste
            System.out.println("Consultation annulée avec succès.");
        }
    }



    public static void modifyConsultation() {
        // Modifie une consultation en recherchant par code de référence
        String[] consult = searchByRefCode();
        if (consult == null) {
            return; // Si aucune consultation n'a été trouvée, on quitte la méthode
        }

        String typeCode = consult[4]; // Récupère le code du type de consultation

        LocalDateTime newDateTime = chooseConsultDateTime(typeCode); // Choisit une nouvelle date et heure pour la consultation

        // génération du nouveau code de référence
        String Initials = (consult[1].charAt(0) +""+ consult[2].charAt(0)).toUpperCase(); // Initiales du patient
        String dateCode = newDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")); // Date et heure de la consultation
        String newRefCode = Initials + dateCode + typeCode; // Nouveau code de référence de la consultation

        consult[0] = newRefCode; // Met à jour le code de référence de la consultation
        consult[5] = newDateTime.format(formatter); // Met à jour la date et l'heure de la consultation

        System.out.println("\nConsultation modifiée avec succès.");
        System.out.println("Nouveau code de référence : " + newRefCode);
    }



    public static String[] searchByRefCode() {
        // Recherche une consultation par une partie de son code de référence
        System.out.print("Entrez une partie du code de référence recherchée : ");
        String input = scanner.nextLine().trim().toUpperCase();

        List<String[]> matchResults = new ArrayList<>(); // Liste pour stocker les résultats de la recherche
        for (String[] consult : consultations) {
            if (consult[0].contains(input)) { // Vérifie si le code de référence contient l'entrée de l'utilisateur
                matchResults.add(consult); // Ajoute la consultation à la liste des résultats
            }
        }

        if (matchResults.isEmpty()) {
            System.out.println("Aucune consultation trouvée avec un code de référence correspondant.");
            return null; // Retourne null si aucune consultation n'a été trouvée
        }

        // Affiche les résultats de la recherche
        System.out.println("\nCodes de référence correspondant :");
        for (int i = 0; i < matchResults.size(); i++) {
            System.out.println((i + 1) + ". " + matchResults.get(i)[0] + " - " + matchResults.get(i)[2] + 
                " " + matchResults.get(i)[1] + " (" + matchResults.get(i)[5]); // Affiche le code de référence de chaque consultation trouvée
        }

        System.out.print("\nSélectionnez une consultation par son numéro : ");
        int choice = verifyInput(1, matchResults.size()); // Vérifie que le choix est dans la plage valide
        return matchResults.get(choice - 1); // Retourne la consultation choisie par l'utilisateur
    }



    public static int verifyInput(int min, int max) {
        // Vérifie si l'entrée de l'utilisateur est valide dans une plage donnée
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.print("Veuillez entrer un nombre valide : ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrée invalide, veuillez entrer un nombre : ");
            }
        }
    }
}

