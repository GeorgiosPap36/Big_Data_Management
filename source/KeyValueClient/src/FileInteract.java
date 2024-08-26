import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileInteract
{
    private String inputPath;

    public FileInteract(String inputPath){
        this.inputPath = inputPath;
    }

    public List<String> ReadFile()
    {
        List<String> inputs = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputPath));
            String txtLine = reader.readLine();
            while (txtLine != null) {
                inputs.add(txtLine);
                txtLine = reader.readLine();
            }
            reader.close();
        } catch (IOException ioe) {
            System.out.println("Incorrect input file path");
        }
        return inputs;
    }
}
