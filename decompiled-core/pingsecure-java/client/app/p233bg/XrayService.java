package com.pingsecure.client.app.p233bg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.VpnService;
import android.os.Build;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.work.WorkManager;
import com.applovin.impl.e0$$ExternalSyntheticApiModelOutline0;
import com.inmobi.media.B2$$ExternalSyntheticApiModelOutline0;
import com.pingsecure.client.app.Application;
import com.pingsecure.client.app.Settings;
import com.pingsecure.client.app.handlers.VPNStateStreamHandler;
import com.pingsecure.client.app.handlers.VPNStateStreamHandler$Companion$VpnStatus;
import com.pingsecure.client.app.p233bg.xray.Xray;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.RandomKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.SupervisorJobImpl;
import kotlinx.coroutines.android.HandlerContext;
import kotlinx.coroutines.internal.MainDispatcherLoader;
import kotlinx.coroutines.scheduling.DefaultScheduler;
import kotlinx.coroutines.sync.Mutex;
import kotlinx.coroutines.sync.MutexKt;
import okio.Okio;

/* loaded from: classes7.dex */
public final class XrayService {
    public static final Companion Companion = new Companion(null);
    private final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private volatile boolean audioFocusHeld;
    private AudioFocusRequest audioFocusRequest;
    private volatile boolean isStopping;
    private final Mutex lifecycleMutex;
    private final ServiceNotification notification;
    private final XrayService$receiver$1 receiver;
    private boolean receiverRegistered;
    private final VPNService service;
    private Job serviceJob;
    private CoroutineScope serviceScope;
    private Job statsJob;
    private Xray xray;

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void start() {
            Intent intent = (Intent) JobKt.runBlocking(EmptyCoroutineContext.INSTANCE, new XrayService$Companion$start$intent$1(null));
            Application application = Application.application;
            ContextCompat.startForegroundService(Okio.getApplication(), intent);
        }

        public final void stop() {
            Application application = Application.application;
            Okio.getApplication().sendBroadcast(new Intent("com.pingsecure.client.app.SERVICE_CLOSE").setPackage(Okio.getApplication().getPackageName()));
        }

