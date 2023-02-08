package com.tencent.tmf.module.keyboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIViewPager;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.keyboard.common.KeyboardInputType;
import com.tencent.tmf.keyboard.common.SubKeyboard;
import com.tencent.tmf.keyboard.component.keyboard.CustomKeyboard;
import com.tencent.tmf.keyboard.component.keyboard.CustomKeyboardView;
import com.tencent.tmf.keyboard.component.keyboard.CustomKeyboardWrapper;
import com.tencent.tmf.keyboard.component.layout.IBorderLayout;
import com.tencent.tmf.keyboard.component.model.MagicText;
import com.tencent.tmf.keyboard.component.preview.DefaultKeyItemPreview;
import com.tencent.tmf.keyboard.config.IRelayoutKeyboardHelper;
import com.tencent.tmf.keyboard.utils.Tools;
import java.util.HashMap;
import java.util.Map;

/**
 * XML集成演示
 */
public class CustomKeyboardActivity extends TopBarActivity {

    private CustomKeyboard mSafeKeyboard = null;
    QMUITabSegment mTabSegment;
    QMUIViewPager mContentViewPager;
    private Map<ContentPage, View> mPageMap = new HashMap<>();
    private ContentPage mDestPage = ContentPage.BORDER;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String text = getResources().getString(R.string.custom_keyboard_activity_title);
        mTopBar.setTitle(text);

        mSafeKeyboard = findViewById(R.id.keyboard);
        mTabSegment = findViewById(R.id.tabs);
        mContentViewPager = findViewById(R.id.pager);

        CustomKeyboardWrapper wrapper = new CustomKeyboardWrapper(this, mSafeKeyboard);
        // 属性设置
        setProperty(wrapper);
        /*
         * 注意:
         * 此方法只需调用一次 每次调用都会向tmf 安全键盘服务请求 键盘映射数据 与 解密上下文
         */
        wrapper.wrap(new IRelayoutKeyboardHelper() {
            @Override
            public void doRelayout() {
                // 键盘重新布局了，输入框内的旧数据需要清除掉，让用户重新完整输入一遍
            }
        });
        /*
         * 注意:
         * 解密上下文的获取需要在安全键盘绘制完成之后
         * 如果在 wrapper.wrap 方法之后立即获取则有可能为null
         */
        byte[] context = wrapper.getContext();

