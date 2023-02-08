package com.tencent.tmf.module.portal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.service.IModulePortalDynamicService;
import com.tencent.tmf.module.portal.interceptors.DynamicGlobalInterceptor;
import com.tencent.tmf.module.portal.interceptors.LaunchInterceptor;
import com.tencent.tmf.module.portal.interceptors.MethodInterceptor;
import com.tencent.tmf.portal.DefaultMapping;
import com.tencent.tmf.portal.LaunchCallback;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.Mapping;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.SimpleLaunchCallback;
import com.tencent.tmf.portal.annotations.Destination;

@Destination(url = PortalConst.PORTAL_ACTIVITY,
        launcher = Launcher.ACTIVITY,
        description = "Portal演示")
public class PortalActivity extends TopBarActivity {

    private static final int REQUEST_CODE = 101;

    private static final int START_NORMAL = 0;
    private static final int START_WITH_ANIME = START_NORMAL + 1;
    private static final int START_ACTIVITY_OPTIONS = START_WITH_ANIME + 1;
    private static final int START_WITH_PARAM = START_ACTIVITY_OPTIONS + 1;
    private static final int START_FOR_RESULT = START_WITH_PARAM + 1;
    private static final int START_WITH_CALLBACK = START_FOR_RESULT + 1;
    private static final int START_FRAGMENT = START_WITH_CALLBACK + 1;
    private static final int START_WEB_URL = START_FRAGMENT + 1;
    private static final int START_WITH_INTERCEPTOR = START_WEB_URL + 1;
    private static final int START_DESTINATION_INTERCEPTOR = START_WITH_INTERCEPTOR + 1;
    private static final int SWITCH_DYNAMIC_GLOBAL_INTERCEPTOR = START_DESTINATION_INTERCEPTOR + 1;
    private static final int START_METHOD = SWITCH_DYNAMIC_GLOBAL_INTERCEPTOR + 1;
    private static final int START_METHOD_WITH_INTERCEPTOR = START_METHOD + 1;
    private static final int SWITCH_DYNAMIC_MODULE = START_METHOD_WITH_INTERCEPTOR + 1;
    private static final int START_DYNAMIC_MODULE = SWITCH_DYNAMIC_MODULE + 1;
    private static final int START_DYNAMIC_SERVICE = START_DYNAMIC_MODULE + 1;
    private static final int SWITCH_DYNAMIC_ROUTE = START_DYNAMIC_SERVICE + 1;
    private static final int START_DYNAMIC_ROUTE = SWITCH_DYNAMIC_ROUTE + 1;
    private static final int SWITCH_ASYNC = START_DYNAMIC_ROUTE + 1;
    private static final int START_ASYNC_METHOD = SWITCH_ASYNC + 1;
    private static final int SWITCH_LAUNCHER = START_ASYNC_METHOD + 1;
    private static final int START_DYNAMIC_LAUNCHER = SWITCH_LAUNCHER + 1;

    private SparseArray<String> mButtons = new SparseArray<>();
    private SparseArray<Pair<String, Boolean>> mSwitches = new SparseArray<>();

    private RecyclerView mRecyclerView;
    private Integer[] mItems = new Integer[]{
            START_NORMAL, START_WITH_ANIME, START_ACTIVITY_OPTIONS, START_WITH_PARAM, START_FOR_RESULT,
            START_WITH_CALLBACK, START_FRAGMENT, START_WEB_URL, START_WITH_INTERCEPTOR, START_DESTINATION_INTERCEPTOR,
            SWITCH_DYNAMIC_GLOBAL_INTERCEPTOR, START_METHOD, START_METHOD_WITH_INTERCEPTOR, SWITCH_DYNAMIC_MODULE,
            START_DYNAMIC_MODULE, START_DYNAMIC_SERVICE, SWITCH_DYNAMIC_ROUTE, START_DYNAMIC_ROUTE, SWITCH_ASYNC,
            START_ASYNC_METHOD, SWITCH_LAUNCHER, START_DYNAMIC_LAUNCHER
    };

    private final SimpleLaunchCallback mLaunchCallback = new SimpleLaunchCallback() {
        @Override
        public void onLaunchComplete(Bundle bundle) {
        }

        @Override
        public void onLaunchFailed(int i, String s) {
            showAlert(getStringById(R.string.module_portal_49) + s);
        }
    };
    private String getStringById(int id){
        return getResources().getString(id);
    }

