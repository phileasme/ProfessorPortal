package test;

import studentdata.*;

public class TestStudentData {
    
    public static void main(String[] args) {
    
        // Create a Connector object and open the connection to the server
        Connector server = new Connector();
        boolean success = server.connect("MNP","0a34d4ea3cc0da36ee91172b9cccb621");
        
        if (success == false) {
            System.out.println("Fatal error: could not open connection to server");
            System.exit(1);
        }
        
        DataTable data = server.getData();
        
        int rowCount = data.getRowCount();
        for (int row = 0; row < rowCount; ++row) {
            for (int col = 0; col < 4; ++col) {
                if (col > 0) {
                    System.out.print(",");
                }
                System.out.print(data.getCell(row,col));
            }
            System.out.println();
        }
    
    }

}
