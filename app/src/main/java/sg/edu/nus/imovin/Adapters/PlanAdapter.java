package sg.edu.nus.imovin.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import sg.edu.nus.imovin.Event.PlanEvent;
import sg.edu.nus.imovin.Objects.PlanDataCategory;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.PlanData;
import sg.edu.nus.imovin.System.EventConstants;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.ValueConstants;

public class PlanAdapter extends ExpandableRecyclerAdapter<PlanDataCategory, PlanData, PlanAdapter.PlanDataCategoryViewHolder, PlanAdapter.PlanDataViewHolder> {
    private LayoutInflater mInflater;
    private int module;

    public PlanAdapter(Context context, @NonNull List<PlanDataCategory> parentList) {
        super(parentList);
        mInflater = LayoutInflater.from(context);
        this.module = PlanEvent.MODULE_GOAL;
    }

    public PlanAdapter(Context context, @NonNull List<PlanDataCategory> parentList, int module) {
        super(parentList);
        mInflater = LayoutInflater.from(context);
        this.module = module;
    }

    class PlanDataCategoryViewHolder extends ParentViewHolder {

        private TextView plan_category_title;

        public PlanDataCategoryViewHolder(View itemView) {
            super(itemView);
            plan_category_title = itemView.findViewById(R.id.plan_category_title);
        }

        public void bind(PlanDataCategory planDataCategory) {
            plan_category_title.setText(planDataCategory.getCategoryTitle());
        }
    }

    class PlanDataViewHolder extends ChildViewHolder {

        private TextView plan_title;
        private ImageView delete_plan_btn;

        public PlanDataViewHolder(View itemView) {
            super(itemView);
            plan_title = itemView.findViewById(R.id.plan_title);
            delete_plan_btn = itemView.findViewById(R.id.delete_plan_btn);
        }

        public void bind(PlanData planData) {
            plan_title.setText(planData.getName());
            if(planData.getPlanType().equals(ValueConstants.DefaultPlanType)){
                delete_plan_btn.setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    @Override
    public PlanDataCategoryViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View parentView = mInflater.inflate(R.layout.item_plan_category, parentViewGroup, false);

        return new PlanDataCategoryViewHolder(parentView);
    }

    @NonNull
    @Override
    public PlanDataViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View childView = mInflater.inflate(R.layout.item_plan, childViewGroup, false);

        return new PlanDataViewHolder(childView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull PlanDataCategoryViewHolder parentViewHolder, int parentPosition, @NonNull PlanDataCategory parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull PlanDataViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull final PlanData child) {
        childViewHolder.bind(child);

        if(child.getPlanType().equals(ValueConstants.CustomPlanType)) {
            childViewHolder.delete_plan_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new PlanEvent(EventConstants.DELETE, child.getId(), module));
                }
            });
        }

        if(child.getId().equals(ImovinApplication.getUserData().getSelectedPlan())){
            childViewHolder.plan_title.setTypeface(Typeface.DEFAULT_BOLD);
            childViewHolder.delete_plan_btn.setVisibility(View.GONE);
        }else{
            childViewHolder.plan_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new PlanEvent(EventConstants.SELECT, child.getId(), module));
                }
            });
        }
    }
}
