package com.collin.notes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Note {

    private String id;
    private String title;
    private String content;
    private String author;
    private List<String> tags;
    private LocalDateTime created;
    private LocalDateTime modified;

    // Constructor for creating a brand-new note
    public Note(String id, String title, String content, String author, List<String> tags) {
        this.id = id;
        this.title = validateText(title, "Title");
        this.content = content == null ? "" : content;
        this.author = validateText(author, "Author");
        this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }

    // Constructor for loading an existing note from file
    public Note(String id, String title, String content, String author, List<String> tags,
                LocalDateTime created, LocalDateTime modified) {
        this.id = id;
        this.title = validateText(title, "Title");
        this.content = content == null ? "" : content;
        this.author = validateText(author, "Author");
        this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
        this.created = created;
        this.modified = modified;
    }

    private String validateText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getTags() {
        return new ArrayList<>(tags);
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setTitle(String title) {
        this.title = validateText(title, "Title");
        this.modified = LocalDateTime.now();
    }

    public void setContent(String content) {
        this.content = content == null ? "" : content;
        this.modified = LocalDateTime.now();
    }

    public void setAuthor(String author) {
        this.author = validateText(author, "Author");
        this.modified = LocalDateTime.now();
    }

    public void setTags(List<String> tags) {
        this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
        this.modified = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Note{id='" + id + "', title='" + title + "', author='" + author + "', tags=" + tags + "}";
    }
}