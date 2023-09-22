package com.tbc.enablement.clover.report.data.tree;

import com.tbc.enablement.clover.report.data.Metrics;
import com.tbc.enablement.clover.report.data.SummaryReport;

import java.util.LinkedList;
import java.util.List;

public class CodePackageNode {

    private final Metrics metrics;
    private final List<CodePackageNode> children;
    private String name;
    private final SummaryReport summaryReport;

    public CodePackageNode(Metrics metrics, List<CodePackageNode> children, String name) {
        this.metrics = metrics;
        this.children = children;
        this.name = name;
        this.summaryReport = new SummaryReport(metrics.getStatements(), metrics.getCoveredStatements(), metrics.getConditionals(), metrics.getCoveredConditionals(), name);
    }

    public static class CodePackageNodeBuilder{
        private Metrics metrics;
        private List<CodePackageNode> children = new LinkedList<>();
        private String name;

        public void withMetrics(Metrics metrics){
            this.metrics = metrics;
        }

        public void withChild(CodePackageNode child){
            this.children.add(child);
        }

        public void withName(String name){
            this.name = name;
        }

        public CodePackageNode build(){
            return new CodePackageNode(metrics, children, name);
        }
    }

    public void compress(){
        if (this.children!=null && this.children.size() == 1){
            this.name = this.name + this.children.get(0).name;
            CodePackageNode child = this.children.get(0);
            this.children.clear();
            this.children.addAll(child.children);
        }else if (this.children == null || this.children.isEmpty()){

        }
    }


}
