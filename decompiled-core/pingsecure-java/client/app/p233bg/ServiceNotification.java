package com.pingsecure.client.app.p233bg;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import androidx.collection.ArraySet$$ExternalSyntheticOutline0;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.impl.WorkManagerImpl$$ExternalSyntheticLambda0;
import com.applovin.impl.e0$$ExternalSyntheticApiModelOutline0;
import com.inmobi.media.B2$$ExternalSyntheticApiModelOutline0;
import com.pingsecure.client.app.Application;
import com.pingsecure.client.app.MainActivity;
import com.pingsecure.client.app.R;
import com.pingsecure.client.app.Settings;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import kotlin.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okio.Okio;

/* loaded from: classes7.dex */
public final class ServiceNotification {
    private static final String NOTIFICATION_CHANNEL = "service";
    private static final int NOTIFICATION_ID = 1;
    private static final String WORK_MANAGER_TAG = "NOTIFICATION_WORK_MANAGER";
    private final Lazy notificationBuilder$delegate;
    private final Service service;
    public static final Companion Companion = new Companion(null);
    private static final int flags = AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL;

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean checkPermission() {
            if (Build.VERSION.SDK_INT < 33) {
                return true;
            }
            return ((NotificationManager) Application.notification$delegate.getValue()).areNotificationsEnabled();
        }

        public final int getFlags() {
            return ServiceNotification.flags;
        }

        private Companion() {
        }
    }

    public ServiceNotification(Service service) {
        Intrinsics.checkNotNullParameter(service, "service");
        this.service = service;
        this.notificationBuilder$delegate = Okio.lazy(new WorkManagerImpl$$ExternalSyntheticLambda0(this, 3));
    }

    private final String formatBytes(long j) {
        if (j >= 1000000) {
            return String.format("%.1f MB", Arrays.copyOf(new Object[]{Double.valueOf(j / 1000000.0d)}, 1));
        }
        if (j >= 1000) {
            return String.format("%.1f KB", Arrays.copyOf(new Object[]{Double.valueOf(j / 1000.0d)}, 1));
        }
        return j + " B";
    }

    private final NotificationCompat.Builder getNotificationBuilder() {
        return (NotificationCompat.Builder) this.notificationBuilder$delegate.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final NotificationCompat.Builder notificationBuilder_delegate$lambda$1(ServiceNotification serviceNotification) {
        NotificationCompat.Builder category = new NotificationCompat.Builder(serviceNotification.service, "service").setShowWhen(false).setOngoing(true).setContentTitle("Ping VPN").setOnlyAlertOnce(true).setSmallIcon(R.drawable.ic_menu).setCategory("service");
        Service service = serviceNotification.service;
        Intent flags2 = new Intent(serviceNotification.service, (Class<?>) MainActivity.class).setFlags(131072);
        int i = flags;
        NotificationCompat.Builder priority = category.setContentIntent(PendingIntent.getActivity(service, 0, flags2, i)).setPriority(-1);
        priority.addAction(new NotificationCompat.Action.Builder(0, serviceNotification.service.getText(R.string.stop), PendingIntent.getBroadcast(serviceNotification.service, 0, new Intent("com.pingsecure.client.app.SERVICE_CLOSE").setPackage(serviceNotification.service.getPackageName()), i)).build());
        return priority;
    }

    private final void setupTurnOffTimer() {
        WorkManager.Companion.getInstance(this.service).enqueueUniqueWork(WORK_MANAGER_TAG, ExistingWorkPolicy.REPLACE, new OneTimeWorkRequest.Builder((Class<? extends ListenableWorker>) TurnOffWorker.class).setInitialDelay(Settings.getPreferences().getInt("flutter.vpn_timeout", 7200), TimeUnit.SECONDS).addTag(WORK_MANAGER_TAG).build());
    }

    public final void close() {
        ServiceCompat.stopForeground(this.service, 1);
    }

    public final void show(String lastProfileName, int i) {
        Intrinsics.checkNotNullParameter(lastProfileName, "lastProfileName");
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) Application.notification$delegate.getValue();
            e0$$ExternalSyntheticApiModelOutline0.m$1();
            notificationManager.createNotificationChannel(B2$$ExternalSyntheticApiModelOutline0.m18443m());
        }
        Service service = this.service;
        NotificationCompat.Builder notificationBuilder = getNotificationBuilder();
        if (StringsKt.isBlank(lastProfileName)) {
            lastProfileName = null;
        }
        if (lastProfileName == null) {
            lastProfileName = "Ping VPN";
        }
        service.startForeground(1, notificationBuilder.setContentTitle(lastProfileName).setContentText(this.service.getString(i)).build());
        if (Settings.getPreferences().getBoolean("flutter.is_paid", false)) {
            return;
        }
        setupTurnOffTimer();
    }

    public final void start() {
    }

    public final void updateSpeed(long j, long j2) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatBytes(j2));
        sb.append("/s ↑\t");
        ((NotificationManager) Application.notificationManager$delegate.getValue()).notify(1, getNotificationBuilder().setContentText(ArraySet$$ExternalSyntheticOutline0.m62m(sb, formatBytes(j), "/s ↓")).build());
    }
}
