package com.collin.notes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NoteFileSerializer {

    public String serialize(Note note) {
        String tagsLine = "tags: [" + String.join(", ", note.getTags()) + "]";

        return "---\n" +
                "title: " + note.getTitle() + "\n" +
                "author: " + note.getAuthor() + "\n" +
                "created: " + note.getCreated() + "\n" +
                "modified: " + note.getModified() + "\n" +
                tagsLine + "\n" +
                "---\n\n" +
                note.getContent();
    }

    public Note deserialize(String id, String fileText) {
        String[] lines = fileText.split("\\R");

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
}
