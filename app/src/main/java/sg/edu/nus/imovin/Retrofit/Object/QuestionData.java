package sg.edu.nus.imovin.Retrofit.Object;

import java.io.Serializable;
import java.util.List;

public class QuestionData implements Serializable {
    private String _id;
    private String error_text;
    private String regex;
    private List<String> choices;
    private String created_at;
    private String detail;
    private Boolean is_custom;
    private Boolean is_reverse;
    private Boolean is_skippable;
    private String name;
    private String question;
    private String question_type;
    private String unit;
    private String updated_at;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getError_text() {
        return error_text;
    }

    public void setError_text(String error_text) {
        this.error_text = error_text;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Boolean getIs_custom() {
        return is_custom;
    }

    public void setIs_custom(Boolean is_custom) {
        this.is_custom = is_custom;
    }

    public Boolean getIs_reverse() {
        return is_reverse;
    }

    public void setIs_reverse(Boolean is_reverse) {
        this.is_reverse = is_reverse;
    }

    public Boolean getIs_skippable() {
        return is_skippable;
    }

    public void setIs_skippable(Boolean is_skippable) {
        this.is_skippable = is_skippable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
