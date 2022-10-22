<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

# Java CPU Emulator (JaCEm)

[![Build Status](https://cloud.drone.io/api/badges/khmarbaise/jacem/status.svg)](https://cloud.drone.io/khmarbaise/jacem)

This is the 3nd incarnation of the [Java CPU Emulator](https://github.com/khmarbaise/CPUEmu).

I had started that because I had long time ago an idea to make an emulator for 8085/6502 cpu working just as a learning
experiment. In the meantime I have other design idea how to handle that.

This project is now the state of the design ideas.

* Instruction codes https://www.masswerk.at/6502/6502_instruction_set.html
* More details http://wilsonminesco.com/6502primer/index.html
* https://www.masswerk.at/6502/

# Build

* JDK17+
* Maven 3.8.6+

Code coverage via:

```bash
mvn clean verify org.jacoco:jacoco-maven-plugin:report
```

Create Mutation coverage via:

```bash
mvn clean verify org.pitest:pitest-maven:mutationCoverage
```