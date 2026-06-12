    .line 326
    :cond_a
    invoke-virtual {p1}, Lcom/pingsecure/client/app/bg/xray/VpnConfig;->getMtu()I

    .line 327
    .line 328
    .line 329
    move-result p1

    # stability patch: clamp TUN MTU to <= 1400 to avoid fragmentation/blackhole on cellular/PPPoE
    const/16 v0, 0x578

    if-le p1, v0, :pv_mtu_ok

    const/16 p1, 0x578

    :pv_mtu_ok
    .line 330
    invoke-virtual {p2, p1}, Landroid/net/VpnService$Builder;->setMtu(I)Landroid/net/VpnService$Builder;

    .line 331
    .line 332
    .line 333
    invoke-virtual {p2, v1}, Landroid/net/VpnService$Builder;->setBlocking(Z)Landroid/net/VpnService$Builder;
