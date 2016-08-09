package com.kiba.bottomnavigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kiba.bottomnavigation.entity.BadgeView;
import com.kiba.bottomnavigation.entity.IconView;
import com.kiba.bottomnavigation.entity.LabelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KiBa on 2016/6/21.
 */
public class BottomNavigationView extends LinearLayout {

    private Context context;

    private List<BottomNavigationItem> items;
    private List<View> itemLayoutList;
    private int itemCount = 0;

    // By Default enable the icon and label
    private boolean isIconEnable = true;
    private boolean isLabelEnable = true;
    private boolean isBadgeEnable = true;

    // label color before selected default---YELLOW
    private int labelColorBefore = 0;
    // label color after selected default---GREEN
    private int labelColorAfter = 0;
    // label text size  default---12sp
    private float labelTextSize = 0f;
    private int currentPosition = 0;

    // click listener
    private OnNavigationItemSelectListener navigationItemSelectListener;

    // Constructors
    public BottomNavigationView(Context context) {
        this(context, null);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationView);
        this.isIconEnable = arr.getBoolean(R.styleable.BottomNavigationView_enable_icon, true);
        this.isLabelEnable = arr.getBoolean(R.styleable.BottomNavigationView_enable_label, true);
        this.isBadgeEnable = arr.getBoolean(R.styleable.BottomNavigationView_enable_badge, true);
        this.labelColorBefore = arr.getColor(R.styleable.BottomNavigationView_label_color_before, Color.YELLOW);
        this.labelColorAfter = arr.getColor(R.styleable.BottomNavigationView_label_color_after, Color.GREEN);
        this.labelTextSize = arr.getDimension(R.styleable.BottomNavigationView_label_textSize, 12);
        arr.recycle();
        itemLayoutList = new ArrayList<>();
    }

    public enum BadgeStatus{
        SHOW,
        DISMISS
    }

    public int getItemCount() {
        return itemCount;
    }

    /**
     * Add items from outside.
     * @param items a list of BottomNavigationItem
     */
    public void addItems(List<BottomNavigationItem> items){
        if(this.items != null){
            this.items.clear();
            this.items.addAll(items);
        }else{
            this.items = items;
        }
        // set item count
        this.itemCount = this.items.size();

        try {
            createItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateItems() throws Exception {
        if(this.items != null){
            try {
                createItems();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            throw new Exception("item list is null.");
        }
    }

    private void createItems() throws Exception {
        if(this.items == null || this.items.isEmpty()){
            throw new Exception("The item list should not be null or empty.");
        }else{
            // the item layouts should bu clear first
            itemLayoutList.clear();

            int length = this.items.size();
            for (int i = 0; i < length; i++) {

                View layout = LayoutInflater.from(this.context).inflate(R.layout.navigation_item_layout, this, false);
                LinearLayout.LayoutParams lp = (LayoutParams) layout.getLayoutParams();
                lp.weight = 1;

                LabelView labelTextView =  (LabelView) layout.findViewById(R.id.navigation_item_labelView);
                BadgeView badgeTextView =  (BadgeView) layout.findViewById(R.id.navigation_item_badgeView);
                IconView iconImageView = (IconView) layout.findViewById(R.id.navigation_item_iconView);

                BottomNavigationItem item = this.items.get(i);

                // ICON
                if(this.isIconEnable){
                    // set item icon
                    iconImageView.setImageResource(item.getDefaultIcon());
                    if(!this.isLabelEnable){
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iconImageView.getLayoutParams();
                        params.height = Utils.dip2px(this.context, 48);
                        params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    }
                }else{
                    iconImageView.setVisibility(GONE);
                }

                // LABEL
                if(this.isLabelEnable){
                    if(!this.isIconEnable){
                        // set layout params if the icon is disabled.
                        lp.gravity = Gravity.CENTER;
                    }
                    // set item label
                    labelTextView.setText(item.getLabel());
                    labelTextView.setTextColor(this.labelColorBefore);
                    float textSize = Utils.px2sp(this.context, this.labelTextSize);
                    labelTextView.setTextSize(textSize);
                }else{
                    labelTextView.setVisibility(GONE);
                }

                // BADGE
                if(this.isBadgeEnable){
                    if(!this.isIconEnable){
                        // set layout params if the icon is disabled.
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) badgeTextView.getLayoutParams();
                        params.addRule(RelativeLayout.RIGHT_OF, R.id.navigation_item_labelView);
                        params.leftMargin = Utils.dip2px(this.context, 0f);
                    }
                    // set item badge
                    if(this.items.get(i).getBadgeNumber()!= -1){
                        badgeTextView.setText(item.getBadgeNumber());
                        badgeTextView.setVisibility(VISIBLE);
                    }
                }else{
                    badgeTextView.setVisibility(GONE);
                }

                // add view to parent
                this.addView(layout);

                // add view to a list
                itemLayoutList.add(i, layout);
            }

            setNavigationItemSelectListener(this.navigationItemSelectListener);

        }
    }

    public LabelView getLabelViewByPosition(int position){
        if(isLabelEnable()){
            return (LabelView) getItemView(position).findViewById(R.id.navigation_item_labelView);
        }else{
            return null;
        }
    }

    public IconView getIconViewByPosition(int position){
        if(isIconEnable()){
            return (IconView) getItemView(position).findViewById(R.id.navigation_item_iconView);
        }else{
            return null;
        }
    }

    public BadgeView getBadgeViewByPosition(int position){
        if(isBadgeEnable()){
            return (BadgeView) getItemView(position).findViewById(R.id.navigation_item_badgeView);
        }else{
            return null;
        }
    }

    /**
     * set number to the badgeView by position
     * @param position current item position
     * @param number
     *      if number == 0, The view will disappear. if number &gt; 99, it will show '99+'.
     *      if number &lt; 0, it don't allow.
     */
    public void setBadgeViewNumberByPosition(int position, int number){
        BadgeView badgeView = getBadgeViewByPosition(position);
        if(badgeView == null){
            try {
                throw new Exception("Badge is not enabled.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            if(number == 0){
                if(isBadgeShowing(badgeView)){
                    playBadgeViewAnimation(badgeView, BadgeStatus.DISMISS);
                    badgeView.setVisibility(INVISIBLE);
                }
            }else if(number > 99){
                badgeView.setText("99+");
                playBadgeViewAnimation(badgeView, BadgeStatus.SHOW);
                badgeView.setVisibility(VISIBLE);
            }else if(number < 0){
                badgeView.setVisibility(INVISIBLE);
                try {
                    throw new Exception("The badge number should not be less than 0.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                badgeView.setText(String.valueOf(number));
                playBadgeViewAnimation(badgeView, BadgeStatus.SHOW);
                badgeView.setVisibility(VISIBLE);
            }
        }
    }

    public boolean isBadgeShowing(BadgeView badgeView){
        return badgeView.getVisibility() == VISIBLE;
    }

    /**
     * Set navigation item click listener
     * @param navigationItemSelectListener pass a new listener, not null.
     */
    public void setNavigationItemSelectListener(final OnNavigationItemSelectListener navigationItemSelectListener) {
        this.navigationItemSelectListener = navigationItemSelectListener;

        // set click listener
        if(navigationItemSelectListener != null){
            for (int i = 0; i < itemLayoutList.size(); i++) {
                final int position = i;
                final View itemLayout = itemLayoutList.get(i);
                itemLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // set item to default first
                        resetActiveItemToDefault(itemLayoutList, currentPosition);
                        // set current position
                        currentPosition = position;
                        // set item to active
                        setItemToActive(itemLayout, currentPosition);
                        navigationItemSelectListener.onSelected(getBottomNavigationView(), view, position);

                    }
                });
            }
        }
    }

    /**
     * reset item to default
     * @param layouts layout list
     * @param position the position to be reset
     */
    private void resetActiveItemToDefault(List<View> layouts, int position){
        if(isIconEnable()){
            resetIconToDefault(layouts, position);
        }
        if(isLabelEnable()){
            resetLabelToDefault(layouts, position);
        }
    }

    /**
     * reset item to active
     * @param layout item in which layout to be activated.
     * @param position the item's index
     */
    private void setItemToActive(View layout, int position){
        if(isIconEnable()){
            setIconToActive(layout, position);
        }
        if(isLabelEnable()){
            setLabelToActive(layout, position);
        }
    }

    /**
     * set icon to default icon after item clicked.
     * @param layouts layout list
     * @param position the position to be reset
     */
    private void resetIconToDefault(List<View> layouts, int position) {
        IconView icon = (IconView) layouts.get(position).findViewById(R.id.navigation_item_iconView);
        BottomNavigationItem item = this.items.get(position);
        icon.setImageResource(item.getDefaultIcon());
    }

    /**
     * set label color to default after item clicked.
     * @param layouts layout list
     * @param position the position to be reset
     */
    private void resetLabelToDefault(List<View> layouts, int position) {
        LabelView label = (LabelView) layouts.get(position).findViewById(R.id.navigation_item_labelView);
        label.setTextColor(this.labelColorBefore);
    }

    /**
     * set icon to default icon after item clicked.
     * @param layout icon in which layout to be activated
     * @param position the item's index
     */
    private void setIconToActive(View layout, int position){
        IconView icon = (IconView) layout.findViewById(R.id.navigation_item_iconView);
        BottomNavigationItem item = this.items.get(position);
        icon.setImageResource(item.getAfterSelectedIcon());
    }

    /**
     * set label to default icon after item clicked.
     * @param layout label in which layout to be active
     * @param position the item's index
     */
    private void setLabelToActive(View layout, int position){
        LabelView label = (LabelView) layout.findViewById(R.id.navigation_item_labelView);
        label.setTextColor(this.labelColorAfter);
    }

    /**
     * Set current tab according to the position.
     * If {@link OnNavigationItemSelectListener} has been set,
     * the {@code onSelected()} will be called.
     * @param position which item to switch to.
     */
    public void setTab(int position){
        this.itemLayoutList.get(position).performClick();
    }

    private void playBadgeViewAnimation(BadgeView badgeView, BadgeStatus status){
        if (BadgeStatus.SHOW == status){
            badgeView.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.scale_in));
        }else if(BadgeStatus.DISMISS == status){
            badgeView.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.scale_out));
        }
    }

    public View getItemView(int position){
        return this.itemLayoutList.get(position);
    }

    public boolean isIconEnable() {
        return isIconEnable;
    }

    public boolean isLabelEnable() {
        return isLabelEnable;
    }

    public boolean isBadgeEnable() {
        return isBadgeEnable;
    }

    /**
     * This operation will redraw the layout
     * @param iconEnable true to enable icon, false to disable icon
     */
    public void setIconEnable(boolean iconEnable) {
        if(this.isIconEnable != iconEnable){
            isIconEnable = iconEnable;
            updateNavigation();
        }
    }

    /**
     * This operation will redraw the layout
     * @param labelEnable true to enable label, false to disable label
     */
    public void setLabelEnable(boolean labelEnable) {
        if(this.isLabelEnable != labelEnable){
            isLabelEnable = labelEnable;
            updateNavigation();
        }
    }

    /**
     * This operation will redraw the layout
     * @param badgeEnable true to enable badge, false to disable badge
     */
    public void setBadgeEnable(boolean badgeEnable) {
        if(this.isBadgeEnable() != badgeEnable){
            isBadgeEnable = badgeEnable;
            updateNavigation();
        }
    }

    private BottomNavigationView getBottomNavigationView(){
        return this;
    }

    private void updateNavigation(){
        try {
            this.removeAllViews();
            updateItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Replace the bottom navigation item.
     * @param item a new item to replace
     * @param position the index of item to be replaced
     */
    public void replaceNavigationItems(BottomNavigationItem item, int position){
        if(isIconEnable){
            IconView icon = (IconView) itemLayoutList.get(position).findViewById(R.id.navigation_item_iconView);
            this.items.remove(position);
            this.items.add(position, item);
            if(currentPosition == position){
                icon.setImageResource(item.getAfterSelectedIcon());
            }else{
                icon.setImageResource(item.getDefaultIcon());
            }
        }else{
            try {
                throw new Exception("The icon is disabled.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
