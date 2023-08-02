package dev.resteasy.example.grpc.greet;

import java.io.File;
import java.nio.file.Paths;

public class SetupProject {

    public static void main(String[] args) throws Exception {
        String workDir = Paths.get("")
                .toAbsolutePath()
                .toString();
        System.out.println(workDir);

        new File(workDir + "/src/main/test").mkdirs();

    }
}
