package com.kiba.bottomnavigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KiBa on 2016/6/21.
 */
public class BottomNavigationView extends LinearLayout {

    // TODO: 2016/6/22 create BadgeView or LabelView or IconView?  &&  show and hide navigation?
    private Context context;

    private List<BottomNavigationItem> items;
    private List<View> itemLayoutList;
    private int itemCount = 0;

    // By Default enable the icon and label
    private boolean isIconEnable = true;
    private boolean isLabelEnable = true;
    private boolean isBadgeEnable = true;

    private int currentPosition = -1;

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
                LayoutParams lp = (LayoutParams) layout.getLayoutParams();
                lp.weight = 1;

                TextView labelTextView =  (TextView)  layout.findViewById(R.id.navigation_item_textView_label);
                TextView badgeTextView =  (TextView)  layout.findViewById(R.id.navigation_item_textView_badge);
                ImageView iconImageView = (ImageView) layout.findViewById(R.id.navigation_item_imageView_icon);

                BottomNavigationItem item = this.items.get(i);

                // ICON
                if(this.isIconEnable){
                    // set item icon
                    iconImageView.setImageResource(item.getDefaultIcon());
                    if(!this.isLabelEnable){
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iconImageView.getLayoutParams();
                        params.height = Utils.dip2px(this.context, 50);
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
                }else{
                    labelTextView.setVisibility(GONE);
                }

                // BADGE
                if(this.isBadgeEnable){
                    if(!this.isIconEnable){
                        // set layout params if the icon is disabled.
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) badgeTextView.getLayoutParams();
                        params.addRule(RelativeLayout.RIGHT_OF, R.id.navigation_item_textView_label);
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

    public TextView getLabelViewByPosition(int position){
        if(isLabelEnable()){
            return (TextView) getItemView(position).findViewById(R.id.navigation_item_textView_label);
        }else{
            return null;
        }
    }

    public ImageView getIconViewByPosition(int position){
        if(isIconEnable()){
            return (ImageView) getItemView(position).findViewById(R.id.navigation_item_imageView_icon);
        }else{
            return null;
        }
    }

    public TextView getBadgeViewByPosition(int position){
        if(isBadgeEnable()){
            return (TextView) getItemView(position).findViewById(R.id.navigation_item_textView_badge);
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
        TextView badgeView = getBadgeViewByPosition(position);
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

    public boolean isBadgeShowing(TextView badgeView){
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
                        // set current position
                        currentPosition = position;
                        // set icon to default first
                        resetIconToDefault(itemLayoutList);
                        // set icon to active
                        setIconToActive(itemLayout, position);
                        navigationItemSelectListener.onSelected(getBottomNavigationView(), view, position);
                    }
                });
            }
        }
    }

    /**
     * set icon to default icon after item clicked.
     * @param layouts icon list
     */
    private void resetIconToDefault(List<View> layouts){
        for (int i = 0; i < layouts.size(); i++) {
            ImageView icon = (ImageView) layouts.get(i).findViewById(R.id.navigation_item_imageView_icon);
            BottomNavigationItem item = this.items.get(i);
            icon.setImageResource(item.getDefaultIcon());
        }
    }

    /**
     * set icon to default icon after item clicked.
     * @param layout icon in which layout to be active
     * @param position the item's index
     */
    private void setIconToActive(View layout, int position){
        ImageView icon = (ImageView) layout.findViewById(R.id.navigation_item_imageView_icon);
        BottomNavigationItem item = this.items.get(position);
        icon.setImageResource(item.getAfterSelectedIcon());
    }

    /**
     * Set current tab according to the position. <br/>
     * If {@link OnNavigationItemSelectListener} has been set,
     * the {@code onSelected()} will be called.
     * @param position which item to switch to.
     */
    public void setTab(int position){
        this.itemLayoutList.get(position).performClick();
    }

    private void playBadgeViewAnimation(TextView badgeView, BadgeStatus status){
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
            ImageView icon = (ImageView) itemLayoutList.get(position).findViewById(R.id.navigation_item_imageView_icon);
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
