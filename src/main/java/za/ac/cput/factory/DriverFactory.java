
package za.ac.cput.factory;

import za.ac.cput.domain.Driver;
import za.ac.cput.util.Helper;

import java.util.UUID;

public class DriverFactory {
    public static Driver buildDriver(String firstName, String lastName,
                                     String licenseNumber, String phoneNumber,
                                     boolean availability) {

        if(Helper.isEmptyOrNull(firstName)|| Helper.isEmptyOrNull(lastName)
        || Helper.isEmptyOrNull(licenseNumber)|| Helper.isEmptyOrNull(phoneNumber)){
            return null;
        }

        return new Driver.Builder()
                .setId(UUID.randomUUID().toString())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setLicenseNumber(licenseNumber)
                .setPhoneNumber(phoneNumber)
                .setAvailability(availability)
                .build();
    }
}