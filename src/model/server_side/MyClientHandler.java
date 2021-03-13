package model.server_side;

import java.io.*;
public class MyClientHandler implements ClientHandler{
    Solver<Solution, Problem> solver;
    CacheManager cm;

    public MyClientHandler(Solver<Solution, Problem> solver, CacheManager cm) {
        this.solver = solver;
        this.cm = cm;
    }
    public void handleClient(InputStream input, OutputStream output) throws IOException {
        PrintWriter out = new PrintWriter(output);
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String line = null, sol;
        while (true) {
            Problem problem = new Problem();

            while ((line = in.readLine()) != null && !line.equals("end")) {
                problem.add(line);
            }
            problem.add("end");
            problem.add(in.readLine()); // read start position
            problem.add(in.readLine()); //read end position
            if (!cm.check(problem.toString())) {
                cm.save(problem.toString(), solver.solve(problem).toString());
            }
            sol = cm.load(problem.toString());
            out.println(sol);
            out.flush();
        }
    }
}