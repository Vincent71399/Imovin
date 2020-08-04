package sg.edu.nus.imovin2.Objects;

public class SubLogData {
    private String name;
    private String value;

    public SubLogData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public SubLogData(String name, Integer value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public SubLogData(String name, Float value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
