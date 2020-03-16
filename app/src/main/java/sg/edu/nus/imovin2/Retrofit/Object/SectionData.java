package sg.edu.nus.imovin2.Retrofit.Object;

import java.io.Serializable;
import java.util.List;

public class SectionData implements Serializable {
    private String _id;
    private String created_at;
    private String display_name;
    private String instruction;
    private Boolean is_demographic;
    private String name;
    private List<QuestionData> questions;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Boolean getIs_demographic() {
        return is_demographic;
    }

    public void setIs_demographic(Boolean is_demographic) {
        this.is_demographic = is_demographic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuestionData> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionData> questions) {
        this.questions = questions;
    }
}
