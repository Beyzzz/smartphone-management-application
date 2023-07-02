import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonManager {

    private final Connection connection;

    public PersonManager(Connection connection) {
        this.connection = connection;
    }

    public void add(Person person) throws SQLException {

        try {
            String queryToAdd = "insert into persons (name, surname, phone_number, email) values (?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(queryToAdd);
            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getSurname());
            preparedStatement.setString(3, person.getPhoneNumber());
            preparedStatement.setString(4, person.getEMail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    public void update(Person person) {

        try {
            String queryToUpdate = "update persons set name = ?, surname = ?, phone_number = ?, email = ?" +
                    "where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(queryToUpdate);
            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getSurname());
            preparedStatement.setString(3, person.getPhoneNumber());
            preparedStatement.setString(4, person.getEMail());
            preparedStatement.setLong(5, person.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }


    }

    public void remove(Long id) {
        try {
            String queryToDelete = "delete from persons where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(queryToDelete);
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    public Optional<Person> get(Long id) throws SQLException {

        try {
            String queryToSelect = "select * from persons where id=" + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryToSelect);
            resultSet.next();
            Person person = new Person();
            person.setId(id);
            person.setName(resultSet.getString("name"));
            person.setSurname(resultSet.getString("surname"));
            person.setPhoneNumber(resultSet.getString("phone_number"));
            person.setEMail(resultSet.getString("email"));
            return Optional.of(person);
        } catch (SQLException e) {
            System.err.println(e);
        }

        return Optional.empty();
    }

    public List<Person> list() throws SQLException {

        try {
            String queryToSelectAll = "select * from persons;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryToSelectAll);

            List<Person> list = new ArrayList<>();
            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getLong("id"));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setPhoneNumber(resultSet.getString("phone_number"));
                person.setEMail(resultSet.getString("email"));
                list.add(person);
            }
            return list;
        } catch (SQLException e) {
            System.err.println(e);
        }

        return new ArrayList<>();
    }

}
