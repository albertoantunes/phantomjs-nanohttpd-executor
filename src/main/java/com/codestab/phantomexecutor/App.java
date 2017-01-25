package com.codestab.phantomexecutor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fi.iki.elonen.NanoHTTPD;
import org.openqa.selenium.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by alberto_martins on 20/01/2017.
 */
public class App extends NanoHTTPD {

    private static Gson gson;

    public App() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    public static void main(String[] args) {
        try {
            gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            new App();
        } catch (IOException e) {
            System.err.println("Couldn't start server:\n" + e);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            if (session.getUri().equals("/")) {
                return prepareForm();
            }
            else if (session.getUri().equals("/favicon.ico")) {
                return newFixedLengthResponse("");
            }
            else if (session.getUri().equals("/run")) {
                session.parseBody(new HashMap<>());
                String url = session.getParms().get("url");
                WebDriver driver = this.setupWebDriver();
                ScenarioResults results = executeScenario(driver, url);
                driver.close();

                return prepareResponse(results);
            }
            else {
                return prepareForm();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(gson.toJson(e));
        }
    }

    /**
     * Setup phantomJS driver
     * Phantomjs executable must be visible in the system path
     *
     * @return
     */
    private WebDriver setupWebDriver() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{"--web-security=no", "--ignore-ssl-errors=yes"});
        caps.setCapability("takesScreenshot", true);
        caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        WebDriver driver = new PhantomJSDriver(caps);
        driver.manage().window().setSize(new Dimension(1920, 1080));
        return driver;
    }

    private ScenarioResults executeScenario(WebDriver driver, String url) throws InterruptedException {
        ScenarioResults results = new ScenarioResults();
        results.setStartTime();

        //Scenario execution
        driver.navigate().to(url);

        results.setFinishTime();
        results.setDuration();
        Thread.sleep(500);
        results.setBase64Screenshot(((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64));

        return results;
    }

    private Response prepareResponse(ScenarioResults results) {
        StringBuilder msg = new StringBuilder();
        String data = gson.toJson(results);
        msg.append("<html><body>");
        msg.append("<h2>Host: " + results.getHostname() + "</h2>");
        msg.append("<pre>");
        msg.append(data);
        msg.append("</pre>");
        msg.append("<h2>Screenshot</h2>");
        msg.append("<img src=\"data:image/png);base64," + results.getBase64Screenshot() + "\"></img>");
        msg.append("</body></html>");
        return newFixedLengthResponse(msg.toString());
    }

    private Response prepareForm() {
        StringBuilder msg = new StringBuilder();
        msg.append("<html><body><form action=\"run\" method=\"post\" enctype=\"multipart/form-data\">");
        msg.append("<label for=\"url\">Url to test:  </label>");
        msg.append("<input id=\"url\" name=\"url\" type=\"text\" value=\"http://www.google.com\"></input><br/><br/>");
        msg.append("<input type=\"submit\" value=\"Run\"></input>");
        msg.append("</form></body></html>");
        return newFixedLengthResponse(msg.toString());
    }

    private Response errorResponse(String data) {
        String msg = "<html><body><pre>";
        msg += data;
        msg += "</pre></body></html>";
        return newFixedLengthResponse(msg);
    }

}
