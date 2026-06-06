package com.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContextTest {

    @Test
    public void testContextMetadata() {
        Context context = new Context();
        context.setMetadata("author", "Leo");
        assertEquals("Leo", context.getMetadata("author"));
        assertNull(context.getMetadata("unknown"));
    }

    @Test
    public void testContextHeadingCounter() {
        Context context = new Context();
        assertEquals(1, context.incrementHeadingCount());
        assertEquals(2, context.incrementHeadingCount());
        assertEquals(3, context.incrementHeadingCount());
    }
}
