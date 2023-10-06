package com.thoughtworks.enablement.coverage.report.formatter.html;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TableRowTag extends Tag{

    private final List<TableCellTag> cells = new LinkedList<>();

    public TableRowTag() {
        super("tr");
    }

    public TableRowTag withCell(TableCellTag cell){
        this.cells.add(cell);
        return this;
    }

    @Override
    public String generateTagContent() {
        return cells.stream().map(Tag::render).collect(Collectors.joining(""));
    }
}
