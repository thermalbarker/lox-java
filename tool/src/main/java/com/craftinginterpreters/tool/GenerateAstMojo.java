package com.craftinginterpreters.tool;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.Path;

@Mojo(name = "generate-ast", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateAstMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {
        List<Dependency> dependencies = project.getDependencies();
        long numDependencies = dependencies.stream().count();
        getLog().info("Number of dependencies: " + numDependencies);

        Build build = project.getBuild();
        String outputDir = build.getOutputDirectory();

        Path outputPath = Paths.get(outputDir).getParent();
        outputPath = outputPath.resolve("generated-sources");
        getLog().info("Output directory: " + outputPath.toString());

        try {
            Files.createDirectories(outputPath);
            GenerateAst.defineAst(outputPath.toString());
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }

        project.addCompileSourceRoot(outputPath.toString());
    }
}
