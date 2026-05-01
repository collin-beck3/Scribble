package com.collin.notes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileNoteRepository {

    private final Path notesDirectory;

    public FileNoteRepository() {
        this.notesDirectory = Paths.get(System.getProperty("user.home"), ".notes", "notes");
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

        String tagsLine = "tags: [" + String.join(", ", note.getTags()) + "]";

        String content =
                "---\n" +
                "title: " + note.getTitle() + "\n" +
                "author: " + note.getAuthor() + "\n" +
                "created: " + note.getCreated() + "\n" +
                "modified: " + note.getModified() + "\n" +
                tagsLine + "\n" +
                "---\n\n" +
                note.getContent();

        Files.writeString(filePath, content, StandardCharsets.UTF_8);
    }

    public Note findById(String id) throws IOException {
        Path filePath = notesDirectory.resolve(id + ".note");

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        String title = "";
        String author = "";
        LocalDateTime created = null;
        LocalDateTime modified = null;
        List<String> tags = new ArrayList<>();
        StringBuilder contentBuilder = new StringBuilder();

        boolean inHeader = false;
        boolean inContent = false;

        for (String line : lines) {
            if (line.equals("---")) {
                if (!inHeader) {
                    inHeader = true;
                } else if (!inContent) {
                    inContent = true;
                }
                continue;
            }

            if (inHeader && !inContent) {
                if (line.startsWith("title: ")) {
                    title = line.substring(7).trim();
                } else if (line.startsWith("author: ")) {
                    author = line.substring(8).trim();
                } else if (line.startsWith("created: ")) {
                    created = LocalDateTime.parse(line.substring(9).trim());
                } else if (line.startsWith("modified: ")) {
                    modified = LocalDateTime.parse(line.substring(10).trim());
                } else if (line.startsWith("tags: [") && line.endsWith("]")) {
                    String tagText = line.substring(7, line.length() - 1).trim();
                    if (!tagText.isEmpty()) {
                        String[] splitTags = tagText.split(",");
                        for (String tag : splitTags) {
                            tags.add(tag.trim());
                        }
                    }
                }
            } else if (inContent) {
                contentBuilder.append(line).append("\n");
            }
        }

        String content = contentBuilder.toString().trim();

        return new Note(id, title, content, author, tags, created, modified);
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