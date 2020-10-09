package com.taobao.onlinemonitor;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProblemCheck {
    Map<Context, Map<BroadcastReceiver, Object>> mArrayMapReceivers;
    Field mFileSpFile;
    boolean mIsUploadBroadCastProblem;
    MySharedPreferenceChangeListener mMySharedPreferenceChangeListener = new MySharedPreferenceChangeListener();
    OnLineMonitor mOnLineMonitor;
    ConcurrentLinkedQueue<Runnable> mPendingWorkFinishers;
    Map<String, Integer> mSharedpreferenceKeyFreq;
    Map<String, Integer> mSharedpreferenceQueuedWork;

    @SuppressLint({"NewApi"})
    public ProblemCheck(OnLineMonitor onLineMonitor) {
        this.mOnLineMonitor = onLineMonitor;
        this.mSharedpreferenceKeyFreq = OnLineMonitor.createArrayMap();
        if (OnLineMonitor.sIsTraceDetail) {
            this.mSharedpreferenceQueuedWork = OnLineMonitor.createArrayMap();
        }
    }

    /* access modifiers changed from: package-private */
    public void regSharedPreferenceListener(Thread thread) {
        try {
            Field declaredField = thread.getClass().getDeclaredField("this$0");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(thread);
            if (obj instanceof SharedPreferences) {
                ((SharedPreferences) obj).registerOnSharedPreferenceChangeListener(this.mMySharedPreferenceChangeListener);
            }
        } catch (Throwable unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public void checkQueuedWork() {
        Object[] array;
        int i;
        try {
            if (this.mPendingWorkFinishers == null) {
                Class<?> cls = Class.forName("android.app.QueuedWork");
                Field declaredField = cls.getDeclaredField("sPendingWorkFinishers");
                declaredField.setAccessible(true);
                this.mPendingWorkFinishers = (ConcurrentLinkedQueue) declaredField.get(cls);
            } else if (this.mPendingWorkFinishers.size() > 0 && (array = this.mPendingWorkFinishers.toArray()) != null) {
                for (int i2 = 0; i2 < array.length; i2++) {
                    Field declaredField2 = array[i2].getClass().getDeclaredField("this$1");
                    declaredField2.setAccessible(true);
                    Object obj = declaredField2.get(array[i2]);
                    Field declaredField3 = obj.getClass().getDeclaredField("this$0");
                    declaredField3.setAccessible(true);
                    Object obj2 = declaredField3.get(obj);
                    if (this.mFileSpFile == null) {
                        Field declaredField4 = obj2.getClass().getDeclaredField("mFile");
                        declaredField4.setAccessible(true);
                        this.mFileSpFile = declaredField4;
                    }
                    File file = (File) this.mFileSpFile.get(obj2);
                    if (file != null) {
                        String absolutePath = file.getAbsolutePath();
                        if (this.mSharedpreferenceQueuedWork != null) {
                            Integer remove = this.mSharedpreferenceQueuedWork.remove(absolutePath);
                            if (remove != null) {
                                this.mSharedpreferenceQueuedWork.put(absolutePath, Integer.valueOf(remove.intValue() + 1));
                            } else {
                                this.mSharedpreferenceQueuedWork.put(absolutePath, 1);
                            }
                        }
                        if (this.mOnLineMonitor.mOnlineStatistics != null) {
                            int size = this.mOnLineMonitor.mOnlineStatistics.size();
                            int i3 = 0;
                            while (i3 < size) {
                                OnlineStatistics onlineStatistics = this.mOnLineMonitor.mOnlineStatistics.get(i3);
                                if (onlineStatistics != null) {
                                    i = i3;
                                    onlineStatistics.onBlockOrCloseGuard(this.mOnLineMonitor.mOnLineStat, 4, "SP-QueuedWork", this.mOnLineMonitor.mActivityName, absolutePath, "", 0);
                                } else {
                                    i = i3;
                                }
                                i3 = i + 1;
                            }
                        }
                    }
                    if (OnLineMonitor.sIsTraceDetail) {
                        Log.e("OnLineMonitor", "检测到SharedPreferences修改导致的阻塞，SharedPreferences File：" + this.mFileSpFile.get(obj2));
                    }
                }
            }
        } catch (Throwable unused) {
        }
    }

    class MySharedPreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        MySharedPreferenceChangeListener() {
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            try {
                if (ProblemCheck.this.mFileSpFile == null) {
                    Field declaredField = sharedPreferences.getClass().getDeclaredField("mFile");
                    declaredField.setAccessible(true);
                    ProblemCheck.this.mFileSpFile = declaredField;
                }
                File file = (File) ProblemCheck.this.mFileSpFile.get(sharedPreferences);
                if (file != null) {
                    String str2 = file.getAbsolutePath() + " ：" + str;
                    if (ProblemCheck.this.mSharedpreferenceKeyFreq != null) {
                        Integer remove = ProblemCheck.this.mSharedpreferenceKeyFreq.remove(str2);
                        if (remove != null) {
                            ProblemCheck.this.mSharedpreferenceKeyFreq.put(str2, Integer.valueOf(remove.intValue() + 1));
                        } else {
                            ProblemCheck.this.mSharedpreferenceKeyFreq.put(str2, 1);
                        }
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int checkBroadCastCount(Application application, boolean z) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6 = 0;
        try {
            if (this.mArrayMapReceivers == null) {
                Field declaredField = Class.forName("android.app.LoadedApk").getDeclaredField("mReceivers");
                Field declaredField2 = Application.class.getDeclaredField("mLoadedApk");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                this.mArrayMapReceivers = (Map) declaredField.get(declaredField2.get(application));
            }
            if (this.mArrayMapReceivers == null) {
                return 0;
            }
            HashMap hashMap = null;
            try {
                Set<Map.Entry<Context, Map<BroadcastReceiver, Object>>> entrySet = this.mArrayMapReceivers.entrySet();
                if (entrySet != null) {
                    i = 0;
                    for (Map.Entry<Context, Map<BroadcastReceiver, Object>> value : entrySet) {
                        try {
                            Map map = (Map) value.getValue();
                            if (map != null) {
                                i += map.size();
                                if (z) {
                                    if (hashMap == null) {
                                        hashMap = new HashMap();
                                    }
                                    Set<Map.Entry> entrySet2 = map.entrySet();
                                    if (entrySet2 != null) {
                                        for (Map.Entry key : entrySet2) {
                                            String name = key.getKey().getClass().getName();
                                            if (name != null) {
                                                Integer num = (Integer) hashMap.remove(name);
                                                if (num != null) {
                                                    hashMap.put(name, Integer.valueOf(num.intValue() + 1));
                                                } else {
                                                    hashMap.put(name, 1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e = e;
                            i6 = i;
                            e.printStackTrace();
                            return i6;
                        } catch (Throwable unused) {
                        }
                    }
                } else {
                    i = 0;
                }
                if (z && hashMap != null && hashMap.size() > 0) {
                    Set<Map.Entry> entrySet3 = hashMap.entrySet();
                    int i7 = 0;
                    for (Map.Entry entry : entrySet3) {
                        String str = (String) entry.getKey();
                        Integer num2 = (Integer) entry.getValue();
                        if (num2 != null && num2.intValue() >= 10) {
                            i7 += num2.intValue();
                            Log.e("OnLineMonitor", str + ", size=" + num2);
                            if (this.mOnLineMonitor.mOnlineStatistics != null) {
                                int size = this.mOnLineMonitor.mOnlineStatistics.size();
                                int i8 = 0;
                                while (i8 < size) {
                                    OnlineStatistics onlineStatistics = this.mOnLineMonitor.mOnlineStatistics.get(i8);
                                    if (onlineStatistics != null) {
                                        i5 = i8;
                                        onlineStatistics.onBlockOrCloseGuard(this.mOnLineMonitor.mOnLineStat, 5, "BroadCast", str, "", "", num2.intValue());
                                    } else {
                                        i5 = i8;
                                    }
                                    i8 = i5 + 1;
                                }
                            }
                        }
                    }
                    if (i7 < i / 2) {
                        for (Map.Entry entry2 : entrySet3) {
                            String str2 = (String) entry2.getKey();
                            Integer num3 = (Integer) entry2.getValue();
                            if (num3 != null && num3.intValue() >= 2 && num3.intValue() < 10) {
                                i7 += num3.intValue();
                                if (this.mOnLineMonitor.mOnlineStatistics != null) {
                                    int size2 = this.mOnLineMonitor.mOnlineStatistics.size();
                                    int i9 = 0;
                                    while (i9 < size2) {
                                        OnlineStatistics onlineStatistics2 = this.mOnLineMonitor.mOnlineStatistics.get(i9);
                                        if (onlineStatistics2 != null) {
                                            i4 = i9;
                                            i3 = size2;
                                            onlineStatistics2.onBlockOrCloseGuard(this.mOnLineMonitor.mOnLineStat, 5, "BroadCast", str2, "", "", num3.intValue());
                                        } else {
                                            i4 = i9;
                                            i3 = size2;
                                        }
                                        i9 = i4 + 1;
                                        size2 = i3;
                                    }
                                }
                            }
                        }
                    }
                    if (i7 < i / 2) {
                        for (Map.Entry entry3 : entrySet3) {
                            String str3 = (String) entry3.getKey();
                            Integer num4 = (Integer) entry3.getValue();
                            if (!(num4 == null || num4.intValue() >= 2 || this.mOnLineMonitor.mOnlineStatistics == null)) {
                                int size3 = this.mOnLineMonitor.mOnlineStatistics.size();
                                int i10 = 0;
                                while (i10 < size3) {
                                    OnlineStatistics onlineStatistics3 = this.mOnLineMonitor.mOnlineStatistics.get(i10);
                                    if (onlineStatistics3 != null) {
                                        i2 = i10;
                                        onlineStatistics3.onBlockOrCloseGuard(this.mOnLineMonitor.mOnLineStat, 5, "BroadCast", str3, "", "", num4.intValue());
                                    } else {
                                        i2 = i10;
                                    }
                                    i10 = i2 + 1;
                                }
                            }
                        }
                    }
                }
                return i;
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                return i6;
            }
        } catch (Throwable unused2) {
            return i6;
        }
    }
}
