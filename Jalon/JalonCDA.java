import java.util.*;

public class JalonCDA {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        int choice;

        do {
            JalonMethods.displayMenu();
            System.out.print("\nVotre choix : ");
            choice = JalonMethods.verifyInput(0, 4);
        

            switch (choice) {
                case 1: 
                    JalonMethods.addConsultation(); break;
                case 2:
                    JalonMethods.modifyConsultation(); break;
                case 3:
                    JalonMethods.cancelConsultation(); break;
                case 4:
                    JalonMethods.displaySortedConsultations(); break;
                case 0:
                    System.out.println("\nAu revoir !\n"); break;
            }

        } while (choice != 0);
    }
}

