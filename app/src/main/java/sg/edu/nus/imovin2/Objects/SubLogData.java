package sg.edu.nus.imovin2.Objects;

import android.annotation.SuppressLint;

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

    @SuppressLint("DefaultLocale")
    public SubLogData(String name, Float value) {
        this.name = name;
        this.value = String.format("%.2f", value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
