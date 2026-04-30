package com.collin.notes;

import java.time.LocalDateTime; 
import java.util.List; 

public class Note {

    private String id; 
    private String title;
    private String content; 
    private String author; 
    private List<String> tags; 
    private LocalDateTime created; 
    private LocalDateTime modified; 

    public Note(String id, String title, String content, String author, List<String> tags) { 
        this.id = id;
        this.title = title;
        this.content = content; 
        this.author = author;
        this.tags = tags; 
        this.created = LocalDateTime.now();   // auto sets date and time note was created 
        this.modified = LocalDateTime.now(); 
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
        return tags;
    }

    public LocalDateTime getCreated() { 
        return created; 
    }

    public LocalDateTime getModified() { 
        return modified; 
    }

    public void setContent(String content) { 
        this.content = content;
        this.modified = LocalDateTime.now(); 
    }

}
