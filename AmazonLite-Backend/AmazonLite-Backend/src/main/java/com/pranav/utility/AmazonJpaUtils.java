package com.pranav.utility;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AmazonJpaUtils {

    public String createId()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