        initTabAndPager();
    }

    private void setProperty(CustomKeyboardWrapper wrapper) {
        // 设置会话ID 为当前页面
        wrapper.setSessionId(this.hashCode());
        // 隐藏键盘收起图标
        wrapper.hideCollapseIcon();
        //设置键盘品牌名称
        String text = getResources().getString(R.string.custom_keyboard_activity_brand_name);
        wrapper.setBrandName(text);
        //设置键盘商标
        wrapper.setBrandIcon(getResources().getDrawable(R.drawable.brand_icon));
        //设置键盘预览
        wrapper.setKeyboardPreview(new DefaultKeyItemPreview(this, mSafeKeyboard));
        /*
         * 键盘的输入类型
         * 默认类型 : 字母 数字 字符 可以切换
         * 数字键盘 : 仅包含数字 不可切换
         * 身份证键盘: 仅包含数字 + X   不可切换
         */
        wrapper.setInputType(KeyboardInputType.NONE);

        //设置字母键盘 功能键背景色
        wrapper.setFnKeyFillStyle(SubKeyboard.SUB_KEYBOARD_LETTER,
                getResources().getColor(R.color.default_fn_key_normal_color),
                getResources().getColor(R.color.default_fn_key_press_color));
        //设置字母键盘 回车键背景色
        wrapper.setEnterKeyFillStyle(SubKeyboard.SUB_KEYBOARD_LETTER,
                getResources().getColor(R.color.default_enter_key_normal_color),
                getResources().getColor(R.color.default_enter_key_press_color));
        //设置字母键盘 内容键背景色
        wrapper.setContentKeyFillStyle(SubKeyboard.SUB_KEYBOARD_LETTER,
                getResources().getColor(R.color.default_content_key_normal_color),
                getResources().getColor(R.color.default_content_key_press_color));

        //设置数字键盘 功能键背景色
        wrapper.setFnKeyFillStyle(SubKeyboard.SUB_KEYBOARD_NUMBER,
                getResources().getColor(R.color.default_fn_key_normal_color),
                getResources().getColor(R.color.default_fn_key_press_color));
        //设置数字键盘 回车键背景色
        wrapper.setEnterKeyFillStyle(SubKeyboard.SUB_KEYBOARD_NUMBER,
                getResources().getColor(R.color.default_enter_key_normal_color),
                getResources().getColor(R.color.default_enter_key_press_color));
        //设置数字键盘 内容键背景色
        wrapper.setContentKeyFillStyle(SubKeyboard.SUB_KEYBOARD_NUMBER,
                getResources().getColor(R.color.default_content_key_normal_color),
                getResources().getColor(R.color.default_content_key_press_color));

        //设置字符键盘 功能键背景色
        wrapper.setFnKeyFillStyle(SubKeyboard.SUB_KEYBOARD_SYMBOL,
                getResources().getColor(R.color.default_fn_key_normal_color),
                getResources().getColor(R.color.default_fn_key_press_color));
        //设置字符键盘 回车键背景色
        wrapper.setEnterKeyFillStyle(SubKeyboard.SUB_KEYBOARD_SYMBOL,
                getResources().getColor(R.color.default_enter_key_normal_color),
                getResources().getColor(R.color.default_enter_key_press_color));
        //设置字符键盘 内容键背景色
        wrapper.setContentKeyFillStyle(SubKeyboard.SUB_KEYBOARD_SYMBOL,
                getResources().getColor(R.color.default_content_key_normal_color),
                getResources().getColor(R.color.default_content_key_press_color));

        // 按键监听
        wrapper.setKeyboardActionListener(new CustomKeyboardView.SafeKeyboardActionListener() {
            @Override
            public void onKey(int primaryKeyCode, MagicText magicText) {
                ToastUtil.showToast(magicText.getText().toString());
            }
        });
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.custom_keyboard_view, null);
    }

    private void initTabAndPager() {
        mContentViewPager.setAdapter(mPagerAdapter);
        mContentViewPager.setCurrentItem(mDestPage.getPosition(), false);
        QMUITabBuilder builder = mTabSegment.tabBuilder();
        String text = getResources().getString(R.string.custom_keyboard_activity_setting_border);
        String text0 = getResources().getString(R.string.custom_keyboard_activity_setting_padding);
        String text1 = getResources().getString(R.string.custom_keyboard_activity_setting_fill);
        String text2 = getResources().getString(R.string.custom_keyboard_activity_setting_text);
        String text3 = getResources().getString(R.string.custom_keyboard_activity_setting_shadow);
        String text4 = getResources().getString(R.string.custom_keyboard_activity_setting_other);

        mTabSegment.addTab(builder.setText(text).build());
        mTabSegment.addTab(builder.setText(text0).build());
        mTabSegment.addTab(builder.setText(text1).build());
        mTabSegment.addTab(builder.setText(text2).build());
        mTabSegment.addTab(builder.setText(text3).build());
        mTabSegment.addTab(builder.setText(text4).build());

        int indicatorHeight = QMUIDisplayHelper.dp2px(this, 2);
        mTabSegment.setIndicator(new QMUITabIndicator(
                indicatorHeight, false, false));
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
            }

            @Override
            public void onDoubleTap(int index) {
                mTabSegment.clearSignCountView(index);
            }
        });
    }

    private View getPageView(ContentPage page) {
        View view = mPageMap.get(page);
        if (view == null) {
            if (page == ContentPage.BORDER) {
                view = createBorderConfigView();
            } else if (page == ContentPage.GAPS) {
                view = createGapsConfigView();
            } else if (page == ContentPage.KEY_FILL) {
                view = createConfigKeyFillStyleView();
            } else if (page == ContentPage.KEY_TEXT) {
                view = createKeyTextConfigView();
            } else if (page == ContentPage.LAYER) {
                view = createKeyLayerConfigView();
            } else {
                view = createOthersConfigView();
            }
            mPageMap.put(page, view);
        }
        return view;
    }

    public enum ContentPage {
        BORDER(0),
        GAPS(1),
        KEY_FILL(2),
        KEY_TEXT(3),
        LAYER(4),
        OTHERS(5);


        public static final int SIZE = 6;
        private final int position;

        ContentPage(int pos) {
            position = pos;
        }

        public static ContentPage getPage(int position) {
            if (position < 0 || position > SIZE) {
                return BORDER;
            }

            return ContentPage.values()[position];
        }

        public int getPosition() {
            return position;
        }
    }

    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return ContentPage.SIZE;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            ContentPage page = ContentPage.getPage(position);
            View view = getPageView(page);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };

    private int mHideSide = IBorderLayout.HIDE_RADIUS_SIDE_NONE;
    private float mBorderWidth = 0;
    private float mBorderCorner;
    private int mBorderNormalColor = Color.TRANSPARENT;
    private int mBorderPressColor = Color.TRANSPARENT;

    private View createBorderConfigView() {
        View view = LayoutInflater.from(this).inflate(R.layout.config_keyboard_border, null);
        SeekBar cornerSeekBar = view.findViewById(R.id.corner_seek_bar);

        final float toWidth = 10.0f;
        SeekBar widthSeekBar = view.findViewById(R.id.stroke_seek_bar);
        widthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBorderWidth = toWidth * progress / 100;
                configBorder();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final float fromCorner = Tools.dip2px(this, 6);
        final float toCorner = Tools.dip2px(this, 12);
        mBorderCorner = fromCorner;
        cornerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBorderCorner = fromCorner + (toCorner - fromCorner) * progress / 100;
                configBorder();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final int fromColor = Color.TRANSPARENT;
        final int toColor = Color.WHITE;
        SeekBar norColorSeekBar = view.findViewById(R.id.normal_color_seek_bar);
        norColorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBorderNormalColor = QMUIColorHelper.computeColor(fromColor, toColor, (float) progress / 100);
                configBorder();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final int fromPressColor = Color.TRANSPARENT;
        final int toPressColor = Color.RED;
        SeekBar pressColorSeekBar = view.findViewById(R.id.press_color_seek_bar);
        pressColorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBorderPressColor = QMUIColorHelper.computeColor(fromPressColor, toPressColor, (float) progress / 100);
                configBorder();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        RadioGroup radioGroup = view.findViewById(R.id.border_side_options);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.none_side) {
                    mHideSide = IBorderLayout.HIDE_RADIUS_SIDE_NONE;
                } else if (checkedId == R.id.top_side) {
                    mHideSide = IBorderLayout.HIDE_RADIUS_SIDE_TOP;
                } else if (checkedId == R.id.left_side) {
                    mHideSide = IBorderLayout.HIDE_RADIUS_SIDE_LEFT;
                } else if (checkedId == R.id.right_side) {
                    mHideSide = IBorderLayout.HIDE_RADIUS_SIDE_RIGHT;
                } else if (checkedId == R.id.bottom_side) {
                    mHideSide = IBorderLayout.HIDE_RADIUS_SIDE_BOTTOM;
                }
                configBorder();
            }
        });
        radioGroup.check(R.id.none_side);
        return view;
    }

    private void configBorder() {
        mSafeKeyboard.setLetterKeyboardBorderStyle(mBorderWidth, mBorderCorner, mHideSide, mBorderNormalColor,
                mBorderPressColor);
        mSafeKeyboard.setNumberKeyboardBorderStyle(mBorderWidth, mBorderCorner, mHideSide, mBorderNormalColor,
                mBorderPressColor);
        mSafeKeyboard.setSymbolKeyboardBorderStyle(mBorderWidth, mBorderCorner, mHideSide, mBorderNormalColor,
                mBorderPressColor);
    }

    private int mHGap;
    private int mVGap;

    private View createGapsConfigView() {
        View view = LayoutInflater.from(this).inflate(R.layout.config_keyboard_gaps, null);

        final int fromHGap = Tools.dip2px(this, 4);
        final int toHGap = Tools.dip2px(this, 10);
        mHGap = fromHGap;
        SeekBar hSeekBar = view.findViewById(R.id.h_gap_seek_bar);
        hSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mHGap = fromHGap + (toHGap - fromHGap) * progress / 100;
                configGap();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final int fromVGap = Tools.dip2px(this, 13);
        final int toVGap = Tools.dip2px(this, 20);
        mVGap = fromVGap;
        SeekBar vSeekBar = view.findViewById(R.id.v_gap_seek_bar);
        vSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mVGap = fromVGap + (toVGap - fromVGap) * progress / 100;
                configGap();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    private void configGap() {
        mSafeKeyboard.setSymbolKeyboardGaps(mHGap, mVGap);
        mSafeKeyboard.setLetterKeyboardGaps(mHGap, mVGap);
        mSafeKeyboard.setNumberKeyboardGaps(mHGap, mVGap);
    }

    private int mCNormalFillColor;
    private int mCPressFillColor;
    private int mFnNormalFillColor;
    private int mFnPressFillColor;

    private View createConfigKeyFillStyleView() {
        View view = LayoutInflater.from(this).inflate(R.layout.config_keyboard_fill, null);
        SeekBar seekBar1 = view.findViewById(R.id.normal_content_fill_seek_bar);

        final int fromCNColor = getResources().getColor(R.color.default_content_key_normal_color);
        final int toCNColor = Color.GRAY;
        mCNormalFillColor = fromCNColor;
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCNormalFillColor = QMUIColorHelper.computeColor(fromCNColor, toCNColor, (float) progress / 100);
                configFill();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final int fromCPColor = getResources().getColor(R.color.default_content_key_press_color);
        final int toCPColor = Color.GREEN;
        mCPressFillColor = fromCPColor;
        SeekBar seekBar2 = view.findViewById(R.id.press_content_fill_seek_bar);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCPressFillColor = QMUIColorHelper.computeColor(fromCPColor, toCPColor, (float) progress / 100);
                configFill();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final int fromFnNColor = getResources().getColor(R.color.default_fn_key_normal_color);
        final int toFnNColor = Color.YELLOW;
        mFnNormalFillColor = fromFnNColor;
        SeekBar seekBar3 = view.findViewById(R.id.normal_fn_fill_seek_bar);
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFnNormalFillColor = QMUIColorHelper.computeColor(fromFnNColor, toFnNColor, (float) progress / 100);
                configFill();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final int fromFnPColor = getResources().getColor(R.color.default_fn_key_press_color);
        final int toFnPColor = Color.RED;
        mFnPressFillColor = fromFnPColor;
        SeekBar seekBar4 = view.findViewById(R.id.press_fn_fill_seek_bar);
        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFnPressFillColor = QMUIColorHelper.computeColor(fromFnPColor, toFnPColor, (float) progress / 100);
                configFill();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    private void configFill() {
        mSafeKeyboard.setLetterKeyboardContentKeyFillStyle(mCNormalFillColor, mCPressFillColor);
        mSafeKeyboard.setNumberKeyboardContentKeyFillStyle(mCNormalFillColor, mCPressFillColor);
        mSafeKeyboard.setSymbolKeyboardContentKeyFillStyle(mCNormalFillColor, mCPressFillColor);

        mSafeKeyboard.setLetterKeyboardFnKeyFillStyle(mFnNormalFillColor, mFnPressFillColor);
        mSafeKeyboard.setNumberKeyboardFnKeyFillStyle(mFnNormalFillColor, mFnPressFillColor);
        mSafeKeyboard.setSymbolKeyboardFnKeyFillStyle(mFnNormalFillColor, mFnPressFillColor);
    }

    private int mKeyTextSize;
    private int mTextNormalColor;
    private int mTextPressColor;

    private View createKeyTextConfigView() {
        View view = LayoutInflater.from(this).inflate(R.layout.config_keyboard_text, null);

        final int fromTextSize = Tools.dip2px(this, 24f);
        final int toTextSize = Tools.dip2px(this, 34f);
        mKeyTextSize = fromTextSize;
        SeekBar seekBar1 = view.findViewById(R.id.text_size_seek_bar);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mKeyTextSize = fromTextSize + (toTextSize - fromTextSize) * progress / 100;
                configKeyText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final int fromTextNormalColor = getResources().getColor(R.color.default_key_normal_state_text_color);
        final int toTextNormalColor = Color.GREEN;
        mTextNormalColor = fromTextNormalColor;
        SeekBar seekBar2 = view.findViewById(R.id.normal_text_color_seek_bar);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextNormalColor = QMUIColorHelper
                        .computeColor(fromTextNormalColor, toTextNormalColor, (float) progress / 100);
                configKeyText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final int fromTextPressColor = getResources().getColor(R.color.default_key_press_state_text_color);
        final int toTextPressColor = Color.RED;
        mTextPressColor = fromTextPressColor;
        SeekBar seekBar3 = view.findViewById(R.id.press_text_color_seek_bar);
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextPressColor = QMUIColorHelper
                        .computeColor(fromTextPressColor, toTextPressColor, (float) progress / 100);
                configKeyText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    private void configKeyText() {
        mSafeKeyboard.setLetterKeyboardKeyTextStyle(mKeyTextSize, mTextNormalColor, mTextPressColor);
        mSafeKeyboard.setNumberKeyboardKeyTextStyle(mKeyTextSize, mTextNormalColor, mTextPressColor);
        mSafeKeyboard.setSymbolKeyboardKeyTextStyle(mKeyTextSize, mTextNormalColor, mTextPressColor);
    }

    private int mLayerColor;
    private int mLayerNormalHeight;
    private int mLayerPressHeight;

    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    private View createKeyLayerConfigView() {
        View view = LayoutInflater.from(this).inflate(R.layout.config_keyboard_layer, null);
        SeekBar seekBar1 = view.findViewById(R.id.layer_normal_height_seek_bar);
        SeekBar seekBar2 = view.findViewById(R.id.layer_press_height_seek_bar);
        SeekBar seekBar3 = view.findViewById(R.id.layer_color_seek_bar);

        final int fromLayerColor = getResources().getColor(R.color.default_key_layer_color);
        final int toLayerColor = Color.WHITE;
        mLayerColor = fromLayerColor;
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLayerColor = QMUIColorHelper.computeColor(fromLayerColor, toLayerColor, (float) progress / 100);
                configKeyLayer();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final int fromNormalLayerHeight = Tools.dip2px(this, 1);
        final int toNormalLayerHeight = Tools.dip2px(this, 5);
        mLayerNormalHeight = fromNormalLayerHeight;
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLayerNormalHeight =
                        fromNormalLayerHeight + (toNormalLayerHeight - fromNormalLayerHeight) * progress / 100;
                configKeyLayer();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final int fromPressLayerHeight = 0;
        final int toPressLayerHeight = Tools.dip2px(this, 5);
        mLayerPressHeight = fromPressLayerHeight;
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLayerPressHeight = fromPressLayerHeight + (toPressLayerHeight - fromPressLayerHeight) * progress / 100;
                configKeyLayer();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    private void configKeyLayer() {
        mSafeKeyboard.setLetterKeyboardLayerStyle(mLayerNormalHeight, mLayerPressHeight, mLayerColor);
        mSafeKeyboard.setNumberKeyboardLayerStyle(mLayerNormalHeight, mLayerPressHeight, mLayerColor);
        mSafeKeyboard.setSymbolKeyboardLayerStyle(mLayerNormalHeight, mLayerPressHeight, mLayerColor);
    }

    private View createOthersConfigView() {
        View view = LayoutInflater.from(this).inflate(R.layout.config_keyboard_others, null);
        return view;
    }
}