    private final Mapping mDynamicMapping = new DefaultMapping();

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_portal, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopBar.setTitle(getStringById(R.string.module_portal_15));

        mButtons.put(START_NORMAL, getStringById(R.string.module_portal_16));
        mButtons.put(START_WITH_ANIME, getStringById(R.string.module_portal_17));
        mButtons.put(START_ACTIVITY_OPTIONS, getStringById(R.string.module_portal_18));
        mButtons.put(START_WITH_PARAM, getStringById(R.string.module_portal_19));
        mButtons.put(START_FOR_RESULT, getStringById(R.string.module_portal_20));
        mButtons.put(START_WITH_CALLBACK, getStringById(R.string.module_portal_21));
        mButtons.put(START_FRAGMENT, getStringById(R.string.module_portal_22));
        mButtons.put(START_WEB_URL, getStringById(R.string.module_portal_23));
        mButtons.put(START_WITH_INTERCEPTOR, getStringById(R.string.module_portal_24));
        mButtons.put(START_DESTINATION_INTERCEPTOR, getStringById(R.string.module_portal_25));
        mButtons.put(START_METHOD, getStringById(R.string.module_portal_26));
        mButtons.put(START_METHOD_WITH_INTERCEPTOR, getStringById(R.string.module_portal_27));
        mButtons.put(START_DYNAMIC_MODULE, getStringById(R.string.module_portal_28));
        mButtons.put(START_DYNAMIC_SERVICE, getStringById(R.string.module_portal_29));
        mButtons.put(START_DYNAMIC_ROUTE, getStringById(R.string.module_portal_30));
        mButtons.put(START_ASYNC_METHOD, getStringById(R.string.module_portal_31));
        mButtons.put(START_DYNAMIC_LAUNCHER, getStringById(R.string.module_portal_32));

        mSwitches.put(SWITCH_DYNAMIC_GLOBAL_INTERCEPTOR, new Pair<>(getStringById(R.string.module_portal_33), false));
        mSwitches.put(SWITCH_DYNAMIC_MODULE, new Pair<>(getStringById(R.string.module_portal_34), false));
        mSwitches.put(SWITCH_DYNAMIC_ROUTE, new Pair<>(getStringById(R.string.module_portal_35), false));
        mSwitches.put(SWITCH_ASYNC, new Pair<>(getStringById(R.string.module_portal_36), false));
        mSwitches.put(SWITCH_LAUNCHER, new Pair<>(getStringById(R.string.module_portal_37), false));

        mDynamicMapping.registerDestination(com.tencent.tmf.portal.Destination.create()
                .url(PortalConst.DYNAMIC_ACTIVITY)
                .launcher(Launcher.ACTIVITY)
                .realPath(DynamicRouteActivity.class.getCanonicalName())
                .targetClass(DynamicRouteActivity.class).build());

