package com.pingsecure.client.app.p233bg.xray;

import androidx.room.Room$$ExternalSyntheticOutline0;
import com.pingsecure.client.app.utils.InetNetwork;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes7.dex */
public final class RouteEntry {
    private final boolean include;
    private final InetNetwork inetNetwork;

    public RouteEntry(InetNetwork inetNetwork, boolean z) {
        Intrinsics.checkNotNullParameter(inetNetwork, "inetNetwork");
        this.inetNetwork = inetNetwork;
        this.include = z;
    }

    public static /* synthetic */ RouteEntry copy$default(RouteEntry routeEntry, InetNetwork inetNetwork, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            inetNetwork = routeEntry.inetNetwork;
        }
        if ((i & 2) != 0) {
            z = routeEntry.include;
        }
        return routeEntry.copy(inetNetwork, z);
    }

    public final InetNetwork component1() {
        return this.inetNetwork;
    }

    public final boolean component2() {
        return this.include;
    }

    public final RouteEntry copy(InetNetwork inetNetwork, boolean z) {
        Intrinsics.checkNotNullParameter(inetNetwork, "inetNetwork");
        return new RouteEntry(inetNetwork, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RouteEntry)) {
            return false;
        }
        RouteEntry routeEntry = (RouteEntry) obj;
        return Intrinsics.areEqual(this.inetNetwork, routeEntry.inetNetwork) && this.include == routeEntry.include;
    }

    public final boolean getInclude() {
        return this.include;
    }

    public final InetNetwork getInetNetwork() {
        return this.inetNetwork;
    }

    public int hashCode() {
        return Boolean.hashCode(this.include) + (this.inetNetwork.hashCode() * 31);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("RouteEntry(inetNetwork=");
        sb.append(this.inetNetwork);
        sb.append(", include=");
        return Room$$ExternalSyntheticOutline0.m640m(sb, this.include, ')');
    }
}
