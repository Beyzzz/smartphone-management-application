import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PhoneManager {

    private final Connection connection;

    public PhoneManager(Connection connection) {
        this.connection = connection;
    }

    public void add(Phone phone) throws SQLException {


        try {
            String queryToAdd = "insert into phones " +
                    "(serial_no, brand, model, storage_capacity, operating_system, capacity_type) " +
                    "values (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(queryToAdd);
            preparedStatement.setString(1, phone.getSerialNo());
            preparedStatement.setString(2, phone.getBrand());
            preparedStatement.setString(3, phone.getModel());
            preparedStatement.setBigDecimal(4, phone.getStorageCapacity());
            preparedStatement.setString(5, phone.getOperatingSystem());
            preparedStatement.setString(6, phone.getCapacityType().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    public void update(Phone phone) {


        try {
            String queryToUpdate = "update phones set brand = ?, model = ?, " +
                    "storage_capacity = ?, operating_system = ?" +
                    "where serial_no = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(queryToUpdate);
            preparedStatement.setString(1, phone.getBrand());
            preparedStatement.setString(2, phone.getModel());
            preparedStatement.setBigDecimal(3, phone.getStorageCapacity());
            preparedStatement.setString(4, phone.getOperatingSystem());
            preparedStatement.setString(5, phone.getSerialNo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }


    }

    public void remove(String serialNo) {
        try {
            String queryToDelete = "delete from phones where serial_no = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(queryToDelete);
            preparedStatement.setString(1, serialNo);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    public Optional<Phone> get(String serialNo) throws SQLException {

        try {
            String queryToSelect = "select * from phones where serial_no = '" + serialNo + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryToSelect);
            resultSet.next();
            Phone phone = new Phone();
            phone.setSerialNo(serialNo);
            phone.setBrand(resultSet.getString("brand"));
            phone.setModel(resultSet.getString("model"));
            phone.setStorageCapacity(resultSet.getBigDecimal("storage_capacity"));
            phone.setOperatingSystem(resultSet.getString("operating_system"));
            return Optional.of(phone);
        } catch (SQLException e) {
            System.err.println(e);
        }

        return Optional.empty();
    }

    public List<Phone> list() throws SQLException {

        try {
            String queryToSelectAll = "select * from phones;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryToSelectAll);

            List<Phone> list = new ArrayList<>();
            while (resultSet.next()) {
                Phone phone = new Phone();
                phone.setSerialNo(resultSet.getString("serial_no"));
                phone.setBrand(resultSet.getString("brand"));
                phone.setModel(resultSet.getString("model"));
                phone.setStorageCapacity(resultSet.getBigDecimal("storage_capacity"));
                phone.setOperatingSystem(resultSet.getString("operating_system"));

                String queryToSelectAllAppsOfThePhone = "select * from apps where phone_serial_no = '" + phone.getSerialNo() + "'";
                Statement statementToFetchApps = connection.createStatement();
                ResultSet resultSetForApps = statementToFetchApps.executeQuery(queryToSelectAllAppsOfThePhone);

                List<App> appListOfThePhone = new ArrayList<>();
                while (resultSetForApps.next()) {
                    App app = new App();
                    app.setId(resultSetForApps.getLong("id"));
                    app.setName(resultSetForApps.getString("name"));
                    app.setVersion(resultSetForApps.getString("version"));
                    app.setSize(resultSetForApps.getBigDecimal("size"));
                    app.setCapacityType(CapacityType.valueOf(resultSetForApps.getString("capacity_type")));
                    appListOfThePhone.add(app);
                }
                phone.setApps(appListOfThePhone);

                list.add(phone);
            }
            return list;
        } catch (SQLException e) {
            System.err.println(e);
        }

        return new ArrayList<>();
    }

    public BigDecimal checkAvailableStorage(String serialNo) throws SQLException {

        BigDecimal storageCapacity = null;
        BigDecimal usedStorageOfPhone = null;
        CapacityType capacityTypeOfPhone = null;

        try {
            String queryToSelectApps = "SELECT a.size, a.capacity_type FROM phones as p " +
                    "join apps as a on p.serial_no = a.phone_serial_no where p.serial_no = ?";
            String queryToSelectPhone = "select storage_capacity, capacity_type from phones " +
                    "where serial_no = ?";

            PreparedStatement preparedStatementForPhone = connection.prepareStatement(queryToSelectPhone);
            preparedStatementForPhone.setString(1, serialNo);
            ResultSet resultSetForPhone = preparedStatementForPhone.executeQuery();
            resultSetForPhone.next();

            storageCapacity = resultSetForPhone.getBigDecimal("storage_capacity");
            capacityTypeOfPhone = CapacityType.valueOf(resultSetForPhone.getString("capacity_type"));

            BigDecimal phoneCapacityInKilobytes = switch (capacityTypeOfPhone) {
                case GB -> storageCapacity.multiply(BigDecimal.valueOf(1048576L));
                case MB -> storageCapacity.multiply(BigDecimal.valueOf(1024L));
                case KB -> storageCapacity.multiply(BigDecimal.ONE);
            };

            PreparedStatement preparedStatementForApps = connection.prepareStatement(queryToSelectApps);
            preparedStatementForApps.setString(1, serialNo);
            ResultSet resultSetForApps = preparedStatementForApps.executeQuery();
            List<App> list = new ArrayList<>();

            usedStorageOfPhone = BigDecimal.ZERO;

            while (resultSetForApps.next()) {
                App app = new App();
                BigDecimal sizeOfApp = resultSetForApps.getBigDecimal("a.size");
                app.setSize(sizeOfApp);
                CapacityType capacityTypeOfApp = CapacityType.valueOf(resultSetForApps.getString("a.capacity_type"));
                app.setCapacityType(capacityTypeOfApp);

                BigDecimal appSizeInKilobytes = switch (capacityTypeOfApp) {
                    case GB -> sizeOfApp.multiply(BigDecimal.valueOf(1048576L));
                    case MB -> sizeOfApp.multiply(BigDecimal.valueOf(1024L));
                    case KB -> sizeOfApp.multiply(BigDecimal.ONE);
                };

                usedStorageOfPhone = usedStorageOfPhone.add(appSizeInKilobytes);

                list.add(app);
            }

            if (list.isEmpty()) {
                throw new NoSuchElementException("There is no application in this phone");
            }

            return phoneCapacityInKilobytes.subtract(usedStorageOfPhone);

        } catch (SQLException | NoSuchElementException e) {
            return switch (capacityTypeOfPhone) {
                case GB -> storageCapacity.multiply(BigDecimal.valueOf(1048576L));
                case MB -> storageCapacity.multiply(BigDecimal.valueOf(1024L));
                case KB -> storageCapacity.multiply(BigDecimal.ONE);
            };
        }

    }

    public List<Phone> listByBrand(String brand){
        try {
            String queryToSelectByBrand = "select * from phones where brand = '" + brand + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryToSelectByBrand);

            List<Phone> list = new ArrayList<>();
            while (resultSet.next()) {
                Phone phone = new Phone();
                phone.setSerialNo(resultSet.getString("serial_no"));
                phone.setBrand(resultSet.getString("brand"));
                phone.setModel(resultSet.getString("model"));
                phone.setStorageCapacity(resultSet.getBigDecimal("storage_capacity"));
                phone.setOperatingSystem(resultSet.getString("operating_system"));
                list.add(phone);
            }
            return list;
        } catch (SQLException e) {
            System.err.println(e);
        }

        return new ArrayList<>();
    }


}

