package com.collin.notes;

import java.io.IOException;
import java.nio.file.Files; 
import java.nio.file.Path;
import java.nio.file.Paths;

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
        initializeNotesDirectory();                                     //makes sure folder exists 

        Path filePath = notesDirectory.resolve(note.getId() + ".note");        //creates file path example (/Users/collin/.notes/notes/1.note)

        String content = 
                "---\n" + 
                "title: " + note.getTitle() + "\n" + 
                "author: " + note.getAuthor() + "\n" +                          //manually build YAML header + content
                "created: " + note.getCreated() + "\n" + 
                "modified: " + note.getModified() + "\n" + 
                "---\n\n" + 
                note.getContent(); 

        Files.write(filePath, content.getBytes());                          //write file
    }

    public Note findById(String id) throws IOException {
        Path filePath = notesDirectory.resolve(id + ".note");

        java.util.List<String> lines = Files.readAllLines(filePath);

        String title = "";
        String author = ""; 
        String content = ""; 

        boolean inHeader = false;
        boolean inContent = false; 
        StringBuilder contentBuilder = new StringBuilder(); 

        for (String line : lines) { 
            if (line.equals("---")) { 
                if (!inHeader) {
                    inHeader = true;        // first --- starts header
                }
                else if (!inContent) {
                    inContent = true;       // second --- starts content
                }
                    continue;
                }
            if (inHeader && !inContent) { 
                if (line.startsWith("title: ")) {
                    title = line.substring(7); 
                } else if (line.startsWith("author: ")) {
                    author = line.substring(8); 
                }
            } else if (inContent) {
                contentBuilder.append(line).append("\n"); 
            }
        }

        content = contentBuilder.toString().trim(); 

        return new Note(id, title, content, author, java.util.Arrays.asList());
    }
    
/* makes sure the notes folder exists
looks inside the folder
finds files ending in .note
gets each note’s id from the filename
reuses findById() to load each one
returns all notes in a list */

    public java.util.List<Note> findAll() throws IOException { 
        initializeNotesDirectory(); 

        java.util.List<Note> notes = new java.util.ArrayList<>(); 

        try (java.util.stream.Stream<Path> paths = Files.list(notesDirectory)) { 
            java.util.List<Path> files = paths
                .filter(path -> path.toString().endsWith(".note"))
                .toList(); 

            for (Path file : files) { 
                String fileName = file.getFileName().toString(); 
                String id = fileName.substring(0, fileName.length() - 5);   //removes ".note"
                notes.add(findById(id)); 
            }
        }

        return notes; 
    }

    public void deleteById(String id) throws IOException { 
        Path filePath = notesDirectory.resolve(id + ".note");     // if file exists, delete it 

        Files.deleteIfExists(filePath); 
    }

}