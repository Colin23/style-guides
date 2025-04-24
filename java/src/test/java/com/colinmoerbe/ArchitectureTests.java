package com.colinmoerbe;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.util.List;

class ArchitectureTests {

    @Test
    void allClassesMustBeAnnotatedWithComponent() {
        JavaClasses allClasses = new ClassFileImporter(List.of(new ImportOption.DoNotIncludeTests()))
            .importPackages("com.colinmoerbe");

        ArchRule mustBeAnnotatedWithComponentRule = ArchRuleDefinition.classes()
            .that()
            .doNotHaveSimpleName("Application") // Use the actual spring boot entrypoint class name
            .should()
            .beAnnotatedWith(Component.class);

        mustBeAnnotatedWithComponentRule.check(allClasses);
    }

}
