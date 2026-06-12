package com.pingsecure.client.app.p233bg.xray;

import androidx.fragment.app.Fragment$$ExternalSyntheticOutline0;
import androidx.room.Room$$ExternalSyntheticOutline0;
import com.pingsecure.client.app.utils.InetNetwork;
import java.net.InetAddress;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes7.dex */
public final class VpnConfig {
    public static final Companion Companion = new Companion(null);
    private static final InetNetwork DEFAULT_IPV4_ADDRESS = new InetNetwork("10.0.42.2", 30);
    private final Set<InetNetwork> addresses;
    private final Set<InetAddress> dnsServers;
    private final int mtu;
    private final Set<RouteEntry> routes;
    private final String socksPassword;
    private final int socksPort;
    private final String socksUsername;

    public static final class Builder {
        private int socksPort;
        private final Set<InetNetwork> addresses = new LinkedHashSet();
        private final Set<InetAddress> dnsServers = new LinkedHashSet();
        private final Set<RouteEntry> routes = new LinkedHashSet();
        private int mtu = 1500;
        private String socksUsername = "";
        private String socksPassword = "";

        public final Builder addAddress(InetNetwork addr) {
            Intrinsics.checkNotNullParameter(addr, "addr");
            this.addresses.add(addr);
            return this;
        }

        public final Builder addDnsServer(InetAddress dns) {
            Intrinsics.checkNotNullParameter(dns, "dns");
            this.dnsServers.add(dns);
            return this;
        }

        public final Builder addRoute(InetNetwork route) {
            Intrinsics.checkNotNullParameter(route, "route");
            this.routes.add(new RouteEntry(route, true));
            return this;
        }

        public final VpnConfig build() {
            return new VpnConfig(this.addresses, this.dnsServers, this.routes, this.mtu, this.socksPort, this.socksUsername, this.socksPassword);
        }

        public final Builder excludeRoute(InetNetwork route) {
            Intrinsics.checkNotNullParameter(route, "route");
            this.routes.add(new RouteEntry(route, false));
            return this;
        }

        public final Set<InetNetwork> getAddresses$app_release() {
            return this.addresses;
        }

        public final Set<InetAddress> getDnsServers$app_release() {
            return this.dnsServers;
        }

        public final int getMtu$app_release() {
            return this.mtu;
        }

        public final Set<RouteEntry> getRoutes$app_release() {
            return this.routes;
        }

        public final String getSocksPassword$app_release() {
            return this.socksPassword;
        }

        public final int getSocksPort$app_release() {
            return this.socksPort;
        }

        public final String getSocksUsername$app_release() {
            return this.socksUsername;
        }

        public final Builder setMtu(int i) {
            this.mtu = i;
            return this;
        }

        public final void setMtu$app_release(int i) {
            this.mtu = i;
        }

        public final Builder setSocksCredentials(String username, String password) {
            Intrinsics.checkNotNullParameter(username, "username");
            Intrinsics.checkNotNullParameter(password, "password");
            this.socksUsername = username;
            this.socksPassword = password;
            return this;
        }

        public final void setSocksPassword$app_release(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.socksPassword = str;
        }

        public final Builder setSocksPort(int i) {
            this.socksPort = i;
            return this;
        }

        public final void setSocksPort$app_release(int i) {
            this.socksPort = i;
        }

        public final void setSocksUsername$app_release(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.socksUsername = str;
        }
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final VpnConfig build(Function1 block) {
            Intrinsics.checkNotNullParameter(block, "block");
            Builder builder = new Builder();
            block.invoke(builder);
            return builder.build();
        }

        public final InetNetwork getDEFAULT_IPV4_ADDRESS() {
            return VpnConfig.DEFAULT_IPV4_ADDRESS;
        }

