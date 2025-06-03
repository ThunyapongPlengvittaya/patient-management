package com.pm.stack;


import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.rds.*;

public class LocalStack extends Stack {
    private final Vpc vpc;
    public LocalStack(final App scope, final String id, final StackProps props){
        super(scope, id, props);

        this.vpc = createVpc();

        DatabaseInstance authServiceDb = createDatabase("AuthServiceDB", "auth-service-db");
        DatabaseInstance patientServiceDb = createDatabase("PatientServiceDB", "patient-service-db");

    }

    private Vpc createVpc() {
        return Vpc.Builder
                .create(this, "PatientManagementVPC") //this stack scope for vpc
                .vpcName("PatientManagementVPC") //name
                .maxAzs(2) //max avaliable zones for aw
                .build();
    }

    // database set up
    private DatabaseInstance createDatabase(String id, String dbName) {
        return DatabaseInstance.Builder.create(this, id) //create instance ใช้บ่อย cdk
                .engine(DatabaseInstanceEngine.postgres(
                        PostgresInstanceEngineProps.builder()
                                .version(PostgresEngineVersion.VER_17_2)
                                .build()))
                .vpc(vpc)
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
                .allocatedStorage(20)
                .credentials(Credentials.fromGeneratedSecret("admin_user"))
                .databaseName(dbName)
                .removalPolicy(RemovalPolicy.DESTROY) //พอ instance ลบแล้วก็ลบ database ด้วย
                .build();
    }

    public static void main(final String[] args) {
        //create cdk app เพื่อให้เวลา stack ทำงานก็จะสร้าง cloud formation template (อันนี้ ./cdk.out) ขึ้ยมา
        App app = new App(AppProps.builder().outdir("./cdk.out").build());

        // synthesizer คือจะแปลง java code เป็น cloud formation template
        StackProps props = StackProps.builder()
                .synthesizer(new BootstraplessSynthesizer())
                .build();

        new LocalStack(app, "localstack", props);
        app.synth();
        System.out.println("App synthesizing in progress... ");
    }
}
