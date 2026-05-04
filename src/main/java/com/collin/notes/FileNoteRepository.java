package com.collin.notes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileNoteRepository {

    private final Path notesDirectory;
    private final NoteFileSerializer serializer;

    public FileNoteRepository() {
        this.notesDirectory = Paths.get(System.getProperty("user.home"), ".notes", "notes");
        this.serializer = new NoteFileSerializer();
    }

    public Path getNotesDirectory() {
        return notesDirectory;
    }

    public void initializeNotesDirectory() throws IOException {
        Files.createDirectories(notesDirectory);
    }

    public void save(Note note) throws IOException {
        initializeNotesDirectory();

        Path filePath = notesDirectory.resolve(note.getId() + ".note");
        String fileText = serializer.serialize(note);

        Files.writeString(filePath, fileText, StandardCharsets.UTF_8);
    }

    public Note findById(String id) throws IOException {
        Path filePath = notesDirectory.resolve(id + ".note");

        String fileText = Files.readString(filePath, StandardCharsets.UTF_8);

        return serializer.deserialize(id, fileText);
    }

    public List<Note> findAll() throws IOException {
        initializeNotesDirectory();

        List<Note> notes = new ArrayList<>();

        try (Stream<Path> paths = Files.list(notesDirectory)) {
            List<Path> files = paths
                    .filter(path -> path.toString().endsWith(".note"))
                    .toList();

            for (Path file : files) {
                String fileName = file.getFileName().toString();
                String id = fileName.substring(0, fileName.length() - 5);
                notes.add(findById(id));
            }
        }

        return notes;
    }

    public void deleteById(String id) throws IOException {
        Path filePath = notesDirectory.resolve(id + ".note");
        Files.deleteIfExists(filePath);
    }
}