        private Companion() {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public VpnConfig(Set<InetNetwork> addresses, Set<? extends InetAddress> dnsServers, Set<RouteEntry> routes, int i, int i2, String socksUsername, String socksPassword) {
        Intrinsics.checkNotNullParameter(addresses, "addresses");
        Intrinsics.checkNotNullParameter(dnsServers, "dnsServers");
        Intrinsics.checkNotNullParameter(routes, "routes");
        Intrinsics.checkNotNullParameter(socksUsername, "socksUsername");
        Intrinsics.checkNotNullParameter(socksPassword, "socksPassword");
        this.addresses = addresses;
        this.dnsServers = dnsServers;
        this.routes = routes;
        this.mtu = i;
        this.socksPort = i2;
        this.socksUsername = socksUsername;
        this.socksPassword = socksPassword;
    }

    public static /* synthetic */ VpnConfig copy$default(VpnConfig vpnConfig, Set set, Set set2, Set set3, int i, int i2, String str, String str2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            set = vpnConfig.addresses;
        }
        if ((i3 & 2) != 0) {
            set2 = vpnConfig.dnsServers;
        }
        if ((i3 & 4) != 0) {
            set3 = vpnConfig.routes;
        }
        if ((i3 & 8) != 0) {
            i = vpnConfig.mtu;
        }
        if ((i3 & 16) != 0) {
            i2 = vpnConfig.socksPort;
        }
        if ((i3 & 32) != 0) {
            str = vpnConfig.socksUsername;
        }
        if ((i3 & 64) != 0) {
            str2 = vpnConfig.socksPassword;
        }
        String str3 = str;
        String str4 = str2;
        int i4 = i2;
        Set set4 = set3;
        return vpnConfig.copy(set, set2, set4, i, i4, str3, str4);
    }

    public final Set<InetNetwork> component1() {
        return this.addresses;
    }

    public final Set<InetAddress> component2() {
        return this.dnsServers;
    }

    public final Set<RouteEntry> component3() {
        return this.routes;
    }

    public final int component4() {
        return this.mtu;
    }

    public final int component5() {
        return this.socksPort;
    }

    public final String component6() {
        return this.socksUsername;
    }

    public final String component7() {
        return this.socksPassword;
    }

    public final VpnConfig copy(Set<InetNetwork> addresses, Set<? extends InetAddress> dnsServers, Set<RouteEntry> routes, int i, int i2, String socksUsername, String socksPassword) {
        Intrinsics.checkNotNullParameter(addresses, "addresses");
        Intrinsics.checkNotNullParameter(dnsServers, "dnsServers");
        Intrinsics.checkNotNullParameter(routes, "routes");
        Intrinsics.checkNotNullParameter(socksUsername, "socksUsername");
        Intrinsics.checkNotNullParameter(socksPassword, "socksPassword");
        return new VpnConfig(addresses, dnsServers, routes, i, i2, socksUsername, socksPassword);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VpnConfig)) {
            return false;
        }
        VpnConfig vpnConfig = (VpnConfig) obj;
        return Intrinsics.areEqual(this.addresses, vpnConfig.addresses) && Intrinsics.areEqual(this.dnsServers, vpnConfig.dnsServers) && Intrinsics.areEqual(this.routes, vpnConfig.routes) && this.mtu == vpnConfig.mtu && this.socksPort == vpnConfig.socksPort && Intrinsics.areEqual(this.socksUsername, vpnConfig.socksUsername) && Intrinsics.areEqual(this.socksPassword, vpnConfig.socksPassword);
    }

    public final Set<InetNetwork> getAddresses() {
        return this.addresses;
    }

    public final Set<InetAddress> getDnsServers() {
        return this.dnsServers;
    }

    public final int getMtu() {
        return this.mtu;
    }

    public final Set<RouteEntry> getRoutes() {
        return this.routes;
    }

    public final String getSocksPassword() {
        return this.socksPassword;
    }

    public final int getSocksPort() {
        return this.socksPort;
    }

    public final String getSocksUsername() {
        return this.socksUsername;
    }

    public int hashCode() {
        return this.socksPassword.hashCode() + Fragment$$ExternalSyntheticOutline0.m355m(Room$$ExternalSyntheticOutline0.m624m(this.socksPort, Room$$ExternalSyntheticOutline0.m624m(this.mtu, (this.routes.hashCode() + ((this.dnsServers.hashCode() + (this.addresses.hashCode() * 31)) * 31)) * 31, 31), 31), 31, this.socksUsername);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("VpnConfig(addresses=");
        sb.append(this.addresses);
        sb.append(", dnsServers=");
        sb.append(this.dnsServers);
        sb.append(", routes=");
        sb.append(this.routes);
        sb.append(", mtu=");
        sb.append(this.mtu);
        sb.append(", socksPort=");
        sb.append(this.socksPort);
        sb.append(", socksUsername=");
        sb.append(this.socksUsername);
        sb.append(", socksPassword=");
        return Fragment$$ExternalSyntheticOutline0.m357m(')', this.socksPassword, sb);
    }
}
