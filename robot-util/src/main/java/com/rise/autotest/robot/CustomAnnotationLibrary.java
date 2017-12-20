package com.rise.autotest.robot;

import org.robotframework.javalib.library.AnnotationLibrary;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Extension of {@link AnnotationLibrary} which implements reading the keyword documentation from a class path resource.
 */
public class CustomAnnotationLibrary extends AnnotationLibrary {

    private static final String KEYWORD_DOC_REGEX_PREFIX = "^[\\+\\#]+.*";
    private final String keywordDocFile;
    private List<String> documentLines = null;

    public CustomAnnotationLibrary(String keywordsPattern, String keywordDocFile) {
        super(keywordsPattern);
        this.keywordDocFile = keywordDocFile;
        initDocumentation();
    }

    private void initDocumentation() {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(keywordDocFile);
            if(url != null) {
                File file = new File(url.getFile());
                documentLines = Files.readAllLines(file.toPath());
            }
        } catch (Exception e) {
            //ignoring as this only affects documentation generation.
        }
    }

    @Override public String getKeywordDocumentation(String keywordName) {
        if(documentLines != null && !documentLines.isEmpty()) {
            boolean found = false;
            boolean reading = true;
            StringBuilder doc = new StringBuilder();

            for(String line: documentLines) {
                if (!found && lineMatchesKeywordPattern(line, keywordName)){
                    found = true;
                    continue;
                }
                if(found && reading) {
                    reading  = readLineAndVerify(doc, line);
                }
            }
            return doc.toString();
        }
        return super.getKeywordDocumentation(keywordName);
    }

    private boolean readLineAndVerify(StringBuilder doc, String line) {
        if(!matches(line)) {
            doc.append(line).append("\n");
            return true;
        }
        return false;
    }

    private boolean lineMatchesKeywordPattern(String line, String keywordName) {
        return matches(line) && line.contains(keywordName);
    }


    private boolean matches(String line) {
        Pattern pattern = Pattern.compile(KEYWORD_DOC_REGEX_PREFIX);
        return pattern.matcher(line).matches();
    }
}
