public class App {
    public static void main(String[] args) {
        System.out.println("************** Welcome to CAR RENTAL MANAGEMENT *****************");
       Vehicle myCar = new Cars("Model S", "Tesla", 12000, "$150", "Sedan", "5");
       myCar.displayDetails();
       ((Cars) myCar).displayCarDetails();
       
    }

   
}