        private Companion() {
        }
    }

    /* JADX WARN: Type inference failed for: r2v6, types: [com.pingsecure.client.app.bg.XrayService$receiver$1] */
    public XrayService(VPNService service) {
        Intrinsics.checkNotNullParameter(service, "service");
        this.service = service;
        this.notification = new ServiceNotification(service);
        this.audioFocusChangeListener = new XrayService$$ExternalSyntheticLambda3();
        SupervisorJobImpl SupervisorJob$default = JobKt.SupervisorJob$default();
        this.serviceJob = SupervisorJob$default;
        this.serviceScope = JobKt.CoroutineScope(Dispatchers.f53823IO.plus(SupervisorJob$default));
        this.lifecycleMutex = MutexKt.Mutex$default();
        this.receiver = new BroadcastReceiver() { // from class: com.pingsecure.client.app.bg.XrayService$receiver$1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Intrinsics.checkNotNullParameter(context, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                String action = intent.getAction();
                if (action != null && action.hashCode() == 1930353718 && action.equals("com.pingsecure.client.app.SERVICE_CLOSE")) {
                    XrayService.this.stopService();
                }
            }
        };
    }

    private final void abandonSilentAudioFocus() {
        if (this.audioFocusHeld) {
            Object systemService = this.service.getSystemService("audio");
            AudioManager audioManager = systemService instanceof AudioManager ? (AudioManager) systemService : null;
            if (audioManager == null) {
                return;
            }
            try {
                if (Build.VERSION.SDK_INT >= 26) {
                    AudioFocusRequest audioFocusRequest = this.audioFocusRequest;
                    if (audioFocusRequest != null) {
                        audioManager.abandonAudioFocusRequest(audioFocusRequest);
                    }
                    this.audioFocusRequest = null;
                } else {
                    audioManager.abandonAudioFocus(this.audioFocusChangeListener);
                }
            } catch (Throwable th) {
                try {
                    Log.w("XrayService", "abandonSilentAudioFocus failed: " + th.getMessage());
                } finally {
                    this.audioFocusHeld = false;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void audioFocusChangeListener$lambda$0(int i) {
    }

    private final void requestSilentAudioFocus() {
        int requestAudioFocus;
        String str;
        AudioFocusRequest.Builder audioAttributes;
        AudioFocusRequest.Builder onAudioFocusChangeListener;
        AudioFocusRequest.Builder willPauseWhenDucked;
        AudioFocusRequest.Builder acceptsDelayedFocusGain;
        AudioFocusRequest build;
        if (this.audioFocusHeld) {
            return;
        }
        Object systemService = this.service.getSystemService("audio");
        AudioManager audioManager = systemService instanceof AudioManager ? (AudioManager) systemService : null;
        if (audioManager == null) {
            return;
        }
        try {
            boolean z = true;
            if (Build.VERSION.SDK_INT >= 26) {
                AudioAttributes build2 = new AudioAttributes.Builder().setUsage(1).setContentType(2).build();
                e0$$ExternalSyntheticApiModelOutline0.m2347m();
                audioAttributes = B2$$ExternalSyntheticApiModelOutline0.m$1().setAudioAttributes(build2);
                onAudioFocusChangeListener = audioAttributes.setOnAudioFocusChangeListener(this.audioFocusChangeListener);
                willPauseWhenDucked = onAudioFocusChangeListener.setWillPauseWhenDucked(false);
                acceptsDelayedFocusGain = willPauseWhenDucked.setAcceptsDelayedFocusGain(true);
                build = acceptsDelayedFocusGain.build();
                this.audioFocusRequest = build;
                requestAudioFocus = audioManager.requestAudioFocus(build);
            } else {
                requestAudioFocus = audioManager.requestAudioFocus(this.audioFocusChangeListener, 3, 1);
            }
            if (requestAudioFocus != 1) {
                z = false;
            }
            this.audioFocusHeld = z;
            StringBuilder sb = new StringBuilder("audio focus request: ");
            if (this.audioFocusHeld) {
                str = "GRANTED";
            } else {
                str = "DENIED(" + requestAudioFocus + ')';
            }
            sb.append(str);
            Log.i("XrayService", sb.toString());
        } catch (Throwable th) {
            Log.w("XrayService", "requestSilentAudioFocus failed: " + th.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't wrap try/catch for region: R(11:0|1|(2:3|(8:5|6|7|(1:(1:(1:(2:12|13)(2:15|16))(4:17|18|19|20))(2:21|22))(5:37|38|39|(1:41)|33)|23|(3:25|(1:27)(1:34)|(1:29)(4:30|(2:32|33)|19|20))|35|36))|48|6|7|(0)(0)|23|(0)|35|36) */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0044, code lost:
    
        r13 = e;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0081 A[Catch: Exception -> 0x0044, TryCatch #1 {Exception -> 0x0044, blocks: (B:18:0x003f, B:19:0x00cc, B:22:0x004b, B:23:0x0073, B:25:0x0081, B:30:0x008c, B:35:0x00d5), top: B:7:0x0026 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0028  */
    /* JADX WARN: Type inference failed for: r2v0, types: [int] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object startService(Continuation continuation) {
        XrayService$startService$1 xrayService$startService$1;
        XrayService xrayService;
        XrayService xrayService2;
        String string;
        if (continuation instanceof XrayService$startService$1) {
            xrayService$startService$1 = (XrayService$startService$1) continuation;
            int i = xrayService$startService$1.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                xrayService$startService$1.label = i - Integer.MIN_VALUE;
                Object obj = xrayService$startService$1.result;
                CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
                xrayService = xrayService$startService$1.label;
                Unit unit = Unit.INSTANCE;
                if (xrayService != 0) {
                    ResultKt.throwOnFailure(obj);
                    try {
                        VPNStateStreamHandler.Companion.m55542getShared().sendEvent(VPNStateStreamHandler$Companion$VpnStatus.CONNECTING);
                        DefaultScheduler defaultScheduler = Dispatchers.Default;
                        HandlerContext handlerContext = MainDispatcherLoader.dispatcher;
                        XrayService$startService$2 xrayService$startService$2 = new XrayService$startService$2(this, null);
                        xrayService$startService$1.L$0 = this;
                        xrayService$startService$1.label = 1;
                        if (JobKt.withContext(xrayService$startService$2, handlerContext, xrayService$startService$1) != coroutineSingletons) {
                            xrayService2 = this;
                        }
                    } catch (Exception e) {
                        e = e;
                        xrayService = this;
                        Log.e("XrayService", "Failed to start VPN: " + e.getMessage(), e);
                        DefaultScheduler defaultScheduler2 = Dispatchers.Default;
                        HandlerContext handlerContext2 = MainDispatcherLoader.dispatcher;
                        XrayService$startService$5 xrayService$startService$5 = new XrayService$startService$5(xrayService, null);
                        xrayService$startService$1.L$0 = null;
                        xrayService$startService$1.label = 3;
                        return JobKt.withContext(xrayService$startService$5, handlerContext2, xrayService$startService$1) == coroutineSingletons ? coroutineSingletons : unit;
                    }
                }
                if (xrayService != 1) {
                    if (xrayService != 2) {
                        if (xrayService == 3) {
                            ResultKt.throwOnFailure(obj);
                        }
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    xrayService2 = (XrayService) xrayService$startService$1.L$0;
                    ResultKt.throwOnFailure(obj);
                    xrayService2.notification.start();
                    xrayService2.startStatsPolling();
                    return unit;
                }
                xrayService2 = (XrayService) xrayService$startService$1.L$0;
                ResultKt.throwOnFailure(obj);
                string = Settings.getPreferences().getString("flutter.config_path", "");
                if (string != null) {
                    if (StringsKt.isBlank(string)) {
                        string = null;
                    }
                    if (string != null) {
                        Xray companion = Xray.Companion.getInstance(xrayService2.service);
                        companion.init();
                        companion.start(string, new VpnService.Builder(xrayService2.service), new XrayService$startService$3(xrayService2.service));
                        xrayService2.xray = companion;
                        xrayService2.requestSilentAudioFocus();
                        VPNStateStreamHandler.Companion.m55542getShared().sendEvent(VPNStateStreamHandler$Companion$VpnStatus.CONNECTED);
                        DefaultScheduler defaultScheduler3 = Dispatchers.Default;
                        HandlerContext handlerContext3 = MainDispatcherLoader.dispatcher;
                        XrayService$startService$4 xrayService$startService$4 = new XrayService$startService$4(xrayService2, null);
                        xrayService$startService$1.L$0 = xrayService2;
                        xrayService$startService$1.label = 2;
                        if (JobKt.withContext(xrayService$startService$4, handlerContext3, xrayService$startService$1) == coroutineSingletons) {
                        }
                        xrayService2.notification.start();
                        xrayService2.startStatsPolling();
                        return unit;
                    }
                }
                Log.e("XrayService", "VPN config is null or empty");
                xrayService2.stopService();
                return unit;
            }
        }
        xrayService$startService$1 = new XrayService$startService$1(this, continuation);
        Object obj2 = xrayService$startService$1.result;
        CoroutineSingletons coroutineSingletons2 = CoroutineSingletons.COROUTINE_SUSPENDED;
        xrayService = xrayService$startService$1.label;
        Unit unit2 = Unit.INSTANCE;
        if (xrayService != 0) {
        }
        string = Settings.getPreferences().getString("flutter.config_path", "");
        if (string != null) {
        }
        Log.e("XrayService", "VPN config is null or empty");
        xrayService2.stopService();
        return unit2;
    }

    private final void startStatsPolling() {
        this.statsJob = JobKt.launch$default(this.serviceScope, null, null, new XrayService$startStatsPolling$1(this, null), 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void stopService() {
        synchronized (this) {
            if (this.isStopping) {
                return;
            }
            this.isStopping = true;
            if (this.receiverRegistered) {
                try {
                    this.service.unregisterReceiver(this.receiver);
                } catch (IllegalArgumentException e) {
                    Log.w("XrayService", "Receiver already unregistered", e);
                }
                this.receiverRegistered = false;
            }
            VPNStateStreamHandler.Companion.m55542getShared().sendEvent(VPNStateStreamHandler$Companion$VpnStatus.DISCONNECTING);
            WorkManager.Companion.getInstance(this.service).cancelUniqueWork("NOTIFICATION_WORK_MANAGER");
            stopStatsPolling();
            abandonSilentAudioFocus();
            this.notification.close();
            JobKt.launch$default(this.serviceScope, null, null, new XrayService$stopService$2(this, null), 3);
        }
    }

    private final void stopStatsPolling() {
        Job job = this.statsJob;
        if (job != null) {
            job.cancel(null);
        }
        this.statsJob = null;
    }

    public final void onRevoke$app_release() {
        stopService();
    }

    public final int onStartCommand$app_release() {
        this.isStopping = false;
        if (!this.serviceJob.isActive()) {
            SupervisorJobImpl SupervisorJob$default = JobKt.SupervisorJob$default();
            this.serviceJob = SupervisorJob$default;
            this.serviceScope = JobKt.CoroutineScope(Dispatchers.f53823IO.plus(SupervisorJob$default));
        }
        RandomKt.isRunning = true;
        if (!this.receiverRegistered) {
            VPNService vPNService = this.service;
            XrayService$receiver$1 xrayService$receiver$1 = this.receiver;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.pingsecure.client.app.SERVICE_CLOSE");
            ContextCompat.registerReceiver(vPNService, xrayService$receiver$1, intentFilter, 4);
            this.receiverRegistered = true;
        }
        JobKt.launch$default(this.serviceScope, null, null, new XrayService$onStartCommand$2(this, null), 3);
        return 2;
    }
}
