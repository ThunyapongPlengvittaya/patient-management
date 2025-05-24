package com.pm.billingservice.grpc;

import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    //streamObserver คือ รับส่งข้อมูลแบบ realtime
    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest ,
                                     StreamObserver<billing.BillingResponse> responseStreamObserver) {

    }
}
