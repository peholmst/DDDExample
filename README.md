# DDD Example

This is an example project for my Vaadin TechLunch presentation on November 22, 2018 about 
*Domain Driven Design and the Hexagonal Architecture*. It demonstrates how you can use Spring Boot to create a 
semi-complex system consisting of multiple bounded contexts, represented here as individual Spring Boot applications.
REST and domain events are used to integrate the contexts with each other.

Here are some notes to pay special attention to:

## Value Objects as Entity IDs

JPA does not support custom types for `@Id` fields out of the box and you can only use `AttributeConverter` for 
non-`@Id` fields. In this example project, I'm using Hibernate custom types which works with one caveat: you can't
use them with `@GeneratedValue` without specifying your own generation strategy. 

The built-in strategies assume an integral data type (such as integer, long, `BigInteger`, etc.) and even if your custom 
type is only a wrapper around a long, the built-in strategies will not recognize it as such.

In this example project, I've solved the problem by using UUIDs and creating the IDs immediately when the entity objects
are created. This has the added advantage of making the ID known and available before anything is persisted, which can
be useful sometimes.

However, if you want to use sequences or identity columns for ID generation, you have to either create your own ID
generator strategy or use integral types and then wrap them manually in your public API. The advantage with this 
approach is that you no longer need any Hibernate custom types but can use `AttributeConverter`s for your reference 
fields.

## Domain Objects, REST controllers and JSON

In this application, I'm using `@JsonProperty` annotations directly on my domain objects (entities, value objects, etc.)
and returning them directly from my REST controllers. I'm doing this to save time but in real-world applications this is
not a good practice. 

You will want to have your external REST API to remain as stable as possible, while being able to evolve the domain 
model as you learn new things and new requirements emerge. This is hard if your REST API is directly based on the domain
model. Therefore, in a real world application, I very much recommmend you to use dedicated DTOs for your REST APIs.

## Lack of Pagination

To keep things simple, I don't use pagination anywhere (except for the domain event log). In real-world applications,
you should *always* use pagination for unbounded queries (i.e. queries that you don't know for sure will return only
a small and limited number of items).

## Domain Event Distribution through REST

The domain event log and REST protocol is based on the approach presented in *Implementing Domain Driven Design* by
*Vaughn Vernon*. The idea itself is production-ready but the implementation in this example project is not. The handling
of JSON is not optimal, caching headers are missing from the REST responses, there is no test coverage and the solution
is not resilient enough. You can however use what's here as a basis for a production ready implementation, but don't
use it 'as-is' in real-world applications.

## @NonNull and @Nullable

I'm using the `@NonNull` and `@Nullable` annotations everywhere to make it crystal-clear which parameters can or can't 
be null and which methods return or don't return null. This is a practice I've started to use lately and so far I really
like it. In this project I'm using the Spring annotations, but I've also used JetBrains' annotations or the
JSR-305 annotations.

In addition, I often use `Objects.requireNonNull(..)` in the beginning of each method that accepts non-null parameters.

With the combination of annotations and explicit null-checks I hope to avoid tracking down annoying NPE-bugs in the 
future.

## The Absence of Getters

In this project I've avoided getter methods wherever I can. I find it makes the code more fluent and easier to read.
However, I've also discovered that since the usage of getter methods is such a large practice in the Java world, a lof 
of conventions and IDE tools no longer work out-of-the-box. This in turn increases the need for annotations and manual
coding.

Since there is no technical difference beween `person.firstName()` and `person.getFirstName()`, you have to consider 
whether leaving out the `get` prefix is really worth the effort.

## Setters and Bean Validation

For all the domain classes, the API is written in such a way that you can't put the aggregate into an inconsistent 
state, nor can you edit all the properties after creation (there are virtually no public setters). This means that Bean
Validation (JSR-303) is not needed in the domain layer.

This in turn means that different objects need to be used in the UI layer for data binding. In this project I'm calling 
these objects *form objects*. The naming is from back in the day when you had to fill out forms on paper, mail them
to the retailer who then entered the information into the system. You could also call these objects *request objects*
(since you use them to request a specific operation), *data transfer objects (DTO)s* or something else.

When an application service receives a form object, it moves the information into the domain layer. To save some time,
you could add JSR-303 annotations to the form object classes and let the bean validator validate the form object before
the application service does anything else with it. Any validation errors could then be reported to the user in a user-
friendly manner.
