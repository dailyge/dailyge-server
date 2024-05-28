package project.dailyge.app.common;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.snippet.Attributes;
import project.dailyge.app.core.task.dto.requesst.TaskRegisterRequest;

import java.util.List;

public interface SnippetUtils {
    String CONSTRAINT = "constraints";

    static Attributes.Attribute getAttribute(String field) {
        StringBuilder sb = new StringBuilder();
        ConstraintDescriptions constraints = new ConstraintDescriptions(TaskRegisterRequest.class);

        List<String> fieldConstraints = constraints.descriptionsForProperty(field);
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
