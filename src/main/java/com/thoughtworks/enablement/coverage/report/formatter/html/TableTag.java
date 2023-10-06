package com.thoughtworks.enablement.coverage.report.formatter.html;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TableTag extends Tag{

    private final List<TableRowTag> rows = new LinkedList<>();

    public TableTag() {
        super("table");
    }

    public TableTag withRow(TableRowTag row){
        this.rows.add(row);
        return this;
    }

    @Override
    public String generateTagContent() {
        return rows.stream().map(TableRowTag::render).collect(Collectors.joining(""));
    }
}
