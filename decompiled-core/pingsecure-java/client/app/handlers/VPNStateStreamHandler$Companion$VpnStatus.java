package com.pingsecure.client.app.handlers;

import kotlin.random.RandomKt;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* loaded from: classes7.dex */
public final class VPNStateStreamHandler$Companion$VpnStatus {
    public static final /* synthetic */ VPNStateStreamHandler$Companion$VpnStatus[] $VALUES;
    public static final VPNStateStreamHandler$Companion$VpnStatus CONNECTED;
    public static final VPNStateStreamHandler$Companion$VpnStatus CONNECTING;
    public static final VPNStateStreamHandler$Companion$VpnStatus DISCONNECTED;
    public static final VPNStateStreamHandler$Companion$VpnStatus DISCONNECTING;
    public final String value;

    static {
        VPNStateStreamHandler$Companion$VpnStatus vPNStateStreamHandler$Companion$VpnStatus = new VPNStateStreamHandler$Companion$VpnStatus("CONNECTING", 0, "Connecting");
        CONNECTING = vPNStateStreamHandler$Companion$VpnStatus;
        VPNStateStreamHandler$Companion$VpnStatus vPNStateStreamHandler$Companion$VpnStatus2 = new VPNStateStreamHandler$Companion$VpnStatus("CONNECTED", 1, "Connected");
        CONNECTED = vPNStateStreamHandler$Companion$VpnStatus2;
        VPNStateStreamHandler$Companion$VpnStatus vPNStateStreamHandler$Companion$VpnStatus3 = new VPNStateStreamHandler$Companion$VpnStatus("DISCONNECTING", 2, "Disconnecting");
        DISCONNECTING = vPNStateStreamHandler$Companion$VpnStatus3;
        VPNStateStreamHandler$Companion$VpnStatus vPNStateStreamHandler$Companion$VpnStatus4 = new VPNStateStreamHandler$Companion$VpnStatus("DISCONNECTED", 3, "Disconnected");
        DISCONNECTED = vPNStateStreamHandler$Companion$VpnStatus4;
        VPNStateStreamHandler$Companion$VpnStatus[] vPNStateStreamHandler$Companion$VpnStatusArr = {vPNStateStreamHandler$Companion$VpnStatus, vPNStateStreamHandler$Companion$VpnStatus2, vPNStateStreamHandler$Companion$VpnStatus3, vPNStateStreamHandler$Companion$VpnStatus4};
        $VALUES = vPNStateStreamHandler$Companion$VpnStatusArr;
        RandomKt.enumEntries(vPNStateStreamHandler$Companion$VpnStatusArr);
    }

    public VPNStateStreamHandler$Companion$VpnStatus(String str, int i, String str2) {
        this.value = str2;
    }

    public static VPNStateStreamHandler$Companion$VpnStatus valueOf(String str) {
        return (VPNStateStreamHandler$Companion$VpnStatus) Enum.valueOf(VPNStateStreamHandler$Companion$VpnStatus.class, str);
    }

    public static VPNStateStreamHandler$Companion$VpnStatus[] values() {
        return (VPNStateStreamHandler$Companion$VpnStatus[]) $VALUES.clone();
    }
}
