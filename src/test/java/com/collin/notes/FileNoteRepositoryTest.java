package com.collin.notes;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class FileNoteRepositoryTest {

    @Test
    public void testNotesDirectoryExistsAsPath() {
        FileNoteRepository repository = new FileNoteRepository();

        Path path = repository.getNotesDirectory();

        assertNotNull(path);
        assertTrue(path.toString().contains(".notes"));
        assertTrue(path.toString().contains("notes"));
    }

    @Test
public void testInitializeNotesDirectoryCreatesFolder() throws Exception {
    FileNoteRepository repository = new FileNoteRepository();

    repository.initializeNotesDirectory();

    assertTrue(java.nio.file.Files.exists(repository.getNotesDirectory()));
    assertTrue(java.nio.file.Files.isDirectory(repository.getNotesDirectory()));
}

@Test
public void testSaveCreatesFile() throws Exception {
    FileNoteRepository repository = new FileNoteRepository();

    Note note = new Note("test1", "Test Note", "Hello world", "Collin", java.util.Arrays.asList("test"));

    repository.save(note);

    Path filePath = repository.getNotesDirectory().resolve("test1.note");

    assertTrue(java.nio.file.Files.exists(filePath));
}

@Test
public void testFindByIdReadsSavedNote() throws Exception {
    FileNoteRepository repository = new FileNoteRepository();

    Note savedNote = new Note("read1", "Read Test", "This is my note content", "Collin", java.util.Arrays.asList("test"));

    repository.save(savedNote);

    Note loadedNote = repository.findById("read1");

    assertEquals("read1", loadedNote.getId());
    assertEquals("Read Test", loadedNote.getTitle());
    assertEquals("This is my note content", loadedNote.getContent());
    assertEquals("Collin", loadedNote.getAuthor());
}

@Test
public void testFindAllReturnsSavedNotes() throws Exception {
    FileNoteRepository repository = new FileNoteRepository();

    Note note1 = new Note("list1", "First Note", "Content one", "Collin", java.util.Arrays.asList("test"));
    Note note2 = new Note("list2", "Second Note", "Content two", "Collin", java.util.Arrays.asList("test"));

    repository.save(note1);
    repository.save(note2);

    java.util.List<Note> notes = repository.findAll();

    assertTrue(notes.size() >= 2);

    boolean foundFirst = false;
    boolean foundSecond = false;

    for (Note note : notes) {
        if (note.getId().equals("list1")) {
            foundFirst = true;
        }
        if (note.getId().equals("list2")) {
            foundSecond = true;
        }
    }

    assertTrue(foundFirst);
    assertTrue(foundSecond);
}

@Test
public void testDeleteByIdRemovesFile() throws Exception {
    FileNoteRepository repository = new FileNoteRepository();

    Note note = new Note("delete1", "Delete Test", "Delete me", "Collin", java.util.Arrays.asList("test"));

    repository.save(note);

    Path filePath = repository.getNotesDirectory().resolve("delete1.note");

    assertTrue(java.nio.file.Files.exists(filePath));

    repository.deleteById("delete1");

    assertFalse(java.nio.file.Files.exists(filePath));
}

}
