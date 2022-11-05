package com.aryzko.scrabble.scrabbledictionary;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.junit.ArchTest;

import java.util.stream.Stream;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.aryzko.scrabble.scrabbledictionary")
public class ArchitectureTest {

    private static final String[] COMMON_PACKAGES = {
            "",
            "java..",
            "javax..",
            "org.junit.jupiter..",
            "org.assertj..",
            "org.mockito..",
            "org.slf4j..",
            "org.springframework.."
    };

    @ArchTest
    static final ArchRule DOMAIN_SHOULD_BE_CLEAN_AND_ONLY_DEPEND_ON_DOMAIN_CLASSES =
            classes().that().resideInAPackage("..domain..")
                    .should().onlyDependOnClassesThat(resideInAnyPackage(commonPackagesAnd("..domain..")));

    private static String[] commonPackagesAnd(String... packages) {
        return Stream.of(COMMON_PACKAGES, packages)
                .flatMap(Stream::of)
                .toArray(String[]::new);
    }
}
