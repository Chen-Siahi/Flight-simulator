package model.server_side;
import java.util.ArrayList;

public class Problem extends ArrayList<String> {
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            stringBuilder.append(this.get(i)+"\n");
        }
        return stringBuilder.toString();
    }
}