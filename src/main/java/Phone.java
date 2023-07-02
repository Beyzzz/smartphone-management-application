import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Phone {
    private String serialNo;
    private String brand;
    private String model;
    private BigDecimal storageCapacity;
    private String operatingSystem;
    private CapacityType capacityType;

    private List<App> apps;

    public Phone() {
    }

    public Phone(String serialNo, String brand, String model, BigDecimal storageCapacity, String operatingSystem, CapacityType capacityType) {
        this.serialNo = serialNo;
        this.brand = brand;
        this.model = model;
        this.storageCapacity = storageCapacity;
        this.operatingSystem = operatingSystem;
        this.capacityType = capacityType;
    }

    public Phone(String serialNo, String brand, String model, BigDecimal storageCapacity, String operatingSystem, CapacityType capacityType, List<App> apps) {
        this.serialNo = serialNo;
        this.brand = brand;
        this.model = model;
        this.storageCapacity = storageCapacity;
        this.operatingSystem = operatingSystem;
        this.capacityType = capacityType;
        this.apps = apps;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(BigDecimal storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public CapacityType getCapacityType() {
        return capacityType;
    }

    public void setCapacityType(CapacityType capacityType) {
        this.capacityType = capacityType;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "serialNo='" + serialNo + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", storageCapacity=" + storageCapacity +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", capacityType=" + capacityType +
                ", apps=" + apps +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return serialNo.equals(phone.serialNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNo);
    }
}
