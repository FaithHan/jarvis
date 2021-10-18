package org.jarvis.id;

import java.util.UUID;

public class UUIDGenerator implements IdGenerator<String>{

    @Override
    public String nextId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
