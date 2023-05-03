package memograph;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

final class Reader {
    private static final String LOG_FILE = "vasa";
    private static final String LINE_START = "Free memory";

    Pair<Date[], long[]> getUsedMemory(String path) throws IOException {
        File[] files = getFiles(path);
        long totalMemory = 0L;
        Date[] dates = new Date[10];
        long[] freeMemories = new long[10];
        int count = 0;
        for (File file : files) {
            if (!file.getName().startsWith(LOG_FILE))
            {
                continue;
            }
            Scanner scanner = getScannerFromFile(file, path);
            String currentLine;
            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                if (currentLine.length() >= 11 && currentLine.startsWith(LINE_START)) {
                    if (count == dates.length) {
                        dates = Arrays.copyOf(dates, (int)(dates.length * 1.6));
                        freeMemories = Arrays.copyOf(freeMemories, (int)(freeMemories.length * 1.6));
                    }
                    long currentTotalMemory = parseTotalMemory(currentLine);
                    if (currentTotalMemory > totalMemory) {
                        totalMemory = currentTotalMemory;
                    }
                    dates[count] = parseDate(currentLine);
                    freeMemories[count] = parseFreeMemory(currentLine);
                    count++;
                }
            }
        }
        if (count < dates.length) {
            dates = Arrays.copyOf(dates, count);
        }
        long[] usedMemories = new long[count + 1];
        for (int i = 0; i < count; i++) {
            usedMemories[i] = totalMemory - freeMemories[i];
        }
        usedMemories[count] = totalMemory;
        return new Pair<>(dates, usedMemories);
    }

    private File[] getFiles(String path) throws IOException {
        File folder = new File(path + '\\');
        File[] listFiles = new File[1];
        if (folder.isDirectory()) {
            listFiles = folder.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                throw new IOException("Folder whit logfiles is empty\n" +
                        "If you didn't provide input - check for presence of logfiles if log folder");
            }
        }
        else {
            listFiles[0] = new File(path + File.separator + LOG_FILE + ".log");
        }
        for (int i = 1; i < listFiles.length; i++) {
            File temp = listFiles[i];
            listFiles[i] = listFiles[i - 1];
            listFiles[i - 1] = temp;
        }
        return listFiles;
    }

    private Scanner getScannerFromFile(File file, String path) throws IOException {
        if (file.getName().endsWith(".gz")) {
            GZIPInputStream zin = new GZIPInputStream(new FileInputStream(path + File.separator + file.getName()));
            return new Scanner(zin, "UTF-8");
        }
        return new Scanner(file);
    }

    private long parseTotalMemory(String line) {
        return Long.parseLong(line.substring(line.indexOf("/") + 1).replaceAll(" ", ""));
    }

    private Date parseDate(String line) {
        return new Date(Long.parseLong(line.substring(line.indexOf("[") + 1, line.indexOf("]"))));
    }

    private long parseFreeMemory(String line) {
        return Long.parseLong(line.substring(line.indexOf("]") + 1, line.indexOf("/")).replaceAll(" ", ""));
    }
}
