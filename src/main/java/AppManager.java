import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppManager {

    private final Connection connection;

    public AppManager(Connection connection) {
        this.connection = connection;
    }

    public void add(App app, String phoneSerialNo) throws SQLException {


        try {
            String queryToAdd = "insert into apps (name, version, size, phone_serial_no, capacity_type) " +
                    "values (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(queryToAdd);
            preparedStatement.setString(1, app.getName());
            preparedStatement.setString(2, app.getVersion());
            preparedStatement.setBigDecimal(3, app.getSize());
            preparedStatement.setString(4, phoneSerialNo);
            preparedStatement.setString(5, app.getCapacityType().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    public void update(App app, String phoneSerialNo) {

        try {
            String queryToUpdate = "update apps set name = ?, version = ?, size = ?, phone_serial_no = ?" +
                    "where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(queryToUpdate);
            preparedStatement.setString(1, app.getName());
            preparedStatement.setString(2, app.getVersion());
            preparedStatement.setBigDecimal(3, app.getSize());
            preparedStatement.setString(4, phoneSerialNo);
            preparedStatement.setLong(5, app.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }


    }

    public void remove(Long id) {
        try {
            String queryToDelete = "delete from apps where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(queryToDelete);
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    public Optional<App> get(Long id) throws SQLException {

        try {
            String queryToSelect = "select * from apps where id=" + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryToSelect);
            resultSet.next();
            App app = new App();
            app.setId(id);
            app.setName(resultSet.getString("name"));
            app.setVersion(resultSet.getString("version"));
            app.setSize(resultSet.getBigDecimal("size"));
            return Optional.of(app);
        } catch (SQLException e) {
            System.err.println(e);
        }

        return Optional.empty();
    }

    public List<App> list() throws SQLException {

        try {
            String queryToSelectAll = "select * from apps;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryToSelectAll);

            List<App> list = new ArrayList<>();
            while (resultSet.next()) {
                App app = new App();
                app.setId(resultSet.getLong("id"));
                app.setName(resultSet.getString("name"));
                app.setVersion(resultSet.getString("version"));
                app.setSize(resultSet.getBigDecimal("size"));
                list.add(app);
            }
            return list;
        } catch (SQLException e) {
            System.err.println(e);
        }

        return new ArrayList<>();
    }
}
