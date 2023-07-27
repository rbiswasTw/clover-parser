package com.tbc.enablement.clover.report.data;

public class CodePackage {
    private final Metrics metrics;
    private final String name;

    public CodePackage(Metrics metrics, String name) {
        this.metrics = metrics;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public static class CodePackageBuilder{
        private Metrics metrics;
        private String name;

        public void withMetrics(Metrics metrics){
            this.metrics = metrics;
        }

        public void withName(String name){
            this.name = name;
        }

        public CodePackage build(){
            return new CodePackage(metrics, name);
        }
    }
}


