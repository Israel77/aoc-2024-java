package com.adventofcode.solutions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adventofcode.solutions.Day14.Robot;

public enum Day14Cache {
    INSTANCE;

    private final Map<Integer, List<Robot>> cache;

    private Day14Cache() {
        cache = new HashMap<>();
    }

    public Map<Integer, List<Robot>> get() {
        return cache;
    }
}
