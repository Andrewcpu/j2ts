package net.andrewcpu.j2ts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.andrewcpu.j2ts.model.EndpointDeclarations;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import static net.andrewcpu.j2ts.APIBuilder.generateAPI;

@Mojo(name = "scan", requiresDependencyResolution= ResolutionScope.COMPILE_PLUS_RUNTIME)
public class GenerateMojo extends AbstractMojo {

    @Parameter(property = "mainPackage", defaultValue = "", required = true)
    String mainPackage;

    @Parameter(property="typePrefix", defaultValue = "I")
    String typePrefix;
    @Parameter(property="moduleName", defaultValue = "api")
    String modulePrefix;

    @Parameter(property="proxyPath", defaultValue = "api/")
    String proxyPath;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter
    EndpointDeclarations endpointDeclarations;


    public void execute() throws MojoExecutionException, MojoFailureException {
        JTSTransformer.MODEL_IMPORT_NAME = modulePrefix;
        JTSTransformer.PREFIX = typePrefix;
        JTSTransformer.PROXY_URL_PREFIX = proxyPath;
        try {
            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(endpointDeclarations));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            List<String> classpathElements = project.getCompileClasspathElements();
            classpathElements.addAll(project.getRuntimeClasspathElements());
            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
            }

            URLClassLoader newClassLoader = new URLClassLoader(urls, getClass().getClassLoader());

            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forClassLoader(newClassLoader))
                    .setScanners(
                            Scanners.SubTypes,
                            Scanners.TypesAnnotated,
                            Scanners.MethodsAnnotated,
                            Scanners.FieldsAnnotated,
                            Scanners.Resources,
                            Scanners.MethodsParameter,
                            Scanners.MethodsSignature,
                            Scanners.MethodsReturn
                    ));

            try {
                generateAPI(reflections, newClassLoader, mainPackage, new File(project.getBuild().getDirectory()), endpointDeclarations);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (DependencyResolutionRequiredException | MalformedURLException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
