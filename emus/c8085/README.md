

* ToDo's

  * Find a way to express the differences in cycles between 8080
    and 8085... maybe we can integrate that somehow into the same
    CPU emulator.

  * Extract common classes from CPU8085/CPU6502 into a common module?
    
  * Can we somehow combine the Ram/Rom/AddressBug concept with
    the in/out of 8085?
```java
addressBus.registerIO(new IOArea(0x00, 0xff));
```

```
Signal  Address
TRAP	0024
RST 7.5	003C
RST 6.5	0034
RST 5.5	002C
```
