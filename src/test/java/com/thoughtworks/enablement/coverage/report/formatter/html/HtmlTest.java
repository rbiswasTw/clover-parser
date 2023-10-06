package com.thoughtworks.enablement.coverage.report.formatter.html;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class HtmlTest {

    @Test
    public void shouldGenerateHtmlWithBodyAndStyle(){
        Html html = new Html(new BodyTag().withChild(new HeaderTag(1, "head")).withChild(new TableTag()), "style1");
        assertEquals(html.render(), "<html><head><style>style1</style></head><body ><h1 >head</h1><table /></body></html>");
    }

    @Test
    public void shouldGenerateHtmlWithBodyAndNoStyle(){
        Html html = new Html(new BodyTag().withChild(new TableTag()), null);
        assertEquals(html.render(), "<html><head><style></style></head><body ><table /></body></html>");
    }

}