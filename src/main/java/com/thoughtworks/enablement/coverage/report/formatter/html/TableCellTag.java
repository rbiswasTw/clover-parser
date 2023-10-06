package com.thoughtworks.enablement.coverage.report.formatter.html;

public class TableCellTag extends Tag{

    private final String content;

    public TableCellTag( String content) {
        super("td");
        this.content = content;
    }

    @Override
    public String generateTagContent() {
        return content;
    }
}
