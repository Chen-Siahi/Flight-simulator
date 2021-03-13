package model.server_side;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ClientHandler {
    void handleClient(InputStream input, OutputStream output) throws IOException;
}