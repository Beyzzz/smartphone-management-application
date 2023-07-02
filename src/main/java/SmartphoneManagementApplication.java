import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class SmartphoneManagementApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        Connection connection = connectToDatabase();
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the smartphone management application...");
        PersonManager personManager = new PersonManager(connection);
        PhoneManager phoneManager = new PhoneManager(connection);
        AppManager appManager = new AppManager(connection);

        FileOperations fileOperations = new FileOperations(phoneManager, personManager);

        /////////////////////////////////////////////////////////////////////////////
        //Person Management:

        outer:
        while (true) {
            System.out.println("Enter a person...");

            System.out.println("name: ");
            String name = sc.nextLine();

            System.out.println("surname: ");
            String surname = sc.nextLine();

            System.out.println("phone number: ");
            String phoneNumber = sc.nextLine();

            System.out.println("email: ");
            String email = sc.nextLine();

            Person person = new Person(name, surname, phoneNumber, email);
            personManager.add(person);
            System.out.println("Person is added successfully!");

            System.out.println("Would you like to add another person?(Y/N)");
            String replyForPerson;
            inner:
            while (true) {

                replyForPerson = sc.nextLine();
                if (replyForPerson.equalsIgnoreCase("Y")) {
                    continue outer;
                } else if (replyForPerson.equalsIgnoreCase("N")) {
                    break outer;
                } else {
                    System.out.println("You've typed a wrong letter, please re-enter: ");
                }
            }
        }

        System.out.println("Here is the person list: ");
        System.out.println(personManager.list());


        System.out.println("Would you like to update a person?(Y/N)");

        String replyForPerson = getReply(sc);

        if (replyForPerson.equalsIgnoreCase("Y")) {
            System.out.println("Enter the person's id to update: ");
            Long id = sc.nextLong();
            sc.nextLine();

            Person person = personManager.get(id).orElseThrow();

            System.out.println("Enter a new phone number: ");
            String phoneNumber = sc.nextLine();
            System.out.println("Enter a new email: ");
            String email = sc.nextLine();

            person.setPhoneNumber(phoneNumber);
            person.setEMail(email);

            personManager.update(person);
            System.out.println("Person is updated successfully!");
        } else {
            System.out.println("You didn't enter Y, so the person is not updated!");
        }

        System.out.println("Here is the updated person list: ");
        System.out.println(personManager.list());

        System.out.println("Would you like to remove a person?(Y/N)");

        replyForPerson = getReply(sc);

        if (replyForPerson.equalsIgnoreCase("Y")) {
            System.out.println("Enter the person's id to remove: ");
            Long id = sc.nextLong();
            sc.nextLine();

            personManager.remove(id);
            System.out.println("Person is removed successfully!");
        } else {
            System.out.println("You didn't enter Y, so the person is not removed!");
        }

        System.out.println("Here is the updated person list: ");
        System.out.println(personManager.list());

        System.out.println("To back up persons to the file, please enter the word 'backup': ");
        String backUp = sc.nextLine();
        if (backUp.equalsIgnoreCase("backUp")) {
            System.out.println("Backing up the persons...");
            fileOperations.backUpPersons();
            System.out.println("Recovering the persons...");
            System.out.println(fileOperations.recover("persons"));
        } else {
            System.out.println("You've entered the wrong word, " +
                    "persons are not backed up, so that they cannot be recovered!");
        }

        /////////////////////////////////////////////////////////////////////////////////////
        //Phone Management:

        outer:
        while (true) {
            System.out.println("Enter a phone's informations...");

            System.out.println("serial number: ");
            String serialNumber = sc.nextLine();

            System.out.println("brand: ");
            String brand = sc.nextLine();

            System.out.println("model: ");
            String model = sc.nextLine();

            System.out.println("storage capacity: ");
            BigDecimal storageCapacity = sc.nextBigDecimal();
            sc.nextLine();

            System.out.println("operating system: ");
            String os = sc.nextLine();

            System.out.println("capacity type: ");
            CapacityType capacityType = CapacityType.valueOf(sc.nextLine());

            Phone phone = new Phone(serialNumber, brand, model, storageCapacity, os, capacityType);
            phoneManager.add(phone);
            System.out.println("Phone is added successfully!");

            System.out.println("Would you like to add another phone?(Y/N)");
            String replyForPhone;
            inner:
            while (true) {

                replyForPhone = sc.nextLine();
                if (replyForPhone.equalsIgnoreCase("Y")) {
                    continue outer;
                } else if (replyForPhone.equalsIgnoreCase("N")) {
                    break outer;
                } else {
                    System.out.println("You've typed a wrong letter, please re-enter: ");
                }
            }
        }

        System.out.println("Here is the phone list: ");
        System.out.println(phoneManager.list());

        System.out.println("Would you like to update a phone?(Y/N)");

        String replyForPhone = getReply(sc);

        if (replyForPhone.equalsIgnoreCase("Y")) {
            System.out.println("Enter the phone's serial number to update: ");
            String serialNo = sc.nextLine();

            Phone phone = phoneManager.get(serialNo).orElseThrow();

            System.out.println("Enter a new capacity: ");
            BigDecimal storageCapacity = sc.nextBigDecimal();
            sc.nextLine();

            System.out.println("capacity type: ");
            CapacityType capacityType = CapacityType.valueOf(sc.nextLine());

            phone.setStorageCapacity(storageCapacity);
            phone.setCapacityType(capacityType);

            phoneManager.update(phone);
            System.out.println("Phone is updated successfully!");
        } else {
            System.out.println("You didn't enter Y, so the phone is not updated!");
        }

        System.out.println("Here is the updated phone list: ");
        System.out.println(phoneManager.list());

        System.out.println("Would you like to remove a phone?(Y/N)");

        replyForPhone = getReply(sc);

        if (replyForPhone.equalsIgnoreCase("Y")) {
            System.out.println("Enter the phone's serial number to remove: ");
            String serialNo = sc.nextLine();
            phoneManager.remove(serialNo);
            System.out.println("Phone is removed successfully!");
        } else {
            System.out.println("You didn't enter Y, so the phone is not removed!");
        }

        System.out.println("Here is the updated phone list: ");
        System.out.println(phoneManager.list());

        System.out.println("To check the available storage of a phone, enter its serial number: ");
        String serialNo = sc.nextLine();

        System.out.println("In Kilobytes: " + phoneManager.checkAvailableStorage(serialNo) + " KB");
        System.out.println("In Megabytes: " + phoneManager.checkAvailableStorage(serialNo)
                .divide(BigDecimal.valueOf(1024L), RoundingMode.HALF_UP) + " MB");
        System.out.println("In Gigabytes: " + phoneManager.checkAvailableStorage(serialNo)
                .divide(BigDecimal.valueOf(1048576L), RoundingMode.HALF_UP) + " GB");

        System.out.println("To list the phones of a brand, please enter the brand name: ");
        String brand = sc.nextLine();
        System.out.println(phoneManager.listByBrand(brand));

        //////////////////////////////////////////////////////////////////////////////////////
        //App Management

        outer:
        while (true) {
            System.out.println("Enter an app...");

            System.out.println("name: ");
            String name = sc.nextLine();

            System.out.println("version: ");
            String version = sc.nextLine();

            System.out.println("size: ");
            BigDecimal size = sc.nextBigDecimal();
            sc.nextLine();

            System.out.println("capacity type: ");
            CapacityType capacityType = CapacityType.valueOf(sc.nextLine());

            System.out.println("Enter a phone serial number to add this app to: ");
            String serialNoForAppsPhone = sc.nextLine();

            App app = new App(name, version, size, capacityType);
            appManager.add(app, serialNoForAppsPhone);
            System.out.println("App is added successfully!");

            System.out.println("Would you like to add another app?(Y/N)");
            String replyForApp;
            inner:
            while (true) {

                replyForApp = sc.nextLine();
                if (replyForApp.equalsIgnoreCase("Y")) {
                    continue outer;
                } else if (replyForApp.equalsIgnoreCase("N")) {
                    break outer;
                } else {
                    System.out.println("You've typed a wrong letter, please re-enter: ");
                }
            }
        }

        System.out.println("Here is the app list: ");
        System.out.println(appManager.list());

        System.out.println("Would you like to update a app?(Y/N)");

        String replyForApp = getReply(sc);

        if (replyForApp.equalsIgnoreCase("Y")) {
            System.out.println("Enter the app's id to update: ");
            Long id = sc.nextLong();
            sc.nextLine();

            App app = appManager.get(id).orElseThrow();

            System.out.println("Enter a new version: ");
            String version = sc.nextLine();
            System.out.println("Enter a new size: ");
            BigDecimal size = sc.nextBigDecimal();
            sc.nextLine();

            System.out.println("Enter a new capacity type: ");
            CapacityType capacityType = CapacityType.valueOf(sc.nextLine());
            System.out.println("Enter a new phone serial no: ");
            String serialNoOfAppsPhone = sc.nextLine();

            app.setVersion(version);
            app.setSize(size);
            app.setCapacityType(capacityType);

            appManager.update(app, serialNoOfAppsPhone);
            System.out.println("App is updated successfully!");
        } else {
            System.out.println("You didn't enter Y, so the app is not updated!");
        }

        System.out.println("Here is the updated app list: ");
        System.out.println(appManager.list());

        System.out.println("Would you like to remove a app?(Y/N)");

        replyForApp = getReply(sc);

        if (replyForApp.equalsIgnoreCase("Y")) {
            System.out.println("Enter the app's id to remove: ");
            Long id = sc.nextLong();
            sc.nextLine();

            appManager.remove(id);
            System.out.println("App is removed successfully!");
        } else {
            System.out.println("You didn't enter Y, so the app is not removed!");
        }

        System.out.println("Here is the updated app list: ");
        System.out.println(appManager.list());

        System.out.println("To back up phones to the file, please enter the word 'backup': ");
        String backUpForPhones = sc.nextLine();
        if (backUpForPhones.equalsIgnoreCase("backUp")) {
            System.out.println("Backing up the phones with their apps...");
            fileOperations.backUpPhonesWithTheirApps();
            System.out.println("Recovering the phones with their apps...");
            System.out.println(fileOperations.recover("phones"));
        } else {
            System.out.println("You've entered the wrong word, " +
                    "phones are not backed up, so that they cannot be recovered!");
        }


    }

    private static String getReply(Scanner sc) {
        String reply;
        while (true) {
            reply = sc.nextLine();
            if (reply.equalsIgnoreCase("Y")) {
                break;
            } else if (reply.equalsIgnoreCase("N")) {
                break;
            } else {
                System.out.println("You've typed a wrong letter, please re-enter: ");
            }
        }
        return reply;
    }

    static Connection connectToDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager
                .getConnection("jdbc:mysql://localhost:3306/smartphone_manager",
                        "root", "Beyza12345.");
    }
}
