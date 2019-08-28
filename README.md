# Swim System Java Implementation

[![package](https://img.shields.io/github/tag/swimOS/swim.svg?label=maven)](https://mvnrepository.com/artifact/ai.swim)
[![documentation](https://img.shields.io/badge/doc-JavaDoc-blue.svg)](https://docs.swimos.org/java/latest)
[![chat](https://img.shields.io/badge/chat-Gitter-green.svg)](https://gitter.im/swimos/community)

<a href="https://www.swimos.org"><img src="https://docs.swimos.org/readme/marlin-blue.svg" align="left"></a>

The **Swim System** Java implementation provides a self-contained distributed
software platform executing stateful Web Agent applications.  **Swim System**
encompasses the [**Swim Core**](swim-core-java) foundation framework, the
[**Swim Mesh**](swim-mesh-java) distributed microkernel and Web Agent framework,
and the [**Swim Polyglot**](swim-polyglot-java) multi-language virtual machine
framework.

## Umbrella Framework

The **Swim System** umbrella framework provides a self-contained distributed
software platform for building stateful, massively real-time streaming
applications that run on any Java 8+ VM.  **Swim System** has no external
dependencies beyond a minimal JVM.

### [**Swim Core** Framework](swim-core-java)

The **Swim Core** framework implements a dependency-free foundation
framework, with a lightweight concurrency engine, incremental I/O engine,
and flow-controlled network protocol implementations.  **Swim Core** consists
of the following component libraries:

- [**swim.util**](swim-core-java/swim.util) –
- [**swim.codec**](swim-core-java/swim.codec) –
- [**swim.collections**](swim-core-java/swim.collections) –
- [**swim.args**](swim-core-java/swim.args) –
- [**swim.structure**](swim-core-java/swim.structure) –
- [**swim.recon**](swim-core-java/swim.recon) –
- [**swim.json**](swim-core-java/swim.json) –
- [**swim.xml**](swim-core-java/swim.xml) –
- [**swim.protobuf**](swim-core-java/swim.protobuf) –
- [**swim.decipher**](swim-core-java/swim.decipher) –
- [**swim.math**](swim-core-java/swim.math) –
- [**swim.security**](swim-core-java/swim.security) –
- [**swim.spatial**](swim-core-java/swim.spatial) –
- [**swim.streamlet**](swim-core-java/swim.streamlet) –
- [**swim.dataflow**](swim-core-java/swim.dataflow) –
- [**swim.observable**](swim-core-java/swim.observable) –
- [**swim.uri**](swim-core-java/swim.uri) –
- [**swim.deflate**](swim-core-java/swim.deflate) –
- [**swim.mqtt**](swim-core-java/swim.mqtt) –
- [**swim.http**](swim-core-java/swim.http) –
- [**swim.ws**](swim-core-java/swim.ws) –
- [**swim.warp**](swim-core-java/swim.warp) –
- [**swim.concurrent**](swim-core-java/swim.concurrent) –
- [**swim.db**](swim-core-java/swim.db) –
- [**swim.io**](swim-core-java/swim.io) –
- [**swim.io.mqtt**](swim-core-java/swim.io.mqtt) –
- [**swim.io.http**](swim-core-java/swim.io.http) –
- [**swim.io.ws**](swim-core-java/swim.io.ws) –
- [**swim.io.warp**](swim-core-java/swim.io.warp) –
- [**swim.web**](swim-core-java/swim.web) –

### [**Swim Mesh** Framework](swim-mesh-java)

The **Swim Mesh** framework provides the Web Agent API, and implements
a distributed WARP microkernel.  **Swim Mesh** consists of the following
component libraries:

- [**swim.api**](swim-mesh-java/swim.api) –
- [**swim.store**](swim-mesh-java/swim.store) –
- [**swim.runtime**](swim-mesh-java/swim.runtime) –
- [**swim.kernel**](swim-mesh-java/swim.kernel) –
- [**swim.auth**](swim-mesh-java/swim.auth) –
- [**swim.actor**](swim-mesh-java/swim.actor) –
- [**swim.service**](swim-mesh-java/swim.service) –
- [**swim.store.mem**](swim-mesh-java/swim.store.mem) –
- [**swim.store.db**](swim-mesh-java/swim.store.db) –
- [**swim.remote**](swim-mesh-java/swim.remote) –
- [**swim.service.web**](swim-mesh-java/swim.service.web) –
- [**swim.java**](swim-mesh-java/swim.java) –
- [**swim.server**](swim-mesh-java/swim.server) –
- [**swim.client**](swim-mesh-java/swim.client) –
- [**swim.cli**](swim-mesh-java/swim.cli) –

### [**Swim Polyglot** Framework](swim-polyglot-java)

The **Swim Polyglot** framework provides multi-language API bindings and
[GraalVM](https://www.graalvm.org/) integration for embedding guest languages
into **swimOS** applications.  **Swim Polyglot** consists of the following
component libraries:

- [**swim.dynamic**](swim-polyglot-java/swim.dynamic) –
- [**swim.dynamic.java**](swim-polyglot-java/swim.dynamic.java) –
- [**swim.dynamic.structure**](swim-polyglot-java/swim.dynamic.structure) –
- [**swim.dynamic.observable**](swim-polyglot-java/swim.dynamic.observable) –
- [**swim.dynamic.api**](swim-polyglot-java/swim.dynamic.api) –
- [**swim.vm**](swim-polyglot-java/swim.vm) –
- [**swim.vm.js**](swim-polyglot-java/swim.vm.js) –
- [**swim.js**](swim-polyglot-java/swim.js) –

## Usage

To embed the **Swim Kernel** directly into your application, add the
**swim-server** library to your project's dependencies:

### Gradle

```groovy
compile group: 'ai.swim', name: 'swim-server', version: '3.10.0'
```

### Maven

```xml
<dependency>
    <groupId>ai.swim</groupId>
    <artifactId>swim-server</artifactId>
    <version>3.10.0</version>
</dependency>
```

## Development

**swimOS** runs on any Java 8+ VM with a minimal `java.base` classpath.
**swimOS** uses [Gradle](https://gradle.org/) as its standard build system.
The included `gradlew` script can be used to build the platform.

### Setup

Install a Java 8+ JDK, such as [OpenJDK](https://openjdk.java.net/) or
[GraalVM](https://www.graalvm.org/downloads/).

### Compiling sources

```sh
swim-system-java $ ./gradlew compileJava
```

### Running tests

```sh
swim-system-java $ ./gradlew test
```

### Building documentation

```sh
swim-system-java $ ./gradlew :javadoc
```
