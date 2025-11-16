public class booking extends Vehicle {
    String startDate;
    String endDate;

    public booking(String model, String make, int millege, String ratePerDay, String startDate, String endDate){
        super(model, make, millege, ratePerDay);
        this.startDate = startDate;
        this.endDate = endDate;
    }


    @Override
    void isAvailable(){
        System.out.println("Booking is Available");
    }
    @Override
    void startRental(){
        System.out.println("Booking started");
    }
    @Override
    void endRental(){
        System.out.println("Booking ended");
    }


    public String getStartDate() {
        return startDate;
    }


    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getEndDate() {
        return endDate;
    }


    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
