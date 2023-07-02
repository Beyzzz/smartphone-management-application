import java.math.BigDecimal;
import java.util.Objects;

public class App {
    private Long id;
    private String name;
    private String version;
    private BigDecimal size;
    private CapacityType capacityType;


    public App() {
    }

    public App(String name, String version, BigDecimal size, CapacityType capacityType) {
        this.name = name;
        this.version = version;
        this.size = size;
        this.capacityType = capacityType;
    }

    public App(Long id, String name, String version, BigDecimal size, CapacityType capacityType) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.size = size;
        this.capacityType = capacityType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public CapacityType getCapacityType() {
        return capacityType;
    }

    public void setCapacityType(CapacityType capacityType) {
        this.capacityType = capacityType;
    }

    @Override
    public String toString() {
        return "App{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", size=" + size +
                ", capacityType=" + capacityType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        App app = (App) o;
        return id.equals(app.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
