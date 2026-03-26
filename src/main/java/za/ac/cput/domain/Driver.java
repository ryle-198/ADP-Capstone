package za.ac.cput.domain;

public class Driver {
    private String id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String phoneNumber;
    private boolean availability;

    private Driver() {}

    private Driver(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.licenseNumber = builder.licenseNumber;
        this.phoneNumber = builder.phoneNumber;
        this.availability = builder.availability;
    }

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getLicenseNumber() { return licenseNumber; }
    public String getPhoneNumber() { return phoneNumber; }
    public boolean isAvailability() { return availability; }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", availability=" + availability +
                '}';
    }

    public static class Builder {
        private String id;
        private String firstName;
        private String lastName;
        private String licenseNumber;
        private String phoneNumber;
        private boolean availability;

        public Builder setId(String id) { this.id = id; return this; }
        public Builder setFirstName(String firstName) { this.firstName = firstName; return this; }
        public Builder setLastName(String lastName) { this.lastName = lastName; return this; }
        public Builder setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; return this; }
        public Builder setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public Builder setAvailability(boolean availability) { this.availability = availability; return this; }

        public Builder copy(Driver driver) {
            this.id=driver.id;
            this.firstName=driver.firstName;
            this.lastName=driver.lastName;
            this.licenseNumber=driver.licenseNumber;
            this.phoneNumber=driver.phoneNumber;
            this.availability= driver.availability;
            return this;
        }

        public Driver build() { return new Driver(this); }
    }
}