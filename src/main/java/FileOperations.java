import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FileOperations {

    private final PhoneManager phoneManager;
    private final PersonManager personManager;

    public FileOperations(PhoneManager phoneManager, PersonManager personManager) {
        this.phoneManager = phoneManager;
        this.personManager = personManager;
    }

    public void backUpPhonesWithTheirApps() throws SQLException, IOException {
        List<Phone> list = phoneManager.list();

        var writer = new PrintWriter(new FileWriter("phones"));
        writer.println(list);
        writer.flush();

    }

    public void backUpPersons() throws SQLException, IOException {
        List<Person> list = personManager.list();

        var writer = new PrintWriter(new FileWriter("persons"));
        writer.println(list);
        writer.flush();

    }

    public String recover(String fileName) throws IOException {
        var reader = new BufferedReader(new FileReader(fileName));

        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null){
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

}
