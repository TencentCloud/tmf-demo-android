/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.tmf.demo.qmui.fragment.components;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import com.tencent.tmf.demo.qmui.model.QDItemDescription;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link QMUIDialog} 的使用示例。
 * Created by cgspine on 15/9/15.
 */
public class QDDialogFragment extends BaseFragment {

    QMUITopBarLayout mTopBar;
    ListView mListView;

    private QDItemDescription mQDItemDescription;
    private int mCurrentDialogStyle = R.style.QMUI_Dialog;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_listview, null);
        mTopBar = view.findViewById(R.id.topbar);
        mListView = view.findViewById(R.id.listview);
        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
        initTopBar();
        initListView();
        return view;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheet();
                    }
                });

        mTopBar.setTitle(mQDItemDescription.getName());
    }

    private String getStringById(int id){
        return getResources().getString(id);
    }


    private void initListView() {
        String[] listItems = new String[]{
                getStringById(R.string.qmui_16),
                getStringById(R.string.qmui_17),
                getStringById(R.string.qmui_18),
                getStringById(R.string.qmui_19),
                getStringById(R.string.qmui_20),
                getStringById(R.string.qmui_21),
                getStringById(R.string.qmui_22),
                getStringById(R.string.qmui_23),
                getStringById(R.string.qmui_24),
                getStringById(R.string.qmui_25)
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        mListView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_list_item, data));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showMessagePositiveDialog();
                        break;
                    case 1:
                        showMessageNegativeDialog();
                        break;
                    case 2:
                        showLongMessageDialog();
                        break;
                    case 3:
                        showMenuDialog();
                        break;
                    case 4:
                        showConfirmMessageDialog();
                        break;
                    case 5:
                        showSingleChoiceDialog();
                        break;
                    case 6:
                        showMultiChoiceDialog();
                        break;
                    case 7:
                        showNumerousMultiChoiceDialog();
                        break;
                    case 8:
                        showEditTextDialog();
                        break;
                    case 9:
                        showAutoDialog();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    // ================================ 生成不同类型的对话框
    private void showMessagePositiveDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle(getStringById(R.string.qmui_26))
                .setMessage(getStringById(R.string.qmui_27))
                .addAction(getStringById(R.string.qmui_28), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(getStringById(R.string.qmui_29), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), getStringById(R.string.qmui_30), Toast.LENGTH_SHORT).show();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showMessageNegativeDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle(getStringById(R.string.qmui_31))
                .setMessage(getStringById(R.string.qmui_32))
                .addAction(getStringById(R.string.qmui_33), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, getStringById(R.string.qmui_34), QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Toast.makeText(getActivity(), getStringById(R.string.qmui_35), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showLongMessageDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle(getStringById(R.string.qmui_31))
                .setMessage("这是一段很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长"
                        + "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很"
                        + "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很"
                        + "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很"
                        + "长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长"
                        + "很长很长很长很长很很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长"
                        + "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长"
                        + "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长"
                        + "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长"
                        + "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长"
                        + "很长很长很长很长很长很长很长很长很长很长很长很长很长很长长很长的文案")
                .addAction(getStringById(R.string.qmui_33), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showConfirmMessageDialog() {
        new QMUIDialog.CheckBoxMessageDialogBuilder(getActivity())
                .setTitle(getStringById(R.string.qmui_36))
                .setMessage(getStringById(R.string.qmui_37))
                .setChecked(true)
                .addAction(getStringById(R.string.qmui_38), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(getStringById(R.string.qmui_39), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }
    
    private String getChoose(String index){
        return getResources().getString(R.string.qmui_40,index);
    }

    private void showMenuDialog() {
        final String[] items = new String[]{getChoose("1"), getChoose("2"), getChoose("3")};
        new QMUIDialog.MenuDialogBuilder(getActivity())
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), getStringById(R.string.qmui_41) + items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showSingleChoiceDialog() {
        final String[] items = new String[]{getChoose("1"), getChoose("2"), getChoose("3")};
        final int checkedIndex = 1;
        new QMUIDialog.CheckableDialogBuilder(getActivity())
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), getStringById(R.string.qmui_41) + items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showMultiChoiceDialog() {
        final String[] items = new String[]{getChoose("1"), getChoose("2"), getChoose("3"), getChoose("4"), getChoose("5"), getChoose("6")};
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(getActivity())
                .setCheckedItems(new int[]{1, 3})
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.addAction(getStringById(R.string.qmui_38), new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction(getStringById(R.string.qmui_42), new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                String result = getStringById(R.string.qmui_41);
                for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                    result += "" + builder.getCheckedItemIndexes()[i] + "; ";
                }
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.create(mCurrentDialogStyle).show();
    }

    private void showNumerousMultiChoiceDialog() {
        final String[] items = new String[]{
                getChoose("1"), getChoose("2"), getChoose("3"), getChoose("4"), getChoose("5"), getChoose("6"),
                getChoose("7"), getChoose("8"), getChoose("9"), getChoose("10"), getChoose("11"), getChoose("12"),
                getChoose("13"), getChoose("14"), getChoose("15"), getChoose("16"), getChoose("17"), getChoose("18")
        };
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(getActivity())
                .setCheckedItems(new int[]{1, 3})
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.addAction(getStringById(R.string.qmui_38), new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction(getStringById(R.string.qmui_42), new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                String result =getStringById(R.string.qmui_41);
                for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                    result += "" + builder.getCheckedItemIndexes()[i] + "; ";
                }
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.create(mCurrentDialogStyle).show();
    }

    private void showEditTextDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle(getStringById(R.string.qmui_26))
                .setPlaceholder(getStringById(R.string.qmui_43))
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction(getStringById(R.string.qmui_38), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(getStringById(R.string.qmui_29), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            Toast.makeText(getActivity(), getStringById(R.string.qmui_44) + text, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), getStringById(R.string.qmui_45), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showAutoDialog() {
        QMAutoTestDialogBuilder autoTestDialogBuilder = (QMAutoTestDialogBuilder) new QMAutoTestDialogBuilder(
                getActivity())
                .addAction(getStringById(R.string.qmui_38), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(getStringById(R.string.qmui_29), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Toast.makeText(getActivity(), getStringById(R.string.qmui_46), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        autoTestDialogBuilder.create(mCurrentDialogStyle).show();
        QMUIKeyboardHelper.showKeyboard(autoTestDialogBuilder.getEditText(), true);
    }

    class QMAutoTestDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {

        private Context mContext;
        private EditText mEditText;

        public QMAutoTestDialogBuilder(Context context) {
            super(context);
            mContext = context;
        }

        public EditText getEditText() {
            return mEditText;
        }

        @Override
        public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = QMUIDisplayHelper.dp2px(mContext, 20);
            layout.setPadding(padding, padding, padding, padding);
            mEditText = new AppCompatEditText(mContext);
            QMUIViewHelper.setBackgroundKeepingPadding(mEditText,
                    QMUIResHelper.getAttrDrawable(mContext, R.attr.qmui_list_item_bg_with_border_bottom));
            mEditText.setHint(getStringById(R.string.qmui_47));
            LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    QMUIDisplayHelper.dpToPx(50));
            editTextLP.bottomMargin = QMUIDisplayHelper.dp2px(getContext(), 15);
            mEditText.setLayoutParams(editTextLP);
            layout.addView(mEditText);
            TextView textView = new TextView(mContext);
            textView.setLineSpacing(QMUIDisplayHelper.dp2px(getContext(), 4), 1.0f);
            textView.setText(getStringById(R.string.qmui_48));
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color_description));
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(textView);
            return layout;
        }
    }


    private void showBottomSheet() {
        new QMUIBottomSheet.BottomListSheetBuilder(getContext())
                .addItem(getStringById(R.string.qmui_49))
                .addItem(getStringById(R.string.qmui_50))
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        switch (position) {
                            case 0:
                                mCurrentDialogStyle = R.style.QMUI_Dialog;
                                break;
                            case 1:
                                mCurrentDialogStyle = R.style.DialogTheme2;
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .build().show();
    }
}
