package sg.edu.nus.imovin.Retrofit.Response;

import java.io.Serializable;
import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.SectionData;

public class QuestionnaireResponse implements Serializable {
    private String _id;
    private String created_at;
    private String name;
    private List<SectionData> sections;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SectionData> getSections() {
        return sections;
    }

    public void setSections(List<SectionData> sections) {
        this.sections = sections;
    }
}
