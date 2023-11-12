package com.aryzko.scrabblegame;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.stream.Stream;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.aryzko.scrabblegame")
public class ArchitectureTest {

    private static final String[] STANDARD_PACKAGES = {
            "",
            "java..",
            "javax..",
            "lombok..",
            "org.junit.jupiter..",
            "org.assertj..",
            "org.mockito..",
            "org.slf4j..",
            "org.springframework..",
            "org.aspectj..",
            "com.fasterxml.jackson.."
    };

    @ArchTest
    static final ArchRule DOMAIN_ONLY_DEPEND_ON_DOMAIN_CLASSES =
            classes().that().resideInAPackage("..domain..")
                    .should().onlyDependOnClassesThat(resideInAnyPackage(standardPackagesWith("..domain..")));

    @ArchTest
    static final ArchRule COMPONENTS_SHOULD_HAVE_ONLY_FINAL_FIELDS =
            classes().that().areAnnotatedWith(Component.class).and().areNotAnnotatedWith(ConfigurationProperties.class)
                    .and().resideOutsideOfPackage("..configuration..")
                    .should().haveOnlyFinalFields();

    @ArchTest
    static final ArchRule SERVICES_SHOULD_HAVE_ONLY_FINAL_FIELDS =
            classes().that().areAnnotatedWith(Service.class)
                    .and().resideOutsideOfPackage("..configuration..")
                    .should().haveOnlyFinalFields();

    @ArchTest
    static final ArchRule REST_CONTROLLERS_SHOULD_HAVE_ONLY_FINAL_FIELDS =
            classes().that().areAnnotatedWith(RestController.class)
                    .should().haveOnlyFinalFields();

    @ArchTest
    static final ArchRule REST_CONTROLLER_SHOULD_NOT_EXPOSE_DOMAIN_OBJECTS =
            methods().that().arePublic().and().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
                    .should().notHaveRawReturnType(resideInAnyPackage("..domain.."));

    @ArchTest
    static final ArchRule DATE_CLASS_SHOULD_NOT_BE_USED_IN_DOMAIN =
            noClasses().that().resideInAnyPackage("..domain..")
                    .should().accessClassesThat().belongToAnyOf(Date.class);

    private static String[] standardPackagesWith(String... packages) {
        return Stream.of(STANDARD_PACKAGES, packages)
                .flatMap(Stream::of)
                .toArray(String[]::new);
    }
}
