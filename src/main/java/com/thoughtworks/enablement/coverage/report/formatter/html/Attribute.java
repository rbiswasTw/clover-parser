package com.thoughtworks.enablement.coverage.report.formatter.html;

public class Attribute implements Element{

    private final String name;
    private final String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String render() {
        return String.format("%s=\"%s\"", name, value);
    }
}
