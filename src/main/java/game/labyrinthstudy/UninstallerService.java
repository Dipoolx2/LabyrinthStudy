package game.labyrinthstudy;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UninstallerService {

    private final static String PROGRAM_NAME = "MazeStudy";

    public static void startUninstaller() {
        try {
            String productCode = findProductCode();
            if (productCode != null) {
                runUninstaller(productCode);
            } else {
                giveErrorAlert("Error finding product code");
                System.err.println("Could not find product code for " + PROGRAM_NAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String findProductCode() throws IOException {
        // Run Windows Installer command to list all installed products
        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", "wmic product get name,identifyingnumber");
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;
        Pattern pattern = Pattern.compile("\\{(.*?)\\}.*?" + PROGRAM_NAME);

        while ((line = reader.readLine()) != null) {
            if (line.contains(PROGRAM_NAME)) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    return "{" + matcher.group(1) + "}";
                }
            }
        }

        return null;
    }

    private static void runUninstaller(String productCode) throws IOException {
        System.out.println("Uninstalling " + PROGRAM_NAME + " with product code: " + productCode);

        // Using MsiExec.exe to uninstall silently
        List<String> command = new ArrayList<>();
        command.add("msiexec.exe");
        command.add("/x");
        command.add(productCode);  // Pass as separate argument to avoid escaping issues

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.start();

        System.out.println("Uninstaller started. Application will now exit.");

        // Give the uninstaller a moment to start before exiting
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Exit the application
        System.exit(0);
    }

    private static void giveErrorAlert(String reason) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error running uninstaller");
        alert.setHeaderText(null);
        alert.setContentText(reason+". Please uninstall manually (Settings -> Apps & Features -> MazeStudy -> Uninstall on windows).");
        alert.show();
    }

}
