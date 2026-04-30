package com.collin.notes;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test; 

public class NoteTest {

    @Test
    public void testSimple() {
        assertEquals(2, 1 + 1);
    }

    @Test 
    public void testNoteTitle() { 
        Note note = new Note("1", "My First Note", "Im doing it I think", "Collin", Arrays.asList("school", "java")); 

        assertEquals("1", note.getId()); 
        assertEquals("My First Note", note.getTitle());
        assertEquals("Im doing it I think", note.getContent());
        assertEquals("Collin", note.getAuthor());
        assertEquals(Arrays.asList("school", "java"), note.getTags()); 

        assertNotNull(note.getCreated());
        assertNotNull(note.getModified());
    }

}