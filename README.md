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

This is the 3nd incarnation of the [Java CPU Emulator](https://github.com/khmarbaise/CPUEmu). 

Instruction codes http://www.obelisk.me.uk/6502/reference.html

# Build

* JDK11+
* Maven 3.6.3+

Code coverage via:
```bash
mvn clean verify org.jacoco:jacoco-maven-plugin:report
```