        mRecyclerView = findViewById(R.id.recycler_view);
        PortalAdapter adapter = new PortalAdapter();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            showAlert(getStringById(R.string.module_portal_48)+ data.getStringExtra("result"));
        }
    }

    private void handleClick(int id) {
        switch (id) {
            case START_NORMAL: {
                Portal.from(this)
                                .url(PortalConst.NO_RESULT_ACTIVITY).launch();
                break;
            }
            case START_WITH_ANIME: {
                Portal.from(this)
                        .url(PortalConst.NO_RESULT_ACTIVITY)
                        .activityTransition(R.anim.push_left_in, R.anim.push_left_out)
                        .launch();
                break;
            }
            case START_ACTIVITY_OPTIONS: {
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeClipRevealAnimation(getWindow().getDecorView(),
                                getWindow().getDecorView().getWidth() / 2,
                                getWindow().getDecorView().getHeight() / 2,
                                1000, 1000);
                Portal.from(this)
                        .url(PortalConst.NO_RESULT_ACTIVITY)
                        .activityOptionsCompat(options)
                        .launch();
                break;
            }
            case START_WITH_PARAM: {
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getStringById(R.string.module_portal_38));
                builder.setView(input);
                builder.setPositiveButton(getStringById(R.string.module_portal_39), (dialog, which) -> {
                    String text = input.getText().toString();
                    try {
                        int value = Integer.parseInt(text);
                        Portal.from(PortalActivity.this)
                                .url(PortalConst.NO_RESULT_ACTIVITY)
                                .param("param", value)
                                .launch(mLaunchCallback);
                    } catch (Exception e) {
                        Portal.from(PortalActivity.this)
                                .url(PortalConst.NO_RESULT_ACTIVITY)
                                .param("param", text)
                                .launch(mLaunchCallback);
                    }
                });
                builder.setNegativeButton(getStringById(R.string.module_portal_40), (dialog, which) -> dialog.cancel());
                builder.show();
                break;
            }
            case START_FOR_RESULT: {
                Portal.from(this)
                        .url(PortalConst.WITH_RESULT_ACTIVITY)
                        .startForActivityResult(REQUEST_CODE)
                        .launch();
                break;
            }
            case START_WITH_CALLBACK: {
                Portal.from(this)
                        .url(PortalConst.WITH_RESULT_ACTIVITY)
                        .startActivityWithCallback((i, intent) -> {
                            if (i == RESULT_OK && intent != null) {
                                showAlert(getStringById(R.string.module_portal_41) + intent.getStringExtra("result"));
                            }
                        })
                        .launch();
                break;
            }
            case START_FRAGMENT: {
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getStringById(R.string.module_portal_42));
                builder.setView(input);
                builder.setPositiveButton(getStringById(R.string.module_portal_43), (dialog, which) -> {
                    Portal.from(this)
                            .url(PortalConst.SIMPLE_FRAGMENT)
                            .param("param", input.getText().toString())
                            .getTargetInstance(instance -> {
                                if (instance != null) {
                                    FragmentManager fm = getSupportFragmentManager();
                                    fm.beginTransaction()
                                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                            .add(R.id.main, (Fragment) instance)
                                            .addToBackStack(null)
                                            .commitAllowingStateLoss();
                                }
                            }).launch();
                });
                builder.setNegativeButton(getStringById(R.string.module_portal_44), (dialog, which) -> dialog.cancel());
                builder.show();
                break;
            }
            case START_WEB_URL: {
                Portal.from(this).url("https://www.qq.com").launch();
                break;
            }
            case START_WITH_INTERCEPTOR: {
                Portal.from(this)
                        .url(PortalConst.NO_RESULT_ACTIVITY)
                        .interceptor(new LaunchInterceptor())
                        .launch(mLaunchCallback);
                break;
            }
            case START_DESTINATION_INTERCEPTOR: {
                Portal.from(this)
                        .url(PortalConst.INTERCEPTOR_ACTIVITY)
                        .launch();
                break;
            }
            case SWITCH_DYNAMIC_GLOBAL_INTERCEPTOR: {
                if (mSwitches.get(SWITCH_DYNAMIC_GLOBAL_INTERCEPTOR).second) {
                    Portal.registerGlobalInterceptor("dynamic", DynamicGlobalInterceptor.class, -2);
                } else {
                    Portal.unregisterGlobalInterceptor("dynamic");
                }
                break;
            }
            case START_METHOD: {
                Portal.from(this).url(PortalConst.TIME_METHOD).launch(new SimpleLaunchCallback() {
                    @Override
                    public void onLaunchComplete(Bundle bundle) {
                        showAlert(bundle.getString("result"));
                    }

                    @Override
                    public void onLaunchFailed(int i, String s) {
                        showAlert(s);
                    }
                });
                break;
            }
            case START_METHOD_WITH_INTERCEPTOR: {
                Portal.from(this)
                        .url(PortalConst.TIME_METHOD)
                        .interceptor(new MethodInterceptor())
                        .launch(new SimpleLaunchCallback() {
                            @Override
                            public void onLaunchComplete(Bundle bundle) {
                                showAlert(bundle.getString("result"));
                            }

                            @Override
                            public void onLaunchFailed(int i, String s) {
                                showAlert(s);
                            }
                        });
                break;
            }
            case SWITCH_DYNAMIC_MODULE: {
                if (mSwitches.get(SWITCH_DYNAMIC_MODULE).second) {
                    Portal.attachModule("module-portal-dynamic");
                } else {
                    Portal.detachModule("module-portal-dynamic");
                }
                break;
            }
            case START_DYNAMIC_MODULE: {
                Portal.from(this).url(PortalConst.MODULE_ACTIVITY).launch(mLaunchCallback);
                break;
            }
            case START_DYNAMIC_SERVICE: {
                IModulePortalDynamicService service = Portal.getService(IModulePortalDynamicService.class);
                if (service != null) {
                    showAlert(service.getMessage());
                } else {
                    showAlert(getStringById(R.string.module_portal_45));
                }
                break;
            }
            case SWITCH_DYNAMIC_ROUTE: {
                if (mSwitches.get(SWITCH_DYNAMIC_ROUTE).second) {
                    Portal.registerMapping(mDynamicMapping);
                } else {
                    Portal.unregisterMapping(mDynamicMapping);
                }
                break;
            }
            case START_DYNAMIC_ROUTE: {
                Portal.from(this).url(PortalConst.DYNAMIC_ACTIVITY).launch(mLaunchCallback);
                break;
            }
            case START_ASYNC_METHOD: {
                LaunchCallback callback = new SimpleLaunchCallback() {
                    @Override
                    public void onLaunchComplete(Bundle bundle) {
                        showAlert(bundle.getString("result"));
                    }

                    @Override
                    public void onLaunchFailed(int i, String s) {

                    }
                };
                if (mSwitches.get(SWITCH_ASYNC).second) {
                    Portal.from(this).url(PortalConst.ASYNC_METHOD).launch(callback);
                } else {
                    Portal.from(this).url(PortalConst.ASYNC_METHOD).synchronously().launch(callback);
                }
                break;
            }
            case SWITCH_LAUNCHER: {
                if (mSwitches.get(SWITCH_LAUNCHER).second) {
                    Portal.registerLauncher(new CustomLauncherFactory());
                } else {
                    Portal.unregisterLauncher(CustomLauncherFactory.NAME);
                }
                break;
            }
            case START_DYNAMIC_LAUNCHER: {
                Portal.from(this).url(PortalConst.EMPTY).launch(mLaunchCallback);
            }
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Portal.unregisterGlobalInterceptor("dynamic");
        Portal.detachModule("module-portal-dynamic");
        Portal.unregisterMapping(mDynamicMapping);
        Portal.unregisterLauncher(CustomLauncherFactory.NAME);
    }

    private class PortalHolder extends RecyclerView.ViewHolder {

        private final SwitchCompat switchView;
        private final TextView textView;

        int id = -1;

        public PortalHolder(@NonNull View itemView) {
            super(itemView);
            switchView = itemView.findViewById(R.id.switcher);
            textView = itemView.findViewById(R.id.content);
            itemView.setOnClickListener(v -> {
                if (mButtons.indexOfKey(id) >= 0) {
                    handleClick(id);
                }
            });
            switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (mSwitches.indexOfKey(id) >= 0) {
                    mSwitches.put(id, new Pair<>(mSwitches.get(id).first, isChecked));
                    handleClick(id);
                }
            });
        }

        public void bind(int id, String content) {
            this.id = id;
            switchView.setVisibility(View.GONE);
            textView.setText(content);
        }

        public void bind(int id, Pair<String, Boolean> switcher) {
            this.id = id;
            switchView.setVisibility(View.VISIBLE);
            switchView.setChecked(switcher.second);
            textView.setText(switcher.first);
        }

        public void bind(int id) {
            this.id = id;
            switchView.setVisibility(View.GONE);
            textView.setText("");
        }
    }

    private class PortalAdapter extends RecyclerView.Adapter<PortalHolder> {

        @NonNull
        @Override
        public PortalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PortalHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_portal, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PortalHolder holder, int position) {
            if (position < mItems.length) {
                int id = mItems[position];
                if (mButtons.indexOfKey(id) >= 0) {
                    holder.bind(id, mButtons.get(id));
                } else if (mSwitches.indexOfKey(id) >= 0) {
                    holder.bind(id, mSwitches.get(id));
                } else {
                    holder.bind(id);
                }
            }
        }

        @Override
        public int getItemCount() {
            return mItems.length;
        }
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getStringById(R.string.module_portal_46))
                .setMessage(message)
                .setPositiveButton(getStringById(R.string.module_portal_47), (dialog, which) -> {
                    dialog.cancel();
                }).show();
    }
}