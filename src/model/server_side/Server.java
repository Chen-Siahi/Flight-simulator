package model.server_side;

import java.io.IOException;

public interface Server {
    void start(int port) throws IOException;
    void stop();
}