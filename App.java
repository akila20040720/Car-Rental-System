import java.util.Scanner;

public class App {
    public static Vehicle[] vehicle = new Vehicle[10];
    public static booking[] bookings = new booking[100];
    private static int vehicleCount = 0;

    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("************** Welcome to CAR RENTAL MANAGEMENT *****************");
        selector();
        sc.close();
    }

    public static void addCar() {
        if (vehicleCount >= vehicle.length) {
            System.out.println("Fleet is full!");
            return;
        }

        System.out.println("Adding a new car to the fleet");

        System.out.print("Enter Car Model: ");
        String model = sc.nextLine();

        System.out.print("Enter Car Make: ");
        String make = sc.nextLine();

        System.out.print("Enter the current mileage: ");
        int currentMileage = sc.nextInt();
        sc.nextLine();  // ← FIX 1: consume leftover newline

        System.out.print("Enter Rate per Day: ");
        String ratePerDay = sc.nextLine();

        System.out.print("Enter Car Type: ");
        String type = sc.nextLine();

        System.out.print("Enter Seating Capacity: ");
        String seatingCapacity = sc.nextLine();

        Vehicle newCar = new Cars(model, make, currentMileage, ratePerDay, type, seatingCapacity);

        vehicle[vehicleCount] = newCar;
        vehicleCount++;

        System.out.println("New car added successfully!");
        newCar.displayDetails();
        System.out.println();
    }

    public static void viewCars() {
        System.out.println("Viewing all cars in the fleet:");

        if (vehicleCount == 0) {
            System.out.println("No cars added yet.");
            return;
        }

        for (int i = 0; i < vehicleCount; i++) {
            Vehicle v = vehicle[i];
            System.out.println("---- Car #" + (i+1) + " ----");
            System.out.println("Make: " + v.getMake());
            System.out.println("Model: " + v.getModel());
            System.out.println();
        }
    }

    public static void selector() {
        while (true) {
            System.out.println("Menu - Select an option:");
            System.out.println("1: View Cars");
            System.out.println("2: Add Car");
            System.out.println("3: Exit");

            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // ← FIX 2: consume leftover newline
            System.out.println();

            switch (choice) {
                case 1:
                    viewCars();
                    break;

                case 2:
                    addCar();
                    break;

                case 3:
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}






// import java.util.Scanner;


// public class App {
//     public static Vehicle[] vehicle = new Vehicle[10];
//     booking[] bookings = new booking[10];
    
//     public static void main(String[] args) {
//         System.out.println("************** Welcome to CAR RENTAL MANAGEMENT *****************");
//         selector();
      
       
//     }

//     public static  void addCar(){
//         int vehicleCount = 0;
//         Scanner sc = new Scanner(System.in);
//         System.out.println("Adding a new car to the fleet");
//         System.out.print("Enter Car Model:");
//         String model = sc.nextLine();
//         System.out.print("Enter Car Make:");
//         String make = sc.nextLine();
//         System.out.print("Enter the current millege");
//         int currentMillege = sc.nextInt();
//         System.out.print("Enter Rate per Day:");
//         String ratePerDay = sc.nextLine();
//         System.out.println("Enter Car Type:");
//         String type = sc.nextLine();
//         System.out.print("Enter Seating Capacity:");
//         String seatingCapacity = sc.nextLine();
//         Vehicle newCar = new Cars(model, make, currentMillege, ratePerDay, type, seatingCapacity);
//         System.out.println("New car added successfully!");
//         newCar.displayDetails();
//         sc.close();
//         vehicle[vehicleCount] = newCar;
//         vehicleCount++;
//     }

//     public static void viewCars(){
//         System.out.println("Viewing all cars in the fleet:");
//         for(int i=0; i<vehicle.length; i++){
//             System.out.println("Make" + " " +vehicle[i].getMake() );
//             System.out.println("Model" +" " +vehicle[i].getModel());
//         }
//         }

//     public static void selector(){
//         Scanner sc = new Scanner(System.in);
//         System.out.println("Select a car to book:");
//         System.out.println("1: View Cars");
//         System.out.println("2: Add Car");
//         int choice = sc.nextInt();

//         while(true){

        
//         switch(choice){
//             case 1:
//             viewCars();
         
//             break;
//             case 2:
//             addCar();
            
           
    
//         }
//     }
// }
//     }






   

