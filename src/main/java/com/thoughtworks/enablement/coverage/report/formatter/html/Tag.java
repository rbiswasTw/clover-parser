package com.thoughtworks.enablement.coverage.report.formatter.html;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Tag implements Element{

    private final String tagName;
    private final List<Attribute> attributes = new LinkedList<>();


    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public void withAttribute(Attribute attribute){
        this.attributes.add(attribute);
    }

    public abstract String generateTagContent();


    @Override
    public String render() {
        StringBuilder stringBuilder = new StringBuilder();
        String tagContent = generateTagContent();
        stringBuilder.append(String.format("<%s ", tagName));
        stringBuilder.append(
                attributes.stream().map(Attribute::render).collect(Collectors.joining(" "))
        );
        if (tagContent==null || tagContent.trim().isEmpty()){
            stringBuilder.append("/>");
        }else{
            stringBuilder.append(">");
            stringBuilder.append(tagContent);
            stringBuilder.append(String.format("</%s>",tagName));
        }

        return stringBuilder.toString();
    }
}
