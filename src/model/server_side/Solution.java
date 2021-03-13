package model.server_side;

import java.util.ArrayList;

public class Solution extends ArrayList<String> {
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            str.append(this.get(i));
            if(i<this.size()-1) {
                str.append(",");
            }
        }
        return str.toString();
    }
}