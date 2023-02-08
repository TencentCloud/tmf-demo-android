package com.tencent.tmf.h5container.doc;

import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.tmf.h5container.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileReaderDialog
        extends Dialog
        implements ListAdapter, Comparator<File>, OnClickListener {

    private float mDensity = -1;
    private String mRootDir = null;
    private File mCurrDir = null;
    private LinearLayout mRootView = null;

    private LinearLayout mHeadView = null;
    private Button mBackButn = null;

    private ListView mListView = null;

    private LinearLayout mFootView = null;
    private TextView mPathView = null;

    private DataSetObserver mObserver = null;

    private List<File> mFileList = new ArrayList<File>();
    private Set<String> mFileType = new HashSet<String>();
    Context mContext = null;

    /**
     * FileReaderDialog
     * @param context
     */
    public FileReaderDialog(Context context) {
        super(context, android.R.style.Theme_Light);
        mContext = context;
        mRootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        mCurrDir = new File(mRootDir);

        mFileType.add("xls");
        mFileType.add("xlsx");

        mFileType.add("ppt");
        mFileType.add("pptx");

        mFileType.add("doc");
        mFileType.add("docx");

        mFileType.add("txt");
        mFileType.add("log");

        mFileType.add("pdf");
        mFileType.add("epub");

        initDialog(context);
    }

    private void initDialog(Context context) {
        setTitle("FileReaderDemo");
        mRootView = new LinearLayout(context);
        mRootView.setOrientation(LinearLayout.VERTICAL);
        setContentView(mRootView);

        mHeadView = new LinearLayout(context);
        mHeadView.setOrientation(LinearLayout.HORIZONTAL);
        mHeadView.setBackgroundColor(0x44000000);
        mHeadView.setGravity(Gravity.LEFT);
        mRootView.addView(mHeadView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp2px(50)));

        //header
        {
            mBackButn = new Button(context);
            mBackButn.setText("back");
            mBackButn.setOnClickListener(this);
            mHeadView.addView(mBackButn, new LinearLayout.LayoutParams(dp2px(80), LayoutParams.MATCH_PARENT));
        }

        mListView = new ListView(context);
        mListView.setAdapter(this);
        mRootView.addView(mListView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));

        mFootView = new LinearLayout(context);
        mFootView.setOrientation(LinearLayout.HORIZONTAL);
        mFootView.setBackgroundColor(0x22000FFF);
        mFootView.setGravity(Gravity.LEFT);
        mRootView.addView(mFootView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp2px(50)));

        //footer
        {
            mPathView = new TextView(context);
            mPathView.setText(mCurrDir.getAbsolutePath());
            mPathView.setTextColor(0xFF000000);
            mPathView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            mFootView.addView(mPathView,
                    new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }


    public void registerDataSetObserver(DataSetObserver observer) {
        mObserver = observer;
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    public int getCount() {
        return mFileList.size();
    }

    public Object getItem(int position) {
        return mFileList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public boolean hasStableIds() {
        return false;
    }

    /**
     * getView
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        File fileItem = mFileList.get(position);
        if (convertView == null) {
            FileItemView textView = new FileItemView(parent.getContext());
            textView.setTextColor(0xFF000000);
            textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            textView.setPadding(dp2px(5), 0, dp2px(5), 0);
            textView.setLayoutParams(new ListView.LayoutParams(parent.getWidth(), dp2px(40)));
            convertView = textView;
        }

        FileItemView textView = (FileItemView) convertView;
        textView.setFileObject(fileItem);
        textView.setText(fileItem.getName());
        textView.setOnClickListener(this);

        if (fileItem.isDirectory()) {
            textView.setBackgroundColor(0x55993300);
        } else {
            textView.setBackgroundColor(0xFFFFFFFF);
        }

        return convertView;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean isEmpty() {
        return mFileList.isEmpty();
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public boolean isEnabled(int position) {
        return false;
    }

    public void showFileList() {
        refreshFileList();
        show();
    }

    public void refreshFileList() {

        File[] subFiles = mCurrDir.listFiles();
        mFileList.clear();
        for (File subFile : subFiles) {
            if (!subFile.getName().startsWith(".") && !subFile.isHidden()) {
                mFileList.add(subFile);
            }
        }
        Collections.sort(mFileList, this);

        mPathView.setText(mCurrDir.getAbsolutePath());
        mObserver.onChanged();
    }

    public void onClick(View view) {
        if (view == mBackButn) {
            File parentFile = mCurrDir.getParentFile();
            if (parentFile.getAbsolutePath().length() >= mRootDir.length()) {
                mCurrDir = parentFile;
                refreshFileList();
            }
        } else if (view instanceof FileItemView) {
            FileItemView fileItem = (FileItemView) view;
            onFileItemClicked(fileItem);
        }
    }

    /**
     * onFileItemClicked
     * @param fileItem
     */
    public void onFileItemClicked(FileItemView fileItem) {
        if (fileItem.getFileObject().isDirectory()) {
            mCurrDir = fileItem.getFileObject();
            refreshFileList();
            return;
        } else {
            String fileName = fileItem.getFileObject().getName();
            String[] items = fileName.split("\\.");
            if (items.length > 1) {
                String filePath = fileItem.getFileObject().getAbsolutePath();
                int ret = QbSdk.openFileReader(mContext, filePath);
                if (ret >= 0) {
                    return;
                }
            }
        }
        String text = fileItem.getContext().getResources().getString(R.string.custom_doc_activity_load_doc_failed);

        Toast.makeText(fileItem.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public int compare(File lhs, File rhs) {
        if (lhs.isDirectory() && !rhs.isDirectory()) {
            return -1;
        } else if (!lhs.isDirectory() && rhs.isDirectory()) {
            return 1;
        } else {
            return lhs.getName().compareTo(rhs.getName());
        }
    }

    public int dp2px(float dp) {
        if (mDensity < 0) {
            mDensity = getContext().getResources().getDisplayMetrics().density;
        }
        return (int) (dp * mDensity + 0.5);
    }


    public static class FileItemView extends TextView {

        public FileItemView(Context context) {
            super(context);
        }

        public File mFileObject = null;

        public File getFileObject() {
            return mFileObject;
        }

        public void setFileObject(File fileObject) {
            mFileObject = fileObject;
        }
    }
}
