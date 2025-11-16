public abstract class Vehicle {
        protected String Model;
        protected String Make;
        protected int Millege;
        protected String ratePerDay;

        abstract void isAvailable();
        abstract void startRental();
        abstract void endRental();

        public Vehicle(String model, String make, int millege, String ratePerDay){
            this.Model = model;
            this.Make = make;
            this.Millege = millege;
            this.ratePerDay = ratePerDay;
        }

        
        public String getModel() {
                return Model;
        }
        public void setModel(String model) {
                Model = model;
        }
        public String getMake() {
                return Make;
        }
        public void setMake(String make) {
                Make = make;
        }
        public int getMillege() {
                return Millege;
        }
        public void setMillege(int millege) {
                Millege = millege;
        }
        public String getRatePerDay() {
                return ratePerDay;
        }
        public void setRatePerDay(String ratePerDay) {
                this.ratePerDay = ratePerDay;
        }
        
        public void displayDetails(){
            System.out.println("Model: " + Model);
            System.out.println("Make: " + Make);
            System.out.println("Millege: " + Millege);
            System.out.println("Rate Per Day: " + ratePerDay);
        }


        
       

       
}