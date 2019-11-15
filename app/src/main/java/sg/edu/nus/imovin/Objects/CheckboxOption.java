package sg.edu.nus.imovin.Objects;

public class CheckboxOption {
    private Boolean is_check;
    private String option_message;

    public CheckboxOption(String option_message) {
        this.is_check = false;
        this.option_message = option_message;
    }

    public Boolean getIs_check() {
        return is_check;
    }

    public void setIs_check(Boolean is_check) {
        this.is_check = is_check;
    }

    public String getOption_message() {
        return option_message;
    }

    public void setOption_message(String option_message) {
        this.option_message = option_message;
    }
}
