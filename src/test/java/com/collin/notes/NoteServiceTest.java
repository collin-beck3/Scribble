package com.collin.notes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class NoteServiceTest {

    private final FileNoteRepository repository = new FileNoteRepository();
    private final NoteService service = new NoteService(repository);

    @AfterEach
    void cleanUp() throws IOException {
        Path notesDirectory = repository.getNotesDirectory();

        if (Files.exists(notesDirectory)) {
            try (var paths = Files.walk(notesDirectory)) {
                paths.sorted(Comparator.reverseOrder())
                     .forEach(path -> {
                         try {
                             Files.deleteIfExists(path);
                         } catch (IOException e) {
                             throw new RuntimeException(e);
                         }
                     });
            }
        }
    }

    @Test
    void createNote_shouldCreateAndSaveNote() throws IOException {
        Note note = service.createNote("Practice Note", "Worked on Java today", "Collin Beck");

        assertNotNull(note);
        assertNotNull(note.getId());
        assertEquals("Practice Note", note.getTitle());
        assertEquals("Worked on Java today", note.getContent());
        assertEquals("Collin Beck", note.getAuthor());
    }

    @Test
    void listNotes_shouldReturnCreatedNotes() throws IOException {
        service.createNote("Note One", "First content", "Collin");
        service.createNote("Note Two", "Second content", "Collin");

        List<Note> notes = service.listNotes();

        assertEquals(2, notes.size());
    }

    @Test
    void getNoteById_shouldReturnCorrectNote() throws IOException {
        Note created = service.createNote("Read Test", "Testing get by id", "Collin");

        Note found = service.getNoteById(created.getId());

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals("Read Test", found.getTitle());
        assertEquals("Testing get by id", found.getContent());
        assertEquals("Collin", found.getAuthor());
    }

  @Test
void deleteNote_shouldRemoveNote() throws IOException {
    Note created = service.createNote("Delete Test", "This note will be deleted", "Collin");

    service.deleteNote(created.getId());

    List<Note> notes = service.listNotes();

    boolean found = notes.stream()
            .anyMatch(note -> note.getId().equals(created.getId()));

    assertFalse(found);
}

    @Test
    void listNotes_shouldReturnEmptyListWhenNoNotesExist() throws IOException {
        List<Note> notes = service.listNotes();

        assertNotNull(notes);
        assertTrue(notes.isEmpty());
    }

    @Test
void searchNotes_shouldFindMatchingNotes() throws IOException {
    service.createNote("Java Note", "Learning streams", "Collin");
    service.createNote("Food Note", "Milk and eggs", "Collin");

    List<Note> results = service.searchNotes("java");

    assertEquals(1, results.size());
    assertEquals("Java Note", results.get(0).getTitle());
}

@Test
void searchNotes_shouldReturnEmptyWhenNoMatch() throws IOException {
    service.createNote("Java Note", "Learning streams", "Collin");

    List<Note> results = service.searchNotes("basketball");

    assertTrue(results.isEmpty());
}

@Test
void editNote_shouldUpdateFields() throws IOException {
    Note created = service.createNote("Old Title", "Old Content", "Collin");

    Note updated = service.editNote(
            created.getId(),
            "New Title",
            "New Content",
            "Collin Beck"
    );

    assertEquals("New Title", updated.getTitle());
    assertEquals("New Content", updated.getContent());
    assertEquals("Collin Beck", updated.getAuthor());
}

@Test
void editNote_shouldPersistChanges() throws IOException {
    Note created = service.createNote("Old Title", "Old Content", "Collin");

    service.editNote(
            created.getId(),
            "Updated Title",
            "Updated Content",
            "Collin Beck"
    );

    Note reloaded = service.getNoteById(created.getId());

    assertEquals("Updated Title", reloaded.getTitle());
    assertEquals("Updated Content", reloaded.getContent());
}

@Test
void editNote_shouldKeepOldValuesWhenBlankInputIsGiven() throws IOException {
    Note created = service.createNote("Old Title", "Old Content", "Collin", List.of("ffbfff", "ffffffff"));

    Note updated = service.editNote(created.getId(), "", "New Content", "");

    assertEquals("Old Title", updated.getTitle());
    assertEquals("New Content", updated.getContent());
    assertEquals("Collin", updated.getAuthor());
}

@Test
void createNote_shouldStoreTags() throws IOException {
    Note note = service.createNote(
            "Tagged Note",
            "Testing tags",
            "Collin",
            List.of("java", "school")
    );

    assertEquals(List.of("java", "school"), note.getTags());
}

@Test
void searchNotes_shouldFindMatchingTag() throws IOException {
    service.createNote("Java Note", "Streams practice", "Collin", List.of("java", "backend"));
    service.createNote("Shopping", "Milk and eggs", "Collin", List.of("personal"));

    List<Note> results = service.searchNotes("backend");

    assertEquals(1, results.size());
    assertEquals("Java Note", results.get(0).getTitle());
}

@Test
void editNote_shouldUpdateTags() throws IOException {
    Note created = service.createNote(
            "Old Note",
            "Old content",
            "Collin",
            List.of("old")
    );

    Note updated = service.editNote(
            created.getId(),
            "",
            "",
            "",
            List.of("java", "project")
    );

    assertEquals(List.of("java", "project"), updated.getTags());
}
}