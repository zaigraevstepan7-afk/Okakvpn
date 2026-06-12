package com.pingsecure.client.app.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.regex.Pattern;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes7.dex */
public final class InetNetwork {
    public final InetAddress address;
    public final int mask;

    public InetNetwork(String str, int i) {
        InetAddress byName = InetAddress.getByName(str);
        Intrinsics.checkNotNullExpressionValue(byName, "getByName(...)");
        this.address = byName;
        this.mask = i;
        boolean z = byName instanceof Inet4Address;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof InetNetwork)) {
            return false;
        }
        InetNetwork inetNetwork = (InetNetwork) obj;
        return Intrinsics.areEqual(this.address, inetNetwork.address) && this.mask == inetNetwork.mask;
    }

    public final int hashCode() {
        return Integer.hashCode(this.mask) + (this.address.hashCode() * 31);
    }

    public final String toString() {
        String replaceAll;
        StringBuilder sb = new StringBuilder();
        InetAddress inetAddress = this.address;
        Intrinsics.checkNotNullParameter(inetAddress, "<this>");
        if (inetAddress instanceof Inet4Address) {
            replaceAll = ((Inet4Address) inetAddress).getHostAddress();
            Intrinsics.checkNotNull(replaceAll);
        } else {
            String hostAddress = inetAddress.getHostAddress();
            Intrinsics.checkNotNull(hostAddress);
            Pattern compile = Pattern.compile("((?:(?:^|:)0+\\b){2,}):?(?!\\S*\\b\\1:0+\\b)(\\S*)");
            Intrinsics.checkNotNullExpressionValue(compile, "compile(...)");
            replaceAll = compile.matcher(hostAddress).replaceAll("::$2");
            Intrinsics.checkNotNullExpressionValue(replaceAll, "replaceAll(...)");
        }
        sb.append(replaceAll);
        sb.append('/');
        sb.append(this.mask);
        return sb.toString();
    }
}
