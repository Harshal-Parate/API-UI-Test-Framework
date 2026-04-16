package org.example.drivermanager;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v139.network.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DevToolsManager {

    private static DevTools devTools;
    private static List<String> networkLogs = new ArrayList<>();

    public static void initDevTools() {

        if (!(DriverFactory.getDriver() instanceof HasDevTools)) {
            return;
        }

        devTools = ((HasDevTools) DriverFactory.getDriver()).getDevTools();
        devTools.createSession();

        devTools.send(Network.enable(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));

        devTools.addListener(Network.requestWillBeSent(), request ->
                networkLogs.add("REQUEST: " +
                        request.getRequest().getMethod() + " " +
                        request.getRequest().getUrl())
        );

        devTools.addListener(Network.responseReceived(), response ->
                networkLogs.add("RESPONSE: " +
                        response.getResponse().getStatus() + " " +
                        response.getResponse().getUrl())
        );
    }

    public static String getLogs() {
        return String.join("\n", networkLogs);
    }

    public static void clearLogs() {
        networkLogs.clear();
    }
}