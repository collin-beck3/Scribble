package com.collin.notes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; 

public class Main {

    public static void main(String[] args) {
        FileNoteRepository repository = new FileNoteRepository();
        NoteService service = new NoteService(repository);

        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];

        try {
            if (command.equals("list")) {
                List<Note> notes;
                
                if (args.length >= 3 && args[1].equals("--tag")) {
                    String tag = args[2]; 
                    notes = service.listNotesByTag(tag);
                } else {
                    notes = service.listNotes();
                }

                if (notes.isEmpty()) {
                    System.out.println("No notes found!");
                    return;
                }

                for (Note note : notes) {
                    System.out.println(note.getId() + " | " + note.getTitle() + " | " + note.getAuthor() + " | " + note.getTags());
                }

            } else if (command.equals("read")) {
                if (args.length < 2) {
                    System.out.println("Usage: read <note-id>");
                    return;
                }

                String id = args[1];
                Note note = service.getNoteById(id);

                System.out.println("ID: " + note.getId());
                System.out.println("Title: " + note.getTitle());
                System.out.println("Author: " + note.getAuthor());
                System.out.println("Created: " + note.getCreated());
                System.out.println("Modified: " + note.getModified());
                System.out.println("Tags: " + note.getTags());
                System.out.println();
                System.out.println(note.getContent());

            } else if (command.equals("create")) {
                if (args.length < 5) {
                    System.out.println("Usage: create <title> <content> <author> <tags>");
                    System.out.println("Example: create \"Practice Note\" \"Worked on NoteService today\" \"Collin Beck\" \"java,school,project\"");
                    return;
                }

                String title = args[1];
                String content = args[2];
                String author = args[3];
                List<String> tags = parseTags(args[4]);

                Note note = service.createNote(title, content, author, tags);
                System.out.println("Note saved: " + note.getId());

            } else if (command.equals("delete")) {
                if (args.length < 2) {
                    System.out.println("Usage: delete <note-id>");
                    return;
                }

                String id = args[1];
                service.deleteNote(id);
                System.out.println("Deleted note: " + id);

            } else if (command.equals("search")) {
                if (args.length < 2) {
                    System.out.println("Usage: search <query>");
                    return;
                }

                String query = args[1];
                List<Note> results = service.searchNotes(query);

                if (results.isEmpty()) {
                    System.out.println("No matching notes found.");
                    return;
                }

                for (Note note : results) {
                    System.out.println(note.getId() + " | " + note.getTitle() + " | " + note.getAuthor() + " | " + note.getTags());
                }

            } else if (command.equals("edit")) {
                if (args.length < 2) {
                    System.out.println("Usage: edit <note-id>");
                    return;
                }

                String id = args[1];
                Note existingNote = service.getNoteById(id);

                Scanner scanner = new Scanner(System.in);

                System.out.println("Editing note: " + existingNote.getId());
                System.out.println("Press Enter to keep the current value.");
                System.out.println();

                System.out.println("Current title: " + existingNote.getTitle());
                System.out.print("New title: ");
                String newTitle = scanner.nextLine();

                System.out.println("Current content: " + existingNote.getContent());
                System.out.print("New content: ");
                String newContent = scanner.nextLine();

                System.out.println("Current author: " + existingNote.getAuthor());
                System.out.print("New author: ");
                String newAuthor = scanner.nextLine();

                System.out.println("Current Tags: " + existingNote.getTags());
                System.out.print("New tags (comma-seperated, press Enter to keep current): ");
                String tagInput = scanner.nextLine(); 

                List<String> newTags = null; 
                if (!tagInput.isBlank()) {
                    newTags = parseTags(tagInput);
                }


                Note updated = service.editNote(id, newTitle, newContent, newAuthor, newTags);
                System.out.println("Updated note: " + updated.getId());

            } else {
                System.out.println("Unknown command: " + command);
                printHelp();
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static List<String> parseTags(String input) {
        List<String> tags = new ArrayList<>();

        if (input == null || input.isBlank()) {
            return tags;
        }

        String[] parts = input.split(","); 

        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                tags.add(trimmed);
            }
        }
        return tags; 
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  list");
        System.out.println("  read <note-id>");
        System.out.println("  create <title> <content> <author>");
        System.out.println("  edit <note-id>");
        System.out.println("  delete <note-id>");
        System.out.println("  search <query>");
    }
}


