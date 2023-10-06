package com.thoughtworks.enablement.coverage.report.formatter.html;

public class HeaderTag extends Tag{


    private final String content;

    public HeaderTag(int size, String content) {
        super(String.format("h%d",size));
        this.content = content;
        if (size<=0 || size>=4){
            throw new IllegalArgumentException("Header size must be between 1 and 4");
        }
    }

    @Override
    public String generateTagContent() {
        return content;
    }
}
