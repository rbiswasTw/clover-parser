package com.thoughtworks.enablement.coverage.report.formatter.html;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BodyTag extends Tag{

    private final List<Tag> tags = new LinkedList<>();

    public BodyTag() {
        super("body");
    }

    public BodyTag withChild(Tag tag){
        this.tags.add(tag);
        return this;
    }

    @Override
    public String generateTagContent() {
        return tags.stream().map(Tag::render).collect(Collectors.joining(""));
    }
}
