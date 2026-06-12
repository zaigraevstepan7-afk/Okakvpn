package com.pingsecure.client.app.p233bg.xray;

import android.content.Context;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.TrafficStats;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.util.Log;
import com.ironsource.sdk.controller.v$$ExternalSyntheticLambda3;
import com.p232my.target.a3$$ExternalSyntheticLambda0;
import com.p232my.target.g7$$ExternalSyntheticLambda1;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.p253io.FilesKt;
import kotlin.p253io.LinesSequence;
import kotlin.sequences.ConstrainedOnceSequence;
import kotlin.text.Charsets;

/* loaded from: classes7.dex */
public final class Tun2Socks {
    private final VpnConfig config;
    private final Context context;
    private volatile boolean isStarted;
    private final String passwordFileName;
    private volatile Process process;
    private volatile Thread sendFdThread;
    private final String sockName;
    private volatile Thread stdoutPumpThread;
    private final ParcelFileDescriptor tunFd;

    public Tun2Socks(Context context, ParcelFileDescriptor tunFd, VpnConfig config) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(tunFd, "tunFd");
        Intrinsics.checkNotNullParameter(config, "config");
        this.context = context;
        this.tunFd = tunFd;
        this.config = config;
        this.sockName = "sock_path";
        this.passwordFileName = "socks_pass";
    }

    private final List<String> buildArgs(String str) {
        return CollectionsKt__CollectionsKt.listOf((Object[]) new String[]{str, "--netif-ipaddr", "26.26.26.2", "--netif-netmask", "255.255.255.252", "--socks-server-addr", "127.0.0.1:" + this.config.getSocksPort(), "--username", this.config.getSocksUsername(), "--password-file", this.passwordFileName, "--tunmtu", String.valueOf(this.config.getMtu()), "--sock-path", this.sockName, "--socks5-udp", "--loglevel", "error"});
    }

    private final void sendTunFd(String str) {
        Exception exc = null;
        int i = 1;
        while (i < 51) {
            if (!this.isStarted) {
                return;
            }
            LocalSocket localSocket = new LocalSocket();
            try {
                localSocket.connect(new LocalSocketAddress(str, LocalSocketAddress.Namespace.FILESYSTEM));
                localSocket.setFileDescriptorsForSend(new FileDescriptor[]{this.tunFd.getFileDescriptor()});
                localSocket.getOutputStream().write(42);
                localSocket.getOutputStream().flush();
                Log.i("Tun2Socks", "TUN fd sent to tun2socks on attempt " + i);
                try {
                    localSocket.close();
                    return;
                } catch (Exception unused) {
                    return;
                }
            } catch (Exception e) {
                try {
                    localSocket.close();
                } catch (Exception unused2) {
                }
                try {
                    Thread.sleep(100L);
                    i++;
                    exc = e;
                } catch (InterruptedException unused3) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } catch (Throwable th) {
                try {
                    localSocket.close();
                } catch (Exception unused4) {
                }
                throw th;
            }
        }
        Log.e("Tun2Socks", "Failed to send TUN fd to tun2socks after retries", exc);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final CharSequence start$lambda$0(Tun2Socks tun2Socks, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.equals(tun2Socks.config.getSocksUsername()) ? "<user>" : it;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void start$lambda$3(Tun2Socks tun2Socks) {
        InputStream inputStream;
        try {
            Process process = tun2Socks.process;
            if (process == null || (inputStream = process.getInputStream()) == null) {
                return;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8), 8192);
            try {
                Iterator it = new ConstrainedOnceSequence(new LinesSequence(bufferedReader, 0)).iterator();
                while (it.hasNext()) {
                    Log.i("Tun2Socks", "[tun2socks] " + ((String) it.next()));
                }
                bufferedReader.close();
            } finally {
            }
        } catch (Exception e) {
            Log.w("Tun2Socks", "tun2socks stdout pump ended: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void start$lambda$5(Tun2Socks tun2Socks, File file) {
        String absolutePath = file.getAbsolutePath();
        Intrinsics.checkNotNullExpressionValue(absolutePath, "getAbsolutePath(...)");
        tun2Socks.sendTunFd(absolutePath);
    }

    public final Pair getStats() {
        int myUid = Process.myUid();
        long uidRxBytes = TrafficStats.getUidRxBytes(myUid);
        if (uidRxBytes < 0) {
            uidRxBytes = 0;
        }
        long uidTxBytes = TrafficStats.getUidTxBytes(myUid);
        return new Pair(Long.valueOf(uidRxBytes), Long.valueOf(uidTxBytes >= 0 ? uidTxBytes : 0L));
    }

    public final boolean isRunning() {
        Process process = this.process;
        if (process == null) {
            return false;
        }
        try {
            process.exitValue();
            return false;
        } catch (IllegalThreadStateException unused) {
            return true;
        }
    }

    public final void start() {
        Log.i("Tun2Socks", "Starting tun2socks subprocess");
        File file = new File(this.context.getApplicationInfo().nativeLibraryDir, "libtun2socks.so");
        if (!file.exists()) {
            throw new RuntimeException("libtun2socks.so not found at " + file.getAbsolutePath());
        }
        File file2 = new File(this.context.getFilesDir(), this.sockName);
        if (file2.exists()) {
            file2.delete();
        }
        File file3 = new File(this.context.getFilesDir(), this.passwordFileName);
        FilesKt.writeText(file3, this.config.getSocksPassword(), Charsets.UTF_8);
        try {
            file3.setReadable(false, false);
            file3.setReadable(true, true);
        } catch (Exception e) {
            Log.w("Tun2Socks", "Failed to tighten password file perms: " + e.getMessage());
        }
        String absolutePath = file.getAbsolutePath();
        Intrinsics.checkNotNullExpressionValue(absolutePath, "getAbsolutePath(...)");
        List<String> buildArgs = buildArgs(absolutePath);
        Log.i("Tun2Socks", "tun2socks args: " + CollectionsKt.joinToString$default(buildArgs, " ", null, null, new v$$ExternalSyntheticLambda3(this, 3), 30));
        this.process = new ProcessBuilder(buildArgs).directory(this.context.getFilesDir()).redirectErrorStream(true).start();
        Log.i("Tun2Socks", "tun2socks subprocess started");
        Thread thread = new Thread(new a3$$ExternalSyntheticLambda0(this, 28));
        thread.setName("tun2socks-stdout");
        thread.setDaemon(true);
        thread.start();
        this.stdoutPumpThread = thread;
        Thread thread2 = new Thread(new g7$$ExternalSyntheticLambda1(21, this, file2));
        thread2.setName("tun2socks-fd-sender");
        thread2.setDaemon(true);
        thread2.start();
        this.sendFdThread = thread2;
        this.isStarted = true;
    }

    public final void stop() {
        Log.i("Tun2Socks", "Stopping tun2socks subprocess");
        this.isStarted = false;
        try {
            Process process = this.process;
            if (process != null) {
                process.destroy();
            }
        } catch (Exception e) {
            Log.w("Tun2Socks", "Error destroying tun2socks process: " + e.getMessage());
        }
        this.process = null;
        try {
            Thread thread = this.sendFdThread;
            if (thread != null) {
                thread.interrupt();
            }
        } catch (Exception unused) {
        }
        this.sendFdThread = null;
        this.stdoutPumpThread = null;
        try {
            new File(this.context.getFilesDir(), this.sockName).delete();
        } catch (Exception unused2) {
        }
        try {
            new File(this.context.getFilesDir(), this.passwordFileName).delete();
        } catch (Exception unused3) {
        }
    }
}
