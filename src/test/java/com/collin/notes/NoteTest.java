package com.collin.notes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class NoteTest {

    @Test
    public void testSimple() {
        assertEquals(2, 1 + 1);
    }

    @Test 
    public void testNoteTitle() { 
        Note note = new Note("My First Note", "Im doing it I think"); 

        assertEquals("My First Note", note.getTitle());
        assertEquals("Im doing it I think", note.getContent());
    }
}