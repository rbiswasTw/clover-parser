package com.thoughtworks.enablement.coverage.report.data;

public class Coverage {
    private final Project project;

    public Coverage(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public static class CoverageBuilder {
        private Project project;

        public void withProject(Project project){
            this.project = project;
        }

        public Coverage build(){
            return new Coverage(project);
        }
    }
}
