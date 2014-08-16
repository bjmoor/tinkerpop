package com.tinkerpop.gremlin.structure;

import com.tinkerpop.gremlin.process.computer.GraphComputer;
import com.tinkerpop.gremlin.process.computer.GraphComputerTest;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * A set of tests that ensure that the {@link ExceptionConsistencyTest} covers all defined TinkerPop exceptions.
 *
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class ExceptionCoverageTest {

    @Test
    public void shouldCoverAllExceptionsInTests() {

        // these are the classes that have Exceptions that need to be checked.
        final Class[] exceptionDefinitionClasses = {
                Edge.Exceptions.class,
                Element.Exceptions.class,
                Graph.Exceptions.class,
                GraphComputer.Exceptions.class,
                Graph.Variables.Exceptions.class,
                Property.Exceptions.class,
                Transaction.Exceptions.class,
                Vertex.Exceptions.class
        };

        // this is a list of exceptions that are "excused" from the coverage tests.  in most cases there should only
        // be exceptions ignored if they are "base" exception methods that are used to compose other exceptions,
        // like the first set listed (and labelled as such) below in the list assignments.
        final Set<String> ignore = new HashSet<String>() {{
            // these exceptions is not used directly...they are called by other exception methods.
            add("com.tinkerpop.gremlin.structure.Property$Exceptions#propertyKeyIsReserved");
            add("com.tinkerpop.gremlin.structure.Graph$Variables$Exceptions#memoryKeyIsReserved");

            // this is a general exception to be used as needed.  it is not explicitly tested:
            add("com.tinkerpop.gremlin.structure.Graph$Exceptions#argumentCanNotBeNull");

            //add("com.tinkerpop.gremlin.structure.Graph$Exceptions#onlyOneOrNoGraphComputerClass");
            //add("com.tinkerpop.gremlin.structure.Graph$Exceptions#graphDoesNotSupportProvidedGraphComputer");

            // todo: need to write consistency tests for the following items still...........
            add("com.tinkerpop.gremlin.process.computer.GraphComputer$Exceptions#adjacentElementPropertiesCanNotBeRead");
            add("com.tinkerpop.gremlin.process.computer.GraphComputer$Exceptions#adjacentElementPropertiesCanNotBeWritten");
            add("com.tinkerpop.gremlin.process.computer.GraphComputer$Exceptions#constantComputeKeyHasAlreadyBeenSet");
            add("com.tinkerpop.gremlin.process.computer.GraphComputer$Exceptions#adjacentVerticesCanNotBeQueried");
            add("com.tinkerpop.gremlin.process.computer.GraphComputer$Exceptions#isolationNotSupported");
            //add("com.tinkerpop.gremlin.process.computer.GraphComputer$Exceptions#computerHasAlreadyBeenSubmittedAVertexProgram");

            add("com.tinkerpop.gremlin.structure.Graph$Exceptions#vertexAdditionsNotSupported");
            add("com.tinkerpop.gremlin.structure.Element$Exceptions#propertyAdditionNotSupported");
            add("com.tinkerpop.gremlin.structure.Vertex$Exceptions#edgeAdditionsNotSupported");

            add("com.tinkerpop.gremlin.structure.Graph$Exceptions#vertexLookupsNotSupported");
            add("com.tinkerpop.gremlin.structure.Graph$Exceptions#edgeLookupsNotSupported");

            add("com.tinkerpop.gremlin.structure.Element$Exceptions#propertyRemovalNotSupported");
            add("com.tinkerpop.gremlin.structure.Vertex$Exceptions#vertexRemovalNotSupported");
            add("com.tinkerpop.gremlin.structure.Edge$Exceptions#edgeRemovalNotSupported");

        }};

        // register test classes here that contain @ExceptionCoverage annotations
        final List<Class<?>> testClassesThatContainConsistencyChecks = new ArrayList<>();
        testClassesThatContainConsistencyChecks.add(EdgeTest.class);
        testClassesThatContainConsistencyChecks.add(GraphTest.class);
        testClassesThatContainConsistencyChecks.add(GraphComputerTest.class);
        testClassesThatContainConsistencyChecks.addAll(Arrays.asList(ExceptionConsistencyTest.class.getDeclaredClasses()));
        testClassesThatContainConsistencyChecks.addAll(Arrays.asList(FeatureSupportTest.class.getDeclaredClasses()));
        testClassesThatContainConsistencyChecks.addAll(Arrays.asList(PropertyTest.class.getDeclaredClasses()));
        testClassesThatContainConsistencyChecks.addAll(Arrays.asList(VariablesTest.class.getDeclaredClasses()));
        testClassesThatContainConsistencyChecks.add(TransactionTest.class);
        testClassesThatContainConsistencyChecks.add(VertexTest.class);

        // implemented exceptions are the classes that potentially contains exception consistency checks.
        final Set<String> implementedExceptions = testClassesThatContainConsistencyChecks.stream()
                .<ExceptionCoverage>flatMap(c -> Stream.of(c.getAnnotationsByType(ExceptionCoverage.class)))
                .flatMap(ec -> Stream.of(ec.methods()).map(m -> String.format("%s#%s", ec.exceptionClass().getName(), m)))
                .collect(Collectors.<String>toSet());

        // evaluate each of the exceptions and find the list of exception methods...assert them against
        // the list of implementedExceptions to make sure they are covered.
        Stream.of(exceptionDefinitionClasses).flatMap(c -> Stream.of(c.getDeclaredMethods()).map((Method m) -> String.format("%s#%s", c.getName(), m.getName())))
                .filter(s -> !ignore.contains(s))
                .forEach(s -> {
                    System.out.println(s);
                    assertTrue(implementedExceptions.contains(s));
                });
    }
}
