public class Cars extends Vehicle {
    public String type;
    public String seatingCapacity;
    
public Cars(String Model, String Make, int Millege, String ratePerDay, String type, String seatingCapacity){
    super(Model, Make, Millege, ratePerDay);
    this.type = type;
    this.seatingCapacity = seatingCapacity;
}

   
    @Override
    void isAvailable(){
        System.out.println("Car is still Available");
    }
    @Override
    void startRental(){
        System.out.println("Car is ready to be RENT");
    }
    @Override
    void endRental(){
        System.out.println("Ended rental");
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getSeatingCapacity() {
        return seatingCapacity;
    }


    public void setSeatingCapacity(String seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    void displayCarDetails(){
        displayDetails();
        System.out.println("Type: " + type);
        System.out.println("Seating Capacity: " + seatingCapacity);
    }

   

    
    
}
