package com.gaa;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.codeborne.selenide.Browsers.CHROME;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.switchTo;
import static org.openqa.selenium.net.PortProber.findFreePort;

public class ShadowDomIFrameTest {
    private int port;


    @Test
    public void clickButtonInsideIframeInShadowDom() throws IOException {
        port = findFreePort();
        new DevHttpServer().start(port);

        open("http://127.0.0.1:" + port + "/index.html?browser=" + CHROME + "&timeout=5000");
        SelenideElement iframe = $(Selectors.shadowCss("iframe", "#shadow-host"));

        switchTo().frame(iframe);
        $("#frameButton").click();
        switchTo().defaultContent();
    }
}
