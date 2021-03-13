package model.server_side;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * check's if the solution exist's and loads it to the client.
 * if it does not exist, it will save the offered solution.
 */

public class FileCacheManager implements CacheManager {

    private String path;
    public FileCacheManager() {
        path = "./db/";
        new File(path).mkdirs();
    }

    @Override
    public Boolean check(String problem) {
        return new File(path + problem.hashCode() + ".sol").exists();
    }

    @Override
    public void save(String problem, String solution) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path + problem.hashCode() + ".sol");
            fileOutputStream.write(solution.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException ignored) { }
    }

    @Override
    public String load(String problem) {
        String result="";
        try {
            File f = new File(path + problem.hashCode()+".sol");
            result = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);

        } catch (FileNotFoundException ignored) { } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // test cash
    public static void main(String[] args) {
        FileCacheManager fileCacheManager = new FileCacheManager();
        fileCacheManager.save("aviv","asaf");
        System.out.println(fileCacheManager.check("dfg"));
        System.out.println(fileCacheManager.check("aviv"));
        System.out.println(fileCacheManager.load("aviv"));
    }
}