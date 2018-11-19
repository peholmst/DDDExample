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
