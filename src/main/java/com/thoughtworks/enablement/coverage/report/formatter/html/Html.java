package com.thoughtworks.enablement.coverage.report.formatter.html;

public class Html {

    private final BodyTag bodyTag;
    private final String style;


    public Html(BodyTag bodyTag, String style) {
        this.bodyTag = bodyTag;
        this.style = style == null ? "" : style;
    }

    public String render(){
        return String.format("<html><head><style>%s</style></head>%s</html>", style,bodyTag.render());
    }
}
