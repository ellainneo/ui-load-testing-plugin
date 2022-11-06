package com.haulmont.ui.load.testing.util;

import com.browserup.harreader.HarReader;
import com.browserup.harreader.HarReaderException;
import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HarHelper {

    public static Har readHarLogFromFile (String filePath) {
        HarReader harReader = new HarReader();
        File harFile = new File(filePath);
        if (!harFile.exists()) {
            throw new IllegalStateException("Har file with name " + harFile.getName() + " not found");
        }
        try {
            return harReader.readFromFile(harFile);
        } catch (HarReaderException ex) {
            throw new IllegalStateException("An error occurs on trying to read a har file " + harFile.getName());
        }
    }

    public static List<HarEntry> getEntriesByPageRef(List<HarEntry> harEntries, String pageRef) {
        List<HarEntry> filteredList = new ArrayList();

        harEntries.forEach(harEntry -> {
            if (harEntry.getPageref().equals(pageRef)) {
                filteredList.add(harEntry);
            }
        });
        return filteredList;
    }

    public static boolean isPageEmpty(List<HarEntry> harEntries, String pageRef) {
        for (HarEntry entry : harEntries) {
            if (entry.getPageref().equals(pageRef)) {
                return false;
            }
        }
        return true;
    }
}
