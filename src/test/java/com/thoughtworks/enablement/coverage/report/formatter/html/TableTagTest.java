package com.thoughtworks.enablement.coverage.report.formatter.html;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TableTagTest {

    @Test
    public void shouldGenerateTableWithRowAndColumnWithAttributes(){
        TableTag tableTag = new TableTag();
        TableRowTag rowTag = new TableRowTag();
        tableTag.withRow(rowTag);
        rowTag.withAttribute(new Attribute("rowAttrName1","rowAttrValue1"));
        rowTag.withAttribute(new Attribute("rowAttrName2","rowAttrValue2"));
        TableCellTag cell1 = new TableCellTag("Cell1");
        cell1.withAttribute(new Attribute("cell1AttrName1","cell1AttrValue1"));
        TableCellTag cell2 = new TableCellTag("Cell2");
        cell2.withAttribute(new Attribute("cell2AttrName1","cell2AttrValue1"));
        rowTag.withCell(cell1).withCell(cell2);
        rowTag.withCell(new TableCellTag(""));

        assertEquals(tableTag.render(), "<table ><tr rowAttrName1=\"rowAttrValue1\" rowAttrName2=\"rowAttrValue2\"><td cell1AttrName1=\"cell1AttrValue1\">Cell1</td><td cell2AttrName1=\"cell2AttrValue1\">Cell2</td><td /></tr></table>");
    }

}