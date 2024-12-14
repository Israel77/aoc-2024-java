package com.adventofcode.routes;

import java.util.Optional;

import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.ws.rs.FormParam;

public class RequestBean {
    @RestPath
    int dayNumber;
    @RestPath
    int partNumber;
    @FormParam("input")
    FileUpload inputPart;

    @RestQuery("day_14_start")
    Optional<Integer> day14IterationStart;
    @RestQuery("day_14_end")
    Optional<Integer> day14IterationEnd;

    @RestQuery("day_14_visualize")
    boolean day14Visualize;

    public boolean getDay14Visualize() {
        return day14Visualize;
    }

    public void setDay14Visualize(boolean day14Visualize) {
        this.day14Visualize = day14Visualize;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public FileUpload getInputPart() {
        return inputPart;
    }

    public void setInputPart(FileUpload inputPart) {
        this.inputPart = inputPart;
    }

    public Optional<Integer> getDay14IterationStart() {
        return day14IterationStart;
    }

    public void setDay14IterationStart(Optional<Integer> day14IterationStart) {
        this.day14IterationStart = day14IterationStart;
    }

    public Optional<Integer> getDay14IterationEnd() {
        return day14IterationEnd;
    }

    public void setDay14IterationEnd(Optional<Integer> day14IterationEnd) {
        this.day14IterationEnd = day14IterationEnd;
    }

}
