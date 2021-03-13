package model.server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MazeClient {

    public static void main(String[] args) throws IOException {
        Problem problem;
        problem = new Problem();
        problem.add("1,2,3");
        problem.add("4,5,6");
        problem.add("7,8,9");
        problem.add("end");
        problem.add("2,0");
        problem.add("0,2");
        Socket ourServer = new Socket("127.0.0.1", 5403);
        BufferedReader in = new BufferedReader(new InputStreamReader(ourServer.getInputStream()));
        PrintWriter out = new PrintWriter(ourServer.getOutputStream());
        out.println(problem.toString());
        out.flush();
        String solution = in.readLine();
        System.out.println("We asked solution for: \n" + problem.toString());
        System.out.println("We got solution: ");
        System.out.println(solution);
        in.close();
        out.close();
        ourServer.close();
        System.out.println("---------");
    }
}