package sg.edu.nus.imovin2.Objects;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

import sg.edu.nus.imovin2.Retrofit.Object.PlanData;

public class PlanDataCategory implements Parent<PlanData> {

    private String categoryTitle;
    private List<PlanData> mPlanDatas;

    public PlanDataCategory(String categoryTitle, List<PlanData> mPlanDatas) {
        this.categoryTitle = categoryTitle;
        this.mPlanDatas = mPlanDatas;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    @Override
    public List<PlanData> getChildList() {
        return mPlanDatas;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}
