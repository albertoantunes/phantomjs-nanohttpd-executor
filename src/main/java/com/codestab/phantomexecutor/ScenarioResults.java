package com.codestab.phantomexecutor;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by alberto_martins on 23/01/2017.
 */
public class ScenarioResults {

    private Instant startTime;
    private Instant finishTime;
    private String base64Screenshot;
    private long duration;
    private String hostname;

    public ScenarioResults() {
        this.hostname = System.getenv("HOSTNAME");
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime() {
        this.startTime = Instant.now();
    }

    public Instant getFinishTime() {
        return finishTime;
    }

    public void setFinishTime() {
        this.finishTime = Instant.now();
    }

    public String getBase64Screenshot() {
        return base64Screenshot;
    }

    public void setBase64Screenshot(String base64Screenshot) {
        this.base64Screenshot = base64Screenshot;
    }

    public Duration getTimeElapsed() {
        return Duration.between(this.getStartTime(), this.getFinishTime());
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration() {
        this.duration = getTimeElapsed().toMillis();
    }

    public String getHostname() {
        return hostname;
    }
}
