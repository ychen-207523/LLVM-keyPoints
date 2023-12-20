package org.parking.menus;

import org.parking.model.Citation;
import org.parking.model.Permit;
import org.parking.model.Vehicle;
import org.parking.model.Zone;
import org.parking.service.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class CitationsMenuImpl implements CitationsMenu{
    private final CitationsService citationsService;
    private final VehicleService vehiclesService;

    private final PermitsService permitsService;
    private final ZoneService zonesService;


    public CitationsMenuImpl(CitationsService citationsService, VehicleService vehicleService, PermitsService permitsService, ZoneService zoneService) {
        this.citationsService = citationsService;
        this.vehiclesService = vehicleService;
        this.permitsService = permitsService;
        this.zonesService = zoneService;
    }

    /**
     * callInterface will present and control the flow of the citations menu
     */
    public void callInterface() {
        System.out.println("Generating and Maintaining Citation Menu\n");
        int option = 0;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.print("1. Generate Citation\n2. Maintain Citation\n3. Get all Citations\n4. Get Citation by Number\n5. Delete Citation by Number\n6. Pay Citation\n7. Appeal Citation\n8. Go back\n\nPlease enter your option:");
            try {
                option = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Your input was not an integer");
                option = 0;
                scan.nextLine();
            }
            switch (option) {
                case 1:
                    createCitationOption(scan);
                    break;
                case 2:
                    updateCitationOption(scan);
                    break;
                case 3:
                    getAllCitationsOption();
                    break;
                case 4:
                   getCitationByNumberOption(scan);
                    break;
                case 5:
                    deleteCitationByNumberOption(scan);
                    break;
                case 6:
                    payCitationOption(scan);
                    break;
                case 7:
                    appealCitationOption(scan);
                    break;
                default:
                    System.out.println(Constants.LogTryInputAgain);
            }

        } while (option != 8);
    }

    /** createCitationOption is an internal method used by the citations menu which builds and persists a citation based on user input.
     * It will check if a vehicle currently exists for the license in the citation and if it does it will verify that vehicles permit is valid.
     * If the permit is expired or if the citation is for a lot that does not belong to the permit then it will log a violation.
     */
    private void createCitationOption(Scanner scan) {
        Citation citationToCreate;
        citationToCreate = buildCitationFromInput(scan);
        try {
            // Check if vehicle exists by calling getByLicense
            boolean createVehicle = false;
            Vehicle vehicle = vehiclesService.getByLicense(citationToCreate.getVehicle().getLicense());
            if (vehicle == null) {
                // The vehicle doesn't exist in the db, so we need to create it
                createVehicle = true;
            } else {
                // We found a vehicle, so we need to check if it has a valid permit
               Collection<Permit> foundPermits = permitsService.getPermitPerCarLicense(vehicle.getLicense());
                for (Permit p : foundPermits) {
                    if ((citationToCreate.getCitationDate().compareTo(p.getExpirationDate()) == 0 && citationToCreate.getCitationTime().after(p.getExpirationTime())) || citationToCreate.getCitationDate().after(p.getExpirationDate()) ) {
                        // Citation date is same as expiration date but citation time is after permit expiration time or citation date is after permit expiration date.
                        System.out.println("VIOLATION DETECTED: This vehicle's permit " + p.getPermitID() + " is expired.");
                    }
                    // Get the zone from the permit
                    String permitZoneID = p.getZoneID();
                    String cLotName = citationToCreate.getLotName();
                    Collection<Zone> foundZones = zonesService.getZonesById(permitZoneID);
                    for (Zone z : foundZones) {
                        if (!cLotName.equals(z.getLotName())) {
                            // The vehicle's permit does not match the lot where the citation took place.
                            System.out.println("VIOLATION DETECTED: This vehicle's permit " + p.getPermitID() + " is for Lot Name "+ z.getLotName() + " but this vehicles citation is for Lot Name " + cLotName + ".");
                        }
                    }
                }
            }
            citationsService.createCitation(citationToCreate, createVehicle);
        } catch (SQLException e) {
            System.out.println(Constants.LogTryAgain);
        }
    }

    /**updateCitationOption is an internal method used by the citations menu which will prompt the user for a citation number and verify that citation exists.
     * If the citation does exist it will allow the user to edit its fields and persist the updated org.parking.model in the database.
     */
    private void updateCitationOption(Scanner scan) {
        System.out.print("Enter Citation number to update: ");
        Citation citationToUpdate;
        try {
            int citationNumberToUpdate = scan.nextInt();
            citationToUpdate = citationsService.getByNumber(citationNumberToUpdate);
            if (citationToUpdate == null) {
                System.out.println("No citation found with number: " + citationNumberToUpdate + "\n");
                return;
            } else {
                System.out.println(citationToUpdate);
            }
        } catch (InputMismatchException e) {
            System.out.println(Constants.LogTryInputAgain);
            scan.nextLine();
            return;
        } catch (SQLException e) {
            System.out.println(Constants.LogTryAgain);
            return;
        }
        scan.nextLine();
        System.out.println("Enter values for fields you would like to update");
        citationToUpdate = buildCitationToUpdate(scan, citationToUpdate);
        try {
            citationsService.updateCitation(citationToUpdate);
        } catch (SQLException e) {
            System.out.println(Constants.LogTryAgain);
        }
    }

    /** getAllCitationsOption is an internal method used by the citations menu which will fetch all citations from the database and
     * print their corresponding fields.
     */
    private void getAllCitationsOption() {
        try {
            Collection<Citation> citations = citationsService.getAll();
            if (citations == null) {
                System.out.println("No citations found\n");
            } else {
                for (Citation citation : citations) {
                    System.out.println(citation);
                }
            }

        } catch (SQLException e) {
            System.out.println(Constants.LogTryAgain);
        }
    }

    /** getCitationByNumberOption is an internal method used by the citations menu which will prompt the user for a citation number, fetch it from the
     * database, and print its fields.
    */
    private void getCitationByNumberOption(Scanner scan) {
        System.out.print("Enter Citation number to find: ");
        try {
            int citationNumberToFind = scan.nextInt();
            Citation citation = citationsService.getByNumber(citationNumberToFind);
            if (citation == null) {
                System.out.println("No citation found with number: " + citationNumberToFind + "\n");
            } else {
                System.out.println(citation);
            }
        } catch (InputMismatchException e) {
            System.out.println(Constants.LogTryInputAgain);
            scan.nextLine();
        } catch (SQLException e) {
            System.out.println(Constants.LogTryAgain);
        }
    }

    /** buildCitationFromInput is an internal method used by the createCitationOption which handles the input and verification
     * of a citation to create.
    */
    private static Citation buildCitationFromInput(Scanner scan) {
        String license = "";
        boolean validLicense = false;
        while (!validLicense) {
            System.out.print("Enter vehicle license: ");
            license = scan.nextLine();
            if (!license.trim().isEmpty()) {
               validLicense = true;
            }
        }
        System.out.print("Enter vehicle org.parking.model: ");
        String model = scan.nextLine();
        System.out.print("Enter vehicle color: ");
        String color = scan.nextLine();
        Vehicle vehicle = new Vehicle(license, model, color, "", 0);
        System.out.print("Enter parking lot name: ");
        String lotName = scan.nextLine();

        String category = "";
        boolean validCategory = false;
        while (!validCategory) {
            System.out.print("Enter category: ");
            category = scan.nextLine();
            if (!category.trim().isEmpty()) {
                validCategory = true;
            }
        }

        boolean validFee = false;
        double fee = 0;
        while (!validFee) {
            System.out.print("Enter fee: ");
            String feeStr = scan.nextLine();
            try {
                fee = Double.parseDouble(feeStr);
                validFee = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid fee format.");
            }
        }

        String status = "";
        boolean validStatus = false;
        while (!validStatus) {
            System.out.print("Enter status [DUE | PAID | APPEALED]: ");
            status = scan.nextLine();
            if (Objects.equals(status, "DUE") || Objects.equals(status, "PAID") || Objects.equals(status, "APPEALED")) {
                validStatus = true;
            } else {
                System.out.println("Invalid status. The provided status must be DUE, PAID, or APPEALED");
            }
        }

        Date citationDate = null;
        boolean validCitationDate = false;
        while (!validCitationDate) {
            System.out.print("Citation Date (yyyy-MM-dd): ");
            String startDateStr = scan.nextLine();
            try {
                citationDate = Date.valueOf(startDateStr);
                validCitationDate = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        Time citationTime = null;
        boolean validCitationTime = false;
        while (!validCitationTime) {
            System.out.print("Expiration Time (HH:mm:ss): ");
            String expirationTimeStr = scan.nextLine();
            try {
                citationTime = Time.valueOf(expirationTimeStr);
                validCitationTime = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid time format. Please use HH:mm:ss.");
            }
        }




        return new Citation(0, vehicle, lotName, category, fee, status, citationDate, citationTime);
    }

    /** buildCitationToUpdate is an internal method used by the updateCitationOption which handles the input and verification
     * of a citation to create.
     */
    private static Citation buildCitationToUpdate(Scanner scan, Citation citation) {
        System.out.print("Enter vehicle license[current: " + citation.getVehicle().getLicense() + "]: ");
        // We only need the vehicle license when doing an update, so we won't set other fields.
        String license = scan.nextLine();
        if (!license.trim().isEmpty()) {
            citation.getVehicle().setLicense(license);
        }

        System.out.print("Enter parking lot name[current: " + citation.getLotName() + "]: ");
        String lotName = scan.nextLine();
        if (!lotName.trim().isEmpty()) {
            citation.setLotName(lotName);
        }

        System.out.print("Enter category[current: " + citation.getCategory()+ "]: ");
        String category = scan.nextLine();
        if (!category.trim().isEmpty()) {
            citation.setCategory(category);
        }

        boolean validFee = false;
        while (!validFee) {
            System.out.print("Enter fee[current: " + citation.getFee()+ "]: ");
            String fee = scan.nextLine();
            if (!fee.trim().isEmpty()) {
                try {
                    Double dobFee = Double.valueOf(fee);
                    citation.setFee(dobFee);
                    validFee = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid fee format.");

                }
            } else {
                validFee = true;
            }

        }


        boolean validStatus = false;
        while (!validStatus) {
            System.out.print("Enter status[current: " + citation.getPaymentStatus()+ "]: ");
            String status = scan.nextLine();
            if (status.trim().isEmpty()) {
                // Skip updating the paymentStatus and use the existing value.
                validStatus = true;
            } else if (Objects.equals(status, "DUE") || Objects.equals(status, "PAID") || Objects.equals(status, "APPEALED")) {
                citation.setPaymentStatus(status);
                validStatus = true;
            } else {
                // They entered something that both is not an empty string and is not a valid payment status so ask again
                System.out.println("Invalid status. The provided status must be DUE, PAID, or APPEALED");
            }
        }






        Date citationDate = citation.getCitationDate();
        boolean validCitationDate = false;
        while (!validCitationDate) {
            System.out.print("Citation Date (yyyy-MM-dd) [current: " + citationDate + "]: ");
            String citationDateStr = scan.nextLine();
            if(citationDateStr.trim().isEmpty()) {
                break;
            }
            try {
                citationDate = Date.valueOf(citationDateStr);
                validCitationDate = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        Time citationTime = citation.getCitationTime();
        boolean validCitationTime = false;
        while (!validCitationTime) {
            System.out.print("Citation Time (HH:mm:ss) [current " + citationTime + " ]: ");
            String citationTimeStr = scan.nextLine();
            if(citationTimeStr.trim().isEmpty()) {
                break;
            }
            try {
                citationTime = Time.valueOf(citationTimeStr);
                validCitationTime = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid time format. Please use HH:mm:ss.");
            }
        }

        return citation;
    }

    /**deleteCitationByNumberOption prompts the user for input of a citation number and calls the citation org.parking.service in order to delete
     * that citation.
     */
    public void deleteCitationByNumberOption(Scanner scan) {
        System.out.print("Enter Citation number to delete: ");
        try {
            int citationNumberToDelete = scan.nextInt();
            citationsService.deleteCitationByNumber(citationNumberToDelete);
        } catch (InputMismatchException e) {
            System.out.println(Constants.LogTryInputAgain);
            scan.nextLine();
        } catch (SQLException e) {
            System.out.println(Constants.LogTryAgain);
        }
    }

    /**payCitationOption prompts the user for input of a citation number and calls the citation org.parking.service in order to update
     * that citation's paymentStatus to paid.
     */
    private void payCitationOption(Scanner scan) {
        System.out.print("Enter Citation number to pay: ");
        try {
            int citationNumberToPay = scan.nextInt();
            boolean citationPaid = citationsService.payCitation(citationNumberToPay);
            if (citationPaid) {
                System.out.println("Citation " + citationNumberToPay + " paid.");
            } else {
                System.out.println("Citation was not updated to PAID. Please verify that the citation number exists and ensure its current status is DUE");
            }
        } catch (InputMismatchException e) {
            System.out.println(Constants.LogTryInputAgain);
            scan.nextLine();
        }
    }

    /**appealCitationOption prompts the user for input of a citation number and calls the citation org.parking.service in order to update
     * that citation's paymentStatus to appealed.
     */
    private void appealCitationOption(Scanner scan) {
        System.out.print("Enter Citation number to appeal: ");
        try {
            int citationNumberToAppeal = scan.nextInt();
            boolean citationAppealed = citationsService.appealCitation(citationNumberToAppeal);
            if (citationAppealed) {
                System.out.println("Citation " + citationNumberToAppeal + " appealed.");
            } else {
                System.out.println("Citation was not updated to APPEALED. Please verify that the citation number exists and ensure its current status is DUE");
            }
        } catch (InputMismatchException e) {
            System.out.println(Constants.LogTryInputAgain);
            scan.nextLine();
        }
    }
}

