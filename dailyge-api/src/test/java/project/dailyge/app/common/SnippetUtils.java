package project.dailyge.app.common;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.snippet.Attributes;
import project.dailyge.app.core.task.presentation.requesst.TaskCreateRequest;

import java.util.List;

public interface SnippetUtils {
    String CONSTRAINT = "constraints";

    static Attributes.Attribute getAttribute(final String field) {
        final StringBuilder sb = new StringBuilder();
        final ConstraintDescriptions constraints = new ConstraintDescriptions(TaskCreateRequest.class);

        final List<String> fieldConstraints = constraints.descriptionsForProperty(field);
        for (String constraint : fieldConstraints) {
            sb.append("-")
                .append(" ")
                .append(constraint)
                .append(".")
                .append("\n")
                .append("\n");
        }
        return Attributes.key(CONSTRAINT)
            .value(sb.toString());
    }

    static Attributes.Attribute getAttribute(
        final Class<?> clazz,
        final String field
    ) {
        final StringBuilder sb = new StringBuilder();
        final ConstraintDescriptions constraints = new ConstraintDescriptions(clazz);

        final List<String> fieldConstraints = constraints.descriptionsForProperty(field);
        for (String constraint : fieldConstraints) {
            sb.append("-")
                .append(" ")
                .append(constraint)
                .append(".")
                .append("\n")
                .append("\n");
        }
        return Attributes.key(CONSTRAINT)
            .value(sb.toString());
    }
}
