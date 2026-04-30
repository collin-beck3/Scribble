package com.collin.notes;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception { 
        FileNoteRepository repository = new FileNoteRepository(); 

        if (args.length == 0) { 
            System.out.println("Please provide a command. "); 
            return;
        }
    
        String command = args[0]; 

        if (command.equals("list")) { 
             List<Note> notes = repository.findAll(); 

            if (notes.isEmpty()) { 
                System.out.println(" No notes found! "); 
                return; 
            }

            for (Note note : notes) {
                System.out.println(note.getId() + " | " + note.getTitle() + " | " + note.getAuthor());
            }
        } else {
            System.out.println(" Unknown command: " + command); 
        }
    }
}


