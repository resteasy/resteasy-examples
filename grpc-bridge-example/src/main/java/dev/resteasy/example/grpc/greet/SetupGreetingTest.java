package dev.resteasy.example.grpc.greet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class SetupGreetingTest {

    public static void main(String[] args) throws Exception {
        // Move test class file to correct position.
        String workDir = Paths.get("")
                .toAbsolutePath()
                .toString();

        moveTestFile(workDir);
        addDepsToPomXml(workDir);
    }

    static final String pomDeps = " <dependency>\n" +
            "            <groupId>io.grpc</groupId>\n" +
            "            <artifactId>grpc-netty-shaded</artifactId>\n" +
            "            <version>${io.grpc.version}</version>\n" +
            "        </dependency>\n" +
            "        <dependency>\n" +
            "            <groupId>junit</groupId>\n" +
            "            <artifactId>junit</artifactId>\n" +
            "            <version>4.13.2</version>\n" +
            "        </dependency>\n" +
            "        <dependency>\n" +
            "            <groupId>org.jboss.resteasy</groupId>\n" +
            "            <artifactId>resteasy-client</artifactId>\n" +
            "            <version>6.2.3.Final</version>\n" +
            "        </dependency>\n" +
            "        <dependency>\n" +
            "            <groupId>org.wildfly.core</groupId>\n" +
            "            <artifactId>wildfly-controller-client</artifactId>\n" +
            "            <version>19.0.0.Beta18</version>\n" +
            "        </dependency>";

    static final String endOfDepSection = "</dependencies>";

    public static void addDepsToPomXml(String workDir) {
        List<String> lines = new ArrayList<>();
        String line = null;

        try {
            File pomXml = new File(workDir + "/pom.xml");
            FileReader pomXmlReader = new FileReader(pomXml);
            BufferedReader bufferedPomXmlReader = new BufferedReader(pomXmlReader);

            while ((line = bufferedPomXmlReader.readLine()) != null) {
                if (line.contains(endOfDepSection))
                    line = line.replace(endOfDepSection, pomDeps + "\r\n" + endOfDepSection);
                lines.add(line + "\r\n");
            }

            pomXmlReader.close();
            bufferedPomXmlReader.close();

            FileWriter fw = new FileWriter(pomXml);
            BufferedWriter out = new BufferedWriter(fw);
            for (String s : lines)
                out.write(s);
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void moveTestFile(String workDir) throws Exception {
        String testFileDir = workDir + "/src/test/java";
        new File(testFileDir).mkdirs();

        String testFilePath = workDir + "/src/main/resources/GreetingTest";

        Files.copy(Paths.get(testFilePath), Paths.get(testFileDir + "/GreetingTest.java"), StandardCopyOption.REPLACE_EXISTING);

    }

}
