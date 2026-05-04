package com.collin.notes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class NoteService {

    private final FileNoteRepository repository;

    public NoteService(FileNoteRepository repository) {
        this.repository = repository;
    }

    public Note createNote(String title, String content, String author, List<String> tags) throws IOException {
        String id = UUID.randomUUID().toString();

        Note note = new Note(
                id,
                title,
                content,
                author,
                tags
        );

        repository.save(note);
        return note;
    }

    public List<Note> listNotes() throws IOException {
        return repository.findAll();
    }

    public List<Note> listNotesByTag(String tag) throws IOException { 
        List<Note> allNotes = repository.findAll();                                 //list all notes
        String lowerTag = tag.toLowerCase();                                        //filters tags making input not sensitive to case 

        return allNotes.stream() 
                .filter(note -> note.getTags().stream()                                    
                    .anyMatch(noteTag -> noteTag.toLowerCase().equals(lowerTag)))       //keeps and notes matching the tag inputed 
                .toList();
    }

    public Note getNoteById(String id) throws IOException {
        return repository.findById(id);
    }

    public void deleteNote(String id) throws IOException {
        repository.deleteById(id);
    }

    public List<Note> searchNotes(String query) throws IOException {
        List<Note> allNotes = repository.findAll();
        String lowerQuery = query.toLowerCase();

        return allNotes.stream()
                .filter(note ->
                        note.getTitle().toLowerCase().contains(lowerQuery) ||
                        note.getContent().toLowerCase().contains(lowerQuery) ||
                        note.getTags().stream()
                                .anyMatch(tag -> tag.toLowerCase().contains(lowerQuery))
                )
                .toList();
    }

    public Note editNote(String id, String newTitle, String newContent, String newAuthor, List<String> newTags) throws IOException {
        Note note = repository.findById(id);

        if (newTitle != null && !newTitle.isBlank()) {
            note.setTitle(newTitle);
        }

        if (newContent != null && !newContent.isBlank()) {
            note.setContent(newContent);
        }

        if (newAuthor != null && !newAuthor.isBlank()) {
            note.setAuthor(newAuthor);
        }

        if (newTags != null) {
            note.setTags(newTags);
        }

        repository.save(note);
        return note;
    }

    public void printStats() throws IOException {
    List<Note> notes = repository.findAll();

    int totalNotes = notes.size();
    int totalCharacters = 0;
    java.util.Map<String, Integer> tagCounts = new java.util.HashMap<>();

    for (Note note : notes) {
        totalCharacters += note.getContent().length();

        for (String tag : note.getTags()) {
            String lowerTag = tag.toLowerCase();
            tagCounts.put(lowerTag, tagCounts.getOrDefault(lowerTag, 0) + 1);
        }
    }

    double averageLength = totalNotes == 0 ? 0 : (double) totalCharacters / totalNotes;

    System.out.println("Notes Stats");
    System.out.println("-----------");
    System.out.println("Total notes: " + totalNotes);
    System.out.println("Total characters: " + totalCharacters);
    System.out.println("Average note length: " + averageLength);

    if (tagCounts.isEmpty()) {
        System.out.println("No tags found.");
    } else {
        System.out.println("Tags:");
        for (String tag : tagCounts.keySet()) {
            System.out.println("  " + tag + ": " + tagCounts.get(tag));
        }
    }
}
}